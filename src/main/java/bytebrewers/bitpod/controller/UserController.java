package bytebrewers.bitpod.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import bytebrewers.bitpod.utils.constant.ApiUrl;
import bytebrewers.bitpod.utils.constant.Messages;
import bytebrewers.bitpod.utils.dto.PageResponseWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransSnapApi;
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
        Page<User> res = userService.getAllUser(pageable, req);
        PageResponseWrapper<User> responseWrapper = new PageResponseWrapper<>(res);
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
    public ResponseEntity<?> topUpViaMidtrans(@RequestBody TopUpSnapDTO topUpSnapDTO) throws MidtransError{

        Midtrans.clientKey = "SB-Mid-client-mmalMqC0Sqqv81WL";
        Midtrans.serverKey = "SB-Mid-server-v6MMOtdWj5g1EG3lAQihiYAV";
        // Get ClientKey from Midtrans Configuration class
        String clientKey = Midtrans.getClientKey();

        UUID idRand = UUID.randomUUID();
        Map<String, Object> requestBody = new HashMap<>();
        
        Map<String, String> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", idRand.toString());
        transactionDetails.put("gross_amount", topUpSnapDTO.getGross_amount().toString());
        
        Map<String, String> creditCard = new HashMap<>();
        creditCard.put("secure", "true");

        requestBody.put("transaction_details", transactionDetails);
        requestBody.put("credit_card", creditCard);

        TopUpMidtransresponseDTO topUpMidtransresponseDTO = new TopUpMidtransresponseDTO();
        topUpMidtransresponseDTO.setRequestBody(requestBody);
        topUpMidtransresponseDTO.setClientKey(clientKey);
        String token = SnapApi.createTransactionToken(requestBody);
        topUpMidtransresponseDTO.setToken("https://app.sandbox.midtrans.com/snap/v3/redirection/"+token);
//        https://app.sandbox.midtrans.com/snap/v1/transactions/{token}/status


        return Res.renderJson(topUpMidtransresponseDTO, "Topup success", HttpStatus.OK);
    }
// 4811 1111 1111 1114 CC accepted
    
}
