package bytebrewers.bitpod.service;

import bytebrewers.bitpod.utils.dto.request.user.AuthRequest;
import bytebrewers.bitpod.utils.dto.response.user.UserResponseDTO;

public interface AuthService {
    public String login(String email, String password);
    UserResponseDTO register(AuthRequest authRequest);
    UserResponseDTO registerAdmin(AuthRequest authRequest);
    
}
