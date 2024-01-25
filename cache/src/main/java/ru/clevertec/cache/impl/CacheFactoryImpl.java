package ru.clevertec.cache.impl;

import org.springframework.beans.factory.annotation.Value;
import ru.clevertec.cache.Cache;
import ru.clevertec.cache.CacheFactory;

public class CacheFactoryImpl<K, V> implements CacheFactory<K, V> {

    @Value("${capacity}")
    private String capacityKey;

    @Value("${algorithm}")
    private String algorithmKey;

    /**
     * Gets a cache object depending on the settings in application.yaml
     *
     * @return Cache
     */
    @Override
    public Cache<K, V> createCache() {
        return "LFU".equals(algorithmKey)
                ? new LFUCache<>(Integer.parseInt(capacityKey))
                : new LRUCache<>(Integer.parseInt(capacityKey));

    }
}
