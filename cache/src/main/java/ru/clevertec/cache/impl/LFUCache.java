package ru.clevertec.cache.impl;


import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.cache.Cache;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

@Slf4j
@ToString
public class LFUCache<K, V> implements Cache<K, V> {

    private final Map<Integer, LinkedList<K>> listMap;
    private final Map<K, V> valueMap;
    private final Map<K, Integer> counterUseMap;
    private final int capacity;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        listMap = new LinkedHashMap<>();
        valueMap = new HashMap<>(capacity);
        counterUseMap = new HashMap<>();
    }

    /**
     * Getting an object from cache by key value
     *
     * @param key key to retrieve from cache
     * @return the value found. null if not found
     */
    @Override
    public V get(K key) {
        V object = valueMap.get(key);
        log.info("Метод put() LFUCache.Объект с id {}: {}", key, object);
        return object;
    }

    /**
     * Stores or updates an object in cache
     *
     * @param key   object key
     * @param value object value
     * @return returns the value of the object added/updated from the cache
     */
    @Override
    public V put(K key, V value) {
        V resultValue = valueMap.get(key);

        if (resultValue == null) {

            log.info("Метод put() LFUCache. Объект из кеша с id {}: null", key);
            if ((valueMap.size() + 1) > capacity) {
                log.info("Метод put() LFUCache. Превышен размер кеша. Удаление наиболее старого значения");
                removeOldValue();
            }
            log.info("Метод put() LFUCache. Добавление нового значения в кеш: {}", value);
            resultValue = addNewValue(key, value);

        } else {
            int counter = counterUseMap.get(key);
            int newCounter = counter + 1;
            counterUseMap.put(key, newCounter);
            listMap.get(counter).remove(key);
            if (listMap.get(newCounter) == null) {
                LinkedList<K> sortedListKeyByUse = new LinkedList<>();
                sortedListKeyByUse.addFirst(key);
                listMap.put(newCounter, sortedListKeyByUse);
            } else {
                listMap.get(newCounter).addFirst(key);
            }
            log.info("Метод put() LFUCache. Обновление счетчика значения в кеш: {}", value);
        }
        log.info("Метод put() LFUCache. Объект из кеша с id {}: {}", key, resultValue);
        return resultValue;
    }

    /**
     * Removes the oldest value from the cache
     */
    private void removeOldValue() {
        int firstOccurrenceNumberMinUse = listMap.entrySet().stream().map((entry) -> {
            if (!entry.getValue().isEmpty()) {
                return entry.getKey();
            }
            return -1;
        }).filter(value -> value > -1).findFirst().orElseThrow();
        K tempKey = listMap.get(firstOccurrenceNumberMinUse).getFirst();
        listMap.get(firstOccurrenceNumberMinUse).remove();
        valueMap.remove(tempKey);
        counterUseMap.remove(tempKey);
    }

    /**
     * Saves or updates an object in cache
     *
     * @param key   object key
     * @param value object value
     * @return returns the value of the object added/updated from the cache
     */
    private V addNewValue(K key, V value) {
        int counter = 1;
        valueMap.put(key, value);
        V resultValue = valueMap.get(key);
        counterUseMap.put(key, counter);
        if (listMap.get(counter) == null) {
            LinkedList<K> sortedListKeyByUse = new LinkedList<>();
            sortedListKeyByUse.addFirst(key);
            listMap.put(counter, sortedListKeyByUse);
        } else {
            listMap.get(counter).addFirst(key);
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
        V resultValue = valueMap.get(key);
        if (resultValue != null) {
            int counterKey = counterUseMap.get(key);
            counterUseMap.remove(key);
            resultValue = valueMap.remove(key);
            listMap.get(counterKey).remove(key);
        }
        log.info("Метод put() LFUCache. Удаление объект из кеша с id {}: {}", key, resultValue);
        return resultValue;
    }
}
