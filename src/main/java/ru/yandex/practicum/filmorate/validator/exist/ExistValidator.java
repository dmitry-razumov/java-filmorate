package ru.yandex.practicum.filmorate.validator.exist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import lombok.extern.slf4j.Slf4j;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
//@RequiredArgsConstructor
public class ExistValidator implements ConstraintValidator<Exist, Object> {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    String type;

    @Autowired
    public ExistValidator(@Qualifier("userDbStorage") UserStorage userStorage,
                          @Qualifier("filmDbStorage") FilmStorage filmStorage,
                          MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public void initialize(Exist annotation) {
        this.type = annotation.message();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        switch (type) {
            case "film": {
                if (o instanceof Long) {
                    return isExist(null, filmStorage, null, null, (Long) o);
                }
                if (o instanceof Film) {
                    Film film = (Film) o;
                    return isExist(null, filmStorage, null, null, film.getId());
                }
                break;
            }
            case "user": {
                if (o instanceof Long) {
                    return isExist(userStorage, null, null, null, (Long) o);
                }
                if (o instanceof User) {
                    User user = (User) o;
                    return isExist(userStorage, null, null, null, user.getId());
                }
                break;
            }
            case "mpa": {
                if (o instanceof Integer) {
                    return isExist(null, null, mpaStorage, null, (long) (Integer) o);
                }
                if (o instanceof Mpa) {
                    Mpa mpa = (Mpa) o;
                    return isExist(null, null, mpaStorage, null, (long) mpa.getId());
                }
                break;
            }
            case "genre": {
                if (o instanceof Integer) {
                    return isExist(null, null, null, genreStorage, (long) (Integer) o);
                }
                if (o instanceof Genre) {
                    Genre genre = (Genre) o;
                    return isExist(null, null, null, genreStorage, (long) genre.getId());
                }
                break;
            }
            default: {
                log.debug(type + " not supported!");
            }
        }
        return false;
    }

    private boolean isExist(UserStorage userStorage, FilmStorage filmStorage, MpaStorage mpaStorage, GenreStorage genreStorage, Long id) {
        if (userStorage != null) {
            if (id != null) {
                if (userStorage.isExistById(id)) {
                    return true;
                }
            }
        }
        if (filmStorage != null) {
            if (id != null) {
                if (filmStorage.isExistById(id)) {
                    return true;
                }
            }
        }
        if (mpaStorage != null) {
            if (id != null) {
                if (mpaStorage.isExistById(id.intValue())) {
                    return true;
                }
            }
        }
        if (genreStorage != null) {
            if (id != null) {
                if (genreStorage.isExistById(id.intValue())) {
                    return true;
                }
            }
        }
        return false;
    }
}
