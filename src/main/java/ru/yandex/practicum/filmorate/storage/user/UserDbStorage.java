package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        String sql = "INSERT INTO USERS (name, email, login, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        log.info("создан пользователь - {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE USERS SET login = ?, name = ?, email = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId()
        );
        log.info("обновлен пользователь - {}", user);
        return user;
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("delete from users where id =?", id);
        log.info("удален пользователь с id - {}", id);
    }

    @Override
    public User getById(long id) {
        String sql = "SELECT u.id, u.name , u.email, u.login, u.birthday, STRING_AGG(f.friend_id, ',') AS friends " +
                "FROM users u LEFT JOIN friends f ON u.id = f.user_id " +
                "WHERE u.id = ? " +
                "GROUP BY u.id";
        User user = jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        log.info("получен пользователь c id - {}", id);
        return user;
    }

    @Override
    public boolean isExistById(long id) {
        return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM USERS WHERE id = ?)", Boolean.class, id);
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT u.id, u.name , u.email, u.login, u.birthday, STRING_AGG(f.friend_id, ',') AS friends " +
                "FROM USERS u LEFT JOIN friends f ON u.id = f.user_id " +
                "GROUP BY u.id";
        List<User> userList = Optional.ofNullable(jdbcTemplate.query(sql, this::mapRowToUser))
                .orElse(Collections.emptyList());
        log.info("получены все пользователи {}", userList);
        return userList;
    }

    @Override
    public List<User> getUserFriends(long id) {
        String sql = "SELECT * FROM USERS WHERE id IN (SELECT friend_id FROM FRIENDS WHERE user_id = ?)";
        return jdbcTemplate.query(sql, this::mapRowToUser, id);
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        String sql = "SELECT * FROM USERS WHERE id IN (SELECT fu.friend_id FROM FRIENDS fu " +
                "JOIN FRIENDS fo ON fu.friend_id = fo.friend_id " +
                "WHERE fu.USER_ID = ? AND fo.USER_ID = ?)";
        return jdbcTemplate.query(sql, this::mapRowToUser, userId, otherId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        Set<Long> friends = new HashSet<>();
        User user = User.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
//                .friends(friends)
                .build();

//        Optional<String> opt = Optional.ofNullable(resultSet.getString("friends"));
//        if (opt.isPresent()) {
//            user.setFriends(Arrays.stream(opt.get().split(","))
//                    .map(x -> Long.parseLong(x))
//                    .collect(Collectors.toSet())
//            );
//        }
        return user;
    }

    @Override
    public void addFriend(long id, long friendId) {
        String sql = "INSERT INTO FRIENDS (user_id, friend_id) VALUES (?, ?);";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        String sql = "DELETE FROM FRIENDS WHERE user_id = ? AND friend_id =?;";
        jdbcTemplate.update(sql, id, friendId);
    }
}
