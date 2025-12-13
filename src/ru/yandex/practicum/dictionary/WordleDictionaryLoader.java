package ru.yandex.practicum.dictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public final class WordleDictionaryLoader {

    public static WordleDictionary load(final String fileName) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8));
        final List<String> sourceWords = new ArrayList<>();
        while (bf.ready()) {
            sourceWords.add(bf.readLine());
        }
        bf.close();
        return new WordleDictionary(sourceWords);
    }
}
