package model;

import java.util.ArrayList;
import java.util.List;

import support.Support.Direction;

public class BinaryTree<T> {
	
	private Node<T> root;
	
	// Attaches new element to the parent node in tree
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
		System.out.println(root.toString());
	}
	
	// Removes element from tree
	public void removeElement(T elem) {
		// Search element in tree
		Node<T> nodeToRemove = searchNode(elem, root);
		if(nodeToRemove == null) {
			System.out.println("Tried to delete non existing element");
			return;
		}
		Node<T> parent = nodeToRemove.getParent();
		// Check if root node neets deletion
		if(nodeToRemove != root) { 
			deleteTree(nodeToRemove); // Delete rest of tree below
			if(nodeToRemove.equals(parent.getLeft())) {
				parent.setLeft(null);
			} else {
				parent.setRight(null);
			}
		} else { // remove root, delete whole tree below
			deleteTree(root);
			root = null;
		}
		nodeToRemove.setParent(null);
		nodeToRemove.setLeft(null);
		nodeToRemove.setRight(null);
		if(root != null) {
			System.out.println(root.toString());
		}
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
	
	public int getDepth(T elem) {
		return getDepth(searchNode(elem, root));
	}
	
	private int getDepth(Node<T> root) {
		if(root == null) {
			return 0;
		}
		int leftDepth = getDepth(root.getLeft());
		int rightDepth = getDepth(root.getRight());
		if(leftDepth > rightDepth) {
			return leftDepth + 1;
		} else {
			return rightDepth + 1;
		}
	}
	
	// Returns list of children below parent node
	public List<T> getChildren(T parent) {
		List<T> list = new ArrayList<T>();
		Node<T> parentNode = searchNode(parent, root);
		getChildren(list, parentNode);
		return list;
	}
	
	// Adds all children below node to given list recursively
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
	
	// Delete everything recursively below node
	private void deleteTree(Node<T> root) {
		Node<T> worker = root;
		if(worker.getLeft() != null) {
			deleteTree(worker.getLeft());
			worker.getLeft().setParent(null);
			worker.setLeft(null);
		}
		if(worker.getRight() != null) {
			deleteTree(worker.getRight());
			worker.getRight().setParent(null);
			worker.setRight(null);
		}
	}
	
	// Search recursively the whole tree
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
}
