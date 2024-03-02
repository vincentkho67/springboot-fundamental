package bytebrewers.bitpod.controller;


import bytebrewers.bitpod.util.Helper;
import bytebrewers.bitpod.utils.dto.Res;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.core.type.TypeReference;
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
@Transactional // we can use this to ensure it doesn't really insert it on database
public class BankControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private static String id;
    private static String address;
    private static String token;

    @Order(1)
    @Test
    public void testLoginAsSuperAdmin() throws Exception {
        token = Helper.loginAsSuperAdmin(mockMvc, objectMapper);
        assertNotNull(token);
    }

    @Order(2)
    @Test
    void createBank() throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("name", "Mandiri");
        req.put("address", "Jl. Jendral Sudirman No. 1");

        ResultActions result = Helper.postWithHeader(mockMvc, objectMapper, "/api/banks", token, req);

        result.andDo(res -> {
            String jsonString = res.getResponse().getContentAsString();
            Map<String, Object> mapResponse = objectMapper.readValue(jsonString, new TypeReference<>(){});

            Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
            assertEquals(req.get("name"), data.get("name"));
            assertEquals(req.get("address"), data.get("address"));

            id = data.get("id").toString();
            address = data.get("address").toString();
        });
    }

    @Order(3)
    @Test
    void index() throws Exception {
        createBank(); // call the 2nd method to create bank first
        ResultActions result = Helper.getAll(mockMvc, "/api/banks", token);
        Helper.processResult(result, objectMapper);
    }


    @Test
    void show() throws Exception {
        createBank();
        ResultActions result = Helper.getById(mockMvc, "/api/banks/", token, id);

        result.andDo(res -> {
            String jsonString = res.getResponse().getContentAsString();
            Map<String, Object> mapResponse = objectMapper.readValue(jsonString, new TypeReference<>(){});
            Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
            assertEquals(id, data.get("id").toString());
            assertEquals(address, data.get("address").toString());
        });
    }

    @Test
    void update() throws Exception {
        createBank();
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("name", "BRI");
        requestParam.put("address", "Jl. Jendral Sudirman No. 2");
        ResultActions result = Helper.update(mockMvc, objectMapper, "/api/banks", token, id, requestParam);

        result.andDo(res -> {
            String jsonString = res.getResponse().getContentAsString();
            Map<String, Object> mapResponse = objectMapper.readValue(jsonString, new TypeReference<>(){});
            Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
            assertEquals(requestParam.get("name"), data.get("name"));
            assertEquals(requestParam.get("address"), data.get("address"));
        });
    }

    @Test
    void delete() throws Exception{
        createBank();
        ResultActions result = Helper.delete(mockMvc, "/api/banks", token, id);
        result.andExpect(status().isOk());
    }
}
