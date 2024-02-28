package bytebrewers.bitpod.repository;

import bytebrewers.bitpod.entity.Portfolio;
import bytebrewers.bitpod.entity.Role;
import bytebrewers.bitpod.utils.enums.ERole;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepository extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByRole(ERole role);
}
