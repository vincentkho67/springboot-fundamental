package bytebrewers.bitpod.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import bytebrewers.bitpod.security.JwtUtils;
import bytebrewers.bitpod.utils.dto.response.user.JwtClaim;
import bytebrewers.bitpod.utils.dto.response.user.UserBasicFormat;
import bytebrewers.bitpod.utils.specification.GeneralSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import bytebrewers.bitpod.entity.Auditable;
import bytebrewers.bitpod.entity.User;
import bytebrewers.bitpod.repository.UserRepository;
import bytebrewers.bitpod.service.UserService;
import bytebrewers.bitpod.utils.dto.request.user.TopUpDTO;
import bytebrewers.bitpod.utils.dto.request.user.UserDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    
    private final UserRepository userRepository;

    private final Cloudinary cloudinary;

    private final JwtUtils jwt;

    public Map<?, ?> upload(MultipartFile multipartFile) throws IOException{
        Map<?, ?> result = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
        return result;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserById(String id) {
        return Auditable.searchById(userRepository.findById(id), "User Not Found");
    }

    @Override
    public Page<UserBasicFormat> getAllUser(Pageable pageable, UserDTO req) {
        Specification<User> specification = GeneralSpecification.getSpecification(req);
         Page<User> users = userRepository.findAll(specification, pageable);
         return users.map(UserBasicFormat::fromUser);
    }

    @Override
    public UserBasicFormat updateUser(UserDTO userDTO, String token) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Map<?,?> result = upload(userDTO.getImage());

            User user = getUserDetails(token);
            user.setName(userDTO.getName());
            user.setUsername(userDTO.getUsername());
            user.setAddress(userDTO.getAddress());
            Date dateOfBirth = dateFormat.parse(userDTO.getBirthDate());
            user.setBirthDate(dateOfBirth);
            user.setProfilePicture(result.get("url").toString());
            User updatedUser = userRepository.save(user);

            return UserBasicFormat.fromUser(updatedUser);
        } catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "failed updated user", e.getCause());
        }
    }

    @Override
    public void deleteUserByid(String id) {
        userRepository.deleteById(id);
    }
    
    
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
    }
    
    @Override
    public User loadByUserId(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
    }

    @Override
    public TopUpDTO topUp(TopUpDTO topUpDTO, String token) {
        try{

            User user = getUserDetails(token);
            assert user != null;
            user.setBalance(topUpDTO.getAmount().add(user.getBalance()));

            userRepository.save(user);

            return topUpDTO;

        } catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found", e.getCause());
        }
    }

    private String parseJwt(String token) {
        if(token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
    private User getUserDetails(String token) {
        String parsedToken = parseJwt(token);
        if(parsedToken != null) {
            JwtClaim user = jwt.getUserInfoByToken(parsedToken);
            return loadByUserId(user.getUserId());
        }
        return null;
    }
}
