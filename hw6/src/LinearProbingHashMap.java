import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Your implementation of a LinearProbingHashMap.
 *
 * @author Henry Liao
 * @version 1.0
 * @userid hliao62
 * @GTID 903682804
 *
 *       Collaborators: N/A
 *
 *       Resources: N/A
 */
public class LinearProbingHashMap<K, V> {

    /**
     * The initial capacity of the LinearProbingHashMap when created with the
     * default constructor.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    /**
     * The max load factor of the LinearProbingHashMap
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final double MAX_LOAD_FACTOR = 0.67;

    // Do not add new instance variables or modify existing ones.
    private LinearProbingMapEntry<K, V>[] table;
    private int size;

    /**
     * Constructs a new LinearProbingHashMap.
     *
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     *
     * Use constructor chaining.
     */
    public LinearProbingHashMap() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Constructs a new LinearProbingHashMap.
     *
     * The backing array should have an initial capacity of initialCapacity.
     *
     * You may assume initialCapacity will always be positive.
     *
     * @param initialCapacity the initial capacity of the backing array
     */
    public LinearProbingHashMap(int initialCapacity) {
        table = (LinearProbingMapEntry<K, V>[]) 
                    new LinearProbingMapEntry[initialCapacity];
        size = 0;
    }

    /**
     * Adds the given key-value pair to the map. If an entry in the map already
     * has this key, replace the entry's value with the new one passed in.
     *
     * In the case of a collision, use linear probing as your resolution
     * strategy.
     *
     * Before actually adding any data to the HashMap, you should check to see
     * if the array would violate the max load factor if the data was added. For
     * example, let's say the array is of length 5 and the current size is 3 (LF
     * = 0.6). For this example, assume that no elements are removed in between
     * steps. If another entry is attempted to be added, before doing anything
     * else, you should check whether (3 + 1) / 5 = 0.8 is larger than the max
     * LF. It is, so you would trigger a resize before you even attempt to add
     * the data or figure out if it's a duplicate. Be careful to consider the
     * differences between integer and double division when calculating load
     * factor.
     *
     * When regrowing, resize the length of the backing table to 2 * old length
     * + 1. You must use the resizeBackingTable method to do so.
     *
     * Return null if the key was not already in the map. If it was in the map,
     * return the old value associated with it.
     *
     * @param key   the key to add
     * @param value the value to add
     * @return null if the key was not already in the map. If it was in the map,
     *         return the old value associated with it
     * @throws java.lang.IllegalArgumentException if key or value is null
     */
    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException(
                    "Tried to add null key into LienarProbingHashMap");
        }

        if (value == null) {
            throw new IllegalArgumentException(
                    "Tried to add null value into LienarProbingHashMap");
        }

        // case for resizing when projected above load factor
        if ((size + 1.0) / table.length > MAX_LOAD_FACTOR) {
            resizeBackingTable(2 * table.length + 1);
        }
        
        int hash = Math.abs(key.hashCode() % table.length);
        int probe = 0;
        int insert = -1;
        int entry = hash % table.length;
        while (probe < table.length && table[entry] != null) {
            if (table[entry].isRemoved()) {
                // case for seeing a DEL marker
                if (insert == -1) {
                    insert = probe + hash;
                }
            } else {
                // duplicate case
                if (table[entry].getKey().equals(key)) {
                    V removed = table[entry].getValue();
                    table[entry].setValue(value);
                    return removed;
                }
            }
            entry = (++probe + hash) % table.length;
        }

        // insertion at first DEL marker or null
        if (insert == -1) {
            table[entry] = new LinearProbingMapEntry<>(key, value);
        } else {
            table[insert] = new LinearProbingMapEntry<>(key, value);
        }

        size++;

        return null;

    }

    /**
     * Removes the entry with a matching key from map by marking the entry as
     * removed.
     *
     * @param key the key to remove
     * @return the value previously associated with the key
     * @throws java.lang.IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException   if the key is not in the map
     */
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException(
                    "Tried to remove null key from LinearProbingHashMap");
        }

        int hash = Math.abs(key.hashCode() % table.length);
        int probe = 0;
        int entry = hash % table.length;
        while (probe < table.length && table[entry] != null) {
            // mark DEL on finding key
            if (!table[entry].isRemoved() && table[entry].getKey().equals(key)) {
                table[entry].setRemoved(true);
                size--;
                return table[entry].getValue();
            }
            entry = (++probe + hash) % table.length;
        }
        throw new NoSuchElementException(
                "Tried to remove key that does not exist in LinearProbingHashMap");
    }

    /**
     * Gets the value associated with the given key.
     *
     * @param key the key to search for in the map
     * @return the value associated with the given key
     * @throws java.lang.IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException   if the key is not in the map
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException(
                    "Tried to get null key from LinearProbingHashMap");
        }

        int hash = Math.abs(key.hashCode() % table.length);
        int probe = 0;
        int entry = hash % table.length;
        while (probe < table.length && table[entry] != null) {
            // return value on finding entry with same key
            if (!table[entry].isRemoved() && table[entry].getKey().equals(key)) {
                return table[entry].getValue();
            }
            entry = (++probe + hash) % table.length;
        }
        throw new NoSuchElementException(
                "Tried to get key that does not exist in LinearProbingHashMap");
    }

    /**
     * Returns whether or not the key is in the map.
     *
     * @param key the key to search for in the map
     * @return true if the key is contained within the map, false otherwise
     * @throws java.lang.IllegalArgumentException if key is null
     */
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException(
                    "Tried to query if LinearProbingHashMap contains null key");
        }

        int hash = Math.abs(key.hashCode() % table.length);
        int probe = 0;
        int entry = hash % table.length;
        while (probe < table.length && table[entry] != null) {
            // return true on finding entry with same key
            if (!table[entry].isRemoved() && table[entry].getKey().equals(key)) {
                return true;
            }
            entry = (++probe + hash) % table.length;

        }
        return false;
    }

    /**
     * Returns a Set view of the keys contained in this map.
     *
     * Use java.util.HashSet.
     *
     * @return the set of keys in this map
     */
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        int count = 0;
        int index = 0;
        while (count < size) {
            if (table[index] != null && !table[index].isRemoved()) {
                keys.add(table[index].getKey());
                count++;
            }
            index++;
        }
        return keys;
    }

    /**
     * Returns a List view of the values contained in this map.
     *
     * Use java.util.ArrayList or java.util.LinkedList.
     *
     * You should iterate over the table in order of increasing index and add
     * entries to the List in the order in which they are traversed.
     *
     * @return list of values in this map
     */
    public List<V> values() {
        List<V> values = new LinkedList<>();
        int count = 0;
        int index = 0;
        while (count < size) {
            if (table[index] != null && !table[index].isRemoved()) {
                values.add(table[index].getValue());
                count++;
            }
            index++;
        }
        return values;
    }

    /**
     * Resize the backing table to length.
     *
     * Disregard the load factor for this method. So, if the passed in length is
     * smaller than the current capacity, and this new length causes the table's
     * load factor to exceed MAX_LOAD_FACTOR, you should still resize the table
     * to the specified length and leave it at that capacity.
     *
     * You should iterate over the old table in order of increasing index and
     * add entries to the new table in the order in which they are traversed.
     * You should NOT copy over removed elements to the resized backing table.
     *
     * Since resizing the backing table is working with the non-duplicate data
     * already in the table, you shouldn't explicitly check for duplicates.
     *
     * Hint: You cannot just simply copy the entries over to the new array.
     *
     * @param length new length of the backing table
     * @throws java.lang.IllegalArgumentException if length is less than the
     *                                            number of items in the hash
     *                                            map
     */
    public void resizeBackingTable(int length) {
        if (size > length) {
            throw new IllegalArgumentException(
                    "Tried to resize to a backing array smaller than "
                            + "number of elements in LinearProbingHashMap");
        }
        LinearProbingMapEntry<K, V>[] temp = (LinearProbingMapEntry<K, V>[]) 
                new LinearProbingMapEntry[length];

        int count = 0;
        int index = 0;
        while (count < size) {
            if (table[index] != null && !table[index].isRemoved()) {
                int hash = Math.abs(table[index].getKey().hashCode() % length);
                int probe = hash;
                while (temp[probe] != null) {
                    probe = (probe + 1) % length;
                }
                temp[probe] = table[index];
                count++;
            }
            index++;
        }
        table = temp;

    }

    /**
     * Clears the map.
     *
     * Resets the table to a new array of the INITIAL_CAPACITY and resets the
     * size.
     *
     * Must be O(1).
     */
    public void clear() {
        table = (LinearProbingMapEntry<K, V>[]) 
                new LinearProbingMapEntry[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the table of the map.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the table of the map
     */
    public LinearProbingMapEntry<K, V>[] getTable() {
        // DO NOT MODIFY THIS METHOD!
        return table;
    }

    /**
     * Returns the size of the map.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the map
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
