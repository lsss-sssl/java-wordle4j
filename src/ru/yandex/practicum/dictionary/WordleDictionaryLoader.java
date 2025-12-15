package ru.yandex.practicum.dictionary;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public final class WordleDictionaryLoader {

    public static WordleDictionary load(final String fileName, PrintWriter log) throws IOException {
        try (BufferedReader bf = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))){
            final List<String> sourceWords = new ArrayList<>();
            while (bf.ready()) {
                sourceWords.add(bf.readLine());
            }
            bf.close();
            log.printf("[PASS] - load source file: %45s\n", fileName);
            return new WordleDictionary(fileName, sourceWords, log);
        } catch (FileNotFoundException e) {
            log.printf("[FAIL] - load source file: %45s\n", fileName);
            throw new FileNotFoundException();
        } catch (IOException e) {
            log.printf("[ERROR] - load source file: %45s\n", fileName);
            throw new IOException();
        }
    }
}
