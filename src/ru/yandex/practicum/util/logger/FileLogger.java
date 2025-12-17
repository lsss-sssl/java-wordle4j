package ru.yandex.practicum.util.logger;

import java.io.PrintWriter;

public final class FileLogger implements Logger {

    private final PrintWriter writer;

    public FileLogger(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void info(String message) {
        writer.println("[INFO] " + message);
        writer.flush();
    }

    @Override
    public void error(String message) {
        writer.println("[ERROR] " + message);
        writer.flush();
    }
}