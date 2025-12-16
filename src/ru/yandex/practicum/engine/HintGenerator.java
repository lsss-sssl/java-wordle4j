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
        for (int i = 0; i < context.wordLength(); i++) {
            if (context.correct()[i] != '\u0000' && word.charAt(i) != context.correct()[i]) return false;
        }
        for (char c : context.excluded()) {
            if (word.indexOf(c) >= 0) return false;
        }
        for (char c : context.presented()) {
            if (word.indexOf(c) < 0) return false;
        }
        return true;
    }

}
