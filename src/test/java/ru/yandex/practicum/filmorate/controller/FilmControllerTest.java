package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {
    @Autowired
    private FilmController filmController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldBeSuccessValidationsForValidFilmData() throws Exception {
        Film film = Film.builder()
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3,25))
                .duration(100)
                .genres(new LinkedHashSet<>())
                .mpa(new Mpa(1, "G"))
                .build();
        String validFilm = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(validFilm))
                .andDo(h -> assertEquals(200, h.getResponse().getStatus()));

        mockMvc.perform(get("/films")
                .contentType("application/json"))
                .andDo(h -> assertEquals(200, h.getResponse().getStatus()));

        mockMvc.perform(get("/films/1")
                .contentType("application/json"))
                .andDo(h -> assertEquals(200, h.getResponse().getStatus()));

        mockMvc.perform(get("/films/2")
                .contentType("application/json"))
                .andDo(h -> assertEquals(404, h.getResponse().getStatus()));

        mockMvc.perform(get("/films/popular")
                .contentType("application/json"))
                .andDo(h -> assertEquals(200, h.getResponse().getStatus()));

        mockMvc.perform(delete("/films/1")
                .contentType("application/json"))
                .andDo(h -> assertEquals(200, h.getResponse().getStatus()));

        assertEquals(Collections.EMPTY_LIST, filmController.getAll());
    }

    @Test
    void shouldBe400ResponseForInvalidFilmData() throws Exception {
        String inValidFilm = objectMapper.writeValueAsString(Film.builder()
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3,25))
                .duration(-100)
                .build()
        );
        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(inValidFilm))
                .andDo(h -> assertEquals(400, h.getResponse().getStatus()));
    }

    @Test
    void shouldBe500ResponseForEmptyBodyRequest() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(""))
                .andDo(h -> assertEquals(500, h.getResponse().getStatus()));
    }

    @Test
    void shouldBe404ResponseForWrongURI() throws Exception {
        mockMvc.perform(get("/film")
                        .contentType("application/json"))
                .andDo(h -> assertEquals(404, h.getResponse().getStatus()));
    }
}
