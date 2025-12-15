package ru.yandex.practicum.engine;

import ru.yandex.practicum.dictionary.WordleDictionary;
import ru.yandex.practicum.exception.gameLogicException.ValidateCharactersException;
import ru.yandex.practicum.exception.gameLogicException.ValidateException;
import ru.yandex.practicum.exception.gameLogicException.ValidateLangException;
import ru.yandex.practicum.exception.gameLogicException.ValidateLengthException;
import ru.yandex.practicum.exception.gameLogicException.WordNotFoundInDictionary;
import ru.yandex.practicum.util.*;

import java.io.PrintWriter;
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
    //private final WordleDictionary dictionary;

    private final char[] correct;
    private final Set<Character> presented;
    private final Set<Character> excluded;

    private final List<String> hints;
    private Status status;
    private final List<Validator> validators;

    private final PrintWriter log;

    public WordleGame(final WordleDictionary wordleDictionary, PrintWriter log) {
        this.log = log;
        //this.dictionary = wordleDictionary;
        this.answer = wordleDictionary.getGuessedWord();
        this.attemptsLeft = 6;
        this.correct = new char[5];
        this.presented = new HashSet<>();
        this.excluded = new HashSet<>();
        this.hints = new ArrayList<>(WordleDictionary.getWords());
        this.status = Status.IN_PROGRESS;
        this.validators = List.of(new LengthValidator(),
                new CharactersValidator(),
                new LangValidator(),
                new ExistInDictionaryValidator());
    }

    public String getAnswer() {
        return answer;
    }

    public String check(String word) throws ValidateException {
        boolean isHint = false;
        if (!word.isEmpty()) {
            checkValidationRules(word);
        } else {
            word = getHint();
            isHint = true;
        }
        decreaseAttempts();
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
        return isHint ? word : String.join("", output);

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
        log.printf("[PASS] - update user status: %42s\n", status);
    }

    public Status getStatus() {
        return status;
    }

    public Integer getAttemptsLeft() {
        return attemptsLeft;
    }

    private void decreaseAttempts() {
        attemptsLeft--;
        if (attemptsLeft <= 0) status = Status.IS_LOSE;
    }

    private void checkValidationRules(final String word) throws ValidateException {
        try {
            for (Validator validator : validators) {
                validator.validate(word);
            }
        } catch (ValidateLengthException e) {
            log.printf("[FAIL] - validation length input: %48s\n", e.getMessage());
            throw e;
        } catch (ValidateLangException e) {
            log.printf("[FAIL] - validation language input: %67s\n", e.getMessage());
            throw e;
        } catch (ValidateCharactersException e) {
            log.printf("[FAIL] - validation characters input: %67s\n", e.getMessage());
            throw e;
        } catch (WordNotFoundInDictionary e) {
            log.printf("[FAIL] - missing in source file: %43s\n", e.getMessage());
            throw e;
        } catch (ValidateException e) {
            log.printf("[ERROR] - unexcepted input error: %50s\n", e.getMessage());
            throw e;
        }
    }
}
