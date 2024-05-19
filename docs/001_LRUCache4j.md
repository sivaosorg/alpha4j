# LRUCache4j

`LRUCache4j` is a thread-safe implementation of a Least Recently Used (LRU) cache in Java. This class extends the Map
interface and provides a cache mechanism that evicts the least recently used items when a specified capacity is reached.
The class ensures thread safety by using a ReentrantReadWriteLock around its read and write operations.

## Features

- **Thread-safe**: Uses ReentrantReadWriteLock to manage concurrent access.
- **Eviction policy**: Removes the least recently used item when the cache exceeds its capacity.
- **Implements Map interface**: Can be used as a drop-in replacement for a Map with LRU eviction.
- **Customizable capacity**: Initialize with a specified capacity to control the maximum number of entries.

## Usage

### Initialization

To create an instance of `LRUCache4j`, simply specify the maximum number of entries it can hold:

```java
LRUCache4j<String, Integer> cache = new LRUCache4j<>(5);
```

### Basic Operations

- Put an entry:

```java
cache.put("key1",1);
```

- Get an entry:

```java
Integer value = cache.get("key1");
```

- Remove an entry:

```java
cache.remove("key1");
```

- Check if the cache contains a key or value:

```java
boolean containsKey = cache.containsKey("key1");
boolean containsValue = cache.containsValue(1);
```

- Clear the cache:

```java
cache.clear();
```

### Advanced Operations

- Put if absent:

```java
cache.putIfAbsent("key2",2);
```

- Bulk operations:

```java
Map<String, Integer> map = new HashMap<>();
map.

put("key3",3);
map.

put("key4",4);
cache.

putAll(map);
```

## Thread Safety

`LRUCache4j` uses read-write locks to ensure thread-safe operations. The `readOperation` and `writeOperation` methods
encapsulate the lock management for read and write operations respectively.

## Example

```java
public class Example {
    public static void main(String[] args) {
        LRUCache4j<String, String> cache = new LRUCache4j<>(3);
        cache.put("a", "apple");
        cache.put("b", "banana");
        cache.put("c", "cherry");

        System.out.println("Cache size: " + cache.size()); // Output: 3

        cache.get("a"); // Access "a" to make it recently used
        cache.put("d", "date"); // This will evict "b" as it is the least recently used

        System.out.println("Cache contains 'b': " + cache.containsKey("b")); // Output: false
        System.out.println("Cache contains 'a': " + cache.containsKey("a")); // Output: true
    }
}
```
