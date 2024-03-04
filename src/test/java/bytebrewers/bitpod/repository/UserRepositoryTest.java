package bytebrewers.bitpod.repository;

import bytebrewers.bitpod.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("abdul");
        user.setEmail("abdul@email.com");

    }

    @Test
    public void givenUserObject_whenSaveUser_thenReturnUser() {
        userRepository.save(user);
        assertNotNull(user.getEmail());
    }

    @Test
    public void givenUserObject_whenFindUserByEmail_thenReturnUser() {
        userRepository.save(user);
        Optional<User> findByEmail = userRepository.findByEmail("abdul@email.com");
        assertEquals(user.getEmail(), findByEmail.get().getEmail());
    }
    @Test
    public void givenUserObject_whenUpdateUser_thenReturnUser(){
        userRepository.save(user);
        user.setName("abdul fatah");
        User savedUser = userRepository.save(user);
        assertEquals(savedUser.getName(),user.getName());
    }

    @Test
    public void givenUserObject_whenDeleteUser_thenReturnNull(){
        userRepository.save(user);
        userRepository.delete(user);
        assertNotNull(userRepository.findById(user.getId()));

    }

}