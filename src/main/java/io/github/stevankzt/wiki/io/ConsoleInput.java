package io.github.stevankzt.wiki.io;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Scanner;

public class ConsoleInput {

    public static final String EXIT_COMMAND = "q";

    public static final int BACK_COMMAND = 0;

    static final int MAX_QUERY_LENGTH = 300;

    private final Scanner scanner;
    private final PrintStream out;

    public ConsoleInput(InputStream in, Charset charset, PrintStream out) {
        this.scanner = new Scanner(Objects.requireNonNull(in), Objects.requireNonNull(charset));
        this.out = Objects.requireNonNull(out);
    }

    public Optional<String> readQuery() {
        while (true) {
            out.print("\nВведите запрос (" + EXIT_COMMAND + " — выход): ");
            String line = readLine();
            if (line == null) {
                return Optional.empty();
            }
            String query = line.strip();
            if (query.equalsIgnoreCase(EXIT_COMMAND)) {
                return Optional.empty();
            }
            if (query.isEmpty()) {
                out.println("Запрос не может быть пустым.");
                continue;
            }
            if (query.length() > MAX_QUERY_LENGTH) {
                out.println("Запрос слишком длинный (максимум " + MAX_QUERY_LENGTH + " символов).");
                continue;
            }
            return Optional.of(query);
        }
    }

    public OptionalInt readChoice(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be positive: " + count);
        }
        while (true) {
            out.printf("Номер статьи (1-%d, %d — новый поиск): ", count, BACK_COMMAND);
            String line = readLine();
            if (line == null) {
                return OptionalInt.empty();
            }
            String text = line.strip();
            if (text.isEmpty()) {
                out.println("Введите номер статьи.");
                continue;
            }
            int number;
            try {
                number = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                out.println("«" + text + "» — не число.");
                continue;
            }
            if (number == BACK_COMMAND) {
                return OptionalInt.empty();
            }
            if (number < 1 || number > count) {
                out.printf("Нет статьи с номером %d. Допустимо: 1-%d.%n", number, count);
                continue;
            }
            return OptionalInt.of(number);
        }
    }

    private String readLine() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }
}
