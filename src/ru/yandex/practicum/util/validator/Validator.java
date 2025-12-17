package ru.yandex.practicum.util.validator;

import ru.yandex.practicum.exception.logicException.ValidateException;

public interface Validator {
    void validate(String word) throws ValidateException;
}

