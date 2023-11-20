package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface Storage <T> {
    T create(T o);

    T update(T o);

    void delete(long id);

    List<T> getAll();
}
