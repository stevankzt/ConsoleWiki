package io.github.stevankzt.wiki;

import io.github.stevankzt.wiki.api.SearchResponseParser;
import io.github.stevankzt.wiki.api.WikiApiException;
import io.github.stevankzt.wiki.api.WikipediaClient;
import io.github.stevankzt.wiki.browser.ArticleOpener;
import io.github.stevankzt.wiki.io.ConsoleInput;
import io.github.stevankzt.wiki.io.ResultPrinter;
import io.github.stevankzt.wiki.model.SearchResult;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

public class WikiSearchApp {

    private final ConsoleInput input;
    private final WikipediaClient client;
    private final SearchResponseParser parser;
    private final ResultPrinter printer;
    private final ArticleOpener opener;

    public WikiSearchApp(ConsoleInput input, WikipediaClient client, SearchResponseParser parser,
                         ResultPrinter printer, ArticleOpener opener) {
        this.input = Objects.requireNonNull(input);
        this.client = Objects.requireNonNull(client);
        this.parser = Objects.requireNonNull(parser);
        this.printer = Objects.requireNonNull(printer);
        this.opener = Objects.requireNonNull(opener);
    }

    public void run() {
        printer.printWelcome();
        Optional<String> query = input.readQuery();
        while (query.isPresent()) {
            try {
                handleQuery(query.get());
            } catch (RuntimeException e) {
                printer.printError("непредвиденная ошибка (" + e.getClass().getSimpleName() + ").");
            }
            query = input.readQuery();
        }
        printer.printMessage("До свидания!");
    }

    private void handleQuery(String query) {
        List<SearchResult> results;
        try {
            results = parser.parse(client.search(query));
        } catch (WikiApiException e) {
            printer.printError(e.getMessage());
            return;
        }
        if (results.isEmpty()) {
            printer.printMessage("По запросу «" + query + "» ничего не найдено.");
            return;
        }
        printer.printResults(query, results);

        OptionalInt choice = input.readChoice(results.size());
        if (choice.isEmpty()) {
            return;
        }
        SearchResult article = results.get(choice.getAsInt() - 1);
        if (opener.open(article)) {
            printer.printMessage("Открываю «" + article.title() + "» в браузере.");
        } else {
            printer.printMessage("Не удалось открыть браузер. Ссылка на статью: " + article.articleUrl());
        }
    }
}
