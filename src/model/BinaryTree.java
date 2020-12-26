package model;

import java.util.ArrayList;
import java.util.List;

import support.Support.Direction;
/**
 * A generic implementation of unsorted binary trees. Insertion and deletion
 * is being done by searching tree for specified elements. (In this project 
 * trees stay very small in size, so searching isn't very expensive)
 * 
 * @author chriz
 * @param <T> Type of BinaryTree.
 */
public class BinaryTree<T> {
	/** Root node of the tree. */
	private Node<T> root;
	
	/** Creates a new tree with the specified element as the root. 
	 * @param root Root of the tree.
	 */
	public BinaryTree(T root) {
		this.root = new Node<T>(root);
	}
	
	/**
	 * Creates a new tree with the specified node as the new root.
	 * @param root Root of the tree
	 */
	private BinaryTree(Node<T> root) {
		this.root = root;
	}
	
	/**
	 * Attaches a new tree / branch to the tree at the specified node and direction.
	 * @param childTree Node to attach the new element to.
	 * @param parent Node in tree to attach the new element to.
	 * @param dir Direction (Left / Right)
	 */
	public void addElement(BinaryTree<T> childTree, T parent, Direction dir) {
		// Get find parent node in tree
		Node<T> parentNode = searchNode(parent, root);
		
		// Attach child tree depending on direction
		if(dir == Direction.LEFT) {
			parentNode.setLeft(childTree.getRootNode());
		} else {
			parentNode.setRight(childTree.getRootNode());
		}
		// Set parent reference
		childTree.getRootNode().setParent(parentNode);
		System.out.println(toString());
	}
	
	/**
	 * Removes a specified element from the tree and returns the deleted element
	 * and its children as a new tree.
	 * @param elem Element to delete.
	 * @return A new tree containing deleted node as root and its children.
	 */
	public BinaryTree<T> removeElement(T elem) {
		// Search element in tree
		Node<T> nodeToRemove = searchNode(elem, root);
		if(nodeToRemove == null) {
			System.out.println("Tried to delete non existing element");
			return null;
		}
		Node<T> parent = nodeToRemove.getParent();
		// Check if root, else delete reference for node above
		if(nodeToRemove != root) { 
			if(nodeToRemove.equals(parent.getLeft())) {
				parent.setLeft(null);
			} else {
				parent.setRight(null);
			}
		}
		nodeToRemove.setParent(null);
		System.out.println(toString());
		return new BinaryTree<T>(nodeToRemove);
	}
	
	/**
	 * Return if tree contains specified element.
	 * @param elem Element to search.
	 * @return Boolean if element was found inside tree.
	 */
	public boolean contains(T elem) {
		return (searchNode(elem, root) != null);
	}
	
	/**
	 * @return Boolean if tree is empty.
	 */
	public boolean isEmpty() {
		return root == null;
	}
	
	private Node<T> getRootNode() {
		return root;
	}
	
	public T getRoot() {
		return root.getContent();
	}
	
	/**
	 * Returns the depth level of the lowest node starting from the specified node recursively.
	 * @param elem Starting element for depth measurement
	 * @param cls Class for filtering. Every element found in tree matching this class gets ignored.
	 * @return Calculated depth level.
	 */
	public int getDepth(T root, Class<?> cls) {
		return getDepth(searchNode(root, this.root), cls);
	}
	
	/**
	 * Searches element and returns left child.
	 * @param elem Element of which to return left child.
	 * @return Left element of specified element.
	 */
	public T getLeft(T elem) {
		Node<T> node = searchNode(elem, root);
		if(node != null && node.getLeft() != null) {
			return node.getLeft().getContent();
		} else {
			return null;
		}
	}
	
	/**
	 * Searches element and returns right child.
	 * @param elem Element of which to return right child.
	 * @return Right element of specified element.
	 */
	public T getRight(T elem) {
		Node<T> node = searchNode(elem, root);
		if(node != null && node.getRight() != null) {
			return node.getRight().getContent();
		} else {
			return null;
		}
	}
	
	/**
	 * Recursive helper method of getDepth()
	 */
	private int getDepth(Node<T> root, Class<?> cls) {
		if(root == null || cls.isInstance(root.getContent())) {
			return 0;
		}
		int leftDepth = getDepth(root.getLeft(), cls);
		int rightDepth = getDepth(root.getRight(), cls);
		if(leftDepth > rightDepth) {
			return leftDepth + 1;
		} else {
			return rightDepth + 1;
		}
	}
	
	/**
	 * Gets list of children below specified node
	 * @param parent Element of which to return all children.
	 * @return List of all found children.
	 */
	public List<T> getChildren(T parent) {
		List<T> list = new ArrayList<T>();
		Node<T> parentNode = searchNode(parent, root);
		getChildren(list, parentNode);
		return list;
	}
	
	/**
	 * Recursive helper method of getChildren().
	 */
	private void getChildren(List<T> list, Node<T> root) {
		Node<T> worker = root;
		if(worker.getLeft() != null) {
			getChildren(list, worker.getLeft());
			list.add(worker.getLeft().getContent());
		}
		if(worker.getRight() != null) {
			getChildren(list, worker.getRight());
			list.add(worker.getRight().getContent());
		}
	}
	
	/**
	 * Helper method for finding elements inside tree, because it's unsorted. 
	 */
	private Node<T> searchNode(T searched, Node<T> root) {
		if(root == null) {
			return null;
		}
		if(searched.equals(root.getContent())) {
			return root;
		}
		Node<T> left = searchNode(searched, root.getLeft());
		if(left != null) {
			return left;
		}
		Node<T> right = searchNode(searched, root.getRight());
		if(right != null) {
			return right;
		}
		return null;
	}
	
	@Override
	public String toString() {
	    return root.toString(new StringBuilder(), true, new StringBuilder()).toString();
	}
	
	/**
	 * Class for BinaryTree. Contains data and references for parent, left and right nodes.
	 * @author chriz
	 * @param <G>
	 */
	private class Node<G> {
		
		private G content;
		
		private Node<G> parent;
		private Node<G> left;
		private Node<G> right;
		
		public Node(G content) {
			parent = null;
			left = null;
			right = null;
			this.content = content;
		}
		
		public void setLeft(Node<G> node) {
			left = node;
		}
		
		public void setRight(Node<G> node) {
			right = node;
		}
		
		public void setParent(Node<G> node) {
			parent = node;
		}
		
		public Node<G> getLeft() {
			return left;
		}
		
		public Node<G> getRight() {
			return right;
		}
		
		public Node<G> getParent() {
			return parent;
		}
		
		public G getContent() {
			return content;
		}
	}
}
