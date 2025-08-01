package com.madiest.moapin.auth.password;

import com.madiest.moapin.auth.User;
import com.madiest.moapin.auth.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.sesv2.SesV2AsyncClient;
import software.amazon.awssdk.services.sesv2.model.*;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Service handling password reset flow.
 */
@Service
public class PasswordResetService {
    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SesV2AsyncClient sesClient;

    /**
     * 비밀번호 재설정 서비스를 초기화합니다.
     *
     * 비밀번호 재설정 토큰 저장소, 사용자 저장소, 비밀번호 인코더, AWS SES V2 비동기 클라이언트를 주입받아 서비스의 의존성을 구성합니다.
     */
    public PasswordResetService(PasswordResetTokenRepository tokenRepository,
                                UserRepository userRepository,
                                PasswordEncoder passwordEncoder,
                                SesV2AsyncClient sesClient) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sesClient = sesClient;
    }

    /** Initiate password reset by creating a token and emailing the user. */
    @Transactional
    public void createResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK));
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(Instant.now().plusSeconds(3600));
        tokenRepository.save(token);
        sendEmail(user.getEmail(), token.getToken());
    }

    /** Reset the user's password using the token. */
    @Transactional
    public void resetPassword(String tokenStr, String newPassword) {
        PasswordResetToken token = tokenRepository.findByToken(tokenStr)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token"));
        if (token.isUsed() || token.getExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired");
        }
        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        token.setUsed(true);
        userRepository.save(user);
        tokenRepository.save(token);
    }

    private CompletableFuture<SendEmailResponse> sendEmail(String to, String token) {
        Destination dest = Destination.builder().toAddresses(to).build();
        Content subject = Content.builder().data("Password Reset").build();
        Content bodyText = Content.builder().data("Use this token to reset your password: " + token).build();
        Body body = Body.builder().text(bodyText).build();
        Message message = Message.builder().subject(subject).body(body).build();
        SendEmailRequest request = SendEmailRequest.builder()
                .destination(dest)
                .fromEmailAddress("noreply@example.com")
                .content(EmailContent.builder().simple(message).build())
                .build();
        return sesClient.sendEmail(request);
    }
}
