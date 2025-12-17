package ru.yandex.practicum.util.loader;

import ru.yandex.practicum.exception.sourceException.EmptySourceException;
import ru.yandex.practicum.exception.sourceException.MissingSourceException;
import ru.yandex.practicum.util.logger.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class FileSourceProvider {
    private final Path path;
    private final Logger logger;

    public FileSourceProvider(Path path, Logger logger) {
        if (path == null) {
            throw new IllegalArgumentException("path must be no null");
        }
        this.path = path;
        this.logger = logger;
    }

    public List<String> load() throws IOException, EmptySourceException, MissingSourceException {
        logger.info("Loading source from: " + path);
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            logger.error("Source file not found: " + path);
            throw new MissingSourceException("source is missing: " + path);
        }
        if (lines.isEmpty()) {
            logger.error("Source file is empty: " + path);
            throw new EmptySourceException("source is empty: " + path);
        }
        logger.info("Loaded " + lines.size() + " lines from: " + path);
        return List.copyOf(lines);
    }
}
