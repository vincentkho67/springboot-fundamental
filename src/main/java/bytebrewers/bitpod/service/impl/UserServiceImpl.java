package bytebrewers.bitpod.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
public class UserServiceImpl implements UserService{
    
    private final UserRepository userRepository;

    private final Cloudinary cloudinary;

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
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        try{

            Map<?,?> result = upload(userDTO.getImage());

            User user = findUserById(userDTO.getId());
            user.setId(userDTO.getId());
            user.setName(userDTO.getName());
            user.setUsername(userDTO.getUsername());
            user.setAddress(userDTO.getAddress());
            user.setBirthDate(user.getBirthDate());
            user.setProfilePicture(result.get("url").toString());

            userRepository.save(user);

            return userDTO;

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
    public TopUpDTO topUp(TopUpDTO topUpDTO) {
        try{

            User user = userRepository.findById(topUpDTO.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            user.setBalance(topUpDTO.getAmount().add(user.getBalance()));

            userRepository.save(user);

            return topUpDTO;

        } catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found", e.getCause());
        }
    }    
}
