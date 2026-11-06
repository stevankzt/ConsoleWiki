package io.github.stevankzt.wiki.browser;

import io.github.stevankzt.wiki.model.SearchResult;

import java.awt.Desktop;
import java.io.IOException;

public class DesktopArticleOpener implements ArticleOpener {

    @Override
    public boolean open(SearchResult article) {
        try {
            if (!Desktop.isDesktopSupported()) {
                return false;
            }
            Desktop desktop = Desktop.getDesktop();
            if (!desktop.isSupported(Desktop.Action.BROWSE)) {
                return false;
            }
            desktop.browse(article.articleUrl());
            return true;
        } catch (IOException | UnsupportedOperationException | SecurityException e) {
            return false;
        }
    }
}
