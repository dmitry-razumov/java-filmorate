package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

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

    public User getById(long id) {
        return userStorage.getById(id);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public void addFriend(long id, long friendId) {
        userStorage.addFriend(id, friendId);
        User user = getById(id);
        log.info("пользователю с id - " + id + " добавлен id друга - " + friendId + " user=" + user);
    }

    public void deleteFriend(long id, long friendId) {
        userStorage.deleteFriend(id, friendId);
        User user = getById(id);
        log.info("у пользователя с id - " + id + " удален id друга - " + friendId  + " user=" + user);
    }

    public List<User> getUserFriends(long id) {
        Set<Long> ids = getById(id).getFriends();
        log.info("получены друзья пользователя с id - " + id);
        return getAll().stream()
                .filter(user -> ids.contains(user.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long id, long otherId) {
        log.info("получены общие друзья пользователя с id - " + id + " и пользователя с id - " + otherId);
        return getUserFriends(id).stream()
                .filter(user -> getById(otherId).getFriends().contains(user.getId()))
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
