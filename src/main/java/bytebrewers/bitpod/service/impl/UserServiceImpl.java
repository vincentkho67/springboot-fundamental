package bytebrewers.bitpod.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        try{

            User user = userRepository.findById(userDTO.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            user.setId(userDTO.getId());
            user.setName(userDTO.getName());
            user.setUsername(userDTO.getUsername());
            user.setAddress(userDTO.getAddress());
            user.setBirthDate(userDTO.getBirthDate());
            user.setProfilePicture(userDTO.getProfilePicture());

            userRepository.save(user);

            return userDTO;

        } catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found", e.getCause());
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
