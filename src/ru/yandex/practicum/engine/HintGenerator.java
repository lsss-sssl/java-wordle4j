package ru.yandex.practicum.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HintGenerator {
    private final List<String> hints;
    private final Random random;

    public HintGenerator(List<String> dictionary, Random random) {
        this.hints = new ArrayList<>(dictionary);
        this.random = random;
    }

    public String randomHint() {
        return hints.get((random.nextInt(hints.size())));
    }

    public void updateHints(GameContext context) {
        hints.removeIf(hint -> !isValid(hint, context));
    }

    private boolean isValid(final String word, GameContext context) {
        return matchCorrect(word, context) &&
                notContainsExcluded(word, context) &&
                containsPresented(word, context);
    }

    private boolean matchCorrect(final String word, final GameContext context) {
        for (int i = 0; i < context.wordLength(); i++) {
            if (context.correct()[i] != '\u0000' && word.charAt(i) != context.correct()[i]) return false;
        }
        return true;
    }

    private boolean notContainsExcluded(final String word, final GameContext context) {
        for (char c : context.excluded()) {
            if (word.indexOf(c) >= 0) return false;
        }
        return true;
    }

    private boolean containsPresented(final String word, final GameContext context) {
        for (char c : context.presented()) {
            if (word.indexOf(c) < 0) return false;
        }
        return true;
    }
}