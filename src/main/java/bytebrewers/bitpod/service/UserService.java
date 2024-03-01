package bytebrewers.bitpod.service;

import bytebrewers.bitpod.utils.dto.response.user.UserBasicFormat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import bytebrewers.bitpod.entity.User;
import bytebrewers.bitpod.utils.dto.request.user.TopUpDTO;
import bytebrewers.bitpod.utils.dto.request.user.UserDTO;

public interface UserService extends UserDetailsService{
    User loadByUserId(String userId);
    public User createUser(User user);
    User findUserById(String id);
    Page<User> getAllUser(Pageable pageable, UserDTO userDTO);
    UserBasicFormat updateUser(UserDTO userDTO, String token);
    void deleteUserByid(String id);
    TopUpDTO topUp (TopUpDTO topUpDTO, String token);
}
