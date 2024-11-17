package ru.yandex.workshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiError {
    private String message;
    private String status;
    private String reason;
    private String timestamp;
}
