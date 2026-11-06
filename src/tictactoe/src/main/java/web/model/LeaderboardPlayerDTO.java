package web.model;
import java.util.UUID;

public record LeaderboardPlayerDTO(UUID userId, String login, double winRatio) {}
