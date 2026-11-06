package domain.service;
import domain.model.*;
import security.JwtAuthentication;


public interface AuthService {

    boolean signUp(SignUpRequest request);

    JwtResponse signIn(JwtRequest request);

    JwtResponse refreshAccessToken(String refreshToken);

    JwtResponse refreshRefreshToken(String refreshToken);

    JwtAuthentication getAuthentication();
}
