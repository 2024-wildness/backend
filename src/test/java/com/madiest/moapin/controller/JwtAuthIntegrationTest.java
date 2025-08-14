package com.madiest.moapin.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madiest.moapin.auth.dto.LoginRequest;
import com.madiest.moapin.auth.dto.SignUpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@SpringBootTest(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:testdb",
      "spring.datasource.driver-class-name=org.h2.Driver",
      "app.jwt.secret=dGhpcy1pcy1hLXZlcnktbG9uZy1hbmQtc2VjdXJlLXNlY3JldC1rZXktZm9yLWp3dC10ZXN0aW5nCg==",
      "app.storage.endpoint=http://localhost:9000",
      "app.storage.access-key=dummy",
      "app.storage.secret-key=dummy",
      "app.storage.bucket=test",
      "app.search.host=http://localhost:7700",
      "app.search.api-key=dummy",
      "app.email.access-key=dummy",
      "app.email.secret-key=dummy",
      "app.email.region=us-east-1",
      "spring.test.aot.enabled=false"
    })
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class JwtAuthIntegrationTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper mapper;

  @MockBean private S3Presigner s3Presigner;

  @Test
  void testAuthenticationFlowAndProtectedEndpoint() throws Exception {
    // Register user
    SignUpRequest signup = new SignUpRequest();
    signup.setUsername("testuser");
    signup.setEmail("test@example.com");
    signup.setPassword("password123");
    mvc.perform(
            post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signup)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("User registered successfully"));

    // Login user
    LoginRequest login = new LoginRequest();
    login.setUsername("testuser");
    login.setPassword("password123");
    String loginResp =
        mvc.perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").exists())
            .andReturn()
            .getResponse()
            .getContentAsString();
    String token = mapper.readValue(loginResp, JsonNode.class).get("accessToken").asText();

    // Access protected endpoint without token
    mvc.perform(get("/api/test/protected")).andExpect(status().isUnauthorized());

    // Access protected endpoint with valid token
    mvc.perform(get("/api/test/protected").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(content().string("Success"));
  }
}
