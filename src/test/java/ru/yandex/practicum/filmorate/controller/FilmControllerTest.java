package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.exception.ValidationException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmControllerTest {
    private FilmController filmController;
    Film film;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        film = new Film(1, "nisi eiusmod",
                "adipisicing", LocalDate.of(1997, 3, 25),
                100);
    }

    @Test
    void shouldCreateFilm() {
        assertEquals(filmController.create(film).getId(), 1);
    }

    @Test
    void shouldFailEmptyName() {
        film.setName("");

        assertThrows(ValidationException.class, () -> filmController.create(film)
                , "создался фильм без имени");
    }

    @Test
    void shouldFailDescription() {
        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль." +
                " Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, " +
                "а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.");

        assertThrows(ValidationException.class, () -> filmController.create(film),
                "создался фильм c длинной описания больше допустимой");
    }

    @Test
    void shouldFailRelease() {
        film.setReleaseDate(LocalDate.of(1890, 3, 25));

        assertThrows(ValidationException.class, () -> filmController.create(film),
                "создался фильм c датой релиза ранее допустимой");
    }

    @Test
    void shouldFailDuration() {
        film.setDuration(0);

        assertThrows(ValidationException.class, () -> filmController.create(film),
                "создался фильм c длительностью 0");

        film.setDuration(-100);

        assertThrows(ValidationException.class, () -> filmController.create(film),
                "создался фильм c отрицательной длительностью");

    }

    @Test
    void shouldUpdateFilm() {
        filmController.create(film);
        film.setName("Film Updated");

        assertEquals(film, filmController.update(film));
    }

    @Test
    void shouldFailUpdateUnknownFilm() {
        filmController.create(film);
        film.setId(99);

        assertThrows(ValidationException.class, () -> filmController.update(film),
                "обновился фильм с несуществующим id");
    }

    @Test
    void shouldGetAllFilms() {
        filmController.create(film);
        film.setName("secondFilm");
        filmController.create(film);

        assertEquals(filmController.getAll().size(), 2);
    }
}