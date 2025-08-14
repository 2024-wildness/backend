// Quick test to check JWT encoding standalone
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.security.oauth2.jwt.*;
import java.time.Instant;

public class TestJWT {
    public static void main(String[] args) {
        String secret = "dGhpcy1pcy1hLXZlcnktbG9uZy1hbmQtc2VjdXJlLXNlY3JldC1rZXktZm9yLWp3dC10ZXN0aW5nCg==";
        
        try {
            byte[] keyBytes = Base64.getDecoder().decode(secret);
            System.out.println("Key length: " + keyBytes.length);
            
            SecretKeySpec key = new SecretKeySpec(keyBytes, "HmacSHA256");
            NimbusJwtEncoder encoder = new NimbusJwtEncoder(new ImmutableSecret<>(key));
            
            JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject("test")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
                
            String token = encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
            System.out.println("Success: " + token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
