package bytebrewers.bitpod.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import bytebrewers.bitpod.entity.Role;
import bytebrewers.bitpod.repository.RoleRepository;
import bytebrewers.bitpod.utils.enums.ERole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrSaveRoleExists() {
        ERole expectedRole = ERole.ROLE_MEMBER;
        Role existingRole = Role.builder().role(expectedRole).build();

        when(roleRepository.findByRole(expectedRole)).thenReturn(Optional.of(existingRole));
        Role resultRole = roleService.getOrSave(expectedRole);

        assertEquals(existingRole, resultRole);
        verify(roleRepository, times(1)).findByRole(expectedRole);
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void testGetOrSaveRoleNotExists() {
        ERole expectedRole = ERole.ROLE_ADMIN;
        Role existRole = null;
        if (roleRepository.findByRole(expectedRole).isEmpty()) {
            existRole = Role.builder().role(expectedRole).build();
            roleRepository.save(existRole);
        }
        Role resultRole = existRole;

        assertNotNull(resultRole);
        assertEquals(expectedRole, resultRole.getRole());
        verify(roleRepository, times(1)).findByRole(expectedRole);
        verify(roleRepository, times(1)).save(any(Role.class));
    }
}