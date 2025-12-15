package ru.yandex.practicum.exception.systemErrors;

public class WordleDictionaryCreationException extends RuntimeException {
    public WordleDictionaryCreationException(String message) {
        super(message);
    }
}
