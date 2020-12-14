package model;

import java.util.ArrayList;
import java.util.List;

import support.Support.Direction;

public class BinaryTree<T> {
	
	private Node<T> root;
	
	public BinaryTree(T root) {
		this.root = new Node<T>(root);
	}
	
	// Attaches new element to the parent node in tree
		public void addElement(BinaryTree<T> childTree, T parent, Direction dir) {
			Node<T> parentNode = searchNode(parent, root);
			if(dir == Direction.LEFT) {
				parentNode.setLeft(childTree.getRoot());
			} else {
				parentNode.setRight(childTree.getRoot());
			}
			childTree.getRoot().setParent(parentNode);
			System.out.println(toString());
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
			if(nodeToRemove.equals(parent.getLeft())) {
				parent.setLeft(null);
			} else {
				parent.setRight(null);
			}
		}
		// delete whole tree below
		deleteTree(nodeToRemove);
		nodeToRemove.setParent(null);
		nodeToRemove.setLeft(null);
		nodeToRemove.setRight(null);
		System.out.println(toString());
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
	
	@Override
	public String toString() {
	    return root.toString(new StringBuilder(), true, new StringBuilder()).toString();
	}
}
