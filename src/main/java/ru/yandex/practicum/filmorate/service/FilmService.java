package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.exception.ValidationException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        log.info("фильму с id - " + id + " добавлен лайк пользователя с id  - " + userId);
        Film film = getById(id);
        film.getLikes().add(userId);
        film.setRate(film.getRate() + 1);
        update(film);
    }

    public void deleteLikeFromFilm(long id, long userId) {
        log.info("у фильма с id - " + id + " удален лайк пользователя с id  - " + userId);
        Film film = getById(id);
        film.getLikes().remove(userId);
        film.setRate(film.getRate() - 1);
        update(film);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public List<Film> getMostPopularFilms(int quantity) {
        log.info("получено {} наиболее популярных фильмов", quantity);
        return getAll().stream()
                .sorted(Comparator.comparing(Film::getRate).reversed())
                .limit(quantity)
                .collect(Collectors.toList());
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
