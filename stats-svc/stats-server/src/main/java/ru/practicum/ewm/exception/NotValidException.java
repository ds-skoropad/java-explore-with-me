package ru.practicum.ewm.exception;

public class NotValidException extends IllegalArgumentException {
    public NotValidException(String message) {
        super(message);
    }
}
