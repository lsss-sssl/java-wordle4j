package ru.yandex.practicum.dictionary;

import ru.yandex.practicum.exception.systemErrors.NullAnswerException;
import ru.yandex.practicum.exception.systemErrors.WordleDictionaryCreationException;
import ru.yandex.practicum.exception.systemErrors.WordleDictionaryEmptyException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/*
    этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public final class WordleDictionary {
    private static final int MAX_WORD_LENGTH = 5;
    private static List<String> words;

    public WordleDictionary(final String sourceName, final List<String> sourceWords, PrintWriter log) {
        try {
            words = normalize(sourceWords);
            log.printf("[PASS] - create Wordle dictionary from source file: %20s\n", sourceName);
        } catch (WordleDictionaryCreationException e) {
            log.printf("[FAIL] - create Wordle dictionary from source file: %20s\n", sourceName);
        } catch (WordleDictionaryEmptyException e) {
            log.println(e.getMessage());
        }

    }

    private List<String> normalize(final List<String> sourceWords) throws WordleDictionaryEmptyException {
        final List<String> words = new ArrayList<>();
        for (String word : sourceWords) {
            if (word.length() == MAX_WORD_LENGTH)
                words.add(word.toLowerCase().replace('ё', 'е'));
        }
        if (words.isEmpty()) throw new WordleDictionaryEmptyException("[FAIL] - final instance of dictionary is empty");
        else return words;
    }

    public String getGuessedWord() throws NullAnswerException {
        try {
            Random random = new Random();
            final int index = random.nextInt(words.size());
            return words.get(index);
        } catch (NullPointerException e) {
            throw new NullAnswerException("[FAIL] - create secret word");
        }
    }

    public static List<String> getWords() {
        return words;
    }

    public static int getMaxWordLength() {
        return MAX_WORD_LENGTH;
    }
}