package domain.model;

public record JwtResponse(String type, String accessToken, String refreshToken) {}
