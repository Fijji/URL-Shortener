package com.revolut.interview.constants;

import java.util.regex.Pattern;

/**
 * Constants used across the URL Shortener library.
 */
public final class Constants {

//    private Constants() {
        // Private constructor to prevent instantiation
//        throw new AssertionError("Cannot instantiate Constants class");
//    }

    public static final String BASE_URL = "http://short.url/";
    public static final Pattern URL_PATTERN = Pattern.compile("^(http://|https://).+");

//    Components of the Regex Pattern
//
//    ^ (Caret)
//    This asserts the position at the start of a line. It ensures that the match must start from the beginning of the string.
//
//            (http://|https://)
//    This is a capturing group that matches either "http://" or "https://".
//    The pipe symbol | within the parentheses acts as an OR operator.
//    http:// matches the literal string "http://".
//    https:// matches the literal string "https://".
//    The parentheses () group these two options together so that the OR operator applies only within the group.
//
//            . (Dot)
//    This matches any single character except newline characters.
//
//            + (Plus)
//    This is a quantifier that matches one or more of the preceding element (in this case, any character).
}

