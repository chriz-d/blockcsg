package model;

import java.util.ArrayList;
import java.util.List;

import support.Support.Direction;

public class BinaryTree<T> {
	
	private Node<T> root;
	
	public BinaryTree(T root) {
		this.root = new Node<T>(root);
	}
	
	private BinaryTree(Node<T> root) {
		this.root = root;
	}
	
	// Attaches new element to the parent node in tree
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
	
	// Removes element from tree
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
	
	public boolean contains(T elem) {
		return (searchNode(elem, root) != null);
	}
	
	public boolean isEmpty() {
		return root == null;
	}
	
	private Node<T> getRootNode() {
		return root;
	}
	
	public T getRoot() {
		return root.getContent();
	}
	
	/*
	 * Returns max depth of the tree. Second parameter is used for filtering undesired nodes
	 */
	public int getDepth(T elem, Class<?> cls) {
		return getDepth(searchNode(elem, root), cls);
	}
	
	public T getLeft(T elem) {
		Node<T> node = searchNode(elem, root);
		if(node != null && node.getLeft() != null) {
			return node.getLeft().getContent();
		} else {
			return null;
		}
	}
	
	public T getRight(T elem) {
		Node<T> node = searchNode(elem, root);
		if(node != null && node.getRight() != null) {
			return node.getRight().getContent();
		} else {
			return null;
		}
	}
	
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
		
		public void setContent(G content) {
			this.content = content;
		}
		
		// toString from https://stackoverflow.com/a/27153988
		public StringBuilder toString(StringBuilder prefix, boolean isTail, StringBuilder sb) {
		    if(right!=null) {
		        right.toString(new StringBuilder().append(prefix).append(isTail ? "│   " : "    "), false, sb);
		    }
		    sb.append(prefix).append(isTail ? "└── " : "┌── ").append(content.toString()).append("\n");
		    if(left!=null) {
		        left.toString(new StringBuilder().append(prefix).append(isTail ? "    " : "│   "), true, sb);
		    }
		    return sb;
		}
	}
}
