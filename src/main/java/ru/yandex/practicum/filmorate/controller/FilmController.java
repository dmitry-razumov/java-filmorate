package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.exist.Exist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
@Validated
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> getAll() {
        log.info("GET /films");
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable @Exist(message = "film") long id) {
        log.info("GET /films/{" + id + "}");
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.info("POST /films with body " + film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid @Exist(message = "film") Film film) {
        log.info("UPDATE /films with body " + film);
        return filmService.update(film);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Exist(message = "film") long id) {
        log.info("DELETE /films/{" + id + "}");
        filmService.delete(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable @Exist(message = "film") long id,
                              @PathVariable @Exist(message = "user") long userId) {
        log.info("PUT /films/{" + id + "}/like/{" + userId + "}");
        filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable @Exist(message = "film") long id,
                                   @PathVariable @Exist(message = "user") long userId) {
        log.info("DELETE /films/{" + id + "}/like/{" + userId + "}");
        filmService.deleteLikeFromFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("GET /popular?count=" + count);
        return filmService.getMostPopularFilms(count);
    }
}
