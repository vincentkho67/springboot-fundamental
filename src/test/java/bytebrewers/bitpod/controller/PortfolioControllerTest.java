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


    @BeforeEach
    @Test
    void loginAsSuperAdmin() throws Exception {
        token = Helper.loginAsSuperAdmin(mockMvc, objectMapper);
        assertNotNull(token);
    }

    @Order(1)
    @Test
    void index() throws Exception{
        User member = helper.createMember();
        portfolio = helper.createPortfolio(member);
        ResultActions result = Helper.getAll(mockMvc, "/api/portfolios", token);
        Helper.processResult(result, objectMapper);
    }

    @Order(2)
    @Test
    void show() throws Exception{
        User member = helper.createMember();
        portfolio = helper.createPortfolio(member);
        Helper.showById(mockMvc, "/api/portfolios/", token, portfolio.getId(), objectMapper);
    }

    @Order(3)
    @Test
    void delete() throws Exception {
        User member = helper.createMember();
        portfolio = helper.createPortfolio(member);
        token = Helper.loginAsSuperAdmin(mockMvc, objectMapper);

        ResultActions result = Helper.delete(mockMvc, "/api/portfolios/", token, portfolio.getId());
        result.andExpectAll(status().isOk());
    }
}