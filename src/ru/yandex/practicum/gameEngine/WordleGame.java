package ru.yandex.practicum.gameEngine;

import ru.yandex.practicum.dictionary.WordleDictionary;
import ru.yandex.practicum.exception.gameLogicException.ValidateException;
import ru.yandex.practicum.exception.gameLogicException.WordNotFoundInDictionary;
import ru.yandex.practicum.util.CharactersValidator;
import ru.yandex.practicum.util.LangValidator;
import ru.yandex.practicum.util.LengthValidator;
import ru.yandex.practicum.util.Validator;

import java.util.*;

/*
    в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

    в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

    не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */

public class WordleGame {

    private final String answer;
    private int attemptsLeft;
    private final WordleDictionary dictionary;

    private final char[] correct;
    private final Set<Character> presented;
    private final Set<Character> excluded;

    private final List<String> hints;
    private Status status;
    private final List<Validator> validators;

    public WordleGame(final WordleDictionary wordleDictionary) {
        this.dictionary = wordleDictionary;
        this.answer = wordleDictionary.getGuessedWord();
        this.attemptsLeft = 6;
        this.correct = new char[5];
        this.presented = new HashSet<>();
        this.excluded = new HashSet<>();
        this.hints = new ArrayList<>(wordleDictionary.getWords());
        this.status = Status.IN_PROGRESS;
        this.validators = List.of(new LengthValidator(), new CharactersValidator(), new LangValidator());
    }

    public String getAnswer() {
        return answer;
    }

    public String check( String word) throws ValidateException, WordNotFoundInDictionary {
        decreaseAttempts();
        boolean isHint = false;
        if (!word.isEmpty()) {
            checkValidationRules(word);
            checkDictionary(word);
        } else {
            word = getHint();
            isHint = true;
        }

        String[] output = new String[WordleDictionary.getMaxWordLength()];
        boolean[] used = new boolean[WordleDictionary.getMaxWordLength()];

        for (int i = 0; i < WordleDictionary.getMaxWordLength(); i++) {
            if (word.charAt(i) == answer.charAt(i)) {
                output[i] = "+";
                correct[i] = answer.charAt(i);
                presented.add(answer.charAt(i));
                used[i] = true;
            }
            if (answer.indexOf(word.charAt(i)) < 0) excluded.add(word.charAt(i));
        }

        for (int i = 0; i < WordleDictionary.getMaxWordLength(); i++) {
            if (output[i] == null) {
                boolean found = false;
                for (int j = 0; j < WordleDictionary.getMaxWordLength(); j++) {
                    if (!used[j] && word.charAt(i) == answer.charAt(j)) {
                        found = true;
                        used[j] = true;
                        presented.add(answer.charAt(j));
                        break;
                    }
                }
                output[i] = found ? "^" : "-";
            }
        }
        updateHints();
        updateStatus(word);
        return isHint ? word: String.join("", output);

    }

    private boolean isValidHint(final String word) {
        for (int i = 0; i < WordleDictionary.getMaxWordLength(); i++) {
            if (correct[i] != '\u0000' && word.charAt(i) != correct[i]) return false;
        }
        for (char c : excluded) {
            if (word.indexOf(c) >= 0) return false;
        }
        for (char c : presented) {
            if (word.indexOf(c) < 0) return false;
        }
        return true;
    }

    private void updateHints() {
        List<String> currentHints = List.copyOf(hints);
        for (String hint : currentHints) {
            if (!isValidHint(hint)) hints.remove(hint);
        }
    }

    private String getHint() {
        Random random = new Random();
        int index = random.nextInt(hints.size());
        return hints.get(index);
    }

    private void updateStatus(final String input) {
        if (input.equals(answer) | hints.isEmpty()) status = Status.IS_WIN;
        else if (attemptsLeft == 0) status = Status.IS_LOSE;
    }

    public Status getStatus() {
        return status;
    }

    public Integer getAttemptsLeft() {
        return attemptsLeft;
    }

    private void decreaseAttempts() {
        attemptsLeft--;
    }

    private void checkValidationRules(final String word) throws ValidateException {
        for (Validator validator : validators) {
            validator.validate(word);
        }
    }

    private void checkDictionary(final String word) throws WordNotFoundInDictionary {
        if (!dictionary.contains(word))
            throw new WordNotFoundInDictionary("Word is missing in source file. Add word to file.");
    }
}
