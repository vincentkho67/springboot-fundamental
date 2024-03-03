package bytebrewers.bitpod.controller;

import bytebrewers.bitpod.service.AuthService;
import bytebrewers.bitpod.utils.constant.ApiUrl;
import bytebrewers.bitpod.utils.dto.Res;
import bytebrewers.bitpod.utils.dto.request.user.AuthRequest;
import bytebrewers.bitpod.utils.dto.request.user.RegisterDTO;
import bytebrewers.bitpod.utils.dto.response.user.UserResponseDTO;
import bytebrewers.bitpod.utils.swagger.auth.SwaggerAuth;
import bytebrewers.bitpod.utils.swagger.auth.SwaggerAuthAdmin;
import bytebrewers.bitpod.utils.swagger.auth.SwaggerAuthLogin;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth", description = "Auth API")
public class AuthController {

     private final AuthService authService;
     @SwaggerAuth
    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody RegisterDTO registerDTO){
        UserResponseDTO userResponse = authService.register(registerDTO);
        return Res.renderJson(userResponse, "register success", HttpStatus.CREATED);
    }


   @SwaggerAuthAdmin
    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin (@RequestBody RegisterDTO registerDTO){
        UserResponseDTO userResponse = authService.registerAdmin(registerDTO);
        return Res.renderJson(userResponse, "register success", HttpStatus.CREATED);
    }

    @SwaggerAuthLogin
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){
        String token = authService.login(authRequest.getEmail(), authRequest.getPassword());
        return Res.renderJson(token, "login success", HttpStatus.ACCEPTED);
    }
}
