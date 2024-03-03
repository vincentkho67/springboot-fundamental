package bytebrewers.bitpod.controller;

import bytebrewers.bitpod.service.AuthService;
import bytebrewers.bitpod.utils.constant.ApiUrl;
import bytebrewers.bitpod.utils.dto.ControllerResponse;
import bytebrewers.bitpod.utils.dto.Res;
import bytebrewers.bitpod.utils.dto.request.user.AuthRequest;
import bytebrewers.bitpod.utils.dto.request.user.RegisterDTO;
import bytebrewers.bitpod.utils.dto.response.user.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    
    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody RegisterDTO registerDTO){
        UserResponseDTO userResponse = authService.register(registerDTO);
        return Res.renderJson(userResponse, "register success", HttpStatus.CREATED);
    }


    
    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin (@RequestBody RegisterDTO registerDTO){
        UserResponseDTO userResponse = authService.registerAdmin(registerDTO);
        return Res.renderJson(userResponse, "register success", HttpStatus.CREATED);
    }

    @Operation(
        description = "Login user",
        summary = "Login user",
        responses = {
            @ApiResponse(
                description = "Login success",
                responseCode = "202",
                content = {
                    @io.swagger.v3.oas.annotations.media.Content(
                        mediaType = "application/json",
                        schema = @io.swagger.v3.oas.annotations.media.Schema(
                            // implementation = ControllerResponse.class,
                            type = "object",
                            example = """
                                    {
                                       "status": "Accepted",
                                       "message": "login success",
                                       "data" : "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"  
                                    }
                                    """
                        )
                    )
                }
                
            ),
            @ApiResponse(
                description = "Login failed",
                responseCode = "400",
                content = {
                    @io.swagger.v3.oas.annotations.media.Content(
                        mediaType = "application/json",
                        schema = @io.swagger.v3.oas.annotations.media.Schema(
                            // implementation = ControllerResponse.class,
                            type = "object",
                            example = """
                                    {
                                       "status": "Bad Request",
                                       "message": "login failed",
                                       "data" : "null"  
                                    }
                                    """
                        )
                    )
                }
            )
        }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){
        String token = authService.login(authRequest.getEmail(), authRequest.getPassword());
        return Res.renderJson(token, "login success", HttpStatus.ACCEPTED);
    }
}
