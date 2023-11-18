package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.exception.ValidationException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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

    public Film findFilmById(long id) {
        return filmStorage.findFilmById(id);
    }

    public void addLikeToFilm(long id, long userId) {
        findFilmById(id).getLikes().add(userId);
    }

    public void deleteLikeFromFilm(long id, long userId) {
        findFilmById(id).getLikes().remove(userId);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public List<Film> getMostPopularFilms(int quantity) {
        return getAll().stream()
                .sorted(Comparator.comparing(Film::getNumberOfLikes).reversed())
                .limit(quantity)
                .collect(Collectors.toList());
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("дата релиза раньше допустимой - " + film.getReleaseDate());
        }
    }
}
