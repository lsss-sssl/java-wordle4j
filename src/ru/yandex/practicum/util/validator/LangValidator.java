package ru.yandex.practicum.util.validator;

import ru.yandex.practicum.exception.logicException.ValidateException;
import ru.yandex.practicum.exception.logicException.ValidateLangException;

public class LangValidator implements Validator {
    @Override
    public void validate(String word) throws ValidateException {
        for (int i = 0; i < word.length(); i++) {
            if (!Character.UnicodeBlock.of(word.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)) {
                throw new ValidateLangException(word);
            }
        }
    }
}
