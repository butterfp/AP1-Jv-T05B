package domain.service.impl;

import domain.repository.UserRepository;
import domain.model.User;
import domain.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(String login, String rawPassword) {
        if (login == null || login.isBlank() || rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Вы что-то не дописываете!!!(я логин или пароль имею в виду)");
        }

        if (userRepository.findByLogin(login).isPresent()) {
            throw new IllegalArgumentException("Логин " + login + " уже существует");
        }
        return userRepository.save(new User(UUID.randomUUID(), login, passwordEncoder.encode(rawPassword)));
    }

    @Override
    public Optional<User> findByUuid(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }


    @Override
    public boolean validateCredentials(String login, String password) {
        Optional<User> user = userRepository.findByLogin(login);

        return user.isPresent()
                && password != null
                && passwordEncoder.matches(password, user.get().password());
    }
}
