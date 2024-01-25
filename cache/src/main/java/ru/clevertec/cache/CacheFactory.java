package ru.clevertec.cache;

public interface CacheFactory<K, V> {

    Cache<K, V> createCache();
}
