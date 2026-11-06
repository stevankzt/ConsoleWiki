package io.github.stevankzt.wiki.model;

import com.google.gson.annotations.SerializedName;

import java.net.URI;

public record SearchResult(
        @SerializedName("pageid") long pageId,
        @SerializedName("title") String title,
        @SerializedName("snippet") String snippet) {

    private static final String ARTICLE_URL = "https://ru.wikipedia.org/w/index.php?curid=";

    public URI articleUrl() {
        return URI.create(ARTICLE_URL + pageId);
    }
}
