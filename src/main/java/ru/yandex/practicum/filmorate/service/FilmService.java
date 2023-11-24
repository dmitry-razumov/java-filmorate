package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.exception.ValidationException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;

    public Film create(Film film) {
        validateFilm(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validateFilm(film);
        return filmStorage.update(film);
    }

    public void delete(long id) {
        filmStorage.delete(id);
    }

    public Film getById(long id) {
        return filmStorage.getById(id);
    }

    public void addLikeToFilm(long id, long userId) {
        filmStorage.addLikeToFilm(id, userId);
        log.info("фильму с id - " + id + " добавлен лайк пользователя с id  - " + userId);
    }

    public void deleteLikeFromFilm(long id, long userId) {
        filmStorage.deleteLikeFromFilm(id, userId);
        log.info("у фильма с id - " + id + " удален лайк пользователя с id  - " + userId);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public List<Film> getMostPopularFilms(int count) {
        log.info("получено {} наиболее популярных фильмов", count);
        return filmStorage.getMostPopularFilms(count);
    }

    private void validateFilm(Film film) {
        if (film == null) {
            log.info("получен объект фильма null");
            throw new ValidationException("получен объект фильма null");
        } else {
            if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
                log.info("дата релиза фильма раньше допустимой - " + film.getReleaseDate());
                throw new ValidationException("дата релиза фильма раньше допустимой - " + film.getReleaseDate());
            }
        }
        log.info("успешная валидация фильма {}", film);
    }
}
