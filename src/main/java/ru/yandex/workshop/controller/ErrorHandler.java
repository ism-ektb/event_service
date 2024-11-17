package ru.yandex.workshop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.workshop.exception.ConflictException;
import ru.yandex.workshop.exception.NotFoundException;
import ru.yandex.workshop.model.ApiError;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConflictException e) {
        log.debug("Получен статус 409 CONFLICT {}", e.getMessage(), e);
        return new ApiError(e.getMessage(),
                HttpStatus.CONFLICT.toString(),
                Arrays.toString(e.getStackTrace()),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.debug("Получен статус 404 NOT FOUND {}", e.getMessage(), e);
        return new ApiError(e.getMessage(),
                HttpStatus.NOT_FOUND.toString(),
                Arrays.toString(e.getStackTrace()),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
