package com.revolut.interview.utils;

/**
 * Utility methods for URL Shortener.
 */
public final class Utils {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = ALPHABET.length();  // The base of the encoding, which is 62 for Base62.

    private Utils() {
        // Private constructor to prevent instantiation
        throw new AssertionError("Cannot instantiate Utils class");
    }

    /**
     * Encodes an integer ID to a base62 string.
     *
     * @param num the integer ID
     * @return the base62 encoded string
     */
    public static String encodeId(long num) {
        StringBuilder sb = new StringBuilder();  // A StringBuilder instance used to build the resulting Base62 string.
        while (num > 0) {  //num: The numeric identifier that you want to encode. The loop continues to run as long as num is greater than 0.
                            // This ensures that each digit of the Base62 encoded string is processed.
            sb.append(ALPHABET.charAt((int)(num % BASE)));  //The expression num % BASE computes the remainder when num is divided by 62.
                                                            //    This gives the position in the ALPHABET string for the current least significant digit of the Base62 number.
                                                            //      for example, if num % 62 is 0, it corresponds to the first character in ALPHABET, which is 'a'.
            num /= BASE; //The expression num /= BASE divides num by 62 and assigns the result back to num.
//            This  shifts the digits of num to the right, so the next iteration of the loop processes the next most significant digit.
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
//    Convert String to Character Array: str.toCharArray()
//
//    The toCharArray() method converts the input string str into an array of characters. This allows us to iterate through each character individually.
//
//    Iterate Over Each Character: for (char c : str.toCharArray())
//
//    This for loop iterates over each character c in the character array derived from str.
//
//    Character Ranges and Decoding Logic
//
//    Lowercase Letters: 'a' <= c && c <= 'z'
//    If c is a lowercase letter (between 'a' and 'z'), the character's position in the alphabet is calculated by c - 'a'. For example, if c is 'b', then c - 'a' is 1.
//    This value is then added to the current num, multiplied by the base (62 in Base62 encoding). This effectively shifts the previously computed value left by one digit in Base62 and adds the new digit.
//    Uppercase Letters: 'A' <= c && c <= 'Z'
//    If c is an uppercase letter (between 'A' and 'Z'), the character's position in the alphabet is calculated by c - 'A', and then 26 is added to account for the 26 lowercase letters that come before the uppercase letters in Base62. For example, if c is 'B', then c - 'A' + 26 is 27.
//    This value is added to the current num after multiplying num by 62.
//    Digits: '0' <= c && c <= '9'
//    If c is a digit (between '0' and '9'), the character's numeric value is calculated by c - '0', and then 52 is added to account for the 26 lowercase and 26 uppercase letters that come before the digits in Base62. For example, if c is '2', then c - '0' + 52 is 54.
//    This value is added to the current num after multiplying num by 62.
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

    // Encodes a numeric ID to a Base64 URL-safe string
//    public static String encodeId(long num) {
//        // Convert the number to bytes
//        byte[] bytes = longToBytes(num);
//        // Encode to Base64 URL-safe string without padding
//        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
//    }

    // Decodes a Base64 URL-safe string back to a numeric ID
//    public static long decodeId(String str) {
//        // Decode the Base64 URL-safe string to bytes
//        byte[] bytes = Base64.getUrlDecoder().decode(str);
//        // Convert the bytes back to a long
//        return bytesToLong(bytes);
//    }

    // Convert a long to a byte array
//    private static byte[] longToBytes(long x) {
//        byte[] result = new byte[8];
//        for (int i = 7; i >= 0; i--) {
//            result[i] = (byte)(x & 0xff);
//            x >>= 8;
//        }
//        return result;
//    }

    // Convert a byte array to a long
//    private static long bytesToLong(byte[] bytes) {
//        long result = 0;
//        for (int i = 0; i < 8; i++) {
//            result <<= 8;
//            result |= (bytes[i] & 0xff);
//        }
//        return result;
//    }

//    public static void main(String[] args) {
//        long id = 125;
//        String encoded = encodeId(id);
//        System.out.println("Encoded: " + encoded);
//
//        long decoded = decodeId(encoded);
//        System.out.println("Decoded: " + decoded);
//    }
}

