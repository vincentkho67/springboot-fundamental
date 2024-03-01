package bytebrewers.bitpod.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    public void loginAsSuperAdmin() throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("email", "superadmin@gmail.com");
        req.put("password", "superadmin");
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(req))
                ).andExpectAll(status().isAccepted())
                .andDo(result -> {
                    String jsonString = result.getResponse().getContentAsString();
                    Map<String, Object> mapResponse = objectMapper.readValue(jsonString, new TypeReference<>(){});

                    token = mapResponse.get("data").toString();
                    assertNotNull(token);
                });
    }


    @Order(2)
    @Test
    public void testCreateBank() throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("name", "Mandiri");
        req.put("address", "Jl. Jendral Sudirman No. 1");

        mockMvc.perform(post("/api/banks")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(req))
        ).andExpectAll(status().isCreated())
                .andDo(result -> {
                    String jsonString = result.getResponse().getContentAsString();
                    Map<String, Object> mapResponse = objectMapper.readValue(jsonString, new TypeReference<>(){});

                    Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
                    assertEquals(req.get("name"), data.get("name"));
                    assertEquals(req.get("address"), data.get("address"));

                    id = data.get("id").toString();
                    address = data.get("address").toString();
                });
    }
}
