package datasource.mapper;

import datasource.model.UserEntity;
import domain.model.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    public UserEntity toEntity(User domain) {
        UserEntity entity = new UserEntity(domain.id(), domain.login(), domain.password());
        entity.setRoles(domain.roles());
        return entity;
    }

    public User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getLogin(), entity.getPassword(), entity.getRoles().isEmpty() ? java.util.List.of(domain.model.Role.USER) : entity.getRoles());
    }
}
