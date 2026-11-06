package datasource.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import datasource.model.GameEntity;
import domain.model.GameBoard;
import domain.model.GameSession;
import org.springframework.stereotype.Component;
import domain.model.GameState;
import domain.model.PlayerType;


@Component
public class GameMapper {


    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameEntity toEntity(GameSession domain) {
        try {
            String boardJson = objectMapper.writeValueAsString(domain.board().board());
            return new GameEntity(
                    domain.id(),
                    boardJson,
                    domain.xPlayerType() == null ? null : domain.xPlayerType().name(),
                    domain.xPlayerId(),
                    domain.oPlayerType() == null ? null : domain.oPlayerType().name(),
                    domain.oPlayerId(),
                    domain.stateName(),
                    domain.winnerId(), domain.createdAt());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Ошибка при конвертации доски в JSON", e);
        }
    }

    public GameSession toDomain(GameEntity entity) {
        try {
            int[][] boardArray = objectMapper.readValue(entity.getBoard(), int[][].class);
            return new GameSession(
                    entity.getId(),
                    new GameBoard(boardArray),
                    entity.getXPlayerType() == null ? null : PlayerType.valueOf(entity.getXPlayerType()),
                    entity.getXPlayerId(),
                    entity.getOPlayerType() == null ? null : PlayerType.valueOf(entity.getOPlayerType()),
                    entity.getOPlayerId(),
                    GameState.valueOf(entity.getState()),
                    entity.getWinnerId(), entity.getCreatedAt());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Ошибка при чтении доски из JSON", e);
        }
    }
}