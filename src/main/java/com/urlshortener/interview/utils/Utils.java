package com.urlshortener.interview.utils;

/**
 * Utility methods for URL Shortener.
 */
public final class Utils {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = ALPHABET.length();

    private Utils() {
        throw new AssertionError("Cannot instantiate Utils class");
    }

    /**
     * Encodes an integer ID to a base62 string.
     *
     * @param num the integer ID
     * @return the base62 encoded string
     */
    public static String encodeId(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {

            sb.append(ALPHABET.charAt((int) (num % BASE)));
            num /= BASE;
        }
        return sb.reverse().toString();
    }

    /**
     * Decodes a base62 string to an integer ID.
     *
     * @param str the base62 encoded string
     * @return the decoded integer ID
     * This method converts a Base62 encoded string back to its numeric identifier.
     * It iterates through each character of the input string, looks up its index in the ALPHABET string, and accumulates the value to get the original number.
     */
    public static long decodeId(String str) {
        long num = 0;
        for (char c : str.toCharArray()) {
            if ('a' <= c && c <= 'z') {
                num = num * BASE + (c - 'a');
            } else if ('A' <= c && c <= 'Z') {
                num = num * BASE + (c - 'A' + 26);
            } else if ('0' <= c && c <= '9') {
                num = num * BASE + (c - '0' + 52);
            }
        }
        return num;
    }
}

