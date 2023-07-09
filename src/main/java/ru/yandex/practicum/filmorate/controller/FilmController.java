package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.exception.ValidationException;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final HashMap<Long, Film> films = new HashMap<>();
    private long id = 1;
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1985, 12, 28);

    @GetMapping
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        validateFilm(film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("добавлен фильм - {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            log.error("фильм с id = {} не существует", film);
            throw new ValidationException("фильм с id = " + film.getId() + " не существует");
        }
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("обновлен фильм - {}", film);
        return film;
    }

    private void validateFilm(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            log.error("пустое имя фильма");
            throw new ValidationException("Имя фильма не должно быть пустым");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.error("длина описания больше {} и равна {}", MAX_DESCRIPTION_LENGTH, film.getDescription().length());
            throw new ValidationException("Длина описания фильма не должна быть больше "
                    + MAX_DESCRIPTION_LENGTH + " символов");
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.error("дата релиза раньше допустимой - {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза раньше допустимой");
        }
        if (film.getDuration() <= 0) {
            log.error("длительность должна быть положительной, а равна {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
