package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO FILMS (name, description, release_date, duration, rate, mpa_id)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setLong(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        Optional<LinkedHashSet<Genre>> filmGenres = Optional.ofNullable(film.getGenres());
        if (filmGenres.isPresent()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO FILM_GENRE (film_id, genre_id) VALUES (?,?)", film.getId(), genre.getId());
            }
        }
        log.info("добавлен фильм - {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE FILMS " +
                "SET name = ?, description = ?, release_date = ?, duration = ?, rate = ?, mpa_id = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId()
        );
        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE film_id  = ?", film.getId());
        Optional<LinkedHashSet<Genre>> filmGenres = Optional.ofNullable(film.getGenres());
        if (filmGenres.isPresent()) {
            for (Genre genre : filmGenres.get()) {
                jdbcTemplate.update("INSERT INTO FILM_GENRE (film_id, genre_id) " +
                        "VALUES (?, ?)", film.getId(), genre.getId());
            }
        }
        log.info("обновлен фильм - {}", film);
        return film;
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM FILMS WHERE id =?", id);
        log.info("удален фильм с id - {}", id);
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT f.id, f.name , f.description, f.release_date, f.duration, f.rate, " +
                "m.id AS mpa_id, m.name AS mpa_name, STRING_AGG(DISTINCT g.id || '-' || g.name, ',') AS genres, " +
                "STRING_AGG(DISTINCT l.user_id, ',') AS likes, " +
                "LENGTH (STRING_AGG(distinct l.user_id,'' )) AS likes_count " +
                "FROM FILMS f " +
                "LEFT JOIN MPA m ON f.mpa_id = m.id " +
                "LEFT JOIN film_genre fg ON f.id = fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id = g.id " +
                "LEFT JOIN film_likes l ON f.id = l.film_id " +
                "GROUP BY f.id";
        List<Film> filmList = Optional.ofNullable(jdbcTemplate.query(sql, this::mapRowToFilm))
                .orElse(Collections.emptyList());
        log.info("получены все фильмы");
        return filmList;
    }

    @Override
    public boolean isExistById(long id) {
        return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM FILMS WHERE id = ?)", Boolean.class, id);
    }

    @Override
    public Film getById(long id) {
        String sql = "SELECT f.id, f.name , f.description, f.release_date, f.duration, f.rate, " +
                "m.id AS mpa_id, m.name AS mpa_name, STRING_AGG(DISTINCT g.id || '-' || g.name, ',') AS genres, " +
                "STRING_AGG(DISTINCT l.user_id, ',') AS likes, " +
                "LENGTH (STRING_AGG(distinct l.user_id,'' )) AS likes_count " +
                "FROM FILMS f " +
                "LEFT JOIN MPA m ON f.mpa_id = m.id " +
                "LEFT JOIN film_genre fg ON f.id = fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id = g.id " +
                "LEFT JOIN film_likes l ON f.id = l.film_id " +
                "WHERE f.id = ? " +
                "GROUP BY f.id";
        Film film = jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
        log.info("получен фильм c id - {}", id);
        return film;
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        String sql = "SELECT f.id, f.name , f.description, f.release_date, f.duration, f.rate, " +
                "m.id AS mpa_id, m.name AS mpa_name, STRING_AGG(DISTINCT g.id || '-' || g.name, ',') AS genres, " +
                "STRING_AGG(DISTINCT l.user_id, ',') AS likes, " +
                "LENGTH (STRING_AGG(distinct l.user_id,'' )) AS likes_count " +
                "FROM FILMS f " +
                "LEFT JOIN MPA m ON f.mpa_id = m.id " +
                "LEFT JOIN film_genre fg ON f.id = fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id = g.id " +
                "LEFT JOIN film_likes l ON f.id = l.film_id " +
                "GROUP BY f.id " +
                "ORDER BY likes_count DESC " +
                "LIMIT ?";

        List<Film> filmList = Optional.ofNullable(jdbcTemplate.query(sql, this::mapRowToFilm, count))
                .orElse(Collections.emptyList());
        return filmList;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        Film film = Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getLong("duration"))
                .rate(resultSet.getInt("rate"))
                .mpa(new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa_name")))
                .genres(genres)
                .build();

        Optional<String> optGenres = Optional.ofNullable(resultSet.getString("genres"));
        if (optGenres.isPresent()) {
            film.setGenres(Arrays.stream(optGenres.get().split(","))
                    .map(x -> new Genre(Integer.parseInt(x.split("-")[0]),x.split("-")[1]))
                    .collect(Collectors.toCollection(LinkedHashSet::new))
            );
        }
        return film;
    }

    @Override
    public void addLikeToFilm(long id, long userId) {
        String sql = "INSERT INTO FILM_LIKES (user_id, film_id) VALUES (?,?)";
        jdbcTemplate.update(sql, userId, id);
    }

    @Override
    public void deleteLikeFromFilm(long id, long userId) {
        String sql = "DELETE FROM FILM_LIKES WHERE user_id = ? AND film_id = ?";
        jdbcTemplate.update(sql, userId, id);
    }
}
