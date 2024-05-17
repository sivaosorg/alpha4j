package org.alpha4j;

import org.alpha4j.ds.LRUCache;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class LRUCacheTest {
    protected static final Logger logger = LoggerFactory.getLogger(LRUCacheTest.class);
    protected LRUCache<Integer, String> cache;

    @Before
    public void setUp() {
        cache = new LRUCache<>(3);
        logger.info("LRUCache initialized with size: {}", cache.size());
    }

    @Test
    public void testPutAndGet() {
        logger.info("LRUCache initialized with size: {}", cache.size());
        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");

        assertEquals("one", cache.get(1));
        assertEquals("two", cache.get(2));
        assertEquals("three", cache.get(3));
    }

    @Test
    public void testLRUCacheEviction() {
        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");
        cache.put(4, "four"); // this should evict key 1

        assertNull(cache.get(1));
        assertEquals("two", cache.get(2));
        assertEquals("three", cache.get(3));
        assertEquals("four", cache.get(4));
    }

    @Test
    public void testLRUCacheAccessOrder() {
        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");
        cache.get(1); // access 1 to make it most recently used
        cache.put(4, "four"); // this should evict key 2

        assertEquals("one", cache.get(1));
        assertNull(cache.get(2));
        assertEquals("three", cache.get(3));
        assertEquals("four", cache.get(4));
    }

    @Test
    public void testThreadSafety() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            executor.submit(() -> {
                for (int j = 0; j < 10; j++) {
                    cache.put(index * 10 + j, "value" + j);
                }
            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            // wait for all tasks to finish
        }
        // Check the cache size does not exceed the limit
        assertTrue(cache.size() <= 3);
    }
}
