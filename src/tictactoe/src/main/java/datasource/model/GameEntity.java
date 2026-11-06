package datasource.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;


@Entity
@Table(name = "game_sessions")
public class GameEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "board", nullable = false, columnDefinition = "TEXT")
    private String board;

    @Column(name = "x_player_type")
    private String xPlayerType;

    @Column(name = "x_player_id")
    private UUID xPlayerId;

    @Column(name = "o_player_type")
    private String oPlayerType;

    @Column(name = "o_player_id")
    private UUID oPlayerId;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "winner_id")
    private UUID winnerId;

    @Column(name = "created_at", nullable = false, updatable = false,
            columnDefinition = "timestamp with time zone default current_timestamp")
    private java.time.Instant createdAt;


    public java.time.Instant getCreatedAt() { return createdAt; }

    public GameEntity() {
    }



    public GameEntity(UUID id, String board, String xPlayerType, UUID xPlayerId,
                      String oPlayerType, UUID oPlayerId, String state, UUID winnerId, java.time.Instant createdAt) {
        this.id = id;
        this.board = board;
        this.xPlayerType = xPlayerType;
        this.xPlayerId = xPlayerId;
        this.oPlayerType = oPlayerType;
        this.oPlayerId = oPlayerId;
        this.state = state;
        this.winnerId = winnerId;
        this.createdAt = createdAt;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getXPlayerType() {
        return xPlayerType;
    }

    public void setXPlayerType(String xPlayerType) {
        this.xPlayerType = xPlayerType;
    }

    public UUID getXPlayerId() {
        return xPlayerId;
    }

    public void setXPlayerId(UUID xPlayerId) {
        this.xPlayerId = xPlayerId;
    }

    public String getOPlayerType() {
        return oPlayerType;
    }

    public void setOPlayerType(String oPlayerType) {
        this.oPlayerType = oPlayerType;
    }

    public UUID getOPlayerId() {
        return oPlayerId;
    }

    public void setOPlayerId(UUID oPlayerId) {
        this.oPlayerId = oPlayerId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public UUID getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(UUID winnerId) {
        this.winnerId = winnerId;
    }
}
