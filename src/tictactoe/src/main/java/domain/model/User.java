package domain.model;
import java.util.List;
import java.util.UUID;

public record User(UUID id, String login, String password, List<Role> roles) {

    public User { roles = List.copyOf(roles); }

    public User(UUID id, String login, String password) { this(id, login, password, List.of(Role.USER)); }
}
