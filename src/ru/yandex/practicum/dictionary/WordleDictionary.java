package ru.yandex.practicum.dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public final class WordleDictionary {

    private static List<String> removedWords;
    private static List<String> words;
    private final Random random;


    public WordleDictionary(final List<String> sourceWords) {
        removedWords = new ArrayList<>();
        words = normalize(sourceWords);
        random = new Random();
    }

    private List<String> normalize(final List<String> sourceWords) {
        final List<String> words = new ArrayList<>();
        for (String word : sourceWords) {
            if (word.length() == 5)
                words.add(word.toLowerCase().replace('ё', 'е'));
        }
        return words;
    }

    public String getGuessedWord() {
        final int index = random.nextInt(words.size());
        String guessedWord = words.get(index);
        words.remove(guessedWord);
        return guessedWord;
    }

    public List<String> getWords() {
        return words;
    }

    public void remove(final String word) {
        removedWords.add(word);
        words.remove(word);
    }

    public boolean contains(final String word) {
        return words.contains(word) | removedWords.contains(word);
    }
}