package bytebrewers.bitpod.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
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
        Helper.showById(mockMvc, "/api/users/", token, user.getId(), objectMapper);
    }

    @Test
    void testGetAllUserSuccess() throws Exception {
        ResultActions result = Helper.getAll(mockMvc, "/api/users", token);
        Helper.processResult(result, objectMapper);
    }

}
