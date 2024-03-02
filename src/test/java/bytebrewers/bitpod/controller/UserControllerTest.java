package bytebrewers.bitpod.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import bytebrewers.bitpod.entity.User;
import bytebrewers.bitpod.util.Helper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@Transactional
@Slf4j
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String token;

    private static User user;
    
    @Autowired
    private Helper helper;
    
    @BeforeEach
    void loginAsSuperAdmin() throws Exception {
        user = helper.createMember();
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

    @Test
    void testGetAllUserSuccess() throws Exception {
        ResultActions result = Helper.getAll(mockMvc, "/api/users", token);
        result.andDo(res -> {
            String jsonString = res.getResponse().getContentAsString();
            Map<String, Object> mapResponse = objectMapper.readValue(jsonString, new TypeReference<>(){});
            Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
            List<Map<String, Object>> content = (List<Map<String, Object>>) data.get("content");
            assertNotNull(content);
            assertFalse(content.isEmpty());
        });
    }

    @Test
    void testUpdateUserSucess() throws Exception{
        Path path = Path.of("src/test/java/bytebrewers/bitpod/util/Screenshot 2024-03-02 001001.png");
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("name", "member2");
        requestParam.put("username", "member2");
        requestParam.put("address", "Jogja");
        requestParam.put("image",  path);
        requestParam.put("birthDate", "2000-01-01");

        token = Helper.loginAsMember(mockMvc, objectMapper, "member@email.com", "member");

        ResultActions result =  mockMvc.perform(MockMvcRequestBuilders.put("/api/users")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(requestParam)))
                .andExpect(status().isOk());

        result.andDo(res -> {
            String jsonString = res.getResponse().getContentAsString();
            Map<String, Object> mapResponse = objectMapper.readValue(jsonString, new TypeReference<>(){});
            Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
            assertEquals(requestParam.get("name"), data.get("name"));
            assertEquals(requestParam.get("address"), data.get("address"));
        });
    }

}
