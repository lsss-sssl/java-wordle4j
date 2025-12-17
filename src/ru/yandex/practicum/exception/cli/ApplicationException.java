package ru.yandex.practicum.exception.cli;

public abstract class ApplicationException extends RuntimeException {
    protected ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract String userMessage();
}
