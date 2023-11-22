package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT g.id, g.name FROM GENRES g";
        List<Genre> genreList = Optional.ofNullable(jdbcTemplate.query(sql, this::mapRowToGenre))
                .orElse(Collections.emptyList());
        return genreList;
    }

    @Override
    public Genre getGenreById(int id) {
        String sql = "SELECT g.id, g.name FROM GENRES g WHERE g.id = ?;";
        return jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    @Override
    public boolean isExistById(long id) {
        return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM GENRES WHERE id = ?)", Boolean.class, id);
    }
}
