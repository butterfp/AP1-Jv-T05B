package web.model;

import java.util.UUID;

/**
 * описание участника игры для JSON-ответа
 * id — UUID человека (null для AI)
 */
public record PlayerInfoDTO(String type, UUID id) {
}
