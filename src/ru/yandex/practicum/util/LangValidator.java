package ru.yandex.practicum.util;

import ru.yandex.practicum.exception.gameLogicException.ValidateException;
import ru.yandex.practicum.exception.gameLogicException.ValidateLangException;

public class LangValidator implements Validator {
    @Override
    public void validate(String word) throws ValidateException {
        for (int i = 0; i < word.length(); i++) {
            if (!Character.UnicodeBlock.of(word.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)) {
                throw new ValidateLangException("Word must contain ONLY Cyrillic characters.");
            }
        }

    }
}
