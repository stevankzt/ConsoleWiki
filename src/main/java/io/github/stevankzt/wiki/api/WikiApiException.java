package io.github.stevankzt.wiki.api;

public class WikiApiException extends Exception {

    public WikiApiException(String message) {
        super(message);
    }

    public WikiApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
