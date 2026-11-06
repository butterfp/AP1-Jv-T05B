package domain.service.impl;

import domain.model.GameBoard;
import domain.model.GameSession;
import domain.model.GameState;
import domain.model.PlayerType;
import domain.repository.GameRepository;
import domain.service.TicTacToeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TicTacToeServiceImpl implements TicTacToeService {

    private final GameRepository gameRepository;

    public TicTacToeServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public GameSession createGame(PlayerType opponentType, UUID playerId) {
        if (playerId == null) {
            throw new IllegalArgumentException("Вы даже не гражданин!!!");
        }
        GameSession game;
        if (opponentType == PlayerType.AI) {
            game = new GameSession(UUID.randomUUID(), new GameBoard(),
                    PlayerType.USER, playerId,
                    PlayerType.AI, null,
                    GameState.X_TURN, null);
        } else {
            game = new GameSession(UUID.randomUUID(), new GameBoard(),
                    PlayerType.USER, playerId,
                    null, null,
                    GameState.WAITING, null);
        }
        gameRepository.save(game);
        return game;
    }

    @Override
    public GameSession getGameById(UUID gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Игра с ID " + gameId + " не найдена"));
    }

    @Override
    public List<GameSession> getAvailableGames() {
        return gameRepository.findWaitingGames();
    }

    @Override
    public GameSession joinGame(UUID gameId, UUID playerId) {
        GameSession game = getGameById(gameId);

        if (game.state() != GameState.WAITING) {
            throw new IllegalStateException("В игру уже кто-то подключён или партия начата");
        }
        if (playerId == null || playerId.equals(game.xPlayerId())) {
            throw new IllegalArgumentException("Нельзя присоединиться к собственной игре");
        }

        GameSession joined = new GameSession(game.id(), game.board(),
                game.xPlayerType(), game.xPlayerId(),
                PlayerType.USER, playerId,
                GameState.X_TURN, null);
        gameRepository.save(joined);
        return joined;
    }

    @Override
    public boolean validateMove(UUID gameId, UUID playerId, GameBoard updatedBoard) {
        GameSession game = getGameById(gameId);

        if (!game.state().isActive()) {
            return false; // партия не идёт: ожидание либо уже окончена
        }
        if (updatedBoard == null) {
            return false;
        }
        int playerMark = game.markOf(playerId);
        if (playerMark < 0) {
            return false; // пользователь не участник этой партии
        }
        int expectedMark = game.state() == GameState.X_TURN ? GameBoard.X : GameBoard.O;
        if (playerMark != expectedMark) {
            return false; // ход не той стороны
        }
        return game.board().isValidMove(updatedBoard);
    }

    @Override
    public GameSession makeMove(UUID gameId, UUID playerId, GameBoard updatedBoard) {
        if (!validateMove(gameId, playerId, updatedBoard)) {
            throw new IllegalArgumentException("Некорректный ход");
        }
        GameSession game = getGameById(gameId);

        GameSession afterPlayer = withState(game, updatedBoard);
        gameRepository.save(afterPlayer);

        GameSession current = afterPlayer;
        if (current.isAgainstComputer() && !current.state().isFinished()) {
            int aiMark = current.state() == GameState.X_TURN ? GameBoard.X : GameBoard.O;
            GameBoard boardWithAiMove = bestMove(current.board(), aiMark);
            current = withState(current, boardWithAiMove);
            gameRepository.save(current);
        }
        return current;
    }

    @Override
    public String getGameStatus(GameSession game) {
        return switch (game.state()) {
            case WAITING -> "Ожидание игроков";
            case X_TURN -> "Ход игрока с UUID " + game.xPlayerId() + " (X)";
            case O_TURN -> "Ход игрока с UUID " + game.oPlayerId() + " (O)";
            case DRAW -> "Ничья";
            case WIN_X -> "Победа игрока с UUID " + game.winnerId();
            case WIN_O -> "Победа игрока с UUID " + game.winnerId();
        };
    }

    private GameSession withState(GameSession game, GameBoard newBoard) {
        int movedMark = game.state() == GameState.X_TURN ? GameBoard.X : GameBoard.O;

        GameState newState;
        UUID winnerId = null;

        if (newBoard.isWinner(movedMark)) {
            newState = movedMark == GameBoard.X ? GameState.WIN_X : GameState.WIN_O;
            winnerId = movedMark == GameBoard.X ? game.xPlayerId() : game.oPlayerId();
        } else if (!newBoard.hasEmptyCell()) {
            newState = GameState.DRAW;
        } else {
            newState = movedMark == GameBoard.X ? GameState.O_TURN : GameState.X_TURN;
        }
        return new GameSession(game.id(), newBoard,
                game.xPlayerType(), game.xPlayerId(),
                game.oPlayerType(), game.oPlayerId(),
                newState, winnerId);
    }

    private GameBoard bestMove(GameBoard board, int aiMark) {
        int bestScore = Integer.MIN_VALUE;
        int bestRow = -1;
        int bestCol = -1;
        int[][] cells = board.board();

        for (int i = 0; i < GameBoard.SIZE; i++) {
            for (int j = 0; j < GameBoard.SIZE; j++) {
                if (cells[i][j] == GameBoard.EMPTY) {
                    cells[i][j] = aiMark;                       // пробуем ход
                    int score = minimax(board, 0, false, aiMark); // оцениваем позицию соперника
                    cells[i][j] = GameBoard.EMPTY;               // откатываем пробный ход
                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = i;
                        bestCol = j;
                    }
                }
            }
        }
        if (bestRow >= 0) {
            cells[bestRow][bestCol] = aiMark;
        }
        return new GameBoard(cells);
    }


    private int minimax(GameBoard board, int depth, boolean isMaximizing, int aiMark) {
        int opponentMark = GameBoard.opponentOf(aiMark);

        if (board.isWinner(aiMark)) {
            return 10 - depth;
        }
        if (board.isWinner(opponentMark)) {
            return depth - 10;
        }
        if (!board.hasEmptyCell()) {
            return 0;
        }

        int[][] cells = board.board();
        int best = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < GameBoard.SIZE; i++) {
            for (int j = 0; j < GameBoard.SIZE; j++) {
                if (cells[i][j] == GameBoard.EMPTY) {
                    cells[i][j] = isMaximizing ? aiMark : opponentMark;
                    int score = minimax(board, depth + 1, !isMaximizing, aiMark);
                    cells[i][j] = GameBoard.EMPTY;
                    best = isMaximizing ? Math.max(best, score) : Math.min(best, score);
                }
            }
        }
        return best;
    }
}
