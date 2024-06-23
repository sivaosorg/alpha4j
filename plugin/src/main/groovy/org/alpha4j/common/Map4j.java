package org.alpha4j.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A fluent API wrapper for {@link ConcurrentHashMap} providing chaining methods for
 * map operations.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class Map4j<K, V> {
    protected final Map<K, V> map;

    public Map4j() {
        this.map = new ConcurrentHashMap<>();
    }

    /**
     * Adds a key-value pair to the map.
     *
     * @param key   the key to be added
     * @param value the value associated with the key
     * @return {@code this} instance for method chaining
     */
    @SuppressWarnings({"UnusedReturnValue"})
    public Map4j<K, V> put(K key, V value) {
        map.put(key, value);
        return this;
    }

    /**
     * Adds all key-value pairs from another map to this map.
     *
     * @param m the map containing key-value pairs to be added
     * @return {@code this} instance for method chaining
     */
    public Map4j<K, V> putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
        return this;
    }

    /**
     * Replaces the value associated with a specified key.
     *
     * @param key   the key whose value is to be replaced
     * @param value the new value to be associated with the key
     * @return {@code this} instance for method chaining
     */
    public Map4j<K, V> replace(K key, V value) {
        map.replace(key, value);
        return this;
    }

    /**
     * Replaces the old value associated with a key if it matches the specified value.
     *
     * @param key      the key whose value is to be replaced
     * @param oldValue the expected current value of the key
     * @param newValue the new value to be associated with the key if the current value matches
     * @return {@code this} instance for method chaining
     */
    public Map4j<K, V> replace(K key, V oldValue, V newValue) {
        map.replace(key, oldValue, newValue);
        return this;
    }

    /**
     * Removes the mapping for a key from this map if it is present.
     *
     * @param key the key whose mapping is to be removed from the map
     * @return {@code this} instance for method chaining
     */
    public Map4j<K, V> remove(K key) {
        map.remove(key);
        return this;
    }

    /**
     * Checks if the map contains a mapping for the specified key.
     *
     * @param key the key whose presence in the map is to be tested
     * @return {@code true} if the map contains a mapping for the specified key, otherwise {@code false}
     */
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    /**
     * Checks if the map contains a specific value.
     *
     * @param value the value whose presence in the map is to be tested
     * @return {@code true} if the map contains at least one mapping with the specified value, otherwise {@code false}
     */
    public boolean containsValue(V value) {
        return map.containsValue(value);
    }

    /**
     * Computes a new value for the specified key using the given function.
     *
     * @param key the key whose value is to be computed
     * @param fnc the function to compute a new value
     * @return {@code this} instance for method chaining
     */
    public Map4j<K, V> compute(K key, BiFunction<? super K, ? super V, ? extends V> fnc) {
        map.compute(key, fnc);
        return this;
    }

    /**
     * Computes a new value for the specified key if it is absent using the given function.
     *
     * @param key the key whose value is to be computed if absent
     * @param fnc the function to compute a new value
     * @return {@code this} instance for method chaining
     */
    public Map4j<K, V> computeIfAbsent(K key, Function<? super K, ? extends V> fnc) {
        map.computeIfAbsent(key, fnc);
        return this;
    }

    /**
     * Computes a new value for the specified key if it is present using the given function.
     *
     * @param key the key whose value is to be computed if present
     * @param fnc the function to compute a new value
     * @return {@code this} instance for method chaining
     */
    public Map4j<K, V> computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> fnc) {
        map.computeIfPresent(key, fnc);
        return this;
    }

    /**
     * Merges the specified key and value using the given function.
     *
     * @param key   the key whose value is to be merged
     * @param value the value to be merged with the existing value
     * @param fnc   the function to merge values
     * @return {@code this} instance for method chaining
     */
    public Map4j<K, V> merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> fnc) {
        map.merge(key, value, fnc);
        return this;
    }

    /**
     * Replaces all values in the map with new values computed by the given function.
     *
     * @param fnc the function to compute new values
     * @return {@code this} instance for method chaining
     */
    public Map4j<K, V> replaceAll(BiFunction<? super K, ? super V, ? extends V> fnc) {
        map.replaceAll(fnc);
        return this;
    }

    /**
     * Performs an action for each entry in the map.
     *
     * @param action the action to be performed for each entry
     * @return {@code this} instance for method chaining
     */
    public Map4j<K, V> forEach(BiConsumer<? super K, ? super V> action) {
        map.forEach(action);
        return this;
    }

    /**
     * Removes all mappings from the map.
     *
     * @return {@code this} instance for method chaining
     */
    public Map4j<K, V> clear() {
        map.clear();
        return this;
    }

    /**
     * Returns the number of key-value mappings in the map.
     *
     * @return the number of key-value mappings in the map
     */
    public int size() {
        return map.size();
    }

    /**
     * Checks if the map is empty.
     *
     * @return {@code true} if the map contains no key-value mappings, otherwise {@code false}
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Returns a set view of the keys contained in the map.
     *
     * @return a set view of the keys contained in the map
     */
    public Set<K> keySet() {
        return map.keySet();
    }

    /**
     * Returns a set view of the mappings contained in the map.
     *
     * @return a set view of the mappings contained in the map
     */
    public Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    /**
     * Returns a collection view of the values contained in the map.
     *
     * @return a collection view of the values contained in the map
     */
    public java.util.Collection<V> values() {
        return map.values();
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@code null} if no mapping for the key is found
     */
    public V get(K key) {
        return map.get(key);
    }

    /**
     * Returns the value to which the specified key is mapped, or a default value if the key is not present in the map.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default value to return if no mapping for the key is found
     * @return the value to which the specified key is mapped, or {@code defaultValue} if no mapping for the key is found
     */
    public V getOrDefault(K key, V defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    /**
     * Returns a sequential {@link Stream} of the entries in the map.
     *
     * @return a sequential {@code Stream} of the entries in the map
     */
    public Stream<Map.Entry<K, V>> entrySetStream() {
        return map.entrySet().stream();
    }

    /**
     * Returns a sequential {@link Stream} of the keys in the map.
     *
     * @return a sequential {@code Stream} of the keys in the map
     */
    public Stream<K> keySetStream() {
        return map.keySet().stream();
    }

    /**
     * Returns a sequential {@link Stream} of the values in the map.
     *
     * @return a sequential {@code Stream} of the values in the map
     */
    public Stream<V> valuesStream() {
        return map.values().stream();
    }

    /**
     * Filters the entries of the map based on a predicate and returns a new map.
     *
     * @param predicate the predicate used to filter entries
     * @return a new map containing entries that satisfy the predicate
     */
    public Map<K, V> filter(Predicate<? super Map.Entry<K, V>> predicate) {
        return map.entrySet().stream()
                .filter(predicate)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Maps the keys of the map using a mapper function and returns a new {@code Map4j} instance
     * with mapped keys.
     *
     * @param keyMapper the function to map keys
     * @param <R>       the new type of keys
     * @return a new {@code Map4j} instance with mapped keys
     */
    public <R> Map4j<R, V> mapKeys(Function<? super K, ? extends R> keyMapper) {
        Map4j<R, V> newMap = new Map4j<>();
        map.forEach((key, value) -> newMap.put(keyMapper.apply(key), value));
        return newMap;
    }

    /**
     * Maps the values of the map using a mapper function and returns a new {@code Map4j} instance
     * with mapped values.
     *
     * @param valueMapper the function to map values
     * @param <R>         the new type of values
     * @return a new {@code Map4j} instance with mapped values
     */
    public <R> Map4j<K, R> mapValues(Function<? super V, ? extends R> valueMapper) {
        Map4j<K, R> newMap = new Map4j<>();
        map.forEach((key, value) -> newMap.put(key, valueMapper.apply(value)));
        return newMap;
    }

    /**
     * Reduces the entries of the map into a single value using an initial identity value
     * and an accumulation function.
     *
     * @param identity    the initial value
     * @param accumulator the accumulation function
     * @param <T>         the type of the result
     * @return the result of the reduction
     */
    public <T> T reduce(T identity, BiFunction<T, ? super Map.Entry<K, V>, T> accumulator) {
        return map.entrySet()
                .stream()
                .reduce(identity, accumulator, (t1, t2) -> t2);
    }

    /**
     * Builds and returns a new {@link HashMap} containing the mappings of the current map.
     *
     * @return a new {@code HashMap} containing the mappings of the current map
     */
    public Map<K, V> build() {
        return new HashMap<>(map);
    }

    /**
     * Builds and returns an unmodifiable view of the current map.
     *
     * @return an unmodifiable view of the current map
     */
    @SuppressWarnings({"Java9CollectionFactory"})
    public Map<K, V> buildUnmodifiable() {
        return Collections.unmodifiableMap(new HashMap<>(map));
    }
}
