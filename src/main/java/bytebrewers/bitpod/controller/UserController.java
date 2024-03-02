package bytebrewers.bitpod.controller;

import bytebrewers.bitpod.utils.constant.ApiUrl;
import bytebrewers.bitpod.utils.constant.Messages;
import bytebrewers.bitpod.utils.dto.PageResponseWrapper;
import bytebrewers.bitpod.utils.dto.response.user.UserBasicFormat;
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
public class UserController{
    private final UserService userService;

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
    
    @PostMapping("/topup")
    public ResponseEntity<?> topup(@RequestBody TopUpDTO topUpDTO,
                                   @RequestHeader(name = "Authorization") String token
    ){
        TopUpDTO topUp = userService.topUp(topUpDTO, token);
        return Res.renderJson(topUp, "topup success", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        User user = userService.findUserById(id);
        return Res.renderJson(user, "User found", HttpStatus.OK);
    }

    @PostMapping("/topup/midtrans")
    public ResponseEntity<?> topUpViaMidtrans(@RequestBody TopUpSnapDTO topUpSnapDTO,
                                              @RequestHeader(name = "Authorization") String token
    ) throws MidtransError{
        TopUpMidtransresponseDTO topUpMidtransresponseDTO = userService.topUpViaMidtrans(topUpSnapDTO, token);
        return Res.renderJson(topUpMidtransresponseDTO, "Proceed to payment", HttpStatus.OK);
    }
}
