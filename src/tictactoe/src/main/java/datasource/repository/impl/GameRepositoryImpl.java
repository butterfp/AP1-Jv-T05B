package datasource.repository.impl;

import datasource.mapper.GameMapper;
import datasource.repository.GameCrudRepository;
import domain.model.GameSession;
import domain.model.GameState;
import domain.repository.GameRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;



@Repository
public class GameRepositoryImpl implements GameRepository {

    private final GameCrudRepository crudRepository;
    private final GameMapper mapper;


    public GameRepositoryImpl(GameCrudRepository crudRepository, GameMapper mapper) {
        this.crudRepository = crudRepository;
        this.mapper = mapper;
    }


    @Override
    public void save(GameSession game) {
        crudRepository.save(mapper.toEntity(game));
    }


    @Override
    public Optional<GameSession> findById(UUID id) {
        return crudRepository.findById(id).map(mapper::toDomain);
    }


    @Override
    public List<GameSession> findWaitingGames() {
        return crudRepository.findByState(GameState.WAITING.name()).stream()
                .map(mapper::toDomain)
                .toList();
    }


    @Override
    public List<GameSession> findFinishedByPlayerId(UUID playerId) {
        return crudRepository.findFinishedByPlayerId(playerId).stream().map(mapper::toDomain).toList();
    }


    @Override
    public List<domain.model.PlayerWinInfo> findTopPlayers(int limit) {
        return crudRepository.findTopPlayers(limit).stream()
                .map(row -> new domain.model.PlayerWinInfo(row.getUserId(), row.getLogin(), row.getWinRatio()))
                .toList();
    }

    @Override
    public List<GameSession> findAll() {

        return StreamSupport.stream(crudRepository.findAll().spliterator(), false)
                .map(mapper::toDomain)
                .toList();
    }
}
