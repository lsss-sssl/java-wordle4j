package ru.yandex.practicum.util;

import ru.yandex.practicum.dictionary.WordleDictionary;
import ru.yandex.practicum.exception.gameLogicException.ValidateException;
import ru.yandex.practicum.exception.gameLogicException.WordNotFoundInDictionary;

public class ExistInDictionaryValidator implements Validator{
    @Override
    public void validate(final String word) throws ValidateException {
        if (!WordleDictionary.getWords().contains(word)) throw new WordNotFoundInDictionary("Dictionary error");
    }
}
