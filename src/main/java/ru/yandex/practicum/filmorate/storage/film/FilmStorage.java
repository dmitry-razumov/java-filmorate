package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;
import java.util.List;

public interface FilmStorage extends Storage<Film> {
    List<Film> getMostPopularFilms(int count);

    void addLikeToFilm(long id, long userId);

    void deleteLikeFromFilm(long id, long userId);
}
