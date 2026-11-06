package domain.model;
import java.util.UUID;

public record PlayerWinInfo(UUID userId, String login, double winRatio) {}
