package ru.yandex.practicum.engine;

public record ProcessResult(String pattern, GameState status, int attemptsLeft) {
}
