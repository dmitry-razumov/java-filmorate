package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

public interface UserStorage extends Storage<User> {
    List<User> getUserFriends(long id);

    List<User> getCommonFriends(long userId, long otherId);

    void addFriend(long id, long friendId);

    void deleteFriend(long id, long friendId);
}
