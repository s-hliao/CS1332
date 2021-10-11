import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Your implementation of a MinHeap.
 *
 * @author hliao62
 * @version 1.0
 * @userid hliao62
 * @GTID 903682804
 *
 *       Collaborators: N/A
 *
 *       Resources: N/A
 */
public class MinHeapIterative<T extends Comparable<? super T>> {

    /**
     * The initial capacity of the MinHeap when created with the default
     * constructor.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    // Do not add new instance variables or modify existing ones.
    private T[] backingArray;
    private int size;

    /**
     * Constructs a new MinHeap.
     *
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     */
    public MinHeapIterative() {
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Creates a properly ordered heap from a set of initial values.
     *
     * You must use the BuildHeap algorithm that was taught in lecture! Simply
     * adding the data one by one using the add method will not get any credit.
     * As a reminder, this is the algorithm that involves building the heap from
     * the bottom up by repeated use of downHeap operations.
     *
     * Before doing the algorithm, first copy over the data from the ArrayList
     * to the backingArray (leaving index 0 of the backingArray empty). The data
     * in the backingArray should be in the same order as it appears in the
     * passed in ArrayList before you start the BuildHeap algorithm.
     *
     * The backingArray should have capacity 2n + 1 where n is the number of
     * data in the passed in ArrayList (not INITIAL_CAPACITY). Index 0 should
     * remain empty, indices 1 to n should contain the data in proper order, and
     * the rest of the indices should be empty.
     *
     * @param data a list of data to initialize the heap with
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public MinHeapIterative(ArrayList<T> data) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "Tried initializing MinHeap with null ArrayList");
        }

        backingArray = (T[]) new Comparable[2 * data.size() + 1];
        size = data.size();

        // initialize with data from argument
        for (int i = 0; i < size; i++) {
            if (data.get(i) == null) {
                throw new IllegalArgumentException(
                        "Tried initializing MinHeap with null data in ArrayList");
            }
            backingArray[i + 1] = data.get(i);
        }
        
        // downheap all nodes that aren't leaves
        for (int i = size / 2; i > 0; i--) {
            int cur = i;

            while (cur <= size) {
                System.out.println(cur);
                int left = 2 * cur;
                int right = 2 * cur + 1;

                if (left <= size && right <= size) {
                    // case for two children
                    if (backingArray[left].compareTo(backingArray[right]) < 0) {
                        if (backingArray[left]
                                .compareTo(backingArray[cur]) < 0) {
                            // downheap leftward
                            T temp = backingArray[cur];
                            backingArray[cur] = backingArray[left];
                            backingArray[left] = temp;
                            cur = left;
                        } else {
                            cur = size + 1;
                        }
                    } else {
                        if (backingArray[right]
                                .compareTo(backingArray[cur]) < 0) { 
                            //downheap rightward
                            T temp = backingArray[cur];
                            backingArray[cur] = backingArray[right];
                            backingArray[right] = temp;
                            cur = right;
                        } else {
                            cur = size + 1;
                        }
                    }
                } else if (left <= size) {
                    // case for one child
                    if (backingArray[left].compareTo(backingArray[cur]) < 0) {
                        // downheap leftward
                        T temp = backingArray[cur];
                        backingArray[cur] = backingArray[left];
                        backingArray[left] = temp;
                        cur = left;
                    } else {
                        cur = size + 1;
                    }
                } else {
                    // case for no children
                    cur = size + 1;
                }

            }

        }
    }

    /**
     * Adds an item to the heap. If the backing array is full (except for index
     * 0) and you're trying to add a new item, then double its capacity. The
     * order property of the heap must be maintained after adding.
     * 
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "Tried to add null data to MinHeap");
        }
        if (size + 1 == backingArray.length) {
            T[] newArr = (T[]) new Comparable[2 * backingArray.length];
            for (int i = 1; i <= size; i++) {
                newArr[i] = backingArray[i];
            }
            backingArray = newArr;
        }

        size++;
        backingArray[size] = data;

        int cur = size;
        while (cur > 1) {
            int parent = cur / 2;
            if (backingArray[cur].compareTo(backingArray[parent]) < 0) {
                // upheap if less than parent
                T temp = backingArray[cur];
                backingArray[cur] = backingArray[parent];
                backingArray[parent] = temp;
                cur = parent;
            } else {
                cur = 0;
            }

        }

    }

    /**
     * Removes and returns the min item of the heap. As usual for array-backed
     * structures, be sure to null out spots as you remove. Do not decrease the
     * capacity of the backing array. The order property of the heap must be
     * maintained after adding.
     *
     * @return the data that was removed
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public T remove() {
        if (size == 0) {
            throw new NoSuchElementException(
                    "Tried to remove from empty MinHeap");
        }

        T removed = backingArray[1];
        backingArray[1] = backingArray[size];
        backingArray[size] = null;
        size--;

        if (size > 0) {
            int cur = 1;
            while (cur <= size) {
                int left = 2 * cur;
                int right = 2 * cur + 1;

                if (left <= size && right <= size) {
                    // case for two children
                    if (backingArray[left].compareTo(backingArray[right]) < 0) {
                        if (backingArray[left]
                                .compareTo(backingArray[cur]) < 0) {
                            // downheap leftward
                            T temp = backingArray[cur];
                            backingArray[cur] = backingArray[left];
                            backingArray[left] = temp;
                            cur = left;
                        } else {
                            return removed;
                        }
                    } else {
                        if (backingArray[right]
                                .compareTo(backingArray[cur]) < 0) {
                            // downheap rightward
                            T temp = backingArray[cur];
                            backingArray[cur] = backingArray[right];
                            backingArray[right] = temp;
                            cur = right;
                        } else {
                            return removed;
                        }
                    }
                } else if (left <= size) {
                    // case for one child
                    if (backingArray[left].compareTo(backingArray[cur]) < 0) {
                        // downheap leftward
                        T temp = backingArray[cur];
                        backingArray[cur] = backingArray[left];
                        backingArray[left] = temp;
                        cur = left;
                    } else {
                        return removed;
                    }
                }
                return removed;
            }
        }
        return removed;
    }
    


    /**
     * Returns the minimum element in the heap.
     *
     * @return the minimum element
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public T getMin() {
        if (size == 0) {
            throw new NoSuchElementException(
                    "Tried to getMin on empty MinHeap");
        }

        return backingArray[1];
    }

    /**
     * Returns whether or not the heap is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the heap.
     *
     * Resets the backing array to a new array of the initial capacity and
     * resets the size.
     */
    public void clear() {
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the backing array of the heap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the backing array of the list
     */
    public T[] getBackingArray() {
        // DO NOT MODIFY THIS METHOD!
        return backingArray;
    }

    /**
     * Returns the size of the heap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the list
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
