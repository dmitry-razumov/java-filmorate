package ru.yandex.practicum.filmorate.validator.exist;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@RequiredArgsConstructor
public class ExistValidator implements ConstraintValidator<Exist, Object> {
    final UserStorage userStorage;
    final FilmStorage filmStorage;
    String type;

    @Override
    public void initialize(Exist annotation) {
        this.type = annotation.message();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        switch (type) {
            case "film": {
                if (o instanceof Long) {
                    return isExist(null, filmStorage, (Long) o);
                }
                if (o instanceof Film) {
                    Film film = (Film) o;
                    return isExist(null, filmStorage, film.getId());
                }
                break;
            }
            case "user": {
                if (o instanceof Long) {
                    return isExist(userStorage, null, (Long) o);
                }
                if (o instanceof User) {
                    User user = (User) o;
                    return isExist(userStorage, null, user.getId());
                }
                break;
            }
            default: {
                log.debug(type + " not supported!");
            }
        }
        return false;
    }

    private boolean isExist(UserStorage userStorage, FilmStorage filmStorage, Long id) {
        if (userStorage != null) {
            if (id != null) {
                if (userStorage.getAll().stream().anyMatch(user -> user.getId() == id))
                    return true;
            }
        }
        if (filmStorage != null) {
            if (id != null) {
                if (filmStorage.getAll().stream().anyMatch(film -> film.getId() == id))
                    return true;
            }
        }
        return false;
    }
}
