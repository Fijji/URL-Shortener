package com.urlshortener.interview.cashe;


import java.util.Map;

public interface Cache<K, V> {
    boolean removeEldestEntry(Map.Entry<K, V> eldest);
}
