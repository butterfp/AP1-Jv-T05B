package web.mapper;

import domain.model.GameSession;
import domain.model.PlayerType;
import domain.model.User;
import org.springframework.stereotype.Component;
import web.model.GameResponseDTO;
import web.model.PlayerInfoDTO;
import web.model.UserInfoDTO;

import java.util.UUID;

@Component
public class WebMapper {

    public GameResponseDTO toResponse(GameSession game, String status) {
        return new GameResponseDTO(
                game.id(),
                toPlayer(game.xPlayerType(), game.xPlayerId()),
                toPlayer(game.oPlayerType(), game.oPlayerId()),
                game.board(),
                game.stateName(),
                game.currentPlayerId(),
                game.winnerId(),
                status);
    }

    public UserInfoDTO toUserResponse(User user) {
        return new UserInfoDTO(user.id(), user.login());
    }

    private PlayerInfoDTO toPlayer(PlayerType type, UUID playerId) {
        if (type == null) {
            return null;
        }
        return new PlayerInfoDTO(type.name(), playerId);
    }
}
