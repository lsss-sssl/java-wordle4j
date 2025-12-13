package ru.yandex.practicum.util;

import ru.yandex.practicum.exception.gameLogicException.ValidateException;
import ru.yandex.practicum.exception.gameLogicException.ValidateLengthException;

public class LengthValidator implements Validator {

    @Override
    public void validate(final String word) throws ValidateException {
        if (word.length() != 5) throw new ValidateLengthException("Word length must be 5.");
    }
}
