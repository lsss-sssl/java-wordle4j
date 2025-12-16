package ru.yandex.practicum.engine;

import java.util.Set;

public record GameContext(char[] correct, Set<Character> presented, Set<Character> excluded, int wordLength) {
}
