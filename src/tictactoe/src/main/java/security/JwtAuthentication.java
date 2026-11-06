package security;
import domain.model.Role;
import org.springframework.security.core.Authentication;
import java.util.List;
import java.util.UUID;


public class JwtAuthentication implements Authentication {
    private final UUID userId;
    private final List<Role> roles;
    private boolean authenticated;

    public JwtAuthentication(UUID userId, List<Role> roles) {
        this.userId = userId; this.roles = List.copyOf(roles);
    }

    @Override
    public List<Role> getAuthorities() { return roles; }

    @Override
    public UUID getPrincipal() { return userId; }

    @Override
    public String getName() { return userId.toString(); }

    @Override
    public Object getCredentials() { return null; }

    @Override
    public Object getDetails() { return null; }

    @Override
    public boolean isAuthenticated() { return authenticated; }

    @Override
    public void setAuthenticated(boolean value) { authenticated = value; }
}
