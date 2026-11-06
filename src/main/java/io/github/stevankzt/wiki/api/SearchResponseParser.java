package io.github.stevankzt.wiki.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import io.github.stevankzt.wiki.model.SearchResponse;
import io.github.stevankzt.wiki.model.SearchResult;

import java.util.List;
import java.util.Objects;

public class SearchResponseParser {

    private final Gson gson;

    public SearchResponseParser() {
        this(new Gson());
    }

    public SearchResponseParser(Gson gson) {
        this.gson = Objects.requireNonNull(gson);
    }

    public List<SearchResult> parse(String json) throws WikiApiException {
        SearchResponse response;
        try {
            response = gson.fromJson(json, SearchResponse.class);
        } catch (JsonParseException e) {
            throw new WikiApiException("Не удалось разобрать ответ Википедии.", e);
        }
        if (response == null) {
            throw new WikiApiException("Википедия вернула пустой ответ.");
        }
        if (response.error() != null) {
            throw new WikiApiException("Википедия сообщила об ошибке: " + response.error().info());
        }
        if (response.query() == null || response.query().search() == null) {
            throw new WikiApiException("Ответ Википедии имеет неожиданный формат.");
        }
        return response.query().search().stream()
                .filter(Objects::nonNull)
                .filter(result -> result.title() != null)
                .toList();
    }
}
