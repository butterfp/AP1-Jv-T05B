package datasource.repository;

import datasource.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserCrudRepository extends CrudRepository<UserEntity, UUID> {

    Optional<UserEntity> findByLogin(String login);
}
