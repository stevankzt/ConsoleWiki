package io.github.stevankzt.wiki.browser;

import io.github.stevankzt.wiki.model.SearchResult;

public interface ArticleOpener {

    boolean open(SearchResult article);
}
