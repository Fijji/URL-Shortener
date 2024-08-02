package com.urlshortener.interview.exceptions;

/**
 * Exception thrown when an invalid URL is provided.
 */
public class InvalidURLException extends Exception {
    public InvalidURLException(String message) {
        super(message);
    }
}
