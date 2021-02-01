package model;

import java.util.List;

import support.Support.Direction;

public interface IBinaryTree<T> {

	/**
	 * Attaches a new tree / branch to the tree at the specified node and direction.
	 * @param childTree Node to attach the new element to.
	 * @param parent Node in tree to attach the new element to.
	 * @param dir Direction (Left / Right)
	 */
	void addElement(IBinaryTree<T> childTree, T parent, Direction dir);

	/**
	 * Removes a specified element from the tree and returns the deleted element
	 * and its children as a new tree.
	 * @param elem Element to delete.
	 * @return A new tree containing deleted node as root and its children.
	 */
	IBinaryTree<T> removeElement(T elem);

	/**
	 * Return if tree contains specified element.
	 * @param elem Element to search.
	 * @return Boolean if element was found inside tree.
	 */
	boolean contains(T elem);

	/**
	 * @return Boolean if tree is empty.
	 */
	boolean isEmpty();

	T getRoot();

	/**
	 * Returns the depth level of the lowest node starting from the specified node recursively.
	 * @param root Starting element for depth measurement
	 * @param cls Class for filtering. Every element found in tree matching this class gets ignored.
	 * @return Calculated depth level.
	 */
	int getDepth(T root, Class<?> cls);

	/**
	 * Searches element and returns left child.
	 * @param elem Element of which to return left child.
	 * @return Left element of specified element.
	 */
	T getLeft(T elem);

	/**
	 * Searches element and returns right child.
	 * @param elem Element of which to return right child.
	 * @return Right element of specified element.
	 */
	T getRight(T elem);

	/**
	 * Gets list of children below specified node
	 * @param parent Element of which to return all children.
	 * @return List of all found children.
	 */
	List<T> getChildren(T parent);

}