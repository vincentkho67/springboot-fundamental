package bytebrewers.bitpod.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

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

import bytebrewers.bitpod.util.Helper;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@Transactional // we can use this to ensure it doesn't really insert it on database
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String token;

    @Order(1)
    @Test
    void testLoginAsSuperAdminSuccess() throws Exception {
        token = Helper.loginAsSuperAdmin(mockMvc, objectMapper);
        assertNotNull(token);
    }

    @Order(2)
    @Test
    void testRegisterAdminSuccess() throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("email", "admin2@gmail.com");
        req.put("password", "admin2");
        req.put("name", "admin2");
        req.put("username", "admin2");
        req.put("address", "jakarta");
        req.put("birthDate", "2000-01-01");

        ResultActions result = Helper.postWithoutHeader(mockMvc, objectMapper, "/auth/register/admin", req);

        result.andDo(res -> {
            String jsonString = res.getResponse().getContentAsString();
            Map<String, Object> mapResponse = objectMapper.readValue(jsonString, new TypeReference<>(){});

            Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
            assertEquals(req.get("email"), data.get("email"));
        });
    }

    @Order(3)
    @Test
    void testRegisterMemberSucces() throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("email", "member@gmail.com");
        req.put("password", "member");
        req.put("name", "member");
        req.put("username", "member");
        req.put("address", "jakarta");
        req.put("birthDate", "2000-01-01");

        ResultActions result = Helper.postWithoutHeader(mockMvc, objectMapper, "/auth/register", req);

        result.andDo(res -> {
            String jsonString = res.getResponse().getContentAsString();
            Map<String, Object> mapResponse = objectMapper.readValue(jsonString, new TypeReference<>(){});

            Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
            assertEquals(req.get("email"), data.get("email"));
        });
    }
}
