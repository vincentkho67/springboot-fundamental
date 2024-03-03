package bytebrewers.bitpod.service.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import bytebrewers.bitpod.entity.User;
import bytebrewers.bitpod.repository.UserRepository;
import bytebrewers.bitpod.utils.dto.request.user.UserDTO;
import bytebrewers.bitpod.utils.dto.response.user.UserBasicFormat;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testFindByIdSuccess() {
        String id = UUID.randomUUID().toString();
        User mockUser = Mockito.mock(User.class); 
        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));

        User result = userService.findUserById(id);

        assertSame(mockUser, result);
    }

    @Test
    void testCreateUserSuccess() {

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("test");
        user.setBalance(BigDecimal.valueOf(0));
        user.setUsername("test");
        user.setEmail("test@gmail.com");
        user.setPassword("test");
        user.setAddress("test");

        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        
        User savedEmployee = userService.createUser(user);

        Assertions.assertThat(savedEmployee).isNotNull();
    }

    @Test  
    void testGetAllUserSuccess() {

        // Page<User> users = Mockito.mock(Page.class);

        // when(userRepository.findAll(Mockito.any(Pageable.class))).thenReturn(users);

        // Pageable pageable = Mockito.mock(Pageable.class);
        // UserDTO userDTO = Mockito.mock(UserDTO.class);

        // Page<UserBasicFormat> userRes = userService.getAllUser(pageable, userDTO);

        // Assertions.assertThat(userRes).isNotNull();
    }

    @Test
    void testUpdateUserSuccess() {
        // String token = UUID.randomUUID().toString();
        // User user = new User();
        // user.setId(UUID.randomUUID().toString());
        // user.setName("test");
        // user.setBalance(BigDecimal.valueOf(0));
        // user.setUsername("test");
        // user.setEmail("test@gmail.com");
        // user.setPassword("test");
        // user.setAddress("test");

        // UserDTO userDTO = new UserDTO();
        // userDTO.setName("test");
        // userDTO.setUsername("test");
        // userDTO.setAddress("test");
        // userDTO.setBirthDate("2000-01-01");

        // when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        // when(userRepository.save(user)).thenReturn(user);
    
        // UserBasicFormat userUpdate = userService.updateUser(userDTO,token);
    
        // Assertions.assertThat(userUpdate).isNotNull();
    }

    @Test
    void testDeleteUserSuccess() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("test");
        user.setBalance(BigDecimal.valueOf(0));
        user.setUsername("test");
        user.setEmail("test@gmail.com");
        user.setPassword("test");
        user.setAddress("test");

        doNothing().when(userRepository).deleteById(user.getId());
        userService.deleteUserByid(user.getId());
        
        assertAll(() -> userService.deleteUserByid(user.getId()));
    }

    @Test
    void testLoadUserByUsernameSucces() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("test");
        user.setBalance(BigDecimal.valueOf(0));
        user.setUsername("test");
        user.setEmail("test@gmail.com");
        user.setPassword("test");
        user.setAddress("test");

        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));

        UserDetails userRes = userService.loadUserByUsername("test@gmail.com");
        Assertions.assertThat(userRes).isNotNull();
    }

    @Test
    void testGetUserDetailsSucces() {
        // String token = UUID.randomUUID().toString();
        // User user = new User();
        // user.setId(UUID.randomUUID().toString());
        // user.setName("test");
        // user.setBalance(BigDecimal.valueOf(0));
        // user.setUsername("test");
        // user.setEmail("test@gmail.com");
        // user.setPassword("test");
        // user.setAddress("test");

        // when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        // User userMock = Optional.of(user).get();
        // when(userService.loadByUserId(Mockito.anyString())).thenReturn(userMock);
        
        // User userRes = userService.getUserDetails(token);
        // Assertions.assertThat(userRes).isNotNull();
    }
}
