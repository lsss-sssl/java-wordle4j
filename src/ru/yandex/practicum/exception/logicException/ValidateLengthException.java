package ru.yandex.practicum.exception.logicException;

public class ValidateLengthException extends ValidateException {

    public ValidateLengthException(final String word) {
        super("Word length must be equal to 5: " + word);
    }
}
