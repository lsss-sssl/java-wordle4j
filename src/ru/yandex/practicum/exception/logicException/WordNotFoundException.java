package ru.yandex.practicum.exception.logicException;

public class WordNotFoundException extends ValidateException {
    public WordNotFoundException(String word) {
        super("word not found in provided dictionary: " + word);
    }
}
