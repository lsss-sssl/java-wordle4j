package ru.yandex.practicum.util.validator;

import ru.yandex.practicum.exception.logicException.ValidateException;
import ru.yandex.practicum.exception.logicException.ValidateLengthException;

public class LengthValidator implements Validator {

    @Override
    public void validate(final String word) throws ValidateException {
        if (word.length() != 5) throw new ValidateLengthException(word);
    }
}
