package domain.service;

import domain.model.GameBoard;
import domain.model.GameSession;
import domain.model.PlayerType;

import java.util.List;
import java.util.UUID;


public interface TicTacToeService {

    GameSession createGame(PlayerType opponentType, UUID playerId);

    GameSession getGameById(UUID gameId);

    List<GameSession> getAvailableGames();

    List<domain.model.PlayerWinInfo> getTopPlayers(int limit);

    List<GameSession> getFinishedGames(UUID playerId);

    GameSession joinGame(UUID gameId, UUID playerId);

    boolean validateMove(UUID gameId, UUID playerId, GameBoard updatedBoard);

    GameSession makeMove(UUID gameId, UUID playerId, GameBoard updatedBoard);

    String getGameStatus(GameSession game);
}
