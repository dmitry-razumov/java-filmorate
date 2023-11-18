package ru.yandex.practicum.filmorate.service.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjNotFoundException extends RuntimeException {
    public ObjNotFoundException(String message) {
        super(message);
    }
}
