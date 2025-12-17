package ru.yandex.practicum.util.validator;

import ru.yandex.practicum.exception.logicException.ValidateCharactersException;
import ru.yandex.practicum.exception.logicException.ValidateException;

public class CharactersValidator implements Validator {
    @Override
    public void validate(String word) throws ValidateException {
        for (Character c : word.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
                throw new ValidateCharactersException(word);
            }
        }
    }
}
