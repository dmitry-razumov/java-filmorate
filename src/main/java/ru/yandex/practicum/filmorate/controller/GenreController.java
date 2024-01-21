package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.validator.exist.Exist;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Slf4j
@Validated
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<Genre> getAll() {
        log.info("GET /genres");
        return genreService.getAll();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable @Exist(message = "genre") int id) {
        log.info("GET /genres/{" + id + "}");
        return genreService.getGenreById(id);
    }
}
