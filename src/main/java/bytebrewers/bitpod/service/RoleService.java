package bytebrewers.bitpod.service;

import bytebrewers.bitpod.entity.Role;
import bytebrewers.bitpod.utils.enums.ERole;

public interface RoleService {
    Role getOrSave(ERole role);
}
