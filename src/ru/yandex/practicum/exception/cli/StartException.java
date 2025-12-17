package ru.yandex.practicum.exception.cli;

public class StartException extends ApplicationException {

    public StartException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String userMessage() {
        return "[FAIL] - application launch error: " + getMessage();
    }
}
