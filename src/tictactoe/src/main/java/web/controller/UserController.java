package web.controller;

import domain.model.User;
import domain.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.mapper.WebMapper;
import web.model.UserInfoDTO;

import java.util.UUID;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final WebMapper webMapper;


    public UserController(UserService userService, WebMapper webMapper) {
        this.userService = userService;
        this.webMapper = webMapper;
    }


    @GetMapping("/me")
    public ResponseEntity<UserInfoDTO> getCurrentUser(
            @org.springframework.security.core.annotation.AuthenticationPrincipal UUID userId) {
        return getUser(userId);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserInfoDTO> getUser(@PathVariable("id") UUID id) {
        return userService.findByUuid(id)
                .map(webMapper::toUserResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    public User requireUser(UUID id) {
        return userService.findByUuid(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + id));
    }
}
