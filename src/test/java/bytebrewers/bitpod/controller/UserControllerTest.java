package bytebrewers.bitpod.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import bytebrewers.bitpod.entity.User;
import bytebrewers.bitpod.util.Helper;
import jakarta.transaction.Transactional;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String token;

    private static User user;
    
    @Autowired
    private Helper helper;
    
    @Order(1)
    @Test
    void loginAsSuperAdmin() throws Exception {
        token = Helper.loginAsSuperAdmin(mockMvc, objectMapper);
    }

    @Test
    void testGetByIdSucess() throws Exception {
        ResultActions result = helper.getById(mockMvc, "/api/users" , token, user.getId());

        result.andDo(res -> {
            String jsonString = res.getResponse().getContentAsString();
            Map<String, Object> mapResponse = objectMapper.readValue(jsonString, new TypeReference<>(){});
            Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
            assertEquals(user.getId(), data.get("id").toString());
        });
    }

}
