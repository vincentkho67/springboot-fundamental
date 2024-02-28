package bytebrewers.bitpod.controller;

import bytebrewers.bitpod.service.AuthService;
import bytebrewers.bitpod.utils.constant.ApiUrl;
import bytebrewers.bitpod.utils.dto.ControllerResponse;
import bytebrewers.bitpod.utils.dto.Res;
import bytebrewers.bitpod.utils.dto.request.user.AuthRequest;
import bytebrewers.bitpod.utils.dto.response.user.UserResponseDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.BASE_AUTH)
@RequiredArgsConstructor
public class AuthController {

     private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody AuthRequest authRequest){
        UserResponseDTO userResponse = authService.register(authRequest);
        return Res.renderJson(userResponse, "register success", HttpStatus.CREATED);

    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin (@RequestBody AuthRequest authRequest){
        UserResponseDTO userResponse = authService.registerAdmin(authRequest);
        return Res.renderJson(userResponse, "register success", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){
        String token = authService.login(authRequest.getEmail(), authRequest.getPassword());
        return Res.renderJson(token, "login success", HttpStatus.ACCEPTED);
    }
}
