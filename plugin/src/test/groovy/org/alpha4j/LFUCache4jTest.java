package org.alpha4j;

import org.alpha4j.ds.LFUCache4j;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LFUCache4jTest {

    private LFUCache4j<Integer, String> cache;

    @Before
    public void setUp() {
        // Initialize the cache with a capacity of 2
        cache = new LFUCache4j<>(2);
    }

    @Test
    public void testPutAndGet() {
        // Test adding elements to the cache
        cache.put(1, "one");
        cache.put(2, "two");

        // Verify the elements are correctly added
        assertEquals("one", cache.get(1));
        assertEquals("two", cache.get(2));

        // Test updating an existing element
        cache.put(1, "ONE");
        assertEquals("ONE", cache.get(1));
    }

    @Test
    public void testEviction() {
        // Add elements to fill the cache
        cache.put(1, "one");
        cache.put(2, "two");

        // Access element 1 to increase its frequency
        cache.get(1);

        // Add another element to trigger eviction
        cache.put(3, "three");

        // Verify that element 2 is evicted (as it was less frequently accessed)
        assertNull(cache.get(2));
        assertEquals("one", cache.get(1));
        assertEquals("three", cache.get(3));
    }

    @Test
    public void testFrequencyUpdate() {
        // Add elements to the cache
        cache.put(1, "one");
        cache.put(2, "two");

        // Access elements to update their frequencies
        cache.get(1);
        cache.get(1);
        cache.get(2);

        // Add another element to trigger eviction
        cache.put(3, "three");

        // Verify that element 2 is evicted (since element 1 has higher frequency)
        assertNull(cache.get(2));
        assertEquals("one", cache.get(1));
        assertEquals("three", cache.get(3));
    }

    @Test
    public void testUpdateValue() {
        // Add an element to the cache
        cache.put(1, "one");

        // Verify the value is correctly added
        assertEquals("one", cache.get(1));

        // Update the value of the existing element
        cache.put(1, "ONE");

        // Verify the value is correctly updated
        assertEquals("ONE", cache.get(1));
    }

    @Test
    public void testMinFrequency() {
        // Add elements to the cache
        cache.put(1, "one");
        cache.put(2, "two");

        // Access elements to update their frequencies
        cache.get(1);
        cache.get(2);
        cache.get(2);

        // Add another element to trigger eviction
        cache.put(3, "three");

        // Verify that element 1 is evicted (since it has the lowest frequency)
        assertNull(cache.get(1));
        assertEquals("two", cache.get(2));
        assertEquals("three", cache.get(3));
    }

    @Test
    public void testZeroCapacity() {
        // Initialize the cache with zero capacity
        LFUCache4j<Integer, String> zeroCapacityCache = new LFUCache4j<>(0);

        // Attempt to add elements to the zero-capacity cache
        zeroCapacityCache.put(1, "one");

        // Verify that no elements are added
        assertNull(zeroCapacityCache.get(1));
    }
}
