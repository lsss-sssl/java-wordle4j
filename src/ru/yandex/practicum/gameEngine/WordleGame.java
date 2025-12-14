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
    private final char[] correctChars;
    private final Map<Character, Integer> foundChars;
    private final Set<Character> invalidChars;
    private final List<String> hints;
    private final List<String> history;
    private Status status;
    private final List<Validator> validators;

    public WordleGame(final WordleDictionary wordleDictionary) {
        this.dictionary = wordleDictionary;
        this.answer = wordleDictionary.getGuessedWord();
        this.attemptsLeft = 6;
        this.correctChars = new char[5];
        this.foundChars = new HashMap<>();
        this.invalidChars = new HashSet<>();
        this.hints = wordleDictionary.getWords();
        this.history = new ArrayList<>();
        this.status = Status.IN_PROGRESS;
        this.validators = List.of(new LengthValidator(), new CharactersValidator(), new LangValidator());
    }

    public String check(final String word) throws ValidateException, WordNotFoundInDictionary {
        decreaseAttempts();
        final String response;
        boolean isHint = false;
        if (!word.isEmpty()) {
            checkValidationRules(word);
            checkDictionary(word);

            response = word;
        } else {
            isHint = true;
            response = getHint();
        }
        dictionary.remove(response);
        history.add(response);
        updateCharacters(response);



        

        return response;
    }
    private void updateCharacters(final String word) {
        char[] sourceChars = word.toCharArray();
        for (int i = 0; i < sourceChars.length; i++) {
            char c = sourceChars[i];
            if (c == answer.charAt(i)) {
                correctChars[i] = c;
            } else if (answer.indexOf(c) < 0) {
                invalidChars.add(c);
            } else {
                foundChars.merge(c, 1, Integer::sum);
            }
        }
    }

    private void updateStatus() {
        /**
         * equls check
         *
         */
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
        if(!dictionary.contains(word)) throw new WordNotFoundInDictionary("Word is missing in source file. Add word to file.");
    }

    private String getHint() {
        return "HINTS";
    }
}
