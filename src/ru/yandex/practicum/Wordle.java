package ru.yandex.practicum;

import ru.yandex.practicum.engine.GameState;
import ru.yandex.practicum.engine.ProcessResult;
import ru.yandex.practicum.exception.cli.ApplicationException;
import ru.yandex.practicum.exception.cli.StartException;
import ru.yandex.practicum.exception.sourceException.EmptySourceException;
import ru.yandex.practicum.dictionary.WordleDictionary;
import ru.yandex.practicum.engine.WordleGame;
import ru.yandex.practicum.exception.logicException.GameIsFinishedException;
import ru.yandex.practicum.exception.sourceException.MissingSourceException;
import ru.yandex.practicum.exception.logicException.ValidateException;
import ru.yandex.practicum.util.loader.FileSourceProvider;
import ru.yandex.practicum.util.logger.FileLogger;
import ru.yandex.practicum.util.logger.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public final class Wordle {
    private final Scanner scanner;
    private final Random random;
    private final Logger logger;

    public Wordle(Scanner scanner, Random random, Logger logger) {
        this.scanner = scanner;
        this.random = random;
        this.logger = logger;
    }

    public static void main(String[] args) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("log.txt"))) {
            Logger logger = new FileLogger(writer);
            Scanner scanner = new Scanner(System.in);
            Random random = new Random();
            logger.info("Application started");
            new Wordle(scanner, random, logger).run();
            logger.info("Application finished successfully");
        } catch (ApplicationException e) {
            System.err.println(e.userMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("[ERROR] - unexpected fatal error");
            e.printStackTrace();
            System.exit(2);
        }
    }

    private void run() {
        try {
            WordleGame game = create();
            process(game);
            result(game);
        } catch (MissingSourceException | EmptySourceException | IOException e) {
            logger.error("Failed to load dictionary");
            throw new StartException("load dictionary: ", e);
        }
    }

    private WordleGame create() throws MissingSourceException, EmptySourceException, IOException {
        FileSourceProvider fsp = new FileSourceProvider(Path.of("words_ru.txt"), logger);
        WordleDictionary dictionary = new WordleDictionary(fsp.load(), logger);
        WordleGame game = new WordleGame(dictionary, random, logger);
        logger.info("Game created with: " + dictionary.words().size() + " words");
        return game;
    }

    private void process(WordleGame game) {
        System.out.println("Введите слово или нажмите Enter для подсказки");
        while (game.status() == GameState.IN_PROGRESS) {
            System.out.printf("Попыток осталось: %2s  ", game.attempts());
            final String input = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            try {
                ProcessResult output = game.process(input);
                if (input.isEmpty()) {
                    System.out.printf("Подсказка: %16s", output.pattern());
                } else {
                    System.out.printf("%27s", output.pattern());
                }
            } catch (ValidateException e) {
                System.out.print("Input error: " + e.getMessage());
                logger.info("Validation failed: " + e.getMessage());
            } catch (GameIsFinishedException e) {
                System.out.print("Game status: " + e.getMessage());
                logger.info("Game finished: " + e.getMessage());
            }
            System.out.println();
        }
        scanner.close();
    }

    private void result(WordleGame game) {
        System.out.printf("Ответ: %20s  ", game.answer());
        if (game.status() == GameState.WIN) {
            System.out.print("EPIC VICTORY");
            logger.info("Player won. Answer: " + game.answer());
        } else {
            System.out.print("DEFEATED");
            logger.info("Player lost. Answer: " + game.answer());
        }
    }
}
