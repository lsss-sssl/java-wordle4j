package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.practicum.dictionary.WordleDictionary;
import ru.yandex.practicum.engine.GameState;
import ru.yandex.practicum.engine.ProcessResult;
import ru.yandex.practicum.engine.WordleGame;
import ru.yandex.practicum.exception.logicException.GameIsFinishedException;
import ru.yandex.practicum.exception.logicException.ValidateException;
import ru.yandex.practicum.exception.sourceException.EmptySourceException;
import ru.yandex.practicum.exception.sourceException.MissingSourceException;
import ru.yandex.practicum.util.loader.FileSourceProvider;
import ru.yandex.practicum.util.logger.NullLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordleGameTest {

    static FileSourceProvider fsp;
    static WordleDictionary dictionary;
    static Random random;
    static WordleGame game;
    static String answer;
    static String noAnswer1;
    static String noAnswer2;

    @TempDir
    static Path tempDir;

    @BeforeEach
    void createGame() throws IOException, MissingSourceException, EmptySourceException {
        Path file = tempDir.resolve("valid.txt");
        Files.write(file, List.of("кошка", "роман", "борат"));
        fsp = new FileSourceProvider(file, new NullLogger());
        dictionary = new WordleDictionary(fsp.load(), new NullLogger());
        random = new Random();
        game = new WordleGame(dictionary, random, new NullLogger());
        answer = game.answer();
        List<String> words = new ArrayList<>(List.copyOf(dictionary.words()));
        words.removeIf(word -> word.equals(answer));
        noAnswer1 = words.getFirst();
        noAnswer2 = words.getLast();
    }

    @Test
    void testCorrectGuessWin() throws ValidateException, GameIsFinishedException {
        ProcessResult result = game.process(answer);
        assertEquals("+++++", result.pattern());
        assertEquals(GameState.WIN, result.status());
    }

    @Test
    void testIncorrectGuessSixTimesLose() throws ValidateException, GameIsFinishedException {
        for (int i = 0; i < 6; i++) {
            game.process(noAnswer1);
        }
        assertEquals(GameState.LOSE, game.status());
        assertEquals(0, game.attempts());
    }

    @Test
    void testMatchPattern() throws ValidateException, GameIsFinishedException {
        ProcessResult result = game.process(noAnswer1);
        String pattern = result.pattern();
        assertTrue(pattern.contains("+") && pattern.contains("-") && pattern.contains("^"));
    }

    @Test
    void testEmptyInputGetsHint() throws ValidateException, GameIsFinishedException {
        String hint = game.process("").pattern();
        assertTrue(dictionary.words().contains(hint));
        assertTrue(hint.equals(answer) || hint.equals(noAnswer1) || hint.equals(noAnswer2));
    }

    @Test
    void testGameCycle() throws ValidateException, GameIsFinishedException {
        game.process(noAnswer1);
        assertThrows(ValidateException.class, () -> game.process("ложка"));
        game.process(noAnswer2);
        ProcessResult result3 = game.process("");
        assertThrows(GameIsFinishedException.class, () -> game.process(answer));
        assertEquals(3, result3.attemptsLeft());
        assertEquals(GameState.WIN, result3.status());
    }
}
