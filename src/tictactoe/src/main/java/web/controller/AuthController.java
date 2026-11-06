package web.controller;
import domain.model.*;
import domain.service.AuthService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService auth;

    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.status(auth.signUp(request) ? HttpStatus.CREATED : HttpStatus.CONFLICT).build();
    }

    @PostMapping("/sign-in")
    public JwtResponse signIn(@RequestBody JwtRequest request) { return auth.signIn(request); }

    @PostMapping("/refresh-access")
    public JwtResponse refreshAccess(@RequestBody RefreshJwtRequest request) {
        return auth.refreshAccessToken(request.refreshToken());
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody RefreshJwtRequest request) {
        return auth.refreshRefreshToken(request.refreshToken());
    }
}
