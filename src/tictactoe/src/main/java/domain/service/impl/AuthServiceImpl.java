package domain.service.impl;

import domain.model.SignUpRequest;
import domain.model.User;
import domain.service.AuthService;
import domain.service.UserService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;


@Service
public class AuthServiceImpl implements AuthService {

    private static final String BASIC_PREFIX = "Basic ";

    private final UserService userService;


    public AuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean signUp(SignUpRequest signUpRequest) {
        if (signUpRequest == null
                || signUpRequest.login() == null || signUpRequest.login().isBlank()
                || signUpRequest.password() == null || signUpRequest.password().isBlank()) {
            return false;
        }
        try {
            userService.createUser(signUpRequest.login(), signUpRequest.password());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public UUID signIn(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new IllegalArgumentException("Заголовок Authorization отсутствует");
        }

        String encoded = authorizationHeader.startsWith(BASIC_PREFIX)
                ? authorizationHeader.substring(BASIC_PREFIX.length()).trim()
                : authorizationHeader.trim();

        String decoded;
        try {
            decoded = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Не удалось раскодировать base64 из заголовка");
        }

        int separatorIndex = decoded.indexOf(':');
        if (separatorIndex <= 0 || separatorIndex == decoded.length() - 1) {
            throw new IllegalArgumentException("Ожидался формат base64(login:password)");
        }
        String login = decoded.substring(0, separatorIndex);
        String password = decoded.substring(separatorIndex + 1);

        Optional<User> user = userService.findByLogin(login);
        if (user.isEmpty() || !userService.validateCredentials(login, password)) {
            throw new IllegalArgumentException("Неверный логин или пароль");
        }
        return user.get().id();
    }
}
