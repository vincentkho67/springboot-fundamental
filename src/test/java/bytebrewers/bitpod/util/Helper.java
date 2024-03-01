package bytebrewers.bitpod.util;
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
}
