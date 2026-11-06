package web.controller;

import domain.model.SignUpRequest;
import domain.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

/**
 *  @RestController — класс отдаёт JSON
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest request) {
        boolean registered = authService.signUp(request);
        return registered
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
        try {
            UUID userId = authService.signIn(authorizationHeader);
            return ResponseEntity.ok(Map.of("uuid", userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
