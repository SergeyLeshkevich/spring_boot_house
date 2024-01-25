package ru.clevertec.cache.impl;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.cache.Cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Slf4j
@ToString
public class LRUCache<K, V> implements Cache<K, V> {

    private final LinkedList<K> list;
    private final Map<K, V> valueMap;
    private final int capacity;

    public LRUCache(int capacity) {
        this.valueMap = new HashMap<>();
        this.capacity = capacity;
        this.list = new LinkedList<>();
    }

    /**
     * Retrieving an object from the cache by key value
     *
     * @param key key to retrieve from cache
     * @return the value found. null if not found
     */
    @Override
    public V get(K key) {
        V object = valueMap.get(key);
        log.info("Метод put() LRUCache.Объект с id {}: {}", key, object);
        return object;
    }

    /**
     * Saves or updates an object in cache
     *
     * @param key object key
     * @param value object value
     * @return returns the value of the object added/updated from the cache
     */
    @Override
    public V put(K key, V value) {
        V resultValue = valueMap.get(key);

        if (resultValue == null) {
            log.info("Метод put() LRUCache.Объект с id {}: null", key);
            if (list.size() >= capacity) {
                K keyToRemove = list.remove();
                valueMap.remove(keyToRemove);
            }
            list.addFirst(key);
            valueMap.put(key, value);
            resultValue = valueMap.get(key);
            log.info("Метод put() LRUCache.Объект с id {} добавлен в кеш", key);
        } else {
            list.remove(key);
            list.addFirst(key);
            log.info("Метод put() LRUCache.Объект с id {} обнавлен в кеше", key);
        }
        return resultValue;
    }

    /**
     * Removes an object from the cache by key value
     *
     * @param key object key
     * @return returns the value of an object removed from the cache
     */
    @Override
    public V removeByKey(K key) {
        V tempValue = valueMap.get(key);
        list.remove(key);
        valueMap.remove(key);
        log.info("Метод put() LRUCache.Объект с id {} удален из кеша", key);
        return tempValue;
    }
}
