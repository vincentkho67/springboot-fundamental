package bytebrewers.bitpod.controller;

import bytebrewers.bitpod.entity.*;
import bytebrewers.bitpod.util.Helper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@Transactional
@Slf4j
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Helper helper;

    private Stock stock;
    private Bank bank;

    private Portfolio portfolio;

    private Transaction transaction;

    private String token;

    private User member;



    @BeforeEach
    void setUp() throws Exception {
        token = Helper.loginAsSuperAdmin(mockMvc, objectMapper);
        member = helper.createMember();
        stock = helper.createStock();
        bank = helper.createBank();
        portfolio = helper.createPortfolio(member);
        transaction = helper.createBuyTransaction(stock, portfolio, bank);
    }

    @Test
    void index() throws Exception {
        ResultActions result = Helper.getAll(mockMvc, "/api/transactions", token);
        Helper.processResult(result, objectMapper);
    }

    @Test
    void show() throws Exception {
        Helper.showById(mockMvc, "/api/transactions/", token, transaction.getId(), objectMapper);
    }


    @Test
    void create() throws Exception {
        ResultActions result = Helper.getAll(mockMvc, "/api/transactions", token);
        Helper.processResult(result, objectMapper);
    }

    @Test
    void delete() throws Exception {
        ResultActions result = Helper.delete(mockMvc, "/api/transactions/", token, transaction.getId());
        result.andExpectAll(status().isOk());
    }
}