package domain.model;

import java.util.UUID;


public record GameSession(
        UUID id,
        GameBoard board,
        PlayerType xPlayerType,
        UUID xPlayerId,
        PlayerType oPlayerType,
        UUID oPlayerId,
        GameState state,
        UUID winnerId,
        java.time.Instant createdAt
) {


    public GameSession(UUID id, GameBoard board, PlayerType xPlayerType, UUID xPlayerId,
                       PlayerType oPlayerType, UUID oPlayerId, GameState state, UUID winnerId) {
        this(id, board, xPlayerType, xPlayerId, oPlayerType, oPlayerId, state, winnerId,
                java.time.Instant.now());
    }



    public GameSession(UUID id, PlayerType firstPlayerType, UUID firstPlayerId) {
        this(id, new GameBoard(), firstPlayerType, firstPlayerId, null, null, GameState.WAITING, null);
    }


    public int markOf(UUID playerId) {
        if (playerId == null) {
            return -1;
        }
        if (playerId.equals(xPlayerId)) {
            return GameBoard.X;
        }
        if (playerId.equals(oPlayerId)) {
            return GameBoard.O;
        }
        return -1;
    }


    public UUID currentPlayerId() {
        if (state == GameState.X_TURN) {
            return xPlayerType == PlayerType.USER ? xPlayerId : null;
        }
        if (state == GameState.O_TURN) {
            return oPlayerType == PlayerType.USER ? oPlayerId : null;
        }
        return null;
    }

    public boolean isAgainstComputer() {
        return xPlayerType == PlayerType.AI || oPlayerType == PlayerType.AI;
    }

    public String stateName() {
        return state.name();
    }
}