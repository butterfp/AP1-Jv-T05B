package security;
import domain.model.Role;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.UUID;


public final class JwtUtil {

    private JwtUtil() {}

    public static JwtAuthentication createAuthentication(Claims claims) {
        Object value = claims.get("roles");
        if (!(value instanceof List<?> roles) || roles.isEmpty()) {
            throw new IllegalArgumentException("Token has no roles");
        }
        JwtAuthentication authentication = new JwtAuthentication(
                UUID.fromString(claims.get("uuid", String.class)),
                roles.stream().map(role -> Role.valueOf((String) role)).toList());
        authentication.setAuthenticated(true);
        return authentication;
    }
}
