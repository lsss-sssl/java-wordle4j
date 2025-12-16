package ru.yandex.practicum.dictionary;

import ru.yandex.practicum.util.logger.Logger;

import java.util.*;

public final class WordleDictionary {
    private final List<String> words;
    private static final int WORD_LENGTH = 5;

    public WordleDictionary(final List<String> words, Logger logger) {
        this.words = words.stream()
                .map(this::normalize)
                .filter(this::isValid)
                .toList();
        logger.info("Dictionary created");
        if (this.words.isEmpty()) {
            logger.info("Dictionary is empty");
            throw new IllegalStateException("dictionary is empty");
        }
    }

    private String normalize(final String word) {
        return word.toLowerCase(Locale.ROOT).replace("ë", "е");
    }

    private boolean isValid(final String word) {
        return word.length() == WORD_LENGTH;
    }

    public static int wordLength() {
        return WORD_LENGTH;
    }

    public List<String> words() {
        return List.copyOf(words);
    }
}