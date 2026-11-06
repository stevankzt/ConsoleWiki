package io.github.stevankzt.wiki;

import io.github.stevankzt.wiki.api.SearchResponseParser;
import io.github.stevankzt.wiki.api.WikipediaClient;
import io.github.stevankzt.wiki.browser.DesktopArticleOpener;
import io.github.stevankzt.wiki.io.ConsoleInput;
import io.github.stevankzt.wiki.io.ResultPrinter;

import java.io.Console;
import java.nio.charset.Charset;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        WikiSearchApp app = new WikiSearchApp(
                new ConsoleInput(System.in, inputCharset(), System.out),
                new WikipediaClient(),
                new SearchResponseParser(),
                new ResultPrinter(System.out),
                new DesktopArticleOpener());
        app.run();
    }

    private static Charset inputCharset() {
        Console console = System.console();
        if (console != null) {
            return console.charset();
        }
        String encoding = System.getProperty("stdin.encoding");
        try {
            return encoding != null ? Charset.forName(encoding) : Charset.defaultCharset();
        } catch (IllegalArgumentException e) {
            return Charset.defaultCharset();
        }
    }
}
