package domain.service.impl;
import domain.model.*;
import domain.service.*;
import security.*;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import java.util.UUID;


@Service
public class AuthServiceImpl implements AuthService {
    private final UserService users;
    private final JwtProvider jwt;

    public AuthServiceImpl(UserService users, JwtProvider jwt) { this.users = users; this.jwt = jwt; }

    @Override
    public boolean signUp(SignUpRequest request) {
        if (request == null) return false;
        try { users.createUser(request.login(), request.password()); return true; }
        catch (IllegalArgumentException e) { return false; }
    }

    @Override
    public JwtResponse signIn(JwtRequest request) {
        if (request == null || request.login() == null || request.password() == null
                || !users.validateCredentials(request.login(), request.password())) {
            throw new BadCredentialsException("Invalid login or password");
        }
        User user = users.findByLogin(request.login()).orElseThrow(() -> new BadCredentialsException("User not found"));
        return tokens(user);
    }

    private User refreshUser(String token) {
        if (!jwt.validateRefreshToken(token)) throw new BadCredentialsException("Invalid refresh token");
        UUID id = UUID.fromString(jwt.getClaims(token).get("uuid", String.class));
        return users.findByUuid(id).orElseThrow(() -> new BadCredentialsException("User not found"));
    }

    @Override
    public JwtResponse refreshAccessToken(String token) {
        return new JwtResponse("Bearer", jwt.generateAccessToken(refreshUser(token)), null);
    }

    @Override
    public JwtResponse refreshRefreshToken(String token) {
        User user = refreshUser(token);
        JwtAuthentication auth = getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !user.id().equals(auth.getPrincipal())) {
            throw new BadCredentialsException("Refresh token belongs to another user");
        }
        return tokens(user);
    }

    private JwtResponse tokens(User user) {
        return new JwtResponse("Bearer", jwt.generateAccessToken(user), jwt.generateRefreshToken(user));
    }

    @Override
    public JwtAuthentication getAuthentication() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth instanceof JwtAuthentication jwtAuth ? jwtAuth : null;
    }
}
