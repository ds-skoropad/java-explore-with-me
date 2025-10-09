package ru.practicum.ewm.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.MainConstants.DATE_TIME_PATTERN;

public record ApiError(
        List<String> errors,
        String message,
        String reason,
        String status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
        LocalDateTime timestamp
) {
}
