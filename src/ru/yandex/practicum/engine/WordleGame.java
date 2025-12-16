package ru.yandex.practicum.engine;

import java.util.*;

import ru.yandex.practicum.dictionary.WordleDictionary;
import ru.yandex.practicum.exception.logicException.ValidateException;
import ru.yandex.practicum.exception.logicException.GameIsFinishedException;
import ru.yandex.practicum.util.logger.Logger;
import ru.yandex.practicum.util.validator.*;

import static ru.yandex.practicum.dictionary.WordleDictionary.wordLength;

public final class WordleGame {

    private static final int MAX_ATTEMPTS = 6;

    private final String answer;
    private final int wordLength;

    private final char[] correct;
    private final Set<Character> presented = new HashSet<>();
    private final Set<Character> excluded = new HashSet<>();

    private int attemptsLeft = MAX_ATTEMPTS;

    private GameState state = GameState.IN_PROGRESS;

    private final HintGenerator hintGenerator;
    private final List<Validator> validators;

    private final Logger logger;

    public WordleGame(WordleDictionary dictionary, Random random, Logger logger) {
        this.answer = dictionary.words().get(random.nextInt(dictionary.words().size()));
        this.wordLength = wordLength();
        this.correct = new char[wordLength()];
        this.logger = logger;
        this.hintGenerator = new HintGenerator(dictionary.words(), random);
        this.validators = List.of(
                new LengthValidator(),
                new CharactersValidator(),
                new LangValidator(),
                new ExistValidator(dictionary.words()));
        logger.info("Game created. Answer chosen");
    }
    public String answer() {
        return answer;
    }

    public GameState status() {
        return state;
    }

    public int attempts() {
        return attemptsLeft;
    }

    public GuessResult process(final String word) throws ValidateException, GameIsFinishedException {
        ensureInProgress();
        String input = word;
        boolean isHint = false;
        if (word.isEmpty()) {
            input = hintGenerator.randomHint();
            isHint = true;
            logger.info("Hint provided: " + input);
        } else {
            validate(word);
            logger.info("Player guessed: " + word);
        }
        decreaseAttempts();
        String pattern = evaluate(input);
        hintGenerator.updateHints(new GameContext(correct, presented, excluded, wordLength));
        transition(input);
        if (isHint) {
            pattern = input;
        }
        return new GuessResult(pattern, state, attemptsLeft);
    }

    private void transition(final String word) {
        GameEvent event;
        if (word.equals(answer)) {
            event = GameEvent.WIN;
        } else if (attemptsLeft == 0) {
            event = GameEvent.OUT_OF_ATTEMPTS;
        } else {
            event = GameEvent.CONTINUE;
        }
        state = state.next(event);
    }

    private String evaluate(String word) {
        String[] result = new String[wordLength];
        boolean[] used = new boolean[wordLength];

        for (int i = 0; i < wordLength(); i++) {
            if (word.charAt(i) == answer.charAt(i)) {
                result[i] = "+";
                correct[i] = answer.charAt(i);
                presented.add(answer.charAt(i));
                used[i] = true;
            }
            if (answer.indexOf(word.charAt(i)) < 0) excluded.add(word.charAt(i));
        }
        for (int i = 0; i < wordLength(); i++) {
            if (result[i] == null) {
                boolean found = false;
                for (int j = 0; j < wordLength(); j++) {
                    if (!used[j] && word.charAt(i) == answer.charAt(j)) {
                        found = true;
                        used[j] = true;
                        presented.add(answer.charAt(j));
                        break;
                    }
                }
                result[i] = found ? "^" : "-";
            }
        }
        return String.join("", result);
    }

    private void decreaseAttempts() {
        attemptsLeft--;
    }

    private void validate(final String word) throws ValidateException {
        for (Validator validator : validators) {
            validator.validate(word);
        }
    }

    private void ensureInProgress() throws GameIsFinishedException {
        if (state != GameState.IN_PROGRESS) {
            throw new GameIsFinishedException("game already finished");
        }
    }
}
