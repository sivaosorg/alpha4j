package org.alpha4j;

import org.alpha4j.ds.LRUCache4j;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class LRUCache4jTest {
    protected LRUCache4j<Integer, String> lruCache;

    @Before
    public void setUp() {
        lruCache = new LRUCache4j<>(3);
    }

    @Test
    public void testPutAndGet() {
        lruCache.put(1, "A");
        lruCache.put(2, "B");
        lruCache.put(3, "C");

        assertEquals("A", lruCache.get(1));
        assertEquals("B", lruCache.get(2));
        assertEquals("C", lruCache.get(3));
    }

    @Test
    public void testEvictionPolicy() {
        lruCache.put(1, "A");
        lruCache.put(2, "B");
        lruCache.put(3, "C");
        lruCache.get(1);
        lruCache.put(4, "D");

        assertNull(lruCache.get(2));
        assertEquals("A", lruCache.get(1));
    }

    @Test
    public void testSize() {
        lruCache.put(1, "A");
        lruCache.put(2, "B");

        assertEquals(2, lruCache.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(lruCache.isEmpty());

        lruCache.put(1, "A");

        assertFalse(lruCache.isEmpty());
    }

    @Test
    public void testRemove() {
        lruCache.put(1, "A");
        lruCache.remove(1);

        assertNull(lruCache.get(1));
    }

    @Test
    public void testContainsKey() {
        lruCache.put(1, "A");

        assertTrue(lruCache.containsKey(1));
        assertFalse(lruCache.containsKey(2));
    }

    @Test
    public void testContainsValue() {
        lruCache.put(1, "A");

        assertTrue(lruCache.containsValue("A"));
        assertFalse(lruCache.containsValue("B"));
    }

    @Test
    public void testKeySet() {
        lruCache.put(1, "A");
        lruCache.put(2, "B");

        assertTrue(lruCache.keySet().contains(1));
        assertTrue(lruCache.keySet().contains(2));
    }

    @Test
    public void testValues() {
        lruCache.put(1, "A");
        lruCache.put(2, "B");

        assertTrue(lruCache.values().contains("A"));
        assertTrue(lruCache.values().contains("B"));
    }

    @Test
    public void testClear() {
        lruCache.put(1, "A");
        lruCache.put(2, "B");
        lruCache.clear();

        assertTrue(lruCache.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Integer, String> map = new LinkedHashMap<>();
        map.put(1, "A");
        map.put(2, "B");
        lruCache.putAll(map);

        assertEquals("A", lruCache.get(1));
        assertEquals("B", lruCache.get(2));
    }

    @Test
    public void testEntrySet() {
        lruCache.put(1, "A");
        lruCache.put(2, "B");

        assertEquals(2, lruCache.entrySet().size());
    }

    @Test
    public void testPutIfAbsent() {
        lruCache.putIfAbsent(1, "A");
        lruCache.putIfAbsent(1, "B");

        assertEquals("A", lruCache.get(1));
    }

    @Test
    public void testConcurrency() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(3);
        lruCache = new LRUCache4j<>(100000);

        // Perform a mix of put and get operations from multiple threads
        int max = 10000;
        int attempts = 0;
        Random random = new SecureRandom();
        while (attempts++ < max) {
            final int key = random.nextInt(max);
            final String value = "V" + key;

            service.submit(() -> lruCache.put(key, value));
            service.submit(() -> lruCache.get(key));
            service.submit(() -> lruCache.size());
            service.submit(() -> {
                lruCache.keySet().remove(random.nextInt(max));
            });
            service.submit(() -> {
                lruCache.values().remove("V" + random.nextInt(max));
            });
            final int attemptsCopy = attempts;
            service.submit(() -> {
                Iterator<?> i = lruCache.entrySet().iterator();
                int walk = random.nextInt(attemptsCopy);
                while (i.hasNext() && walk-- > 0) {
                    i.next();
                }
                int chunk = 10;
                while (i.hasNext() && chunk-- > 0) {
                    i.remove();
                    i.next();
                }
            });
            service.submit(() -> lruCache.remove(random.nextInt(max)));
        }
        service.shutdown();
        assertTrue(service.awaitTermination(1, TimeUnit.MINUTES));
        System.out.println("lruCache = " + lruCache);
        System.out.println("lruCache = " + lruCache.size());
        System.out.println("attempts =" + attempts);
    }
}
