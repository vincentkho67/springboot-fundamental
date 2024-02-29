package bytebrewers.bitpod.service;

import java.util.List;

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
    List<User> getAllUser();
    UserDTO updateUser(UserDTO userDTO);
    void deleteUserByid(String id);
    TopUpDTO topUp (TopUpDTO topUpDTO);
}
