package controller;

import java.util.List;

import support.Support.Direction;
import view.block.BlockComponent;

public interface ITreeManager {

	/**
	 * Creates a new tree for specified block and adds it to map.
	 * @param blockToAdd Block to create a tree for.
	 */
	void createTree(BlockComponent blockToAdd);

	/**
	 * Deletes a tree of a block and removes mesh from jMonkey viewport.
	 * @param blockToDelete Block of which to delete tree.
	 */
	void deleteTree(BlockComponent blockToDelete);

	/**
	 * Adds a block to an existing tree.
	 * @param blockToAdd Block to add.
	 * @param parent Component to attach the new block to.
	 * @param dir Direction of where to attach the new block to.
	 */
	void addToTree(BlockComponent blockToAdd, BlockComponent parent, Direction dir);

	/**
	 * Removes a block and its children from existing tree. Removed block and children
	 * get put into separate tree and are added to map.
	 * @param blockToRemove Block to remove.
	 */
	void removeFromTree(BlockComponent blockToRemove);

	int getDepth(BlockComponent block);

	/**
	 * Returns a list of all children below specified block.
	 * @param block Parent block of which to get all children of.
	 * @return List of children.
	 */
	List<BlockComponent> getChildren(BlockComponent block);

	/**
	 * Gets the root of a tree from given block.
	 * @param block Block of which to get root of.
	 * @return Root of the given block.
	 */
	BlockComponent getRoot(BlockComponent block);

	BlockComponent getLeft(BlockComponent block);

	BlockComponent getRight(BlockComponent block);

	boolean hasTree(BlockComponent block);

}