package domain.repository;

import domain.model.GameSession;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface GameRepository {

    void save(GameSession game);

    Optional<GameSession> findById(UUID id);

    List<GameSession> findWaitingGames();

    List<GameSession> findAll();

    List<domain.model.PlayerWinInfo> findTopPlayers(int limit);

    List<GameSession> findFinishedByPlayerId(UUID playerId);
}
