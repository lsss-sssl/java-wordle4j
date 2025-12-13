package ru.yandex.practicum.gameEngine;

import ru.yandex.practicum.dictionary.WordleDictionary;
import ru.yandex.practicum.exception.gameLogicException.ValidateException;
import ru.yandex.practicum.exception.gameLogicException.WordNotFoundInDictionary;
import ru.yandex.practicum.util.CharactersValidator;
import ru.yandex.practicum.util.LangValidator;
import ru.yandex.practicum.util.LengthValidator;
import ru.yandex.practicum.util.Validator;

import java.util.ArrayList;
import java.util.List;

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

    private final char[] answer;
    private int attemptsLeft;
    private final WordleDictionary dictionary;
    private final char[] correctChars;
    private final List<String> foundChars;
    private final List<String> invalidChars;
    private final List<String> hints;
    private final List<String> attempts;
    private Status status;
    private final List<Validator> validators;

    public WordleGame(final WordleDictionary wordleDictionary) {
        this.dictionary = wordleDictionary;
        this.answer = wordleDictionary.getGuessedWord();
        this.attemptsLeft = 6;
        this.correctChars = new char[5];
        this.foundChars = new ArrayList<>();
        this.invalidChars = new ArrayList<>();
        this.hints = wordleDictionary.getWords();
        this.attempts = new ArrayList<>();
        this.status = Status.IN_PROGRESS;
        validators = List.of(new LengthValidator(), new CharactersValidator(), new LangValidator());
    }

    public String check(final String word) throws ValidateException, WordNotFoundInDictionary {
        decreaseAttempts();
        String response;
        if (!word.isEmpty()) {
            checkValidationRules(word);
            checkDictionary(word);

            response = word;
        } else {
            response = getHint();
        }
        dictionary.remove(response);
        attempts.add(response);
        

        return response;
    }
    private void updateCharacters(final String word) {
        char[] sourceChars = word.toCharArray();

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
