package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();
    private long id = 1;

    @Override
    public Film create(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("добавлен фильм - {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        log.info("обновлен фильм - {}", film);
        return film;
    }

    @Override
    public void delete(long id) {
        log.info("удален фильм с id - {}", id);
        films.remove(id);
    }

    @Override
    public Film getById(long id) {
        log.info("получен фильм c id - {}", id);
        return films.get(id);
    }

    @Override
    public List<Film> getAll() {
        log.info("получены все фильмы");
        return new ArrayList<>(films.values());
    }
}
