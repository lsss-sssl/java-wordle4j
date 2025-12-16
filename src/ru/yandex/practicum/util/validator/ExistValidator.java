package ru.yandex.practicum.util.validator;

import ru.yandex.practicum.exception.logicException.ValidateException;
import ru.yandex.practicum.exception.logicException.WordNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExistValidator implements Validator {

    private final List<String> dictionary;

    public ExistValidator(List<String> dictionary) {
        this.dictionary = new ArrayList<>(dictionary);
    }

    @Override
    public void validate(final String word) throws ValidateException {
        if (!dictionary.contains(word.toLowerCase(Locale.ROOT))) {
            throw new WordNotFoundException(word);
        }
    }
}
