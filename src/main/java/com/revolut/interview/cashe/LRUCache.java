package com.revolut.interview.cashe;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simple LRU Cache implemented using LinkedHashMap.
 * To improve the caching aspect for reads, we can introduce a caching mechanism that ensures frequently accessed short URLs are quickly retrieved.
 * This can significantly improve performance for high-traffic links.
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> implements Cache<K, V>{
    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);  // true for access-order
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

//    Why Use LRUCache<K, V> Instead of Plain LinkedHashMap<K, V>
//
//    Specialization and Clarity:
//    Explicit Purpose: By creating a specialized class LRUCache<K, V>, it's immediately clear to other developers that this class is designed specifically to handle caching with an LRU eviction policy.
//    Encapsulation: Encapsulating the LRU logic within a dedicated class keeps the implementation details separate from the rest of the code, adhering to the Single Responsibility Principle.
//
//    Configuration and Reusability:
//    Easy Configuration: The LRUCache class allows easy configuration of cache capacity and encapsulates the eviction logic.
//    Reusability: This class can be reused in other parts of the application or in different projects without modification.
//
//    Enhanced Readability and Maintainability:
//    Clear Interface: Extending LinkedHashMap and overriding the removeEldestEntry method provides a clean and straightforward way to implement LRU caching. This makes the implementation easy to understand and maintain.
}
