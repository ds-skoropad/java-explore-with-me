package ru.practicum.ewm.exception;

public record ExceptionResponse(
        String error,
        String description
) {
}
