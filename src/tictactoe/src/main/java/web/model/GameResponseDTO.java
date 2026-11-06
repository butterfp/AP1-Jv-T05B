package web.model;

import domain.model.GameBoard;

import java.util.UUID;


public record GameResponseDTO(
        UUID gameId,
        PlayerInfoDTO xPlayer,
        PlayerInfoDTO oPlayer,
        GameBoard board,
        String state,
        UUID currentPlayerId,
        UUID winnerId,
        String status,
        java.time.Instant createdAt
) {
}
