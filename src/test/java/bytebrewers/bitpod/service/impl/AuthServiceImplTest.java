package bytebrewers.bitpod.service.impl;

import bytebrewers.bitpod.entity.Role;
import bytebrewers.bitpod.entity.User;
import bytebrewers.bitpod.repository.UserRepository;
import bytebrewers.bitpod.security.JwtUtils;
import bytebrewers.bitpod.service.AuthService;
import bytebrewers.bitpod.service.RoleService;
import bytebrewers.bitpod.utils.dto.request.user.RegisterDTO;
import bytebrewers.bitpod.utils.dto.response.user.UserResponseDTO;
import bytebrewers.bitpod.utils.enums.ERole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @InjectMocks
    AuthServiceImpl authService;

    @Test
    void initSuperAdmin() {
        String email = "superadmin@gmail.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        when(roleService.getOrSave(ERole.ROLE_SUPER_ADMIN)).thenReturn(mock(Role.class));
        when(roleService.getOrSave(ERole.ROLE_ADMIN)).thenReturn(mock(Role.class));
        when(roleService.getOrSave(ERole.ROLE_MEMBER)).thenReturn(mock(Role.class));

        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("hashedPassword");

        authService.initSuperAdmin();

        verify(userRepository, times(1)).findByEmail(email);
        verify(roleService, times(1)).getOrSave(ERole.ROLE_SUPER_ADMIN);
        verify(roleService, times(1)).getOrSave(ERole.ROLE_ADMIN);
        verify(roleService, times(1)).getOrSave(ERole.ROLE_MEMBER);
        verify(userRepository, times(1)).saveAndFlush(any());

        verify(passwordEncoder, times(1)).encode(any(CharSequence.class));
    }
}