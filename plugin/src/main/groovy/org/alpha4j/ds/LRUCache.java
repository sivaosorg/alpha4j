package org.alpha4j.ds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    protected static final Logger logger = LoggerFactory.getLogger(LRUCache.class);

    // Define a lock to ensure thread safety
    private final ReentrantLock lock = new ReentrantLock();
    // Cache capacity
    private int size;

    public LRUCache(int size) {
        super(size, 0.75f, true);
        this.size = size;
    }

    // Initialization, when the parameter accessOrder is true, it will be sorted in the order of access, with the most recently accessed at the top and the earliest accessed at the back
    // Rewrite the removeEldestEntry method, if the current capacity> size, pop up the tail
    @Override
    public boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        // size() method, every time LinkedHashMap adds an element, it will be ++
        return size() > size;
    }

    // Rewrite the LinkedHashMap method, lock to ensure thread safety
    @Override
    public V put(K key, V value) {
        try {
            lock.lock();
            return super.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V get(Object key) {
        try {
            lock.lock();
            return super.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        try {
            lock.lock();
            return super.remove(key);
        } finally {
            lock.unlock();
        }
    }


}
