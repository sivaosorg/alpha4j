package org.alpha4j.ds;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LFUCache4j is a thread-safe implementation of the Least Frequently Used (LFU) cache.
 * It supports generic key-value pairs and ensures that the least frequently accessed
 * elements are evicted first when the cache reaches its capacity.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */
public class LFUCache4j<K, V> {
    protected final ReentrantLock lock = new ReentrantLock(); // Define a lock to ensure thread safety
    protected int capacity; // Cache capacity
    protected int size = 0; // Current size of the cache
    protected Map<K, V> cache; // Map to store keys and their corresponding values
    protected Map<K, Integer> frequencies; // Map to store keys and their corresponding frequencies
    protected Map<Integer, LinkedHashSet<K>> frequencyIndexes; // Map to store frequencies and the corresponding sets of keys
    protected int minFrequency; // Variable to keep track of the minimum frequency

    /**
     * Constructor to initialize the LFUCache4j with a specific capacity.
     *
     * @param capacity the maximum number of items that can be held in the cache
     */
    public LFUCache4j(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.frequencies = new HashMap<>();
        this.frequencyIndexes = new HashMap<>();
        this.minFrequency = 1;
    }

    /**
     * Retrieves the value associated with the specified key from the cache.
     * If the key is not found, returns null. This method also updates the
     * frequency of the accessed key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value associated with the specified key, or null if the key is not found
     */
    @SuppressWarnings({"UnusedReturnValue"})
    public V get(K key) {
        try {
            lock.lock();
            if (!cache.containsKey(key)) {
                return null;
            }
            int frequency = frequencies.get(key); // Get the current frequency of the key
            frequencyIndexes.get(frequency).remove(key); // Remove the key from the current frequency list
            // If the current frequency list is empty and the frequency equals minFrequency, increment minFrequency
            if (frequencyIndexes.get(frequency).isEmpty() && frequency == minFrequency) {
                minFrequency++;
            }
            frequency++; // Increment the frequency and update the data structures
            frequencies.put(key, frequency);
            frequencyIndexes.computeIfAbsent(frequency, k -> new LinkedHashSet<>()).add(key);
            return cache.get(key);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Inserts the specified key-value pair into the cache. If the cache is at capacity,
     * the least frequently used item will be removed to make space for the new entry.
     * If the key already exists, its value will be updated and its frequency will be incremented.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     */
    public void put(K key, V value) {
        try {
            lock.lock();
            if (capacity <= 0) {
                return;
            }
            if (cache.containsKey(key)) {
                cache.put(key, value); // Update the value and increase the frequency
                this.get(key);  // Update the frequency by calling get
                return;
            }
            if (size >= capacity) {
                // Remove the least frequently used element
                K evict = frequencyIndexes.get(minFrequency).iterator().next();
                frequencyIndexes.get(minFrequency).remove(evict);
                cache.remove(evict);
                frequencies.remove(evict);
                size--;
            }
            // Add the new key and value
            cache.put(key, value);
            frequencies.put(key, 1);
            frequencyIndexes.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
            minFrequency = 1; // Reset minFrequency to 1 for the new entry
            size++;
        } finally {
            lock.unlock();
        }
    }
}
