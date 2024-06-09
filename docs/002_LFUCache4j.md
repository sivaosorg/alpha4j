# LFUCache4j

LFUCache4j is a thread-safe implementation of a Least Frequently Used (LFU) cache in Java. This cache supports generic
key-value pairs and ensures that the least frequently accessed elements are evicted first when the cache reaches its
capacity.

## Features

- **Thread-Safe**: Utilizes a ReentrantLock to ensure thread safety.
- **LFU Eviction Policy**: Evicts the least frequently used elements first.
- **Generic**: Supports generic types for keys and values.

## Usage

### Creating an LFU Cache

```java
LFUCache4j<Integer, String> cache = new LFUCache4j<>(2);
```

### Adding Elements to the Cache

```java
cache.put(1, "one");
cache.put(2, "two");
```

### Retrieving Elements from the Cache

```java
String value1 = cache.get(1); // Returns "one"
String value2 = cache.get(2); // Returns "two"
```

### Updating an Element in the Cache

```java
cache.put(1, "ONE"); // Updates the value associated with key 1
String updatedValue = cache.get(1); // Returns "ONE"
```

### Handling Cache Eviction

When the cache reaches its capacity, the least frequently used element is evicted to make space for new elements.

```java
cache.put(3, "three"); // Evicts the least frequently used element
String evictedValue = cache.get(2); // Returns null, as element with key 2 is evicted
```

## API Reference

### Constructor

- `LFUCache4j(int capacity)`: Creates a new LFU cache with the specified capacity.

### Methods

- `V get(K key)`: Retrieves the value associated with the specified key. Updates the access frequency of the key. Returns null if the key is not found.
- `void put(K key, V value)`: Inserts the specified key-value pair into the cache. If the cache is at capacity, the least frequently used item is evicted. If the key already exists, its value is updated and its frequency is incremented.

## Example

```java
public class Main {
    public static void main(String[] args) {
        LFUCache4j<Integer, String> cache = new LFUCache4j<>(2);

        cache.put(1, "one");
        cache.put(2, "two");

        System.out.println(cache.get(1)); // Output: one
        System.out.println(cache.get(2)); // Output: two

        cache.put(3, "three"); // Evicts key 2

        System.out.println(cache.get(2)); // Output: null
        System.out.println(cache.get(3)); // Output: three

        cache.put(1, "ONE"); // Updates value for key 1

        System.out.println(cache.get(1)); // Output: ONE
    }
}
```
