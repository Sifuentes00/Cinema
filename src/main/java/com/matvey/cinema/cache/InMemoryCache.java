package com.matvey.cinema.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class InMemoryCache {

    private final Map<String, Object> cache = new HashMap<>();

    // Добавить данные в кэш
    public void put(String key, Object value) {
        cache.put(key, value);
    }

    // Получить данные из кэша
    public Optional<Object> get(String key) {
        return Optional.ofNullable(cache.get(key));
    }

    // Удалить данные из кэша
    public void evict(String key) {
        cache.remove(key);
    }

    // Очистить весь кэш
    public void clear() {
        cache.clear();
    }
}