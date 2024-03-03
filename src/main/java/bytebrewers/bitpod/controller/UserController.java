package bytebrewers.bitpod.controller;

import bytebrewers.bitpod.utils.constant.ApiUrl;
import bytebrewers.bitpod.utils.constant.Messages;
import bytebrewers.bitpod.utils.dto.PageResponseWrapper;
import bytebrewers.bitpod.utils.dto.response.user.UserBasicFormat;
import bytebrewers.bitpod.utils.swagger.user.SwaggerUserUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.midtrans.httpclient.error.MidtransError;
import bytebrewers.bitpod.entity.User;
import bytebrewers.bitpod.service.UserService;
import bytebrewers.bitpod.utils.dto.Res;
import bytebrewers.bitpod.utils.dto.request.user.TopUpDTO;
import bytebrewers.bitpod.utils.dto.request.user.TopUpSnapDTO;
import bytebrewers.bitpod.utils.dto.request.user.UserDTO;
import bytebrewers.bitpod.utils.dto.response.user.TopUpMidtransresponseDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiUrl.BASE_URL + ApiUrl.BASE_USER)
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UserController{
    private final UserService userService;

    @SwaggerUserUpdate
    @PutMapping(
        consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MEMBER')")
    public ResponseEntity<?> update(
            @RequestHeader(name = "Authorization") String token,
            @ModelAttribute UserDTO userDTO
    ){
        return Res.renderJson(userService.updateUser(userDTO, token), Messages.USER_UPDATED, HttpStatus.OK);
    }    

    @Operation(
        description = "Get all user",
        summary = "Get all user (ADMIN ONLY)"
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> index(
            @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute UserDTO req
    ){
        Page<UserBasicFormat> res = userService.getAllUser(pageable, req);
        PageResponseWrapper<UserBasicFormat> responseWrapper = new PageResponseWrapper<>(res);
        return Res.renderJson(responseWrapper, Messages.USER_FOUND, HttpStatus.OK);
    }

    @Operation(
        description = "Top up backup",
        summary = "Top up backup (ADMIN ONLY)"
    )
    @PostMapping("/topup")
    public ResponseEntity<?> topup(@RequestBody TopUpDTO topUpDTO,
                                   @RequestHeader(name = "Authorization") String token
    ){
        TopUpDTO topUp = userService.topUp(topUpDTO, token);
        return Res.renderJson(topUp, "topup success", HttpStatus.OK);
    }


    @Operation(
        description = "get User By id",
        summary = "get User By id",
        responses = {
            @ApiResponse(
                description = "User Found",
                responseCode = "200",
                content = {
                    @io.swagger.v3.oas.annotations.media.Content(
                        mediaType = "application/json",
                        schema = @io.swagger.v3.oas.annotations.media.Schema(
                            // implementation = ControllerResponse.class,
                            type = "object",
                            example = """
                                    {
                                        "status": "OK",
                                        "message": "User found",
                                        "data": {
                                          "discardedAt": null,
                                          "id": "52aa27d3-0742-4c50-ac5c-eb4629942287",
                                          "name": "admin123",
                                          "balance": 432000,
                                          "username": "admin@gmail.com",
                                          "email": "admin@gmail.com",
                                          "password": "$2a$10$w2M60rHtAwgDKClR9bTblOSOxDNb42hQxZgqwI1ej4CI03fmk57FC",
                                          "address": "jakarta",
                                          "birthDate": "1999-12-31",
                                          "profilePicture": "http://res.cloudinary.com/de0yidcs5/image/upload/v1709373744/yzpr3mb7ae0xdjjt4ps0.jpg",
                                          "roles": [
                                            {
                                              "id": "6119a9a2-be19-40fb-907f-956a69e0850c",
                                              "role": "ROLE_MEMBER"
                                            },
                                            {
                                              "id": "5146315d-df56-42e3-9f34-82ac8e8e0d10",
                                              "role": "ROLE_ADMIN"
                                            }
                                          ],
                                          "portfolio": null,
                                          "enabled": true,
                                          "authorities": [
                                            {
                                              "authority": "ROLE_MEMBER"
                                            },
                                            {
                                              "authority": "ROLE_ADMIN"
                                            }
                                          ],
                                          "accountNonLocked": true,
                                          "accountNonExpired": true,
                                          "credentialsNonExpired": true
                                        }
                                    }
                                    """
                        )
                    )
                }
                
            ),
            @ApiResponse(
                description = "User not found",
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
                                        "message": "User Not Found",
                                        "data": null
                                          
                                    }
                                    """
                        )
                    )
                }
            )
        }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        User user = userService.findUserById(id);
        return Res.renderJson(user, "User found", HttpStatus.OK);
    }

    @Operation(
        description = "Top up midtrans",
        summary = "Top up mditrans"
    )
    @PostMapping("/topup/midtrans")
    public ResponseEntity<?> topUpViaMidtrans(@RequestBody TopUpSnapDTO topUpSnapDTO,
                                              @RequestHeader(name = "Authorization") String token
    ) throws MidtransError{
        TopUpMidtransresponseDTO topUpMidtransresponseDTO = userService.topUpViaMidtrans(topUpSnapDTO, token);
        return Res.renderJson(topUpMidtransresponseDTO, "Proceed to payment", HttpStatus.OK);
    }
}
