package com.revolut.interview;

import com.revolut.interview.constants.Constants;
import com.revolut.interview.exceptions.InvalidURLException;
import com.revolut.interview.exceptions.URLNotFoundException;
import com.revolut.interview.utils.Utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * URL Shortener service.
 *
 * Why We Need a URL Shortener: For convenience, tracking, branding, and link management.
 * Logic Behind Shortening: Generate a unique ID, encode it into a short string, store the mapping, and retrieve the original URL using the short string.
 * Implementation: Use ConcurrentHashMap for storage, AtomicLong for unique ID generation, and base62 encoding/decoding for short URL generation. Ensure thread safety and include comprehensive tests.
 */
public class UrlShortener {
//    ensures that multiple threads can add and retrieve mappings concurrently without causing data corruption. Performance: It achieves high concurrency by internally partitioning the map into segments. Each segment can be independently locked, allowing multiple threads to read and write to different segments concurrently. This reduces contention and improves performance.
//Consistency: It ensures that the state of the map is consistent even when multiple threads are performing operations on it simultaneously.
    private final ConcurrentHashMap<String, String> urlMap = new ConcurrentHashMap<>();

//    The counter generates unique IDs for each URL to be shortened.
//    Each time a URL is shortened, the counter is incremented to provide a new unique ID.
//    This unique ID is then encoded to create the short URL.
    private final AtomicLong counter = new AtomicLong(0);

    private final String baseUrl;

//    We'll use an LRU (Least Recently Used) cache for this purpose, which evicts the least recently accessed items when it reaches its capacity.
//    private final LRUCache<String, String> cache;

    public UrlShortener(String baseUrl) {
        this.baseUrl = baseUrl;

    }
//    public UrlShortener(String baseUrl, int cacheSize) {
//        this.baseUrl = baseUrl;
//        if (cacheSize > 0) {
//            this.cache = new LRUCache<>(cacheSize);
//        } else {
//            this.cache = null;
//        }
//    }

    /**
     * Shortens the given URL and returns the shortened URL.
     *
     * @param originalUrl the original URL to be shortened
     * @return the shortened URL
     * @throws URISyntaxException if the original URL is invalid
     */
    public String shortenUrl(String originalUrl) throws InvalidURLException {  //Overall Time Complexity for shortenUrl: O(n + m + log(id))

        if (!Constants.URL_PATTERN.matcher(originalUrl).matches()) {  // This is a regex match operation, which typically runs in O(n) time, where n is the length of the URL string
            throw new InvalidURLException("Invalid URL: " + originalUrl);
        }
//      URI: This class from java.net represents a Uniform Resource Identifier (URI). It is used here to validate the format of the input URL.
//                new URI(originalUrl): The constructor of the URI class parses the input string originalUrl. If the string is not a valid URI, it throws a URISyntaxException.
        try {
            URI uri = new URI(originalUrl); // parsing the string, which operates in O(n)
        } catch (URISyntaxException e) {
            throw new InvalidURLException("Invalid URL: " + originalUrl);
        }

//        counter: An AtomicLong instance used to generate unique IDs in a thread-safe manner.
//                incrementAndGet(): This method atomically increments the current value of the AtomicLong and returns the updated value. This ensures that each call to shortenUrl generates a unique ID.
        long id = counter.incrementAndGet();  //O(1) time.

//        Utils.encodeId(id): This method encodes the long integer ID into a base62 string.
//        Base62 encoding uses digits (0-9), lowercase letters (a-z), and uppercase letters (A-Z) to represent the ID in a compact form.
//        This results in a shorter and more readable string.
        String shortCode = Utils.encodeId(id);  //If it converts an integer to a base62 string, the complexity is O(log(id)) due to the division operations.

//        Constants.BASE_URL: A constant string that represents the base URL for all shortened URLs, e.g., "http://short.url/".
        String shortUrl = Constants.BASE_URL + shortCode; //Since baseUrl is constant, this can be considered O(m).

//        urlMap: A ConcurrentHashMap that stores the mapping between short URLs and original URLs. This allows for quick retrieval of the original URL given the short URL.
        urlMap.put(shortUrl, originalUrl);  //The put operation in a ConcurrentHashMap is O(1) on average.
        return shortUrl;
    }

    /**
     * Returns the original URL for the given shortened URL.
     *
     * @param shortUrl the shortened URL
     * @return the original URL
     */
    public String getOriginalUrl(String shortUrl) throws URLNotFoundException {
        // Check the cache first
//        String originalUrl = cache.get(shortUrl);
//        if (originalUrl != null) {
//            return originalUrl;
//        }

        String originalUrl = urlMap.get(shortUrl);
        if (originalUrl == null) {
            throw new URLNotFoundException("URL not found for: " + shortUrl);
        }

        // Update the cache
//        cache.put(shortUrl, originalUrl);
        return originalUrl;
    }
    }


