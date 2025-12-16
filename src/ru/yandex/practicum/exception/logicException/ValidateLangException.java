package ru.yandex.practicum.exception.logicException;

public class ValidateLangException extends ValidateException {

    public ValidateLangException(final String word) {
        super("word must contain ONLY Cyrillic characters: " + word);
    }
}
