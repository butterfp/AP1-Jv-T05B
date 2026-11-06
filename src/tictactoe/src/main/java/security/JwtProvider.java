package security;
import domain.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;


@Component
public class JwtProvider {
    private final SecretKey key;
    private final long accessSeconds;
    private final long refreshSeconds;

    public JwtProvider(@Value("${jwt.secret:}") String secret,
                       @Value("${jwt.access-seconds:900}") long accessSeconds,
                       @Value("${jwt.refresh-seconds:2592000}") long refreshSeconds) {
        key = secret.isBlank() ? Jwts.SIG.HS256.key().build() : Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessSeconds = accessSeconds; this.refreshSeconds = refreshSeconds;
    }

    public String generateAccessToken(User user) { return generate(user, "access", accessSeconds); }


    public String generateRefreshToken(User user) { return generate(user, "refresh", refreshSeconds); }


    private String generate(User user, String type, long seconds) {
        Instant now = Instant.now();
        JwtBuilder builder = Jwts.builder().issuer("tictactoe").subject(user.id().toString())
                .id(UUID.randomUUID().toString()).claim("uuid", user.id().toString()).claim("type", type)
                .issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(seconds)));
        if ("access".equals(type)) builder.claim("roles", user.roles().stream().map(Enum::name).toList());
        return builder.signWith(key).compact();
    }


    public Claims getClaims(String token) {
        return Jwts.parser().verifyWith(key).requireIssuer("tictactoe").build()
                .parseSignedClaims(token).getPayload();
    }

    public boolean validateAccessToken(String token) { return validate(token, "access"); }

    public boolean validateRefreshToken(String token) { return validate(token, "refresh"); }


    private boolean validate(String token, String type) {
        try {
            Claims claims = getClaims(token);
            UUID.fromString(claims.get("uuid", String.class));
            return type.equals(claims.get("type")) && claims.getExpiration() != null;
        } catch (RuntimeException e) { return false; }
    }
}
