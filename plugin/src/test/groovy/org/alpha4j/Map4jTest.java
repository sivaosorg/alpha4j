package org.alpha4j;

import org.alpha4j.common.Map4j;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class Map4jTest {

    @Test
    public void testPutAndGet() {
        Map4j<String, Integer> map4j = new Map4j<>();

        map4j.put("A", 1)
                .put("B", 2)
                .put("C", 3);

        assertEquals(3, map4j.size());
        assertEquals(Integer.valueOf(2), map4j.get("B"));
        assertNull(map4j.get("D"));
    }

    @Test
    public void testMapValues() {
        Map4j<String, Integer> map4j = new Map4j<>();
        map4j.put("A", 1)
                .put("B", 2)
                .put("C", 3);

        Map4j<String, String> mappedMap = map4j.mapValues(Object::toString);

        assertEquals(3, mappedMap.size());
        assertEquals("1", mappedMap.get("A"));
        assertEquals("2", mappedMap.get("B"));
        assertEquals("3", mappedMap.get("C"));
    }

    @Test
    public void testFilter() {
        Map4j<String, Integer> map4j = new Map4j<>();
        map4j.put("A", 1)
                .put("B", 2)
                .put("C", 3)
                .put("D", 4);

        Map<String, Integer> filteredMap = map4j.filter(entry -> entry.getValue() > 2);

        assertEquals(2, filteredMap.size());
        assertTrue(filteredMap.containsKey("C"));
        assertTrue(filteredMap.containsKey("D"));
        assertFalse(filteredMap.containsKey("A"));
        assertFalse(filteredMap.containsKey("B"));
    }

    @Test
    public void testReduce() {
        Map4j<String, Integer> map4j = new Map4j<>();
        map4j.put("A", 1)
                .put("B", 2)
                .put("C", 3);

        int sum = map4j.reduce(0, (acc, entry) -> acc + entry.getValue());

        assertEquals(6, sum);
    }

    @Test
    public void testBuildUnmodifiable() {
        Map4j<String, Integer> map4j = new Map4j<>();
        map4j.put("A", 1)
                .put("B", 2)
                .put("C", 3);

        Map<String, Integer> unmodifiableMap = map4j.buildUnmodifiable();

        assertEquals(3, unmodifiableMap.size());
        assertEquals(Integer.valueOf(2), unmodifiableMap.get("B"));
        try {
            unmodifiableMap.put("D", 4); // This should throw UnsupportedOperationException
            fail("Expected UnsupportedOperationException was not thrown");
        } catch (UnsupportedOperationException e) {
            // Expected behavior
        }
    }
}
