import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Your implementation of an AVL Tree.
 *
 * @author Henry Liao
 * @userid hliao62
 * @GTID 903682804
 * @version 1.0
 */
public class AVL<T extends Comparable<? super T>> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private AVLNode<T> root;
    private int size;

    /**
     * A no-argument constructor that should initialize an empty AVL.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public AVL() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Initializes the AVL tree with the data in the Collection. The data should
     * be added in the same order it appears in the Collection.
     *
     * @throws IllegalArgumentException if data or any element in data is null
     * @param data the data to add to the tree
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "Tried initialize AVL with null Collection");
        }
        for (T datum : data) {
            this.add(datum);
        }
    }

    /**
     * Adds the data to the AVL. Start by adding it as a leaf like in a regular
     * BST and then rotate the tree as needed.
     *
     * If the data is already in the tree, then nothing should be done (the
     * duplicate shouldn't get added, and size should not be incremented).
     *
     * Remember to recalculate heights and balance factors going up the tree,
     * rebalancing if necessary.
     *
     * @throws java.lang.IllegalArgumentException if the data is null
     * @param data the data to be added
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Tried to add null data to AVL");
        }
        root = add(root, data);
    }

    /**
     * Recursive helper method to recursively add data to a tree. Utilizes
     * pointer reinforcement by returning node that should be child of parent
     * node.
     * 
     * If data less than current node, recurses left. If greater than current
     * node, recurses right. Adds data as a leaf, and terminates if current node
     * is a duplicate of data.
     * 
     * Fixes any shape violations on the way back up the tree by updating height
     * and balance factor of each node and checking if rebalancing is necessary.
     * 
     * @param cur  the node that is the root of the current subtree
     * @param data the data to be added to the tree
     * @return the node that should be the child of the parent recursion
     */
    private AVLNode<T> add(AVLNode<T> cur, T data) {
        if (cur == null) {
            size++;
            return new AVLNode<T>(data);
        }
        if (data.compareTo(cur.getData()) < 0) {
            cur.setLeft(add(cur.getLeft(), data));
        }
        if (data.compareTo(cur.getData()) > 0) {
            cur.setRight(add(cur.getRight(), data));
        }
        this.update(cur);
        return this.fixTree(cur);
    }

    /**
     * Removes the data from the tree. There are 3 cases to consider:
     *
     * 1: the data is a leaf. In this case, simply remove it. 2: the data has
     * one child. In this case, simply replace it with its child. 3: the data
     * has 2 children. Use the successor to replace the data, not the
     * predecessor. As a reminder, rotations can occur after removing the
     * successor node.
     *
     * Remember to recalculate heights going up the tree, rebalancing if
     * necessary.
     *
     * @throws IllegalArgumentException         if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data to remove from the tree.
     * @return the data removed from the tree. Do not return the same data that
     *         was passed in. Return the data that was stored in the tree.
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "Tried to remove null data froo AVL");
        }
        AVLNode<T> removed = new AVLNode<T>(null);
        this.remove(root, data, removed);
        return removed.getData();

    }

    /**
     * Helper method for recursively removing data from the tree.
     * 
     * If cur is null, the data cannot be found and terminate the search. If the
     * data is less than the data in cur, recurse on the left subtree. If the
     * data is greater than the data in cur, recurse on right subtree. If the
     * data is equal to the data in cur, remove the node.
     * 
     * In the no child removal case, return null. If there is one child, return
     * that child. If there is two children, uses recursive helper method to
     * remove successor and replace the current node with successor
     * 
     * Fixes any shape violations on the way back up the tree by updating height
     * and balance factor of each node and checking if rebalancing is necessary.
     * 
     * @throws java.util.NoSuchElementException if the data is not found
     * @param cur     the node that is the root of the current subtree
     * @param data    the data to remove from the tree
     * @param removed the dummy node used to capture data from removal
     * @return the node that should be the child of the parent recursive call
     */
    private AVLNode<T> remove(AVLNode<T> cur, T data, AVLNode<T> removed) {
        if (cur == null) {
            throw new NoSuchElementException(
                    "Data to remove was not found in AVL");
        }
        if (data.compareTo(cur.getData()) < 0) {
            cur.setLeft(remove(cur.getLeft(), data, removed));
            this.update(cur);
            return this.fixTree(cur);
        }
        if (data.compareTo(cur.getData()) > 0) {
            cur.setRight(remove(cur.getRight(), data, removed));
            this.update(cur);
            return this.fixTree(cur);
        }
        removed.setData(cur.getData());
        size--;

        if (cur.getLeft() == null) {
            return cur.getRight();
        }
        if (cur.getRight() == null) {
            return cur.getLeft();
        }
        AVLNode<T> successor = new AVLNode<T>(null);
        cur.setRight(this.removeSuccessor(cur.getRight(), successor));
        cur.setData(successor.getData());

        this.update(cur);
        return this.fixTree(cur);

    }

    /**
     * Helper method for recursively removing the successor node of a given
     * subtree. Called in the two-child removal case.
     * 
     * Recurses leftwards until it reaches the successor.
     * 
     * Fixes any shape violations on the way back up the tree by updating height
     * and balance factor of each node and checking if rebalancing is necessary.
     * 
     * Fixes any shape violations on the way back up the tree by updating height
     * and balance factor of each node and checking if rebalancing is necessary.
     * 
     * @param cur       the current node for which to recurse on
     * @param successor the dummy node used to capture successor data
     * @return the node that should be the child of the parent recursive call
     */
    private AVLNode<T> removeSuccessor(AVLNode<T> cur, AVLNode<T> successor) {
        if (cur.getLeft() == null) {
            successor.setData(cur.getData());
            return null;
        }
        cur.setLeft(removeSuccessor(cur.getLeft(), successor));
        this.update(cur);
        return this.fixTree(cur);
    }

    /**
     * A helper method to handle how to solve imbalances at a given node and
     * update height and balance factors of the subtree following the fix.
     * 
     * @param node the node at which to solve imbalances at
     * @return the node at the root of the given subtree
     */
    private AVLNode<T> fixTree(AVLNode<T> node) {
        if (node.getBalanceFactor() == 2) {
            AVLNode<T> newRoot;
            if (node.getLeft().getBalanceFactor() == -1) {
                node.setLeft(this.rotateLeft(node.getLeft()));
                newRoot = this.rotateRight(node);
                this.update(newRoot.getLeft());
            } else {
                newRoot = this.rotateRight(node);
            }
            this.update(newRoot.getRight());
            this.update(newRoot);
            return newRoot;
        } else if (node.getBalanceFactor() == -2) {
            AVLNode<T> newRoot;
            if (node.getRight().getBalanceFactor() == 1) {
                node.setRight(this.rotateRight(node.getRight()));
                newRoot = this.rotateLeft(node);
                update(newRoot.getRight());
            } else {
                newRoot = this.rotateLeft(node);
            }
            this.update(newRoot.getLeft());
            this.update(newRoot);
            return newRoot;
        }
        return node;
    }

    /**
     * A helper method that performs a single AVL left rotation about the root
     * of a subtree in O(1).
     * 
     * @param pivot the node about which to rotate
     * @return the new root of the subtree after rotation completes
     */
    private AVLNode<T> rotateLeft(AVLNode<T> pivot) {
        AVLNode<T> right = pivot.getRight();
        pivot.setRight(right.getLeft());
        right.setLeft(pivot);

        return right;
    }

    /**
     * A helper method that performs a single right AVL rotation about the root
     * of a subtree in O(1).
     * 
     * @param pivot the Node about which to rotate
     * @return the new root of the subtree after rotation completes
     */
    private AVLNode<T> rotateRight(AVLNode<T> pivot) {
        AVLNode<T> left = pivot.getLeft();
        pivot.setLeft(left.getRight());
        left.setRight(pivot);

        return left;
    }

    /**
     * Updates the height and balance factors of a node based on its children.
     * 
     * @param node the node for which to update height and BF
     */
    private void update(AVLNode<T> node) {
        int left = (node.getLeft() == null) ? -1 : node.getLeft().getHeight();
        int right = (node.getRight() == null) ? -1
                : node.getRight().getHeight();
        node.setHeight(Math.max(left, right) + 1);
        node.setBalanceFactor(left - right);
    }

    /**
     * Returns the data in the tree matching the parameter passed in (think
     * carefully: should you use value equality or reference equality?).
     *
     * @throws IllegalArgumentException         if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data to search for in the tree.
     * @return the data in the tree equal to the parameter. Do not return the
     *         same data that was passed in. Return the data that was stored in
     *         the tree.
     */
    public T get(T data) {
        if (data.equals(null)) {
            throw new IllegalArgumentException(
                    "Tried to get null data from AVL.");
        }
        return this.get(root, data);
    }

    /**
     * Recursively finds data within the AVL. If data is less than data at
     * current node, recurses left. If data is greater than data at current
     * node, recurses right. If data is equal to current node's data, returns
     * the data stored in the tree.
     * 
     * Search terminates if data cannot be found.
     * 
     * @throws java.util.NoSuchElementException if the data is not found
     * @param cur  the root of the current subtree
     * @param data the data to search for in the tree
     * @return the data in the tree equal to the parameter
     */
    private T get(AVLNode<T> cur, T data) {
        if (cur == null) {
            throw new NoSuchElementException();
        }

        if (data.compareTo(cur.getData()) < 0) {
            return get(cur.getLeft(), data);
        }
        if (data.compareTo(cur.getData()) > 0) {
            return get(cur.getRight(), data);
        }
        return cur.getData();
    }

    /**
     * Returns whether or not data equivalent to the given parameter is
     * contained within the tree. The same type of equality should be used as in
     * the get method.
     *
     * @throws IllegalArgumentException if the data is null
     * @param data the data to search for in the tree.
     * @return whether or not the parameter is contained within the tree.
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "Tried to query if AVL contains null data.");
        }

        return this.contains(root, data);
    }

    /**
     * Recursively determines if given data exists within the AVL. If data is
     * less than data at current node, recurses left. If data is greater than
     * data at current node, recurses right. If data is equal to current node's
     * data, returns the data stored in the tree.
     * 
     * Search terminates if data cannot be found.
     * 
     * @param cur  the root of the current subtree
     * @param data the data to search for in the tree
     * @return the data in the tree equal to the parameter
     */
    private boolean contains(AVLNode<T> cur, T data) {
        if (cur == null) {
            return false;
        }
        if (data.compareTo(cur.getData()) < 0) {
            return contains(cur.getLeft(), data);
        }
        if (data.compareTo(cur.getData()) > 0) {
            return contains(cur.getRight(), data);
        }
        return true;
    }

    /**
     * Returns the data on branches of the tree with the maximum depth. If you
     * encounter multiple branches of maximum depth while traversing, then you
     * should list the remaining data from the left branch first, then the
     * remaining data in the right branch. This is essentially a preorder
     * traversal of the tree, but only of the branches of maximum depth.
     *
     * Your list should not duplicate data, and the data of a branch should be
     * listed in order going from the root to the leaf of that branch.
     *
     * Should run in worst case O(n), but you should not explore branches that
     * do not have maximum depth. You should also not need to traverse branches
     * more than once.
     *
     * Hint: How can you take advantage of the balancing information stored in
     * AVL nodes to discern deep branches?
     *
     * Example Tree: 10 / \ 5 15 / \ / \ 2 7 13 20 / \ / \ \ / \ 1 4 6 8 14 17
     * 25 / \ \ 0 9 30
     *
     * Returns: [10, 5, 2, 1, 0, 7, 8, 9, 15, 20, 25, 30]
     *
     * @return the list of data in branches of maximum depth in preorder
     *         traversal order
     */
    public List<T> deepestBranches() {
        List<T> elements = new LinkedList<>();
        this.deepestBranches(root, elements, root.getHeight() + 1);
        return elements;
    }

    /**
     * Recursively appends all elements in deepest branches to a list. If the
     * current node is on a deepest branch, adds it to the list and recurses
     * left and right.
     * 
     * @param cur      the root of the current subtree
     * @param elements a list of elements to add all elements in the deepest
     *                 branches to
     * @param height   the height of the parent of the current node
     */
    private void deepestBranches(AVLNode<T> cur, List<T> elements, int height) {
        if (cur != null) {

            if (cur.getHeight() == height - 1) {
                elements.add(cur.getData());
                deepestBranches(cur.getLeft(), elements, height - 1);
                deepestBranches(cur.getRight(), elements, height - 1);

            }
        }
    }

    /**
     * Returns a sorted list of data that are within the threshold bounds of
     * data1 and data2. That is, the data should be > data1 and < data2.
     *
     * Should run in worst case O(n), but this is heavily dependent on the
     * threshold data. You should not explore branches of the tree that do not
     * satisfy the threshold.
     *
     * Example Tree: 10 / \ 5 15 / \ / \ 2 7 13 20 / \ / \ \ / \ 1 4 6 8 14 17
     * 25 / \ \ 0 9 30
     *
     * sortedInBetween(7, 14) returns [8, 9, 10, 13] sortedInBetween(3, 8)
     * returns [4, 5, 6, 7] sortedInBetween(8, 8) returns []
     *
     * @throws java.lang.IllegalArgumentException if data1 or data2 are null
     * @param data1 the smaller data in the threshold
     * @param data2 the larger data in the threshold or if data1 > data2
     * @return a sorted list of data that is > data1 and < data2
     */
    public List<T> sortedInBetween(T data1, T data2) {
        if (data1 == null) {
            throw new IllegalArgumentException(
                    "data1 for sortedInBetween call in AVL was null");
        }

        if (data2 == null) {
            throw new IllegalArgumentException(
                    "data2 for sortedInBetween call in AVL was null");
        }

        List<T> elements = new LinkedList<>();
        this.sortedInBetween(root, elements, data1, data2);
        return elements;
    }

    /**
     * Helper elements for appending all elements in between data1 and data2 to
     * a list.
     * 
     * If the current node is in between data1 and data2, add the node in and
     * recurse on both children in in-order format.
     * 
     * If less than or equal to data 1, recurse left. If greater than data 2,
     * recurse right.
     * 
     * @param cur      the root of the current subtree
     * @param elements the list to append elements between data1 and data2 to
     * @param data1    the lower bound of the list of elements non-inclusive
     * @param data2    the upper bound of the list of elements non-inclusive
     */
    private void sortedInBetween(AVLNode<T> cur, List<T> elements, T data1,
            T data2) {
        if (cur != null) {
            if (cur.getData().compareTo(data1) <= 0) {
                sortedInBetween(cur.getRight(), elements, data1, data2);
            } else if (cur.getData().compareTo(data2) >= 0) {
                sortedInBetween(cur.getLeft(), elements, data1, data2);
            } else {
                sortedInBetween(cur.getLeft(), elements, data1, data2);
                elements.add(cur.getData());
                sortedInBetween(cur.getRight(), elements, data1, data2);
            }
        }
    }

    /**
     * Clears the tree.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * Since this is an AVL, this method does not need to traverse the tree and
     * should be O(1)
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        if (root == null) {
            return -1;
        }
        return root.getHeight();
    }

    /**
     * Returns the size of the AVL tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return number of items in the AVL tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD
        return size;
    }

    /**
     * Returns the root of the AVL tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the AVL tree
     */
    public AVLNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }
}