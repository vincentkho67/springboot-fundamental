package bytebrewers.bitpod.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import bytebrewers.bitpod.entity.Role;
import bytebrewers.bitpod.repository.RoleRepository;
import bytebrewers.bitpod.service.RoleService;
import bytebrewers.bitpod.utils.enums.ERole;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{
    
    private final RoleRepository roleRepository;

    @Override
    public Role getOrSave(ERole role) {
        Optional<Role> optionalRole = roleRepository.findByRole(role);
       if(optionalRole.isPresent()) {
           return optionalRole.get();
       }

       Role newRole = Role.builder().role(role).build();
       return roleRepository.save(newRole);
    }
    
}
