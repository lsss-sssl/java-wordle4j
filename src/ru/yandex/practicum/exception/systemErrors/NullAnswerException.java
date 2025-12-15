package ru.yandex.practicum.exception.systemErrors;

public class NullAnswerException extends RuntimeException {
    public NullAnswerException(String message) {
        super(message);
    }
}
