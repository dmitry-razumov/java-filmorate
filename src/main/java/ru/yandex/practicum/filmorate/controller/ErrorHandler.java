package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.service.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(ConstraintViolationException e) {
        log.error(String.format("Object not found error: %s", e.getMessage()));
        return new ErrorResponse(String.format("Object not found error: %s", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException e) {
        log.error(String.format("Validation error: %s", e.getMessage()));
        return new ErrorResponse(String.format("Validation error: %s", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(String.format("MethodArgumentNotValid: %s", e.getMessage()));
        return new ErrorResponse(String.format("MethodArgumentNotValid error: %s", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        log.error(String.format("Internal error: %s", e));
        return new ErrorResponse(String.format("Internal error: %s", e.getMessage()));
    }
}
