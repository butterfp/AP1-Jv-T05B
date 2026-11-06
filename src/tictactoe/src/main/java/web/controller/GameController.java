package web.controller;

import domain.model.GameSession;
import domain.model.PlayerType;
import domain.service.TicTacToeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import web.mapper.WebMapper;
import web.model.GameRequestDTO;
import web.model.GameResponseDTO;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/game")
public class GameController {

    private final TicTacToeService ticTacToeService;

    private final WebMapper webMapper;


    public GameController(TicTacToeService ticTacToeService, WebMapper webMapper) {
        this.ticTacToeService = ticTacToeService;
        this.webMapper = webMapper;
    }


    @PostMapping
    public ResponseEntity<GameResponseDTO> createGame(
            @RequestParam(name = "opponent", defaultValue = "USER") PlayerType opponentType,
            @AuthenticationPrincipal UUID userId) {
        GameSession game = ticTacToeService.createGame(opponentType, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(game));
    }


    @GetMapping("/leaderboard")
    public List<web.model.LeaderboardPlayerDTO> getLeaderboard(@RequestParam(name = "n") int limit) {
        return ticTacToeService.getTopPlayers(limit).stream()
                .map(webMapper::toLeaderboardPlayer).toList();
    }


    @GetMapping("/history")
    public List<GameResponseDTO> getHistory(@AuthenticationPrincipal UUID userId) {
        return ticTacToeService.getFinishedGames(userId).stream().map(this::toDto).toList();
    }


    @GetMapping("/list/available")
    public List<GameResponseDTO> getAvailableGames() {
        return ticTacToeService.getAvailableGames().stream()
                .map(this::toDto)
                .toList();
    }


    @PostMapping("/{id}/join")
    public GameResponseDTO joinGame(@PathVariable("id") UUID id,
                                    @AuthenticationPrincipal UUID userId) {
        return toDto(ticTacToeService.joinGame(id, userId));
    }


    @PostMapping("/{id}")
    public GameResponseDTO updateGame(@PathVariable("id") UUID id,
                                      @RequestBody GameRequestDTO dto,
                                      @AuthenticationPrincipal UUID userId) {
        if (!ticTacToeService.validateMove(id, userId, dto.board())) {
            throw new IllegalArgumentException("Ход некорректен или сделан не в свою очередь");
        }
        GameSession game = ticTacToeService.makeMove(id, userId, dto.board());
        return toDto(game);
    }


    @GetMapping("/{id}")
    public GameResponseDTO getGame(@PathVariable("id") UUID id) {
        return toDto(ticTacToeService.getGameById(id));
    }


    private GameResponseDTO toDto(GameSession game) {
        return webMapper.toResponse(game, ticTacToeService.getGameStatus(game));
    }
}
