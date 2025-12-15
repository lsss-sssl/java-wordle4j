package ru.yandex.practicum;

import ru.yandex.practicum.dictionary.WordleDictionaryLoader;
import ru.yandex.practicum.exception.gameLogicException.*;
import ru.yandex.practicum.engine.Status;
import ru.yandex.practicum.engine.WordleGame;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/*
    в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        try (PrintWriter log = new PrintWriter(new FileWriter("log.txt"))) {
            WordleGame game = new WordleGame(WordleDictionaryLoader.load("words_ru.txt", log), log);
            while (game.getStatus().equals(Status.IN_PROGRESS)) {
                try {
                    System.out.printf("\nОсталось попыток: %s  ", game.getAttemptsLeft());
                    final String guess = scanner.nextLine();
                    System.out.printf("%26s  ", game.check(guess));
                } catch (ValidateException e) {
                    System.out.println(e.getMessage());
                }
            }
            scanner.close();
            if (game.getStatus().equals(Status.IS_WIN)) {
                String message = String.format("\n%26s%15s", game.getAnswer(), "EPIC VICTORY\n");
                System.out.printf(message);
            } else if (game.getStatus().equals(Status.IS_LOSE)) {
                String message = String.format("\n%26s%15s", game.getAnswer(), "DEFEATED\n");
                System.out.printf(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
