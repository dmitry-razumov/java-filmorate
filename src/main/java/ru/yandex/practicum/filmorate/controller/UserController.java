package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.exist.Exist;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAll() {
        log.info("GET /users");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable @Exist(message = "user") long id) {
        log.info("GET /users/{" + id + "}");
        return userService.getUserById(id);
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.info("POST /users with body " + user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid @Exist(message = "user") User user) {
        log.info("UPDATE /users with body " + user);
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Exist(message = "user") long id) {
        log.info("DELETE /users/{" + id + "}");
        userService.delete(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable @Exist(message = "user") long id,
                          @PathVariable @Exist(message = "user") long friendId) {
        log.info("PUT /users/{" + id + "}/friends/{" + friendId + "}");
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable @Exist(message = "user") long id,
                             @PathVariable @Exist(message = "user") long friendId) {
        log.info("DELETE /users/{" + id + "}/friends/{" + friendId + "}");
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable @Exist(message = "user") long id) {
        log.info("GET /users/{" + id + "}/friends");
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable @Exist(message = "user") long id,
                                       @PathVariable @Exist(message = "user") long otherId) {
        log.info("GET /users/{" + id + "}/friends/common/{" + otherId + "}");
        return userService.getCommonFriends(id, otherId);
    }
}