package ru.yandex.practicum.engine;

public enum GameState {

    IN_PROGRESS {
        @Override
        GameState next(GameEvent event) {
            return switch (event) {
                case WIN -> WIN;
                case OUT_OF_ATTEMPTS -> LOSE;
                case CONTINUE -> IN_PROGRESS;
            };
        }
    },

    WIN {
        @Override
        GameState next(GameEvent event) {
            throw new IllegalStateException("game is finished");
        }
    },

    LOSE {
        @Override
        GameState next(GameEvent event) {
            throw new IllegalStateException("game is finished");
        }
    };

    abstract GameState next(GameEvent event);
}
