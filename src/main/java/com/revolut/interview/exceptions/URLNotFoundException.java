package com.revolut.interview.exceptions;

/**
 * Exception thrown when a short URL does not have a corresponding original URL.
 */
public class URLNotFoundException extends Exception {
    public URLNotFoundException(String message) {
        super(message);
    }
}
