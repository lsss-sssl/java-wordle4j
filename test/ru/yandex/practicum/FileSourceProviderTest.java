package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.practicum.exception.sourceException.EmptySourceException;
import ru.yandex.practicum.exception.sourceException.MissingSourceException;
import ru.yandex.practicum.util.loader.FileSourceProvider;
import ru.yandex.practicum.util.logger.NullLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class FileSourceProviderTest {

    @TempDir
    Path tempDir;

    @Test
    void testLoadValidFile() throws IOException, MissingSourceException, EmptySourceException {
        Path file = tempDir.resolve("words.txt");
        Files.write(file, List.of("кошка", "мышка", "арбуз"));
        FileSourceProvider fsp = new FileSourceProvider(file, new NullLogger());
        List<String> words = fsp.load();
        assertEquals(3, words.size());
        assertTrue(words.contains("кошка"));
        assertTrue(words.contains("мышка"));
        assertTrue(words.contains("арбуз"));

    }

    @Test
    void testLoadEmptyFileThrowsEmptySourceException() throws IOException {
        Path file = tempDir.resolve("empty.txt");
        Files.createFile(file);
        FileSourceProvider fsp = new FileSourceProvider(file, new NullLogger());
        assertThrows(EmptySourceException.class, fsp::load);

    }

    @Test
    void testLoadMissingFileThrowsMissingSourceException() {
        FileSourceProvider fsp = new FileSourceProvider(Path.of("missing.txt"), new NullLogger());
        assertThrows(MissingSourceException.class, fsp::load);
    }

    @Test
    void testLoadFileWithEmptyLines() throws IOException, MissingSourceException, EmptySourceException {
        Path file = tempDir.resolve("empty.txt");
        Files.write(file, List.of("кошка", "", "арбуз"));
        FileSourceProvider fsp = new FileSourceProvider(file, new NullLogger());
        List<String> words = fsp.load();
        assertEquals(3, words.size());
        assertTrue(words.contains("кошка"));
        assertTrue(words.contains(""));
        assertTrue(words.contains("арбуз"));
    }

    @Test
    void testLoadWithNullPathThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new FileSourceProvider(null, new NullLogger()));
    }

}
