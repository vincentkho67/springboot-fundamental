package bytebrewers.bitpod.util;
import bytebrewers.bitpod.entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
public class Helper {

    // TODO: FOR USER START
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
                "member@gmail.com",
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

    // TODO: FOR USER END














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
}
