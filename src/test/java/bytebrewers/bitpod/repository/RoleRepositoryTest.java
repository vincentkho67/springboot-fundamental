package bytebrewers.bitpod.repository;

import bytebrewers.bitpod.entity.Role;
import bytebrewers.bitpod.utils.enums.ERole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class RoleRepositoryTest {
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        Role role = new Role();
        role.setRole(ERole.ROLE_MEMBER);
        role.setRole(ERole.ROLE_ADMIN);
        role.setRole(ERole.ROLE_SUPER_ADMIN);
    }
    @Test
    public void givenRoleObject_whenGetRole_thenReturnRole() {
    assertEquals(3, ERole.values().length);
    assertEquals(ERole.ROLE_MEMBER, ERole.valueOf("ROLE_MEMBER"));
    assertEquals(ERole.ROLE_ADMIN, ERole.valueOf("ROLE_ADMIN"));
    assertEquals(ERole.ROLE_SUPER_ADMIN, ERole.valueOf("ROLE_SUPER_ADMIN"));
    }
    @Test
    public void givenRoleObject_whenRoleToString_thenReturnRole() {
        Assertions.assertEquals("ROLE_MEMBER", ERole.ROLE_MEMBER.toString());
        Assertions.assertEquals("ROLE_ADMIN", ERole.ROLE_ADMIN.toString());
        Assertions.assertEquals("ROLE_SUPER_ADMIN", ERole.ROLE_SUPER_ADMIN.toString());

    }

}