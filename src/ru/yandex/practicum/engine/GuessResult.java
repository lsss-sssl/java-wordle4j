package ru.yandex.practicum.engine;

public record GuessResult(String pattern, GameState status, int attemptsLeft) {}
