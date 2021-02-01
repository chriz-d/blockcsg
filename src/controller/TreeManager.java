package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.BinaryTree;
import model.IBinaryTree;
import support.Support.Direction;
import view.block.BlockComponent;
import view.block.PrimShapeBlock;
/**
 * Controls the application. Creates the view and jMonkey viewport.
 * Organizes the model (BinaryTrees).
 * @author chriz
 *
 */
public class TreeManager implements ITreeManager {
	
	/** Map containing trees of blocks. Every block has a tree. */
	private Map<BlockComponent, IBinaryTree<BlockComponent>> treeMap;
	
	public TreeManager() {
		treeMap = new HashMap<BlockComponent, IBinaryTree<BlockComponent>>();
	}
	
	/**
	 * Creates a new tree for specified block and adds it to map.
	 * @param blockToAdd Block to create a tree for.
	 */
	@Override
	public void createTree(BlockComponent blockToAdd) {
		IBinaryTree<BlockComponent> newTree = new BinaryTree<BlockComponent>(blockToAdd);
		treeMap.put(blockToAdd, newTree);
	}
	
	/**
	 * Deletes a tree of a block and removes mesh from jMonkey viewport.
	 * @param blockToDelete Block of which to delete tree.
	 */
	@Override
	public void deleteTree(BlockComponent blockToDelete) {
		treeMap.remove(blockToDelete);
	}
	
	/**
	 * Adds a block to an existing tree.
	 * @param blockToAdd Block to add.
	 * @param parent Component to attach the new block to.
	 * @param dir Direction of where to attach the new block to.
	 */
	@Override
	public void addToTree(BlockComponent blockToAdd, BlockComponent parent, Direction dir) {
		// Get trees of blocks
		IBinaryTree<BlockComponent> parentTree = treeMap.get(parent);
		IBinaryTree<BlockComponent> blockToAddTree = treeMap.get(blockToAdd);
		// Delete tree of child block and insert it into parent tree
		treeMap.remove(blockToAdd);
		treeMap.put(blockToAdd, parentTree);
		parentTree.addElement(blockToAddTree, parent, dir);
	}
	
	/**
	 * Removes a block and its children from existing tree. Removed block and children
	 * get put into separate tree and are added to map.
	 * @param blockToRemove Block to remove.
	 */
	@Override
	public void removeFromTree(BlockComponent blockToRemove) {
		// Get relevant tree
		IBinaryTree<BlockComponent> tree = treeMap.get(blockToRemove);
		List<BlockComponent> children = tree.getChildren(blockToRemove);
		
		// Delete element from tree, get branch as newTree
		IBinaryTree<BlockComponent> newTree = tree.removeElement(blockToRemove);
		
		//Remove actual element
		treeMap.remove(blockToRemove);
		treeMap.put(blockToRemove, newTree);
		
		// Delete children of block from treeMap and add to same seperate tree
		for(BlockComponent c : children) {
			treeMap.remove(c);
			treeMap.put(c, newTree);
		}
	}
	
	@Override
	public int getDepth(BlockComponent block) {
		IBinaryTree<BlockComponent> tree = treeMap.get(block);
		return tree.getDepth(block, PrimShapeBlock.class);
	}
	
	/**
	 * Returns a list of all children below specified block.
	 * @param block Parent block of which to get all children of.
	 * @return List of children.
	 */
	@Override
	public List<BlockComponent> getChildren(BlockComponent block) {
		IBinaryTree<BlockComponent> tree = treeMap.get(block);
		List<BlockComponent> childrenAsBlock = new ArrayList<>();
		for(BlockComponent e : tree.getChildren(block)) {
			childrenAsBlock.add(e);
		}
		return childrenAsBlock;
	}
	
	/**
	 * Gets the root of a tree from given block.
	 * @param block Block of which to get root of.
	 * @return Root of the given block.
	 */
	@Override
	public BlockComponent getRoot(BlockComponent block) {
		IBinaryTree<BlockComponent> tree = treeMap.get(block);
		return tree.getRoot();
	}
	
	@Override
	public BlockComponent getLeft(BlockComponent block) {
		IBinaryTree<BlockComponent> tree = treeMap.get(block);
		return tree.getLeft(block);
	}
	
	@Override
	public BlockComponent getRight(BlockComponent block) {
		IBinaryTree<BlockComponent> tree = treeMap.get(block);
		return tree.getRight(block);
	}
	
	@Override
	public boolean hasTree(BlockComponent block) {
		return treeMap.containsKey(block);
	}
}
