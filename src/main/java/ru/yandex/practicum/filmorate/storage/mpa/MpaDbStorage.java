package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {
        String sql = "SELECT m.id, m.name FROM MPA m";
        List<Mpa> mpaList = Optional.ofNullable(jdbcTemplate.query(sql, this::mapRowToMpa))
                .orElse(Collections.emptyList());
        return mpaList;
    }

    @Override
    public Mpa getMpaById(int id) {
        String sql = "SELECT m.id, m.name FROM MPA m WHERE m.id = ?;";
        return jdbcTemplate.queryForObject(sql, this::mapRowToMpa, id);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    @Override
    public boolean isExistById(long id) {
        return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM mpa WHERE id = ?)", Boolean.class, id);
    }
}
