package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public User create(User user) {
        validateUser(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validateUser(user);
        return userStorage.update(user);
    }

    public void delete(long id) {
        userStorage.delete(id);
    }

    public User getUserById(long id) {
        return userStorage.getUserById(id);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public void addFriend(long id, long friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        update(user);
        log.info("пользователю с id - " + id + " добавлен id друга - " + friendId + " user=" + user);
        friend.getFriends().add(id);
        update(friend);
        log.info("другу с id - " + friendId + " добавлен id пользователя - " + id + " friend=" + friend);
    }

    public void deleteFriend(long id, long friendId) {
        User user = getUserById(id);
        user.getFriends().remove(friendId);
        update(user);
        log.info("у пользователя с id - " + id + " удален id друга - " + friendId  + " user=" + user);
        User friend = getUserById(friendId);
        friend.getFriends().remove(id);
        update(user);
        log.info("у друга с id - " + friendId + " удален id пользователя - " + id + " friend=" + friend);
    }

    public List<User> getUserFriends(long id) {
        Set<Long> ids = getUserById(id).getFriends();
        log.info("получены друзья пользователя с id - " + id);
        return getAll().stream()
                .filter(user -> ids.contains(user.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long id, long otherId) {
        log.info("получены общие друзья пользователя с id - " + id + " и пользователя с id - " + otherId);
        return getUserFriends(id).stream()
                .filter(user -> userStorage.getUserById(otherId).getFriends().contains(user.getId()))
                .collect(Collectors.toList());
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("имя не задано, в качестве имени будет использован login");
            user.setName(user.getLogin());
        }
        log.info("имя пользователя не пустое");
    }
}
