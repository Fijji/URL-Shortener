package com.urlshortener.interview.cashe;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simple LRU Cache implemented using LinkedHashMap.
 * To improve the caching aspect for reads, we can introduce a caching mechanism that ensures frequently accessed short URLs are quickly retrieved.
 * This can significantly improve performance for high-traffic links.
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> implements Cache<K, V> {
    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    public boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}
