package io.github.stevankzt.wiki.model;

import java.util.List;

public record SearchResponse(Query query, ApiError error) {

    public record Query(List<SearchResult> search) {
    }

    public record ApiError(String code, String info) {
    }
}
