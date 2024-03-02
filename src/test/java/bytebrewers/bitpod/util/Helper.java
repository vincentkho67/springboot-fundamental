package bytebrewers.bitpod.util;
import bytebrewers.bitpod.entity.*;
import bytebrewers.bitpod.repository.*;
import bytebrewers.bitpod.service.UserService;
import bytebrewers.bitpod.utils.enums.ERole;
import bytebrewers.bitpod.utils.enums.ETransactionType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Getter
@Setter
@Component
public class Helper {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final TransactionRepository transactionRepository;
    private final BankRepository bankRepository;
    private final PortfolioRepository portfolioRepository;
    private final RoleRepository roleRepository;

    public Helper(PasswordEncoder passwordEncoder, UserRepository userRepository, StockRepository stockRepository, TransactionRepository transactionRepository, BankRepository bankRepository, PortfolioRepository portfolioRepository, RoleRepository roleRepository, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
        this.transactionRepository = transactionRepository;
        this.bankRepository = bankRepository;
        this.portfolioRepository = portfolioRepository;
        this.roleRepository = roleRepository;
    }

    // TODO: FOR USER START
    public User createMember() {
        String hashPassword = passwordEncoder.encode("member");
        Role member = roleRepository.save(roleMember);
         User user = User.builder()
                .email("member@email.com")
                .password(hashPassword)
                .name("member")
                .username("member")
                .address("jakarta")
                .roles(List.of(member))
                .build();

         return userRepository.save(user);
    }
    public User createAdmin() {
        String hashPassword = passwordEncoder.encode("member");
        User user = User.builder()
                .email("admin@email.com")
                .password(hashPassword)
                .name("admin")
                .username("admin")
                .address("jakarta")
                .roles(List.of(roleAdmin))
                .build();

        return userRepository.save(user);
    }
    public static ResultActions registerAdmin(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        return registerUser(
                mockMvc,
                objectMapper,
                "/auth/register/admin",
                "admin2@gmail.com",
                "admin2",
                "admin2",
                "admin2",
                "jakarta",
                "2000-01-01"
        );
    }
    public static ResultActions registerMember(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        return registerUser(
                mockMvc,
                objectMapper,
                "/auth/register",
                "member@email.com",
                "member",
                "member",
                "member",
                "jakarta",
                "2000-01-01"
        );
    }
    private static ResultActions registerUser(MockMvc mockMvc, ObjectMapper objectMapper, String endpoint, String email, String password, String name, String username, String address, String birthDate) throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("email", email);
        req.put("password", password);
        req.put("name", name);
        req.put("username", username);
        req.put("address", address);
        req.put("birthDate", birthDate);

        return Helper.postWithoutHeader(mockMvc, objectMapper, endpoint, req);
    }
    public static String loginAsSuperAdmin(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("email", "superadmin@gmail.com");
        req.put("password", "superadmin");

        ResultActions result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isAccepted());

        String jsonString = result.andReturn().getResponse().getContentAsString();
        Map<String, Object> mapResponse = objectMapper.readValue(jsonString, new TypeReference<>(){});

        String token = mapResponse.get("data").toString();
        assertNotNull(token);

        return token;
    }
    public static String loginAsMember(MockMvc mockMvc, ObjectMapper objectMapper, String email, String password) throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("email", email);
        req.put("password", password);

        ResultActions result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isAccepted());  // Adjust the status expectation based on your application's behavior

        String jsonString = result.andReturn().getResponse().getContentAsString();
        Map<String, Object> mapResponse = objectMapper.readValue(jsonString, new TypeReference<>(){});

        return mapResponse.get("data").toString();
    }

    public static ResultActions topUp(MockMvc mockMvc, ObjectMapper objectMapper, String token) throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("amount", 1000000);
        return postWithHeader(mockMvc, objectMapper, "/api/users/topup", token, req);
    }

    public Role roleMember = Role.builder().role(ERole.ROLE_MEMBER).build();
    public Role roleAdmin = Role.builder().role(ERole.ROLE_ADMIN).build();
    public Role roleSuperAdmin = Role.builder().role(ERole.ROLE_SUPER_ADMIN).build();

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // TODO: STOCK HELPER
    public Stock createStock() {
        Stock stock = Stock.builder()
                .name("stock")
                .company("company")
                .price(1000.0)
                .lot(1000)
                .build();
        return stockRepository.save(stock);
    }

    public static ResultActions fetchAndCreateStock(MockMvc mockMvc, String token) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/api/stocks/fetch")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    public static ResultActions fetchStocksOnDB(MockMvc mockMvc, String token) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/api/stocks")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    // TODO: PORTFOLIO HELPER
    public Portfolio createPortfolio(User user) {
        Portfolio portfolio = Portfolio.builder()
                .avgBuy(BigDecimal.valueOf(1000.0))
                .returns("1000")
                .user(user)
                .build();
        return portfolioRepository.save(portfolio);
    }

    // TODO: TRANSACTION HELPER
    public Transaction createBuyTransaction(Stock stock, Portfolio portfolio, Bank bank) {
        return Transaction.builder()
                .price(1000.0)
                .lot(1000)
                .transactionType(buy)
                .stock(stock)
                .portfolio(portfolio)
                .bank(bank)
                .build();
    }

    public Transaction createSellTransaction(Stock stock, Portfolio portfolio, Bank bank) {
        return Transaction.builder()
                .price(1000.0)
                .lot(5)
                .transactionType(sell)
                .stock(stock)
                .portfolio(portfolio)
                .bank(bank)
                .build();
    }

    public ETransactionType buy = ETransactionType.BUY;
    public ETransactionType sell = ETransactionType.SELL;

    public static ResultActions createTransaction(MockMvc mockMvc, ObjectMapper objectMapper, String token, Map<String, Object> requestParam) throws Exception {
        return postWithHeader(mockMvc, objectMapper, "/api/transactions/buy-or-sell", token, requestParam);
    }

    // TODO: BANK HELPER
    public static ResultActions postBank(MockMvc mockMvc, ObjectMapper objectMapper, String token, Map<String, Object> requestContent) throws Exception {
        return postWithHeader(mockMvc, objectMapper, "/api/banks", token, requestContent);
    }

    public static ResultActions getAllBanks(MockMvc mockMvc, String token) throws Exception {
        return getAll(mockMvc, "/api/banks", token);
    }

    // TODO: FOR HITTING ENDPOINT START
    public static ResultActions postWithHeader(MockMvc mockMvc, ObjectMapper objectMapper, String endpoint, String token, Map<String, Object> requestContent) throws Exception {
        return mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(requestContent)))
                .andExpect(status().isCreated());
    }

    public static ResultActions postWithoutHeader(MockMvc mockMvc, ObjectMapper objectMapper, String endpoint, Map<String, Object> requestContent) throws Exception {
        return mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(requestContent)))
                .andExpect(status().isCreated());
    }

    public static ResultActions getAll(MockMvc mockMvc, String url, String token) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    public static ResultActions getById(MockMvc mockMvc, String url, String token, String id) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url + "/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    public static ResultActions getWithToken(MockMvc mockMvc, String url, String token) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    public static ResultActions update(MockMvc mockMvc, ObjectMapper objectMapper, String url, String token, String id, Map<String, Object> req) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(url + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    public static ResultActions delete(MockMvc mockMvc, String url, String token, String id) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(url + "/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    // TODO: FOR HITTING ENDPOINT END


    // TODO: Extract first Id from Response
    public static String extractFirstIdFromResponse(ResultActions resultActions, ObjectMapper objectMapper) throws Exception {
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        Map<String, Object> responseMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});

        if (responseMap.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) responseMap.get("data");

            if (data.containsKey("content")) {
                List<Map<String, Object>> contentList = (List<Map<String, Object>>) data.get("content");

                if (!contentList.isEmpty()) {
                    Map<String, Object> firstItem = contentList.get(0);

                    if (firstItem.containsKey("id")) {
                        return firstItem.get("id").toString();
                    }
                }
            }
        }

        throw new RuntimeException("Failed to extract the first 'id' from the response.");
    }

}
