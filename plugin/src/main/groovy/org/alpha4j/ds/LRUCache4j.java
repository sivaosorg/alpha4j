package org.alpha4j.ds;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * This class provides a thread-safe Least Recently Used (LRU) cache API that will evict the least recently used items,
 * once a threshold is met. It implements the Map interface for convenience. It is thread-safe via usage of
 * ReentrantReadWriteLock() around read and write APIs, including delegating to keySet(), entrySet(), and
 * values() and each of their iterators.
 */
@SuppressWarnings({"EqualsWhichDoesntCheckParameterClass", "NullableProblems", "unchecked"})
public class LRUCache4j<K, V> implements Map<K, V> {
    // A ReadWriteLock to ensure thread-safe access to the cache
    protected final transient ReadWriteLock lock = new ReentrantReadWriteLock();
    // The underlying cache implemented using a LinkedHashMap
    protected final Map<K, V> cache;
    // A constant used to denote the absence of an entry
    protected final static Object NO_ENTRY = new Object();

    /**
     * Constructor that initializes the LRU cache with a specified capacity.
     * The LinkedHashMap is used to implement the cache with access order.
     *
     * @param capacity the maximum number of entries the cache can hold
     */
    public LRUCache4j(int capacity) {
        cache = new LinkedHashMap<K, V>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }

    @Override
    public boolean equals(Object obj) {
        return readOperation(() -> cache.equals(obj));
    }

    @Override
    public int hashCode() {
        return readOperation(cache::hashCode);
    }

    @Override
    public String toString() {
        return readOperation(cache::toString);
    }

    @Override
    public int size() {
        return readOperation(cache::size);
    }

    @Override
    public boolean isEmpty() {
        return readOperation(cache::isEmpty);
    }

    @Override
    public boolean containsKey(Object key) {
        return readOperation(() -> cache.containsKey(key));
    }

    @Override
    public boolean containsValue(Object value) {
        return readOperation(() -> cache.containsValue(value));
    }

    @Override
    public V get(Object key) {
        return readOperation(() -> cache.get(key));
    }

    @Override
    public V put(K key, V value) {
        return writeOperation(() -> cache.put(key, value));
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        writeOperation(() -> {
            cache.putAll(m);
            return null;
        });
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return writeOperation(() -> cache.putIfAbsent(key, value));
    }

    @Override
    public V remove(Object key) {
        return writeOperation(() -> cache.remove(key));
    }

    @Override
    public void clear() {
        writeOperation(() -> {
            cache.clear();
            return null;
        });
    }

    @Override
    public Set<K> keySet() {
        return readOperation(() -> new Set<K>() {
            @Override
            public int size() {
                return readOperation(cache::size);
            }

            @Override
            public boolean isEmpty() {
                return readOperation(cache::isEmpty);
            }

            @Override
            public boolean contains(Object o) {
                return readOperation(() -> cache.containsKey(o));
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return readOperation(() -> cache.keySet().containsAll(c));
            }

            @Override
            public Object[] toArray() {
                return readOperation(() -> cache.keySet().toArray());
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return readOperation(() -> cache.keySet().toArray(a));
            }

            @Override
            public Iterator<K> iterator() {
                return new Iterator<K>() {
                    private final Iterator<K> it = cache.keySet().iterator();
                    private K current = (K) NO_ENTRY;

                    @Override
                    public boolean hasNext() {
                        return readOperation(it::hasNext);
                    }

                    @Override
                    public K next() {
                        return readOperation(() -> {
                            current = it.next();
                            return current;
                        });
                    }

                    @Override
                    public void remove() {
                        writeOperation(() -> {
                            if (current == NO_ENTRY) {
                                throw new IllegalStateException("Next not called or key already removed");
                            }
                            it.remove();
                            current = (K) NO_ENTRY;
                            return null;
                        });
                    }
                };
            }

            @Override
            public boolean add(K k) {
                throw new UnsupportedOperationException("add() not supported on .keySet() of a Map");
            }

            @Override
            public boolean remove(Object o) {
                return writeOperation(() -> cache.remove(o) != null);
            }

            @Override
            public boolean addAll(Collection<? extends K> c) {
                throw new UnsupportedOperationException("addAll() not supported on .keySet() of a Map");
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return writeOperation(() -> cache.keySet().retainAll(c));
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return writeOperation(() -> cache.keySet().removeAll(c));
            }

            @Override
            public void clear() {
                writeOperation(() -> {
                    cache.clear();
                    return null;
                });
            }
        });
    }

    @Override
    public Collection<V> values() {
        return readOperation(() -> new Collection<V>() {
            @Override
            public int size() {
                return readOperation(cache::size);
            }

            @Override
            public boolean isEmpty() {
                return readOperation(cache::isEmpty);
            }

            @Override
            public boolean contains(Object o) {
                return readOperation(() -> cache.containsValue(o));
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return readOperation(() -> cache.values().containsAll(c));
            }

            @Override
            public Object[] toArray() {
                return readOperation(() -> cache.values().toArray());
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return readOperation(() -> cache.values().toArray(a));
            }

            @Override
            public Iterator<V> iterator() {
                return new Iterator<V>() {
                    private final Iterator<V> it = cache.values().iterator();
                    private V current = (V) NO_ENTRY;

                    @Override
                    public boolean hasNext() {
                        return readOperation(it::hasNext);
                    }

                    @Override
                    public V next() {
                        return readOperation(() -> {
                            current = it.next();
                            return current;
                        });
                    }

                    @Override
                    public void remove() {
                        writeOperation(() -> {
                            if (current == NO_ENTRY) {
                                throw new IllegalStateException("Next not called or entry already removed");
                            }
                            it.remove();
                            current = (V) NO_ENTRY;
                            return null;
                        });
                    }
                };
            }

            @Override
            public boolean add(V value) {
                throw new UnsupportedOperationException("add() not supported on values() of a Map");
            }

            @Override
            public boolean remove(Object o) {
                return writeOperation(() -> cache.values().remove(o));
            }

            @Override
            public boolean addAll(Collection<? extends V> c) {
                throw new UnsupportedOperationException("addAll() not supported on values() of a Map");
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return writeOperation(() -> cache.values().removeAll(c));
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return writeOperation(() -> cache.values().retainAll(c));
            }

            @Override
            public void clear() {
                writeOperation(() -> {
                    cache.clear();
                    return null;
                });
            }
        });
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return readOperation(() -> new Set<Map.Entry<K, V>>() {
            @Override
            public int size() {
                return readOperation(cache::size);
            }

            @Override
            public boolean isEmpty() {
                return readOperation(cache::isEmpty);
            }

            @Override
            public boolean contains(Object o) {
                return readOperation(() -> cache.entrySet().contains(o));
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return readOperation(() -> cache.entrySet().containsAll(c));
            }

            @Override
            public Object[] toArray() {
                return readOperation(() -> cache.entrySet().toArray());
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return readOperation(() -> cache.entrySet().toArray(a));
            }

            @Override
            public Iterator<Map.Entry<K, V>> iterator() {
                return new Iterator<Map.Entry<K, V>>() {
                    private final Iterator<Map.Entry<K, V>> it = cache.entrySet().iterator();
                    private Map.Entry<K, V> current = (Map.Entry<K, V>) NO_ENTRY;

                    @Override
                    public boolean hasNext() {
                        return readOperation(it::hasNext);
                    }

                    @Override
                    public Map.Entry<K, V> next() {
                        return readOperation(() -> {
                            current = it.next();
                            return current;
                        });
                    }

                    @Override
                    public void remove() {
                        writeOperation(() -> {
                            if (current == NO_ENTRY) {
                                throw new IllegalStateException("Next not called or entry already removed");
                            }
                            it.remove();
                            current = (Map.Entry<K, V>) NO_ENTRY;
                            return null;
                        });
                    }
                };
            }

            @Override
            public boolean add(Map.Entry<K, V> kvEntry) {
                throw new UnsupportedOperationException("add() not supported on entrySet() of a Map");
            }

            @Override
            public boolean remove(Object o) {
                return writeOperation(() -> cache.entrySet().remove(o));
            }

            @Override
            public boolean addAll(Collection<? extends Map.Entry<K, V>> c) {
                throw new UnsupportedOperationException("addAll() not supported on entrySet() of a Map");
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return writeOperation(() -> cache.entrySet().retainAll(c));
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return writeOperation(() -> cache.entrySet().removeAll(c));
            }

            @Override
            public void clear() {
                writeOperation(() -> {
                    cache.clear();
                    return null;
                });
            }
        });
    }

    /**
     * Executes a read operation with a read lock.
     *
     * @param <T>       the type of result expected from the operation
     * @param operation the read operation to execute
     * @return the result of the operation
     */
    private <T> T readOperation(Supplier<T> operation) {
        lock.readLock().lock();
        try {
            return operation.get();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Executes a write operation with a write lock.
     *
     * @param <T>       the type of result expected from the operation
     * @param operation the write operation to execute
     * @return the result of the operation
     */
    private <T> T writeOperation(Supplier<T> operation) {
        lock.writeLock().lock();
        try {
            return operation.get();
        } finally {
            lock.writeLock().unlock();
        }
    }
}