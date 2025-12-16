package ru.yandex.practicum.exception.logicException;

public class ValidateCharactersException extends ValidateException {
    public ValidateCharactersException(String word) {
        super("word must contain ONLY alphabetic letters: " + word);
    }
}
