package ru.yandex.practicum.util;

import ru.yandex.practicum.exception.gameLogicException.ValidateCharactersException;
import ru.yandex.practicum.exception.gameLogicException.ValidateException;

public class CharactersValidator implements Validator {
    @Override
    public void validate(String word) throws ValidateException {
        for (Character c : word.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
                throw new ValidateCharactersException("Word must contain ONLY alphabetic characters.");
            }
        }
    }
}
