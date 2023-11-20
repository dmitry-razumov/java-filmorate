package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> users = new HashMap<>();
    private long id = 1;

    @Override
    public User create(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("создан пользователь - {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        log.info("обновлен пользователь - {}", user);
        return user;
    }

    @Override
    public void delete(long id) {
        log.info("удален пользователь с id - {}", id);
        users.remove(id);
    }

    @Override
    public User getUserById(long id) {
        log.info("получен пользователь c id - {}", id);
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("получены все пользователи");
        return new ArrayList<>(users.values());
    }
}
