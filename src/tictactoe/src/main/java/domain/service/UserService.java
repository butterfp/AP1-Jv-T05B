package domain.service;

import domain.model.User;

import java.util.Optional;
import java.util.UUID;


public interface UserService {

    User createUser(String login, String rawPassword);

    Optional<User> findByUuid(UUID id);

    Optional<User> findByLogin(String login);

    boolean validateCredentials(String login, String password);
}
