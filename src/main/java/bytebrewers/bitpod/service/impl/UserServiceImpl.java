package bytebrewers.bitpod.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import bytebrewers.bitpod.security.JwtUtils;
import bytebrewers.bitpod.utils.dto.response.user.JwtClaim;
import bytebrewers.bitpod.utils.dto.response.user.TopUpMidtransresponseDTO;
import bytebrewers.bitpod.utils.dto.response.user.UserBasicFormat;
import bytebrewers.bitpod.utils.specification.GeneralSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;

import bytebrewers.bitpod.entity.Auditable;
import bytebrewers.bitpod.entity.Stock;
import bytebrewers.bitpod.entity.User;
import bytebrewers.bitpod.repository.UserRepository;
import bytebrewers.bitpod.service.UserService;
import bytebrewers.bitpod.utils.dto.request.stock.StockDTO;
import bytebrewers.bitpod.utils.dto.request.user.TopUpDTO;
import bytebrewers.bitpod.utils.dto.request.user.TopUpSnapDTO;
import bytebrewers.bitpod.utils.dto.request.user.UserDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    
    private final UserRepository userRepository;

    private final Cloudinary cloudinary;

    private final JwtUtils jwt;

    private final RestTemplate restTemplate;

    private final ExecutorService executorService;

    @Override
    public TopUpMidtransresponseDTO topUpViaMidtrans(TopUpSnapDTO topUpSnapDTO, String userToken) throws MidtransError {
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

        executorService.submit(() -> checkStatus(token, topUpSnapDTO.getGross_amount(), userToken));
        return topUpMidtransresponseDTO;
    }
    public Map<?, ?> upload(MultipartFile multipartFile) throws IOException{
        Map<?, ?> result = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
        return result;
    }
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }
    @Override
    public User findUserById(String id) {
        return Auditable.searchById(userRepository.findById(id), "User Not Found");
    }
    @Override
    public Page<UserBasicFormat> getAllUser(Pageable pageable, UserDTO req) {
        Specification<User> specification = GeneralSpecification.getSpecification(req);
         Page<User> users = userRepository.findAll(specification, pageable);
         return users.map(UserBasicFormat::fromUser);
    }
    @Override
    public UserBasicFormat updateUser(UserDTO userDTO, String token) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Map<?,?> result = upload(userDTO.getImage());

            User user = getUserDetails(token);
            user.setName(userDTO.getName());
            user.setUsername(userDTO.getUsername());
            user.setAddress(userDTO.getAddress());
            Date dateOfBirth = dateFormat.parse(userDTO.getBirthDate());
            user.setBirthDate(dateOfBirth);
            user.setProfilePicture(result.get("url").toString());
            User updatedUser = userRepository.save(user);

            return UserBasicFormat.fromUser(updatedUser);
        } catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "failed updated user", e.getCause());
        }
    }
    @Override
    public void deleteUserByid(String id) {
        userRepository.deleteById(id);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
    }
    @Override
    public User loadByUserId(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
    }

    @Override
    public TopUpDTO topUp(TopUpDTO topUpDTO, String token) {
        try{

            User user = getUserDetails(token);
            assert user != null;
            user.setBalance(topUpDTO.getAmount().add(user.getBalance()));

            userRepository.save(user);

            return topUpDTO;

        } catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found", e.getCause());
        }
    }

    @Override
    public User getUserDetails(String token) {
        String parsedToken = null;
        if(token != null && token.startsWith("Bearer ")) {
            parsedToken = token.substring(7);
        }

        JwtClaim user = jwt.getUserInfoByToken(parsedToken);
        return loadByUserId(user.getUserId());
    }

    private void checkStatus(String token, Integer gross_amount, String userId) {
        User user = getUserDetails(userId);
        for(int i = 1; i <= 20; i++){
            try{
                ResponseEntity<JsonNode> res = restTemplate.getForEntity(
                        "https://app.sandbox.midtrans.com/snap/v1/transactions/"+token+"/status",
                        JsonNode.class
                );
                JsonNode response = res.getBody();
                if (response != null && response.has("channel_response_message")) {
                    String channelResponseMessage = response.get("channel_response_message").asText();
                    if ("Approved".equals(channelResponseMessage)) {
                        // Update user balance logic here
                        BigDecimal userBalance = user.getBalance();
                        BigDecimal totalTopUp = BigDecimal.valueOf(gross_amount);
                        BigDecimal totalBalance = userBalance.add(totalTopUp);
                        user.setBalance(totalBalance);
                        userRepository.save(user);
                        break;
                    }
                }
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException("Midtrans timeout, Balance top up failed");
            }
        }
    }
}