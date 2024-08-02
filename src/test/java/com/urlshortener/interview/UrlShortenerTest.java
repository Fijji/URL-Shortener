package com.urlshortener.interview;

import com.urlshortener.interview.exceptions.InvalidURLException;
import com.urlshortener.interview.exceptions.URLNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class UrlShortenerTest {

    private static final String BASE_URL = "http://short.url/";

    /**
     * Tests that shortening a valid URL returns a valid shortened URL.
     */
    @Test
    void testShortenUrl() throws InvalidURLException {
        UrlShortener urlShortener = new UrlShortener(BASE_URL, 100);
        String originalUrl = "http://example.com";
        String shortUrl = urlShortener.shortenUrl(originalUrl);
        Assertions.assertNotNull(shortUrl);
        Assertions.assertTrue(shortUrl.startsWith(BASE_URL));
    }

    /**
     * Tests that retrieving the original URL from a shortened URL works correctly.
     */
    @Test
    void testGetOriginalUrl() throws InvalidURLException, URLNotFoundException {
        UrlShortener urlShortener = new UrlShortener(BASE_URL, 100);
        String originalUrl = "http://example.com";
        String shortUrl = urlShortener.shortenUrl(originalUrl);
        String retrievedUrl = urlShortener.getOriginalUrl(shortUrl);
        Assertions.assertEquals(originalUrl, retrievedUrl);
    }

    /**
     * Tests that shortening an invalid URL throws an InvalidURLException.
     */
    @Test
    void testInvalidUrl() {
        UrlShortener urlShortener = new UrlShortener(BASE_URL, 100);
        String invalidUrl = "invalid_url";
        Assertions.assertThrows(InvalidURLException.class, () -> {
            urlShortener.shortenUrl(invalidUrl);
        });
    }

    /**
     * Tests that retrieving an original URL for a non-existent short URL throws URLNotFoundException.
     */
    @Test
    void testUrlNotFound() {
        UrlShortener urlShortener = new UrlShortener(BASE_URL, 100);
        String nonExistentShortUrl = BASE_URL + "nonexistent";
        Assertions.assertThrows(URLNotFoundException.class, () -> {
            urlShortener.getOriginalUrl(nonExistentShortUrl);
        });
    }

    /**
     * Tests concurrent URL shortening to ensure thread safety.
     */
    @Test
    void testConcurrentShortening() throws InterruptedException {
        UrlShortener urlShortener = new UrlShortener(BASE_URL, 100);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 100; i++) {
            int index = i;
            executorService.submit(() -> {
                try {
                    String originalUrl = "http://example.com/" + index;
                    String shortUrl = urlShortener.shortenUrl(originalUrl);
                    Assertions.assertNotNull(shortUrl);
                    Assertions.assertTrue(shortUrl.startsWith(BASE_URL));
                    Assertions.assertEquals(originalUrl, urlShortener.getOriginalUrl(shortUrl));
                } catch (InvalidURLException | URLNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }

    /**
     * Tests caching mechanism by verifying that frequently accessed short URLs are cached.
     */
    @Test
    void testCachingMechanism() throws InvalidURLException, URLNotFoundException {
        UrlShortener urlShortener = new UrlShortener(BASE_URL, 3);
        String originalUrl1 = "http://example.com/1";
        String originalUrl2 = "http://example.com/2";
        String originalUrl3 = "http://example.com/3";
        String originalUrl4 = "http://example.com/4";

        String shortUrl1 = urlShortener.shortenUrl(originalUrl1);
        String shortUrl2 = urlShortener.shortenUrl(originalUrl2);
        String shortUrl3 = urlShortener.shortenUrl(originalUrl3);
        String shortUrl4 = urlShortener.shortenUrl(originalUrl4);

        // Access short URLs to populate cache
        urlShortener.getOriginalUrl(shortUrl1);
        urlShortener.getOriginalUrl(shortUrl2);
        urlShortener.getOriginalUrl(shortUrl3);

        // ShortUrl4 should not be in cache
        Assertions.assertNull(urlShortener.cache.get(shortUrl4));

        // Access shortUrl1 to keep it in cache
        urlShortener.getOriginalUrl(shortUrl1);

        // Add shortUrl4 to cache, this should evict the least recently used (shortUrl2)
        urlShortener.getOriginalUrl(shortUrl4);

        Assertions.assertNull(urlShortener.cache.get(shortUrl2));
        Assertions.assertNotNull(urlShortener.cache.get(shortUrl1));
        Assertions.assertNotNull(urlShortener.cache.get(shortUrl3));
        Assertions.assertNotNull(urlShortener.cache.get(shortUrl4));
    }

    /**
     * Tests that caching is bypassed when cache size is zero.
     */
    @Test
    void testNoCaching() throws InvalidURLException, URLNotFoundException {
        UrlShortener urlShortener = new UrlShortener(BASE_URL, 0);
        String originalUrl = "http://example.com";
        String shortUrl = urlShortener.shortenUrl(originalUrl);
        String retrievedUrl = urlShortener.getOriginalUrl(shortUrl);

        // The cache should be null, and no entries should be cached
        Assertions.assertNull(urlShortener.cache);
        Assertions.assertEquals(originalUrl, retrievedUrl);
    }

    /**
     * Tests mocking URL shortening.
     */
    @Test
    void testMockShortenUrl() throws InvalidURLException {
        UrlShortener urlShortener = Mockito.spy(new UrlShortener(BASE_URL, 100));
        String originalUrl = "http://example.com";
        String shortUrl = urlShortener.shortenUrl(originalUrl);

        Mockito.verify(urlShortener, Mockito.times(1)).shortenUrl(originalUrl);
        Assertions.assertNotNull(shortUrl);
        Assertions.assertTrue(shortUrl.startsWith(BASE_URL));
    }
}

