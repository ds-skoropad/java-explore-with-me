package ru.practicum.ewm.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(final ConflictException e) {
        String reason = "An integrity constraint has been violated.";
        log.warn("{}: {}", HttpStatus.CONFLICT.name(), reason, e);
        return new ApiError(List.of(), e.getMessage(), reason, HttpStatus.CONFLICT.name(), LocalDateTime.now());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNotValid(final NotValidException e) {
        String reason = "Invalid data.";
        log.warn("{}: {}", HttpStatus.CONFLICT.name(), reason, e);
        return new ApiError(List.of(), e.getMessage(), reason, HttpStatus.BAD_REQUEST.name(), LocalDateTime.now());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        String reason = "Required request body is missing.";
        log.warn("{}: {}", HttpStatus.CONFLICT, reason, e);
        return new ApiError(List.of(), e.getMessage(), reason, HttpStatus.BAD_REQUEST.name(), LocalDateTime.now());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingServletRequestParameter(final MissingServletRequestParameterException e) {
        String reason = "Equired request parameter is missing from the HTTP request.";
        log.warn("{}: {}", HttpStatus.BAD_REQUEST, reason, e);
        return new ApiError(List.of(), e.getMessage(), reason, HttpStatus.BAD_REQUEST.name(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(f -> String.format("field = %s, message = %s", f.getField(), f.getDefaultMessage()))
                .toList();
        String reason = "Incorrectly made request.";
        log.warn("{}: {}", HttpStatus.BAD_REQUEST, reason, e);
        return new ApiError(errors, e.getMessage(), reason, HttpStatus.BAD_REQUEST.name(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolation(final ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations().stream()
                .map(f -> String.format("property = %s, message = %s", f.getPropertyPath(), f.getMessage()))
                .toList();
        String reason = "Violation of data restrictions.";
        log.warn("{}: {}", HttpStatus.BAD_REQUEST, reason, e);
        return new ApiError(errors, e.getMessage(), reason, HttpStatus.BAD_REQUEST.name(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(final NotFoundException e) {
        String reason = "The required object was not found.";
        log.warn("{}: {}", HttpStatus.NOT_FOUND, reason, e);
        return new ApiError(List.of(), e.getMessage(), reason, HttpStatus.NOT_FOUND.name(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(final Exception e) {
        String reason = "Internal Server Error.";
        log.warn("{}: {}", HttpStatus.INTERNAL_SERVER_ERROR, reason, e);
        return new ApiError(List.of(), "", reason, HttpStatus.INTERNAL_SERVER_ERROR.name(),
                LocalDateTime.now());
    }
}
