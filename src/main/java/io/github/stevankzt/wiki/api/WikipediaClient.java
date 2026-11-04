package io.github.stevankzt.wiki.api;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;

public class WikipediaClient {

    private static final String API_URL = "https://ru.wikipedia.org/w/api.php";
    private static final String USER_AGENT = "ConsoleWiki/1.0 (educational console app)";
    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private static final int HTTP_OK = 200;

    static final int RESULT_LIMIT = 10;

    private final HttpClient httpClient;

    public WikipediaClient() {
        this(HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build());
    }

    public WikipediaClient(HttpClient httpClient) {
        this.httpClient = Objects.requireNonNull(httpClient);
    }

    public String search(String query) throws WikiApiException {
        HttpRequest request = HttpRequest.newBuilder(buildSearchUri(query))
                .timeout(TIMEOUT)
                .header("User-Agent", USER_AGENT)
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() != HTTP_OK) {
                throw new WikiApiException("Сервер Википедии ответил с кодом " + response.statusCode() + ".");
            }
            return response.body();
        } catch (HttpTimeoutException e) {
            throw new WikiApiException("Википедия не ответила вовремя. Попробуйте ещё раз.", e);
        } catch (IOException e) {
            throw new WikiApiException("Не удалось связаться с Википедией. Проверьте подключение к интернету.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new WikiApiException("Запрос был прерван.", e);
        }
    }

    URI buildSearchUri(String query) {
        String encoded = URLEncoder.encode(Objects.requireNonNull(query), StandardCharsets.UTF_8);
        return URI.create(API_URL
                + "?action=query&list=search&utf8=&format=json"
                + "&srlimit=" + RESULT_LIMIT
                + "&srsearch=" + encoded);
    }
}
