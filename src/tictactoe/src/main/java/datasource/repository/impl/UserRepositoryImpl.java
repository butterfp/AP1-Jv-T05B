package datasource.repository.impl;

import datasource.mapper.UserMapper;
import datasource.model.UserEntity;
import datasource.repository.UserCrudRepository;
import domain.model.User;
import domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserCrudRepository crudRepository;
    private final UserMapper mapper;


    public UserRepositoryImpl(UserCrudRepository crudRepository, UserMapper mapper) {
        this.crudRepository = crudRepository;
        this.mapper = mapper;
    }


    @Override
    public User save(User user) {
        UserEntity saved = crudRepository.save(mapper.toEntity(user));
        return mapper.toDomain(saved);
    }


    @Override
    public Optional<User> findByLogin(String login) {
        return crudRepository.findByLogin(login).map(mapper::toDomain);
    }


    @Override
    public Optional<User> findById(UUID id) {
        return crudRepository.findById(id).map(mapper::toDomain);
    }
}
