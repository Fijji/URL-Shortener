package com.urlshortener.interview.constants;

import java.util.regex.Pattern;

/**
 * Constants used across the URL Shortener library.
 */
public final class Constants {

    public static final String BASE_URL = "http://short.url/";
    public static final Pattern URL_PATTERN = Pattern.compile("^(http://|https://).+");
}

