package com.madiest.moapin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madiest.moapin.auth.password.repository.PasswordResetTokenRepository;
import com.madiest.moapin.auth.password.dto.PasswordUpdateRequest;
import com.madiest.moapin.auth.password.dto.ResetRequest;
import com.madiest.moapin.auth.dto.SignUpRequest;
import com.madiest.moapin.auth.model.User;
import com.madiest.moapin.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.web.servlet.MockMvc;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
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

public class PasswordResetIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PasswordResetTokenRepository tokenRepo;

    @Autowired
    private UserRepository userRepo;

    @MockBean
    private S3Presigner s3Presigner;

    @Test
    void testPasswordResetFlow() throws Exception {
        // register user
        SignUpRequest signup = new SignUpRequest();
        signup.setUsername("resetuser");
        signup.setEmail("reset@example.com");
        signup.setPassword("pass1234");
        mvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signup)))
            .andExpect(status().isOk());

        // request reset
        ResetRequest req = new ResetRequest();
        req.setEmail("reset@example.com");
        mvc.perform(post("/api/auth/reset-request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
            .andExpect(status().isOk());

        // fetch token
        Optional<com.madiest.moapin.auth.password.model.PasswordResetToken> tokenOpt =
                tokenRepo.findAll().stream().findFirst();
        assertThat(tokenOpt).isPresent();
        String token = tokenOpt.get().getToken();

        // reset password
        PasswordUpdateRequest conf = new PasswordUpdateRequest(token, "newpass");
        mvc.perform(post("/api/auth/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(conf)))
            .andExpect(status().isOk());

        // verify password updated
        User user = userRepo.findByUsername("resetuser").get();
        assertThat(user.getPassword()).isNotEqualTo("pass1234");
    }
}
