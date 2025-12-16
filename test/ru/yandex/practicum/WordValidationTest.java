package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ru.yandex.practicum.dictionary.WordleDictionary;
import ru.yandex.practicum.engine.WordleGame;
import ru.yandex.practicum.exception.logicException.*;
import ru.yandex.practicum.exception.sourceException.EmptySourceException;
import ru.yandex.practicum.exception.sourceException.MissingSourceException;
import ru.yandex.practicum.util.loader.FileSourceProvider;
import ru.yandex.practicum.util.logger.NullLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class WordValidationTest {

    static FileSourceProvider fsp;
    static WordleDictionary dictionary;
    static Random random;
    static WordleGame game;

    @TempDir
    static Path tempDir;

    @BeforeEach
    void createGame() throws IOException, MissingSourceException, EmptySourceException {
        Path file = tempDir.resolve("valid.txt");
        Files.write(file, List.of("кошка"));
        fsp = new FileSourceProvider(file, new NullLogger());
        dictionary = new WordleDictionary(fsp.load(), new NullLogger());
        random = new Random();
        game = new WordleGame(dictionary, random, new NullLogger());
    }

    @Test
    void testInvalidLengthThrowsValidateLengthException() {
        assertThrows(ValidateLengthException.class, () -> game.process("кош"));
        assertThrows(ValidateLengthException.class, () -> game.process("кошечка"));
    }

    @Test
    void testInvalidLangThrowsValidateLangException() {
        assertThrows(ValidateLangException.class, () -> game.process("koska"));
    }

    @Test
    void testInvalidCharacterThrowsValidateCharactersException() {
        assertThrows(ValidateCharactersException.class, () -> game.process("к0шка"));
    }

    @Test
    void testValidWordIfNotInDictionaryThrowsWordNotFoundException() {
        assertThrows(WordNotFoundException.class, () -> game.process("ложка"));
    }

    @Test
    void testValidWordPasses() throws ValidateException, GameIsFinishedException {
        assertEquals("+++++", game.process("кошка").pattern());
    }
}
