import java.util.NoSuchElementException;

/**
 * Your implementation of a non-circular DoublyLinkedList with a tail pointer.
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
public class DoublyLinkedList<T> {

    // Do not add new instance variables or modify existing ones.
    private DoublyLinkedListNode<T> head;
    private DoublyLinkedListNode<T> tail;
    private int size;

    // Do not add a constructor.

    /**
     * Adds the element to the specified index. Don't forget to consider whether
     * traversing the list from the head or tail is more efficient!
     *
     * Must be O(1) for indices 0 and size and O(n) for all other cases.
     *
     * @param index the index at which to add the new element
     * @param data  the data to add at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index > size
     * @throws java.lang.IllegalArgumentException  if data is null
     */
    public void addAtIndex(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Given add index is " + index
                    + " which is out of bounds for DoublyLinkedList of size "
                    + size);
        }
        if (data == null) {
            throw new IllegalArgumentException(
                    "Tried to add null data to DoublyLinkedList");
        }

        if (index == 0) {
            // Add at the front by linking to current head
            head = new DoublyLinkedListNode<T>(data, null, head);
            // Must reassign tail and head if no elements in list yet
            if (size == 0) {
                tail = head;
            } else {
                head.getNext().setPrevious(head);
            }
        } else if (index == size) {
            // Add at tail by linking to current tail
            tail = new DoublyLinkedListNode<T>(data, tail, null);
            tail.getPrevious().setNext(tail);
        } else if (index <= size / 2) {
            // Add in first half of list, iterating from head
            DoublyLinkedListNode<T> cur = head;
            for (int i = 0; i < index - 1; i++) {
                cur = cur.getNext();
            }
            cur.setNext(new DoublyLinkedListNode<T>(data, cur, cur.getNext()));
            cur.getNext().getNext().setPrevious(cur.getNext());
        } else {
            // Add in second half of list, iterating from tail
            DoublyLinkedListNode<T> cur = tail;
            for (int i = size - 1; i > index; i--) {
                cur = cur.getPrevious();
            }
            cur.setPrevious(
                    new DoublyLinkedListNode<T>(data, cur.getPrevious(), cur));
            cur.getPrevious().getPrevious().setNext(cur.getPrevious());
        }
        size++;
    }

    /**
     * Adds the element to the front of the list.
     *
     * Must be O(1).
     *
     * @param data the data to add to the front of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToFront(T data) {
        this.addAtIndex(0, data);
    }

    /**
     * Adds the element to the back of the list.
     *
     * Must be O(1).
     *
     * @param data the data to add to the back of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToBack(T data) {
        this.addAtIndex(size, data);
    }

    /**
     * Removes and returns the element at the specified index. Don't forget to
     * consider whether traversing the list from the head or tail is more
     * efficient!
     *
     * Must be O(1) for indices 0 and size - 1 and O(n) for all other cases.
     *
     * @param index the index of the element to remove
     * @return the data formerly located at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T removeAtIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Given remove index is " + index
                    + " which is out of bounds for DoublyLinkedList of size "
                    + size);
        }

        T removed;
        if (index == 0) {
            // Remove from front by advancing head and removing link
            removed = head.getData();
            head = head.getNext();

            // If list becomes empty reassign tail
            if (size == 1) {
                tail = null;
            } else {
                head.setPrevious(null);
            }
        } else if (index == size - 1) {
            // Remove from back by advancing tail and removing link
            removed = tail.getData();
            tail = tail.getPrevious();
            tail.setNext(null);
        } else if (index < size / 2) {
            // Remove from first half of LinkedList iterating from head
            DoublyLinkedListNode<T> cur = head;
            for (int i = 0; i < index - 1; i++) {
                cur = cur.getNext();
            }
            removed = cur.getNext().getData();
            cur.setNext(cur.getNext().getNext());
            cur.getNext().setPrevious(cur);
        } else {
            // Remove from second half of LinkedList iterating from tail
            DoublyLinkedListNode<T> cur = tail;
            for (int i = size - 1; i > index + 1; i--) {
                cur = cur.getPrevious();
            }
            removed = cur.getPrevious().getData();
            cur.setPrevious(cur.getPrevious().getPrevious());
            cur.getPrevious().setNext(cur);
        }

        size--;
        return removed;

    }

    /**
     * Removes and returns the first element of the list.
     *
     * Must be O(1).
     *
     * @return the data formerly located at the front of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T removeFromFront() {
        if (this.isEmpty()) {
            throw new NoSuchElementException(
                    "Tried to remove from empty DoublyLinkedList");
        }
        return this.removeAtIndex(0);
    }

    /**
     * Removes and returns the last element of the list.
     *
     * Must be O(1).
     *
     * @return the data formerly located at the back of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T removeFromBack() {
        if (this.isEmpty()) {
            throw new NoSuchElementException(
                    "Tried to remove from empty DoublyLinkedList");
        }
        return this.removeAtIndex(size - 1);
    }

    /**
     * Returns the element at the specified index. Don't forget to consider
     * whether traversing the list from the head or tail is more efficient!
     *
     * Must be O(1) for indices 0 and size - 1 and O(n) for all other cases.
     *
     * @param index the index of the element to get
     * @return the data stored at the index in the list
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Given get index is " + index
                    + " which is out of bounds for DoublyLinkedList of size "
                    + size);
        }

        DoublyLinkedListNode<T> cur;
        if (index < size / 2) {
            // Find element in first half of array iterating from head
            cur = head;
            for (int i = 0; i < index; i++) {
                cur = cur.getNext();
            }

        } else {
            // Find element in second half of array iterating from tail
            cur = tail;
            for (int i = size - 1; i > index; i--) {
                cur = cur.getPrevious();
            }
        }
        return cur.getData();

    }

    /**
     * Returns whether or not the list is empty.
     *
     * Must be O(1).
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the list.
     *
     * Clears all data and resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Removes and returns the last copy of the given data from the list.
     *
     * Do not return the same data that was passed in. Return the data that was
     * stored in the list.
     *
     * Must be O(1) if data is in the tail and O(n) for all other cases.
     *
     * @param data the data to be removed from the list
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if data is not found
     */
    public T removeLastOccurrence(T data) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "Tried to remove null data from DoublyLinkedList");
        }

        if (this.isEmpty()) {
            throw new NoSuchElementException(
                    "Tried to remove from empty DoublyLinkedList");
        }

        // Removing from tail if tail is last occurrence of data
        T removed;
        if (tail.getData().equals(data)) {
            removed = tail.getData();
            tail = tail.getPrevious();

            // special case if tail is last node
            if (size == 1) {
                head = tail;
            } else {
                tail.setNext(null);
            }

            size--;

            return removed;
        }

        // Iterating from tail to head to find last occurrence of data
        DoublyLinkedListNode<T> cur = tail;

        while (cur.getPrevious() != null) {
            if (cur.getPrevious().getData().equals(data)) {
                removed = cur.getPrevious().getData();

                cur.setPrevious(cur.getPrevious().getPrevious());
                if (cur.getPrevious() != null) {
                    cur.getPrevious().setNext(cur);
                } else {
                    // special case for if last occurrence of data is at head
                    head = cur;
                }

                size--;
                return removed;
            }
            cur = cur.getPrevious();
        }

        throw new NoSuchElementException(
                "Data " + data.toString() + " not found in DoublyLinkedList");

    }

    /**
     * Returns an array representation of the linked list. If the list is size
     * 0, return an empty array.
     *
     * Must be O(n) for all cases.
     *
     * @return an array of length size holding all of the objects in the list in
     *         the same order
     */
    public Object[] toArray() {
        if (isEmpty()) {
            return new Object[] {};
        } else {
            DoublyLinkedListNode<T> cur = head;
            Object[] elements = new Object[size];

            // Iterate through all elements and add to array
            for (int i = 0; i < size; i++) {
                elements[i] = cur.getData();
                cur = cur.getNext();
            }

            return elements;
        }
    }

    /**
     * Returns the head node of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the node at the head of the list
     */
    public DoublyLinkedListNode<T> getHead() {
        // DO NOT MODIFY!
        return head;
    }

    /**
     * Returns the tail node of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the node at the tail of the list
     */
    public DoublyLinkedListNode<T> getTail() {
        // DO NOT MODIFY!
        return tail;
    }

    /**
     * Returns the size of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the list
     */
    public int size() {
        // DO NOT MODIFY!
        return size;
    }
}
