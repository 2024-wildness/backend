package com.madiest.moapin.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madiest.moapin.auth.payload.SignUpRequest;
import com.madiest.moapin.auth.payload.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for JWT authentication flow and protected endpoint.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthIntegrationTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;

    @Test
    void testAuthenticationFlowAndProtectedEndpoint() throws Exception {
        // Register user
        SignUpRequest signup = new SignUpRequest();
        signup.setUsername("testuser");
        signup.setEmail("test@example.com");
        signup.setPassword("password123");
        mvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signup)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("User registered successfully"));

        // Login user
        LoginRequest login = new LoginRequest();
        login.setUsername("testuser");
        login.setPassword("password123");
        String loginResp = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").exists())
            .andReturn().getResponse().getContentAsString();
        String token = mapper.readValue(loginResp, JsonNode.class).get("accessToken").asText();

        // Access protected endpoint without token
        mvc.perform(get("/api/test/protected"))
            .andExpect(status().isUnauthorized());

        // Access protected endpoint with valid token
        mvc.perform(get("/api/test/protected")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().string("Success"));
    }
}