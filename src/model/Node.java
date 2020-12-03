package model;

public class Node<T> {
	
	private T content;
	
	private Node<T> parent;
	private Node<T> left;
	private Node<T> right;
	
	public Node(T content) {
		parent = null;
		left = null;
		right = null;
		this.content = content;
	}
	
	public void setLeft(Node<T> node) {
		left = node;
	}
	
	public void setRight(Node<T> node) {
		right = node;
	}
	
	public void setParent(Node<T> node) {
		parent = node;
	}
	
	public Node<T> getLeft() {
		return left;
	}
	
	public Node<T> getRight() {
		return right;
	}
	
	public Node<T> getParent() {
		return parent;
	}
	
	public T getContent() {
		return content;
	}
	
	public void setContent(T content) {
		this.content = content;
	}
}
