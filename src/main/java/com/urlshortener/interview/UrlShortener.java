package com.urlshortener.interview;

import com.urlshortener.interview.cashe.LRUCache;
import com.urlshortener.interview.constants.Constants;
import com.urlshortener.interview.exceptions.InvalidURLException;
import com.urlshortener.interview.exceptions.URLNotFoundException;
import com.urlshortener.interview.utils.Utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * URL Shortener service.
 * <p>
 * Why We Need a URL Shortener: For convenience, tracking, branding, and link management.
 * Logic Behind Shortening: Generate a unique ID, encode it into a short string, store the mapping, and retrieve the original URL using the short string.
 * Implementation: Use ConcurrentHashMap for storage, AtomicLong for unique ID generation, and base62 encoding/decoding for short URL generation. Ensure thread safety and include comprehensive tests.
 */
public class UrlShortener {

    private final ConcurrentHashMap<String, String> urlMap = new ConcurrentHashMap<>();

    private final AtomicLong counter = new AtomicLong(0);

    private final String baseUrl;

    final LRUCache<String, String> cache;

    public UrlShortener(String baseUrl, int cacheSize) {
        this.baseUrl = baseUrl;
        if (cacheSize > 0) {
            this.cache = new LRUCache<>(cacheSize);
        } else {
            this.cache = null;
        }
    }

    /**
     * Shortens the given URL and returns the shortened URL.
     *
     * @param originalUrl the original URL to be shortened
     * @return the shortened URL
     * @throws URISyntaxException if the original URL is invalid
     */
    public String shortenUrl(String originalUrl) throws InvalidURLException {
        if (!Constants.URL_PATTERN.matcher(originalUrl).matches()) {
            throw new InvalidURLException("Invalid URL: " + originalUrl);
        }

        try {
            URI uri = new URI(originalUrl);
        } catch (URISyntaxException e) {
            throw new InvalidURLException("Invalid URL: " + originalUrl);
        }

        long id = counter.incrementAndGet();
        String shortCode = Utils.encodeId(id);
        String shortUrl = Constants.BASE_URL + shortCode;

        urlMap.put(shortUrl, originalUrl);
        return shortUrl;
    }

    /**
     * Returns the original URL for the given shortened URL.
     *
     * @param shortUrl the shortened URL
     * @return the original URL
     */
    public String getOriginalUrl(String shortUrl) throws URLNotFoundException {
        if (cache != null) {
            // Check the cache first if caching is enabled
            String originalUrl = cache.get(shortUrl);
            if (originalUrl != null) {
                return originalUrl;
            }
        }

        // If not in cache or caching is disabled, check the main map
        String originalUrl = urlMap.get(shortUrl);
        if (originalUrl == null) {
            throw new URLNotFoundException("URL not found for: " + shortUrl);
        }

        // Update the cache if caching is enabled
        if (cache != null) {
            cache.put(shortUrl, originalUrl);
        }
        return originalUrl;
    }
}


