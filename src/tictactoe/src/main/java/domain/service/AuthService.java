package domain.service;

import domain.model.SignUpRequest;

import java.util.UUID;

public interface AuthService {

    boolean signUp(SignUpRequest signUpRequest);

    UUID signIn(String authorizationHeader);
}
