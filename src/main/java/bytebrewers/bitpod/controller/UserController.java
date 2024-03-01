package bytebrewers.bitpod.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import bytebrewers.bitpod.utils.constant.ApiUrl;
import bytebrewers.bitpod.utils.constant.Messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransSnapApi;

import bytebrewers.bitpod.entity.Auditable;
import bytebrewers.bitpod.entity.Bank;
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
public class UserController extends Auditable{

    private final UserService userService;

    @PutMapping(
        consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MEMBER')")
    public ResponseEntity<?> update(@ModelAttribute UserDTO userDTO){
        userService.updateUser(userDTO);
        return Res.renderJson(userService.findUserById(userDTO.getId()).getName(), Messages.USER_UPDATED, HttpStatus.OK);
    }    

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public List<User> index(){
        return userService.getAllUser();
    }
    
    @PostMapping("/topup")
    public ResponseEntity<?> topup(@RequestBody TopUpDTO topUpDTO){
        TopUpDTO topUp = userService.topUp(topUpDTO);
        return Res.renderJson(topUp, "topup success", HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        User user = userService.findUserById(id);
        return Res.renderJson(user, "User found", HttpStatus.OK);
    }

    @PostMapping("/topup/midtrans")
    public ResponseEntity<?> topUpViaMidtrans(@RequestBody TopUpSnapDTO topUpSnapDTO) throws MidtransError{

        Midtrans.clientKey = "SB-Mid-client-OBiBigdvoAu3vL7h";
        Midtrans.serverKey = "SB-Mid-server-Y3PMGdICvF-bWAz5hqH8BpUV";
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
        topUpMidtransresponseDTO.setToken("https://app.sandbox.midtrans.com/snap/v3/redirection/"+SnapApi.createTransactionToken(requestBody));
        

        return Res.renderJson(topUpMidtransresponseDTO, "Topup success", HttpStatus.OK);
    }

    
}
