package ru.yandex.practicum.exception.systemErrors;

public class WordleDictionaryEmptyException extends RuntimeException {
    public WordleDictionaryEmptyException(String message) {
        super(message);
    }
}
