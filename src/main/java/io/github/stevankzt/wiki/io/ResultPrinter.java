package io.github.stevankzt.wiki.io;

import io.github.stevankzt.wiki.model.SearchResult;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class ResultPrinter {

    private static final Pattern HTML_TAG = Pattern.compile("<[^>]*>");
    private static final Pattern SPACES = Pattern.compile("\\s+");

    private final PrintStream out;

    public ResultPrinter(PrintStream out) {
        this.out = Objects.requireNonNull(out);
    }

    public void printWelcome() {
        out.println("ConsoleWiki — поиск по русской Википедии.");
        out.println("Введите запрос, выберите статью по номеру — она откроется в браузере.");
    }

    public void printResults(String query, List<SearchResult> results) {
        out.println();
        out.println("Результаты по запросу «" + query + "»:");
        for (int i = 0; i < results.size(); i++) {
            SearchResult result = results.get(i);
            out.printf("%2d. %s%n", i + 1, result.title());
            String snippet = toPlainText(result.snippet());
            if (!snippet.isEmpty()) {
                out.println("    " + snippet);
            }
        }
        out.println();
    }

    public void printMessage(String message) {
        out.println(message);
    }

    public void printError(String message) {
        out.println("Ошибка: " + message);
    }

    static String toPlainText(String html) {
        if (html == null) {
            return "";
        }
        String text = HTML_TAG.matcher(html).replaceAll("");
        text = text.replace("&quot;", "\"")
                .replace("&#039;", "'")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&nbsp;", " ")
                .replace("&amp;", "&");
        return SPACES.matcher(text).replaceAll(" ").strip();
    }
}
