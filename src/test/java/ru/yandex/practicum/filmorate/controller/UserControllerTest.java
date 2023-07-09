package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.exception.ValidationException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    private UserController userController;
    User user;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        user = new User(1, "mail@mail.ru", "dolore",
                "Nick Name", LocalDate.of(1946, 8, 20));
    }

    @Test
    void shouldCreateUser() {
        assertEquals(userController.create(user).getId(), 1);
    }

    @Test
    void shouldCreateUserWithEmptyName() {
        user.setName("");

        assertEquals(userController.create(user).getId(), 1);
    }

    @Test
    void shouldCreateUserWithoutName() {
        user.setName(null);

        assertEquals(userController.create(user).getId(), 1);
    }

    @Test
    void shouldFailLogin() {
        user.setLogin("dolore ullamco");

        assertThrows(ValidationException.class, () -> userController.create(user),
                "создался пользователь с пробелом в login");
    }

    @Test
    void shouldFailEmail() {
        user.setEmail("это-неправильный?эмейл@");

        assertThrows(ValidationException.class, () -> userController.create(user),
                "создался пользователь с недопустимым email");
    }

    @Test
    void shouldFailBirthday() {
        user.setBirthday(LocalDate.of(2030,1,10));

        assertThrows(ValidationException.class, () -> userController.create(user),
                "создался пользователь с будущей датой рождения");
    }

    @Test
    void shouldUpdateUser() {
        userController.create(user);
        user.setLogin("doloreUpdate");

        assertEquals(user, userController.update(user));
    }

    @Test
    void shouldFailUpdateUnknownUser() {
        userController.create(user);
        user.setId(99);

        assertThrows(ValidationException.class, () -> userController.update(user),
                "обновился пользователь с несуществующим id");
    }

    @Test
    void shouldGetAllUsers() {
        userController.create(user);
        user.setLogin("secondU");
        userController.create(user);

        assertEquals(userController.getAll().size(), 2);
    }
}