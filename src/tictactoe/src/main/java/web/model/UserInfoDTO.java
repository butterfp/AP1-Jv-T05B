package web.model;

import java.util.UUID;


public record UserInfoDTO(UUID id, String login, java.util.List<domain.model.Role> roles) {
}
