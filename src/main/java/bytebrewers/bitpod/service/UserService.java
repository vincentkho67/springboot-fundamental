package bytebrewers.bitpod.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import bytebrewers.bitpod.entity.User;

public interface UserService extends UserDetailsService{
    User loadByUserId(String userId);
}
