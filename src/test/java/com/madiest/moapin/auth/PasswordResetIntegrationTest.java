package com.madiest.moapin.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madiest.moapin.auth.password.PasswordResetTokenRepository;
import com.madiest.moapin.auth.password.PasswordResetController.ResetConfirm;
import com.madiest.moapin.auth.password.PasswordResetController.ResetRequest;
import com.madiest.moapin.auth.payload.SignUpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for password reset flow.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PasswordResetIntegrationTest {
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private PasswordResetTokenRepository tokenRepo;
    @Autowired private UserRepository userRepo;

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
        Optional<com.madiest.moapin.auth.password.PasswordResetToken> tokenOpt =
                tokenRepo.findAll().stream().findFirst();
        assertThat(tokenOpt).isPresent();
        String token = tokenOpt.get().getToken();

        // reset password
        ResetConfirm conf = new ResetConfirm();
        conf.setToken(token);
        conf.setPassword("newpass");
        mvc.perform(post("/api/auth/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(conf)))
            .andExpect(status().isOk());

        // verify password updated
        User user = userRepo.findByUsername("resetuser").get();
        assertThat(user.getPassword()).isNotEqualTo("pass1234");
    }
}
