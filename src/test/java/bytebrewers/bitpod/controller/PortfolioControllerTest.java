package bytebrewers.bitpod.controller;

import bytebrewers.bitpod.entity.Bank;
import bytebrewers.bitpod.entity.Portfolio;
import bytebrewers.bitpod.entity.Stock;
import bytebrewers.bitpod.entity.User;
import bytebrewers.bitpod.util.Helper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@Transactional
@Slf4j
public class PortfolioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Helper helper;

    private static Portfolio portfolio;

    private static String token;


    @Order(1)
    @Test
    void loginAsSuperAdmin() throws Exception {
        token = Helper.loginAsSuperAdmin(mockMvc, objectMapper);
        assertNotNull(token);
    }

    @Order(2)
    @Test
    void index() throws Exception{
        User member = helper.createMember();
        portfolio = helper.createPortfolio(member);
        ResultActions result = Helper.getAll(mockMvc, "/api/portfolios", token);

        result.andDo(res -> {
            String jsonString = res.getResponse().getContentAsString();
            Map<String, Object> mapResponse = objectMapper.readValue(jsonString, new TypeReference<>(){});
            Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
            List<Map<String, Object>> content = (List<Map<String, Object>>) data.get("content");
            assertNotNull(content);
            assertFalse(content.isEmpty());
        });
    }

    @Order(3)
    @Test
    void show() throws Exception{
        User member = helper.createMember();
        portfolio = helper.createPortfolio(member);
        ResultActions result = Helper.getById(mockMvc, "/api/portfolios/", token, portfolio.getId());
        result.andDo(res -> {
            String jsonString = res.getResponse().getContentAsString();
            Map<String, Object> mapResponse = objectMapper.readValue(jsonString, new TypeReference<>(){});
            Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
            assertNotNull(data);
        });
    }

//    @Order(4)
//    @Test
//    void showByCurrentUser() throws Exception {
//        token = Helper.loginAsSuperAdmin(mockMvc, objectMapper);
//
//        Map<String, Object> bankReq = new HashMap<>();
//        bankReq.put("name", "Mandiri");
//        bankReq.put("address", "Jl. Jendral Sudirman No. 1");
//
//        Helper.postBank(mockMvc, objectMapper, token, bankReq);
//        String bankId = Helper.extractFirstIdFromResponse(Helper.getAllBanks(mockMvc, token), objectMapper);
//
//        Helper.registerMember(mockMvc, objectMapper);
//        String tryToken = Helper.loginAsMember(mockMvc, objectMapper, "member@email.com", "member");
//        log.info("INIIII TOKENNNNNNNNNNNNNNNNNNNNN: {}", tryToken);
//        User user = helper.findByToken(tryToken);
//        log.info("User: {}", user.getEmail());
//
//        Helper.topUp(mockMvc, objectMapper, token);
//
//        Helper.fetchAndCreateStock(mockMvc, token);
//        String stockId = Helper.extractFirstIdFromResponse(Helper.fetchStocksOnDB(mockMvc, token), objectMapper);
//
//
//        Map<String, Object> req = new HashMap<>();
//        req.put("price", 0);
//        req.put("lot", 10);
//        req.put("transactionType", "BUY");
//        req.put("stockId", stockId);
//        req.put("bankId", bankId);
//
//        Helper.createTransaction(mockMvc, objectMapper, token, req);
//
//        ResultActions result = Helper.getWithToken(mockMvc, "/api/portfolios/current", token);
//        result.andDo(res -> {
//            String jsonString = res.getResponse().getContentAsString();
//            Map<String, Object> mapResponse = objectMapper.readValue(jsonString, new TypeReference<>(){});
//            Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
//            assertNotNull(data);
//        });
//    }

    @Order(4)
    @Test
    void delete() throws Exception {
        User member = helper.createMember();
        portfolio = helper.createPortfolio(member);
        token = Helper.loginAsSuperAdmin(mockMvc, objectMapper);

        ResultActions result = Helper.delete(mockMvc, "/api/portfolios/", token, portfolio.getId());
        result.andExpectAll(status().isOk());

    }
}