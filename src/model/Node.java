package model;

import view.BlockComponent;

public class Node {
	
	private BlockComponent component;
	
	private Node parent;
	private Node left;
	private Node right;
	// Operand
	
	public Node(BlockComponent component) {
		parent = null;
		left = null;
		right = null;
		this.component = component;
	}
	
	public Node(Node parent, Node left, Node right, BlockComponent component) {
		this.parent = parent;
		this.left = left;
		this.right = right;
		this.component = component;
	}
	
	public void setLeft(Node node) {
		left = node;
	}
	
	public void setRight(Node node) {
		right = node;
	}
	
	public void setParent(Node node) {
		parent = node;
	}
	
	public Node getLeft() {
		return left;
	}
	
	public Node getRight() {
		return right;
	}
	
	public Node getParent() {
		return parent;
	}
}
