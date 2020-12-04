package model;

import support.Support.Direction;

public class BinaryTree<T> {
	
	private Node<T> root;
	
	/* adds new element to existing parent node. If parent is unknown
	 * the new node gets inserted above root node and becomes new root.
	 * If tree is empty and parent null, node gets inserted as new root */
	public void addElement(T newElem, T parent, Direction dir) {
		Node<T> newNode = searchNode(newElem, root);
		Node<T> parentNode = searchNode(parent, root);
		// Tree is empty, add both parent and child
		if(root == null && parentNode == null && newNode == null) {
			newNode = new Node<T>(newElem);
			parentNode = new Node<T>(parent);
			root = parentNode;
			switch(dir) {
			case LEFT: parentNode.setLeft(newNode); break;
			case RIGHT: parentNode.setRight(newNode); break;
			}
			newNode.setParent(parentNode);
		} else if(parentNode != null && newNode == null) { // Normal insert at lower node
			newNode = new Node<T>(newElem);
			switch(dir) {
			case LEFT: parentNode.setLeft(newNode); break;
			case RIGHT: parentNode.setRight(newNode); break;
			}
			newNode.setParent(parentNode);
		} else if(parentNode == null && newNode != null) { // Parent is unknown, insert as new root
			parentNode = new Node<T>(parent);
			switch(dir) {
			case LEFT: parentNode.setLeft(root); break;
			case RIGHT: parentNode.setRight(root); break;
			}
			root.setParent(parentNode);
			root = parentNode;
		}
	}
	
	public void addElement(BinaryTree<T> tree, T parent, Direction dir) {
		Node<T> parentNode = searchNode(parent, root);
		// Tree is empty
		if(root == null && parent == null) {
			root = tree.getRoot();
		} else if(parentNode != null) { // Normal insert at lower node
			switch(dir) {
			case LEFT: parentNode.setLeft(tree.getRoot()); break;
			case RIGHT: parentNode.setRight(tree.getRoot()); break;
			}
			tree.getRoot().setParent(parentNode);
		} else if(parentNode == null) { // Parent is unknown, insert as new root
			parentNode = new Node<T>(parent);
			switch(dir) {
			case LEFT: parentNode.setLeft(root); break;
			case RIGHT: parentNode.setRight(root); break;
			}
			root.setParent(parentNode);
			root = parentNode;
		}
	}
	
	public void removeElement(T elem) {
		Node<T> nodeToRemove = searchNode(elem, root);
		if(nodeToRemove == null) {
			System.out.println("Tried to delete non existing element");
			return;
		}
		Node<T> parent = nodeToRemove.getParent();
		if(nodeToRemove != root) {
			if(nodeToRemove.equals(parent.getLeft())) {
				parent.setLeft(null);
			} else {
				parent.setRight(null);
			}
		} else {
			root = null;
		}
		nodeToRemove.setParent(null);
		nodeToRemove.setLeft(null);
		nodeToRemove.setRight(null);
	}
	
	public boolean contains(T elem) {
		return (searchNode(elem, root) != null);
	}
	
	public boolean isEmpty() {
		return root == null;
	}
	
	public Node<T> getRoot() {
		return root;
	}
	
	// Search recursively the whole tree
	private Node<T> searchNode(T searched, Node<T> root) {
		if(root == null) {
			return null;
		}
		if(searched.equals(root.getContent())) {
			return root;
		}
		if(root.getLeft() != null) {
			return searchNode(searched, root.getLeft());
		}
		if(root.getRight() != null) {
			return searchNode(searched, root.getRight());
		}
		return null;
	}
}
