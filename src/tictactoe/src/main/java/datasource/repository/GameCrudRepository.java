package datasource.repository;

import datasource.model.GameEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;


@Repository
public interface GameCrudRepository extends CrudRepository<GameEntity, UUID> {


    List<GameEntity> findByState(String state);



    @Query(value = """
            SELECT u.id AS "userId", u.login AS "login",
                   CAST(SUM(CASE WHEN g.winner_id = u.id THEN 1 ELSE 0 END) AS double precision)
                   / GREATEST(SUM(CASE WHEN g.winner_id = u.id THEN 0 ELSE 1 END), 1) AS "winRatio"
            FROM app_users u
            JOIN game_sessions g ON (g.x_player_id = u.id OR g.o_player_id = u.id)
            WHERE g.state IN ('WIN_X', 'WIN_O', 'DRAW')
            GROUP BY u.id, u.login
            ORDER BY "winRatio" DESC, u.id ASC
            LIMIT :limit
            """, nativeQuery = true)
    List<datasource.model.PlayerWinProjection> findTopPlayers(@Param("limit") int limit);

    @Query("""
            SELECT game FROM GameEntity game
            WHERE (game.xPlayerId = :playerId OR game.oPlayerId = :playerId)
              AND game.state IN ('WIN_X', 'WIN_O', 'DRAW')
            ORDER BY game.createdAt DESC, game.id DESC
            """)
    List<GameEntity> findFinishedByPlayerId(@Param("playerId") UUID playerId);
}
