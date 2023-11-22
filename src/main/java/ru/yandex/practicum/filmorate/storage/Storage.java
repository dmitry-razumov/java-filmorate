package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface Storage<T> {
    T create(T t);

    T update(T t);

    void delete(long id);

    List<T> getAll();

    T getById(long id);
}
