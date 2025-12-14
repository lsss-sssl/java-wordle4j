package ru.yandex.practicum;

import ru.yandex.practicum.dictionary.WordleDictionaryLoader;
import ru.yandex.practicum.exception.gameLogicException.*;
import ru.yandex.practicum.gameEngine.Status;
import ru.yandex.practicum.gameEngine.WordleGame;

import java.io.IOException;
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

        try {
            WordleGame game = new WordleGame(WordleDictionaryLoader.load("words_ru.txt"));

            while (game.getStatus().equals(Status.IN_PROGRESS)) {
                try {
                    System.out.printf("\nОсталось попыток: %s  ", game.getAttemptsLeft());
                    final String guess = scanner.nextLine();
                    System.out.printf("%26s  ", game.check(guess));


                } catch (ValidateLengthException e) {
                    System.out.println("1." + e.getMessage());
                } catch (ValidateLangException e) {
                    System.out.println("2." + e.getMessage());
                } catch (WordNotFoundInDictionary e) {
                    System.out.println("3." + e.getMessage());
                } catch (ValidateCharactersException e) {
                    System.out.println("4." + e.getMessage());
                } catch (ValidateException e) {
                    System.out.println("5." + e.getMessage());
                }
            }
            scanner.close();
            if (game.getStatus().equals(Status.IS_WIN)) System.out.printf("\n%26s%15s", game.getAnswer(), "EPIC VICTORY");
            else if (game.getStatus().equals(Status.IS_LOSE)) System.out.printf("%s%10s", game.getAnswer(), "DEFEATED");
        } catch (IOException e) {
            System.out.println("ERROR");
        }


    }

}
