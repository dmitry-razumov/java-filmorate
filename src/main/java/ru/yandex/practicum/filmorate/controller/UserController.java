package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.exception.ValidationException;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<Long, User> users = new HashMap<>();
    private long id = 1;

    @GetMapping
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        validateUser(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("создан пользователь - {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            log.error("user с id = {} не существует", user.getId());
            throw new ValidationException("пользователь с id = " + user.getId() + " не существует");
        }
        validateUser(user);
        users.put(user.getId(), user);
        log.info("обновлен пользователь - {}", user);
        return user;
    }

    private void validateUser(User user) throws ValidationException {
        if (user.getEmail().isBlank()) {
            log.error("email не должен быть пустым");
            throw new ValidationException("email не должен быть пустым");
        }
        if (!user.getEmail().matches("[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,4}")) {
            log.error("недопустимый формат email - {}", user.getEmail());
            throw new ValidationException("недопустимый формат email");
        }
        if (user.getLogin().isEmpty()) {
            log.error("login не должен быть пустым");
            throw new ValidationException("login не должен быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            log.error("login не должeн содержать пробелов - {}", user.getLogin());
            throw new ValidationException("login не должен содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("имя не задано, в качестве имени будет использован login");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("дата рождения не может быть в будущем - {}", user.getBirthday());
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }
}