package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.practicum.dictionary.WordleDictionary;
import ru.yandex.practicum.exception.sourceException.EmptySourceException;
import ru.yandex.practicum.exception.sourceException.MissingSourceException;
import ru.yandex.practicum.util.loader.FileSourceProvider;
import ru.yandex.practicum.util.logger.NullLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class WordleDictionaryTest {

    @TempDir
    protected Path tempDir;

    @Test
    void testNormalizationAndFiltering() throws IOException, MissingSourceException, EmptySourceException {
        Path file = tempDir.resolve("valid.txt");
        Files.write(file, List.of("кОшка", "Фонарь", "арбузик", "", " "));
        FileSourceProvider fsp = new FileSourceProvider(file, new NullLogger());
        WordleDictionary dictionary = new WordleDictionary(fsp.load(), new NullLogger());
        List<String> words = dictionary.words();
        assertEquals(1, words.size());
        assertTrue(words.contains("кошка"));
        assertFalse(words.contains("Фонарь"));
        assertFalse(words.contains("арбузик"));
        assertFalse(words.contains(" "));
    }

    @Test
    void testEmptyDictionaryThrowsIllegalStateException() throws IOException {
        Path file = tempDir.resolve("not_normal.txt");
        Files.write(file, List.of("Фонарь", "арбузик", "", " "));
        FileSourceProvider fsp = new FileSourceProvider(file, new NullLogger());
        assertThrows(IllegalStateException.class, () -> new WordleDictionary(fsp.load(), new NullLogger()));
    }
}
