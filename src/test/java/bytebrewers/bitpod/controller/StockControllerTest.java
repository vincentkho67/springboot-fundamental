package bytebrewers.bitpod.controller;

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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@Transactional
@Slf4j
public class StockControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        token = Helper.loginAsSuperAdmin(mockMvc, objectMapper);
        Helper.fetchAndCreateStock(mockMvc, token);
        assertNotNull(token);
    }
    @Order(1)
    @Test
    void fetch() throws Exception {
        ResultActions result = Helper.fetchAndCreateStock(mockMvc, token);
        result.andDo(res -> {
            String jsonString = res.getResponse().getContentAsString();
            Map<String, Object> mapResponse = objectMapper.readValue(jsonString, new TypeReference<>(){});
            List<Map<String, Object>> data = (List<Map<String, Object>>) mapResponse.get("data");
            assertNotNull(data);
        });
    }
    @Test
    void index() throws Exception {
        ResultActions result = Helper.fetchStocksOnDB(mockMvc, token);
        Helper.processResult(result, objectMapper);
    }

    @Test
    void show() throws Exception {
        String stockId = Helper.extractFirstIdFromResponse(Helper.fetchStocksOnDB(mockMvc, token), objectMapper);
        Helper.showById(mockMvc, "/api/stocks/", token, stockId, objectMapper);
    }

    @Test
    void recommend() throws Exception {
        ResultActions result = Helper.getAll(mockMvc, "/api/stocks/recommendation", token);
        Helper.processResult(result, objectMapper);
    }
}