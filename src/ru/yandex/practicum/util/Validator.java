package ru.yandex.practicum.util;

import ru.yandex.practicum.exception.gameLogicException.ValidateException;

public interface Validator {
    void validate(String word) throws ValidateException;
}

