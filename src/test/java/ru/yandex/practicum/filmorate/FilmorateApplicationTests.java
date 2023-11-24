package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private static User user1;
    private static User user2;
    private static Film film1;
    private static Film film2;
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;

    @BeforeAll
    static void beforeAll() {
        user1 = User.builder()
                .id(1)
                .name("user1name")
                .login("user1login")
                .email("user1@mail.com")
                .birthday(LocalDate.of(1968, 4, 19))
                .build();

        user2 = User.builder()
                .id(2)
                .name("user2name")
                .login("user2login")
                .email("user2@mail.com")
                .birthday(LocalDate.of(1978, 5, 13))
                .build();

        film1 = Film.builder()
                .id(1)
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .rate(4)
                .genres(new LinkedHashSet<>())
                .mpa(new Mpa(1, "G"))
                .build();

        film2 = Film.builder()
                .id(2)
                .name("Titanic")
                .description("description")
                .releaseDate(LocalDate.of(2000, 5, 20))
                .duration(120)
                .rate(4)
                .genres(new LinkedHashSet<>())
                .mpa(new Mpa(2, "PG"))
                .build();
    }

    @BeforeEach
    void beforeEach() {
        userStorage.create(user1);
        userStorage.create(user2);
        filmStorage.create(film1);
        filmStorage.create(film2);
    }

    @AfterEach
    void afterEach() {
        userStorage.delete(user1.getId());
        userStorage.delete(user2.getId());
        filmStorage.delete(film1.getId());
        filmStorage.delete(film2.getId());
    }

    @Test
    void shouldGetUser1ById() {
        assertEquals(user1, userStorage.getById(user1.getId()));
    }

    @Test
    void shouldGetAllUsers() {
        List<User> users = userStorage.getAll();
        assertEquals(user1, users.get(0));
        assertEquals(user2, users.get(1));
        assertEquals(2, users.size());
    }

    @Test
    void shouldUpdateUser1() {
        User updatedUser = User.builder()
                .id(user1.getId())
                .login(user1.getLogin())
                .name("otherName")
                .email(user1.getEmail())
                .birthday(user1.getBirthday())
                .build();
        userStorage.update(updatedUser);
        assertEquals(updatedUser, userStorage.getById(updatedUser.getId()));
    }

    @Test
    void shouldDeleteUser1() {
        userStorage.delete(user1.getId());
        assertThrows(EmptyResultDataAccessException.class, () -> userStorage.getById(user1.getId()));
    }

    @Test
    void shouldAddUser2ToFriendsOfUser1AndDeleteFromFriends() {
        List<User> friends = new ArrayList<>();
        friends.add(user2);
        userStorage.addFriend(user1.getId(), user2.getId());
        assertEquals(friends, userStorage.getUserFriends(user1.getId()));
        userStorage.deleteFriend(user1.getId(), user2.getId());
        assertTrue(userStorage.getUserFriends(user1.getId()).isEmpty());
    }

    @Test
    void shouldGetFilm1ById() {
        assertEquals(film1, filmStorage.getById(film1.getId()));
    }

    @Test
    void shouldGetAllFilms() {
        List<Film> films = filmStorage.getAll();
        assertEquals(film1, films.get(0));
        assertEquals(film2, films.get(1));
        assertEquals(2, films.size());
    }

    @Test
    void shouldUpdateFilm1() {
        Film updatedFilm = Film.builder()
                .id(film1.getId())
                .name("changed Name")
                .description(film1.getDescription())
                .releaseDate(film1.getReleaseDate())
                .duration(film1.getDuration())
                .genres(new LinkedHashSet<>())
                .mpa(film1.getMpa())
                .build();
        filmStorage.update(updatedFilm);
        assertEquals(updatedFilm, filmStorage.getById(film1.getId()));
    }

    @Test
    void shouldDeleteFilm1() {
        filmStorage.delete(film1.getId());
        assertThrows(EmptyResultDataAccessException.class, () -> filmStorage.getById(film1.getId()));
    }

    @Test
    void shouldGetMpa1ById() {
        Mpa expectedMpa = Mpa.builder()
                .id(1)
                .name("G")
                .build();
        assertEquals(expectedMpa, mpaStorage.getMpaById(1));
    }

    @Test
    void shouldGetAllMpa() {
        List<Mpa> mpaList = mpaStorage.getAll();
        assertEquals(5, mpaList.size());
    }

    @Test
    void shouldGetGenre1ById() {
        Genre expectedGenre = Genre.builder()
                .id(1)
                .name("Комедия")
                .build();
        assertEquals(expectedGenre, genreStorage.getGenreById(1));
    }

    @Test
    void shouldGetAllGenres() {
        List<Genre> genres = genreStorage.getAll();
        assertEquals(6, genres.size());
    }
}
