package test;


import org.junit.jupiter.api.Test;

import model.BinaryTree;
import support.Support.Direction;

public class BinaryTreeTest {
	
	@Test
	public void testBinaryTreeInsert() {
		// Create tree
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		
		tree.addElement(0, null, Direction.LEFT);
		for(int i = 1; i < 100; i++) {
			if((i % 2) == 1) {
				tree.addElement(i, i-1, Direction.LEFT);
			} else {
				tree.addElement(i, i-1, Direction.RIGHT);
			}
		}
		for(int i = 0; i < 100; i++) {
			assert(tree.contains(i));
		}
	}
	
	@Test
	public void testBinaryTreeInsertTop() {
		// Create tree
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		
		tree.addElement(0, null, Direction.LEFT);
		for(int i = 1; i < 100; i++) {
			if((i % 2) == 1) {
				tree.addElement(i-1, i, Direction.LEFT);
			} else {
				tree.addElement(i-1, i, Direction.RIGHT);
			}
		}
		for(int i = 0; i < 100; i++) {
			assert(tree.contains(i));
		}
	}
	
	@Test
	public void testBinaryTreeRemove() {
		// Create tree
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		
		tree.addElement(0, null, Direction.LEFT);
		for(int i = 1; i < 10; i++) {
			if((i % 2) == 1) {
				tree.addElement(i-1, i, Direction.LEFT);
			} else {
				tree.addElement(i-1, i, Direction.RIGHT);
			}
		}
		for(int i = 0; i < 5; i++) {
			tree.removeElement(i);
		}
		for(int i = 5; i < 10; i++) {
			assert(tree.contains(i));
		}
		for(int i = 5; i < 10; i++) {
			tree.removeElement(i);
		}
		assert(tree.isEmpty());
	}
}
