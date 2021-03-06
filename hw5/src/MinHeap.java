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
 * Collaborators: N/A
 *
 * Resources: N/A
 */
public class MinHeap<T extends Comparable<? super T>> {

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
    public MinHeap() {
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
    public MinHeap(ArrayList<T> data) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "Tried initializing MinHeap with null ArrayList");
        }

        backingArray = (T[]) new Comparable[2 * data.size() + 1];
        size = data.size();

        // copy in all data values from ArrayList
        for (int i = 0; i < size; i++) {
            if (data.get(i) == null) {
                throw new IllegalArgumentException(
                        "Tried initializing MinHeap with null data in ArrayList");
            }
            backingArray[i + 1] = data.get(i);
        }

        // downheap all non-leaf values
        for (int i = size / 2; i > 0; i--) {
            downheap(i);
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
            // resize backing array
            T[] newArr = (T[]) new Comparable[2 * backingArray.length];
            for (int i = 1; i <= size; i++) {
                newArr[i] = backingArray[i];
            }
            backingArray = newArr;
        }

        size++;
        backingArray[size] = data;

        // upheap new value added as leaf
        upheap(size);
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

        // remove value from root and replace with last value to maintain shape
        T removed = backingArray[1];
        backingArray[1] = backingArray[size];
        backingArray[size] = null;
        size--;

        // downheap from root if not empty tree
        if (size > 0) {
            downheap(1);
        }

        return removed;
    }

    /**
     * Recursively downheaps a value in the Heap in order to rectify order
     * problems in the MinHeap. Downheaps the value in the case that the minimum
     * of the index's children at 2 * index or 2* index + 1 is less than the
     * value at the index. If downheaped, recurses on whichever side the index
     * went to in order to continue downheaping the value.
     * 
     * @param index the index of the value to be downheaped
     */
    private void downheap(int index) {
        int left = 2 * index;
        int right = 2 * index + 1;

        if (left <= size && right <= size) {
            // case for two children
            if (backingArray[left].compareTo(backingArray[right]) < 0) {
                if (backingArray[left].compareTo(backingArray[index]) < 0) {
                    // downheap leftward
                    swap(index, left);
                    downheap(left);
                }
            } else {
                if (backingArray[right].compareTo(backingArray[index]) < 0) {
                    // downheap rightward
                    swap(index, right);
                    downheap(right);
                }
            }
        } else if (left <= size) {
            // case for one child
            if (backingArray[left].compareTo(backingArray[index]) < 0) {
                // downheap leftward
                swap(index, left);
                downheap(left);
            }
        }

    }

    /**
     * Recursively upheaps a value in the Heap in order to rectify order
     * problems in the MinHeap. Upheap the value in the case that the value at
     * index is less than the value of its parent at index / 2. If upheaped,
     * recurses on the parent to continue upheaping the value.
     * 
     * @param index the index of the value to be upheaped
     */
    private void upheap(int index) {
        // can't upheap from root
        if (index > 1) {
            int parent = index / 2;
            if (backingArray[index].compareTo(backingArray[parent]) < 0) {
                // upheap to parent
                swap(index, parent);
                upheap(parent);
            }
        }
    }

    /**
     * Swaps the values at index a and b in the backingArray
     * 
     * @param a the index to be swapped to index b
     * @param b the index to be swapped to index a
     */
    private void swap(int a, int b) {
        T temp = backingArray[a];
        backingArray[a] = backingArray[b];
        backingArray[b] = temp;
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
