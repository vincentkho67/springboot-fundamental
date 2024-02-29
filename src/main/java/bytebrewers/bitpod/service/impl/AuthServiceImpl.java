package bytebrewers.bitpod.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import bytebrewers.bitpod.entity.Role;
import bytebrewers.bitpod.entity.User;
import bytebrewers.bitpod.repository.UserRepository;
import bytebrewers.bitpod.security.JwtUtils;
import bytebrewers.bitpod.service.AuthService;
import bytebrewers.bitpod.service.RoleService;
import bytebrewers.bitpod.utils.dto.request.user.AuthRequest;
import bytebrewers.bitpod.utils.dto.request.user.RegisterDTO;
import bytebrewers.bitpod.utils.dto.response.user.UserResponseDTO;
import bytebrewers.bitpod.utils.enums.ERole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepo;

    private final RoleService roleService;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    @PostConstruct
    public void initSuperAdmin(){
        String email = "superadmin@gmail.com";
        String password = "superadmin";

        Optional<User> OptionalUserCredential = userRepo.findByEmail(email);
        if(OptionalUserCredential.isPresent()){
            return;
        }

        Role roleSuperAdmin = roleService.getOrSave(ERole.ROLE_SUPER_ADMIN);
        Role roleAdmin = roleService.getOrSave(ERole.ROLE_ADMIN);
        Role roleCustomer = roleService.getOrSave(ERole.ROLE_MEMBER);

        String hashPassword = passwordEncoder.encode(password);
        
        User userCredential = User.builder()
            .email(email)
            .password(hashPassword)
            .roles(List.of(roleSuperAdmin, roleAdmin, roleCustomer))
            .build();
            
        userRepo.saveAndFlush(userCredential);
    }

    @Override
    public String login(String email, String password) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        
        //save session
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User userCredential = (User) authentication.getPrincipal();

        try {
            return jwtUtils.generateToken(userCredential);
            
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "login failed", e.getCause());
        }
    }

    @Override
    public UserResponseDTO register(RegisterDTO registerDTO){
        Role roleCustomer = roleService.getOrSave(ERole.ROLE_MEMBER);
        String hashPassword = passwordEncoder.encode(registerDTO.getPassword());
        User userCredential = User.builder()
            .email(registerDTO.getEmail())
            .password(hashPassword)
            .name(registerDTO.getName())
            .username(registerDTO.getUsername())
            .balance(BigDecimal.ZERO)
            .address(registerDTO.getAddress())
            .birthDate(registerDTO.getBirthDate())
            .roles(List.of(roleCustomer))
            .build();
        
        try {
            userRepo.saveAndFlush(userCredential);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "failed to register", e.getCause());
        }

        List<String> roles = userCredential.getRoles().stream().map(role -> role.getRole().name()).toList();
        return UserResponseDTO.builder()
            .email(registerDTO.getEmail())
            .roles(roles)
            .build();
    }

    @Override
    public UserResponseDTO registerAdmin(RegisterDTO registerDTO) {
        Role roleCustomer = roleService.getOrSave(ERole.ROLE_MEMBER);
        Role roleAdmin = roleService.getOrSave(ERole.ROLE_ADMIN);

        String hashPassword = passwordEncoder.encode(registerDTO.getPassword());

        User userCredential = User.builder()
            .email(registerDTO.getEmail())
            .password(hashPassword)
            .name(registerDTO.getName())
            .username(registerDTO.getUsername())
            .balance(BigDecimal.ZERO)
            .address(registerDTO.getAddress())
            .birthDate(registerDTO.getBirthDate())
            .roles(List.of(roleCustomer, roleAdmin))
            .build();

        try {
            userRepo.saveAndFlush(userCredential);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "failed to register", e.getCause());
        }

        List<String> roles = userCredential.getRoles().stream().map(role -> role.getRole().name()).toList();
        return UserResponseDTO.builder()
            .email(userCredential.getEmail())
            .roles(roles)
            .build();
    }
}
