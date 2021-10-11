import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Your implementation of a BST.
 *
 * @author Henry Liao
 * @version 1.0
 * @userid hliao62
 * @GTID 903682804
 *
 * Collaborators: N/A
 *
 * Resources: N/A
 */
public class BST<T extends Comparable<? super T>> {

    /*
     * Do not add new instance variables or modify existing ones.
     */
    private BSTNode<T> root;
    private int size;

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize an empty BST.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public BST() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize the BST with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * Hint: Not all Collections are indexable like Lists, so a regular for loop
     * will not work here. However, all Collections are Iterable, so what type
     * of loop would work?
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public BST(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "Tried to initialize BST with null Collection");
        }

        for (T datum : data) {
            this.add(datum);
        }
    }

    /**
     * Adds the data to the tree.
     *
     * This must be done recursively.
     *
     * The data becomes a leaf in the tree.
     *
     * Traverse the tree to find the appropriate location. If the data is
     * already in the tree, then nothing should be done (the duplicate shouldn't
     * get added, and size should not be incremented).
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Tried to add null data to BST");
        }

        root = this.addHelper(data, root);

    }

    /**
     * Recursive helper method for adding data into a given subtree rooted at
     * node.
     * 
     * Will recurse on subtrees to traverse to proper adding position. Recurses
     * on left subtree if given data is less than that stored in node. Will
     * recurse on right subtree if data is greater than that stored in node.
     * 
     * Terminate once node added or if current node data is equal to given data.
     * 
     * @param data the data to add
     * @param cur  the current node that is the root of the subtree
     * @return the child to be assigned to the parent of previous recursion
     */
    private BSTNode<T> addHelper(T data, BSTNode<T> cur) {
        if (cur == null) {
            size++;
            return new BSTNode<T>(data);
        }
        int comparison = data.compareTo(cur.getData());
        if (comparison < 0) {
            cur.setLeft(addHelper(data, cur.getLeft()));
            return cur;
        }
        if (comparison > 0) {
            cur.setRight(addHelper(data, cur.getRight()));
            return cur;

        }
        return cur;
    }

    /**
     * Removes and returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * There are 3 cases to consider: 1: The node containing the data is a leaf
     * (no children). In this case, simply remove it. 2: The node containing the
     * data has one child. In this case, simply replace it with its child. 3:
     * The node containing the data has 2 children. Use the successor to replace
     * the data. You MUST use recursion to find and remove the successor (you
     * will likely need an additional helper method to handle this case
     * efficiently).
     *
     * Do not return the same data that was passed in. Return the data that was
     * stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "Tried to remove null data from BST");
        }

        BSTNode<T> removed = new BSTNode<>(null);
        root = this.removeHelper(data, root, removed);
        return removed.getData();
    }

    /**
     * Recursively removes and returns data from given subtree rooted at node.
     * 
     * Recurses if data is not equal to node or either of its children. Recurses
     * left if data is less than that stored in node, right if greater than that
     * stored in node.
     * 
     * On finding data, will remove according to three given cases: 1. If the
     * node has no children, 2. If the node has 2 children, 3. If the node has
     * either a left child or right child.
     * 
     * A helper method is used to implement 2 efficiently by finding and
     * removing successor.
     * 
     * @param data    the data to be removed from the subtree
     * @param cur     the current node that is the root of the subtree
     * @param removed the dummy node used to capture removed node
     * @return the desired child of cur's parent
     * @throws java.util.NoSuchElementException if the data does not exist is in
     *                                          the subtree
     */
    private BSTNode<T> removeHelper(T data, BSTNode<T> cur,
            BSTNode<T> removed) {

        if (cur == null) {
            throw new NoSuchElementException("Tried to remove data "
                    + data.toString() + " which does not exist in the BST");
        }

        int comparison = data.compareTo(cur.getData());

        if (comparison < 0) {
            // recurse on left subtree if data is less than current node
            cur.setLeft(removeHelper(data, cur.getLeft(), removed));
            return cur;
        } else if (comparison > 0) {
            // recurse on right subtree if data is greater than current node
            cur.setRight(removeHelper(data, cur.getRight(), removed));
            return cur;
        } else {
            removed.setData(cur.getData());
            size--;
            if (cur.getLeft() == null) {
                return cur.getRight();
            }
            if (cur.getRight() == null) {
                return cur.getLeft();
            }

            // 0 and 1 node cases already handled, now 2 node case

            // find and remove successor
            BSTNode<T> successor = new BSTNode<T>(null);
            cur.setRight((this.removeSuccessor(cur.getRight(), successor)));

            cur.setData(successor.getData());
            return cur;
        }
    }

    /**
     * Recursively finds and deletes the successor (the leftmost node of the
     * right subtree of removed node). Continues recursing left until it reaches
     * the successor.
     * 
     * 
     * @param cur       the node that is the current root of the subtree
     * @param successor the dummy node used to capture the successor
     * @return the desired child of cur's parent
     */
    private BSTNode<T> removeSuccessor(BSTNode<T> cur, BSTNode<T> successor) {
        if (cur.getLeft() == null) {
            successor.setData(cur.getData());
            return cur.getRight();
        }
        // keep recursing leftward if possible
        cur.setLeft(removeSuccessor(cur.getLeft(), successor));
        return cur;
    }

    /**
     * Returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * Do not return the same data that was passed in. Return the data that was
     * stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "Tried to get null data from BST");
        }
        return this.getHelper(data, root);

    }

    /**
     * Recursively finds the data in the subtree equal to the parameter data.
     * 
     * Recurses left if data is less than the current node data. Recurses right
     * if data is greater than the current node data. Terminates if data can't
     * be found or current node is equal to the data.
     * 
     * @param data the data to search for in the subtree
     * @param cur  the current root of the subtree
     * @return the data in the tree equal to the parameter data
     * @throws java.util.NoSuchElementException if data does not exist within
     *                                          the subtree
     */
    private T getHelper(T data, BSTNode<T> cur) {
        if (cur == null) {
            throw new NoSuchElementException("Tried to get data "
                    + data.toString() + " which does not exist in the BST");
        }

        int comparison = data.compareTo(cur.getData());

        if (comparison < 0) {
            return getHelper(data, cur.getLeft());
        } else if (comparison > 0) {
            return getHelper(data, cur.getRight());
        }
        return cur.getData();

    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * This must be done recursively.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return true if the parameter is contained within the tree, false
     *         otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "Tried to query if BST contains null data");
        }

        return this.containsHelper(data, root);
    }

    /**
     * Recursively searches for if subtree contains data. Recurses left if data
     * is less than that stored in current node or right if greater.
     * 
     * @param data the data to be found in the subtree
     * @param cur  the root of the current subtree
     * @return if the data is contained within the subtree
     */
    private boolean containsHelper(T data, BSTNode<T> cur) {

        if (cur == null) {
            return false;
        }

        int comparison = data.compareTo(cur.getData());

        if (comparison < 0) {
            return containsHelper(data, cur.getLeft());
        } else if (comparison > 0) {
            return containsHelper(data, cur.getRight());
        }
        return true;

    }

    /**
     * Generate a pre-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the preorder traversal of the tree
     */
    public List<T> preorder() {
        return this.preorderHelper(root, new ArrayList<T>());
    }

    /**
     * Recursively generates a preorder traversal of the given subtree.
     * 
     * First adds to list, then recurses left, then right.
     * 
     * @param cur      the root node of the current subtree
     * @param elements the list of nodes added so far
     * @return a preorder traversal of the subtree added to elements
     */
    private List<T> preorderHelper(BSTNode<T> cur, List<T> elements) {
        if (cur != null) {
            elements.add(cur.getData());
            preorderHelper(cur.getLeft(), elements);
            preorderHelper(cur.getRight(), elements);
        }
        return elements;
    }

    /**
     * Generate an in-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the inorder traversal of the tree
     */
    public List<T> inorder() {
        return this.inorderHelper(root, new ArrayList<T>());
    }

    /**
     * Recursively generates an inorder traversal of the given subtree.
     * 
     * First recurses left, then adds to list, then recurses right.
     * 
     * @param cur      the root node of the current subtree
     * @param elements the list of nodes added so far
     * @return an inorder traversal of the subtree added to elements
     */
    private List<T> inorderHelper(BSTNode<T> cur, List<T> elements) {
        if (cur != null) {
            inorderHelper(cur.getLeft(), elements);
            elements.add(cur.getData());
            inorderHelper(cur.getRight(), elements);
        }
        return elements;
    }

    /**
     * Generate a post-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the postorder traversal of the tree
     */
    public List<T> postorder() {
        return this.postorderHelper(root, new ArrayList<T>());
    }

    /**
     * Recursively generates a postorder traversal of the given subtree.
     * 
     * First recurses left, then right, then adds to list.
     * 
     * @param cur      the root node of the current subtree
     * @param elements the list of nodes added so far
     * @return a postorder traversal of the subtree added to elements
     */
    private List<T> postorderHelper(BSTNode<T> cur, List<T> elements) {
        if (cur != null) {
            postorderHelper(cur.getLeft(), elements);
            postorderHelper(cur.getRight(), elements);
            elements.add(cur.getData());
        }
        return elements;
    }

    /**
     * Generate a level-order traversal of the tree.
     *
     * This does not need to be done recursively.
     *
     * Hint: You will need to use a queue of nodes. Think about what initial
     * node you should add to the queue and what loop / loop conditions you
     * should use.
     *
     * Must be O(n).
     *
     * @return the level order traversal of the tree
     */
    public List<T> levelorder() {
        Queue<BSTNode<T>> q = new LinkedList<>();

        List<T> elements = new ArrayList<>();

        if (root == null) {
            return elements;
        }

        // use queue of nodes to traverse tree
        q.offer(root);
        while (!q.isEmpty()) {
            BSTNode<T> cur = q.poll();
            if (cur.getLeft() != null) {
                q.offer(cur.getLeft());
            }
            if (cur.getRight() != null) {
                q.offer(cur.getRight());
            }

            elements.add(cur.getData());
        }
        return elements;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * This must be done recursively.
     *
     * A node's height is defined as max(left.height, right.height) + 1. A leaf
     * node has a height of 0 and a null child has a height of -1.
     *
     * Must be O(n).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        return this.heightHelper(root, 0);
    }

    /**
     * Recursively returns the height by finding the maximum depth of any node
     * in the subtree.
     * 
     * Recurses on all nodes, progressively increasing depth with each level of
     * recursion.
     * 
     * @param cur   the current root of the subtree
     * @param depth the depth of the cur
     * @return the maximum height of a node in the subtree
     */
    private int heightHelper(BSTNode<T> cur, int depth) {
        if (cur == null) {
            return depth - 1;
        }
        return Math.max(heightHelper(cur.getLeft(), depth + 1),
                heightHelper(cur.getRight(), depth + 1));
    }

    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Finds and retrieves the k-largest elements from the BST in sorted order,
     * least to greatest.
     *
     * This must be done recursively.
     *
     * In most cases, this method will not need to traverse the entire tree to
     * function properly, so you should only traverse the branches of the tree
     * necessary to get the data and only do so once. Failure to do so will
     * result in an efficiency penalty.
     *
     * EXAMPLE: Given the BST below composed of Integers:
     *
     * 50 / \ 25 75 / \ 12 37 / \ \ 10 15 40 / 13
     *
     * kLargest(5) should return the list [25, 37, 40, 50, 75]. kLargest(3)
     * should return the list [40, 50, 75].
     *
     * Should have a running time of O(log(n) + k) for a balanced tree and a
     * worst case of O(n + k).
     *
     * @param k the number of largest elements to return
     * @return sorted list consisting of the k largest elements
     * @throws java.lang.IllegalArgumentException if k > n, the number of data
     *                                            in the BST
     */
    public List<T> kLargest(int k) {
        if (k > size) {
            throw new IllegalArgumentException(
                    "k is greater than BST of size " + size);
        }

        return this.kLargestHelper(k, root, new LinkedList<T>());
    }

    /**
     * Recursively adds the largest elements in the given subtree until the size
     * of the list elements reaches k.
     * 
     * Always recurses right first, then adds, then recurses left.
     * 
     * @param k        the number of greatest elements that should end up in
     *                 elements
     * @param cur      the root of the current tree
     * @param elements the list of the kth largest numbers
     * @return the largest numbers of the subtree added to elements until its
     *         size equals k
     */
    private List<T> kLargestHelper(int k, BSTNode<T> cur, List<T> elements) {
        if (cur != null) {
            kLargestHelper(k, cur.getRight(), elements);
            if (elements.size() < k) {
                elements.add(0, cur.getData());
            }
            if (elements.size() < k) {
                kLargestHelper(k, cur.getLeft(), elements);
            }
        }
        return elements;

    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public BSTNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }

    /**
     * Returns the size of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
