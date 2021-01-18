package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme3.app.SimpleApplication;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import model.BinaryTree;
import model.CSGModel;
import net.wcomohundro.jme3.csg.CSGShape;
import support.Support.Direction;
import view.View;
import view.block.BlockComponent;
import view.block.PrimShapeBlock;
/**
 * Controls the application. Creates the view and jMonkey viewport.
 * Organizes the model (BinaryTrees).
 * @author chriz
 *
 */
public class TreeManager {
	
	/** Map containing trees of blocks. Every block has a tree. */
	private Map<BlockComponent, BinaryTree<BlockComponent>> treeMap;
	
	public TreeManager() {
		treeMap = new HashMap<BlockComponent, BinaryTree<BlockComponent>>();
	}
	
	/**
	 * Creates a new tree for specified block and adds it to map.
	 * @param blockToAdd Block to create a tree for.
	 */
	public void createTree(BlockComponent blockToAdd) {
		BinaryTree<BlockComponent> newTree = new BinaryTree<BlockComponent>(blockToAdd);
		treeMap.put(blockToAdd, newTree);
	}
	
	/**
	 * Deletes a tree of a block and removes mesh from jMonkey viewport.
	 * @param blockToDelete Block of which to delete tree.
	 */
	public void deleteTree(BlockComponent blockToDelete) {
		treeMap.remove(blockToDelete);
	}
	
	/**
	 * Adds a block to an existing tree.
	 * @param blockToAdd Block to add.
	 * @param parent Component to attach the new block to.
	 * @param dir Direction of where to attach the new block to.
	 */
	public void addToTree(BlockComponent blockToAdd, BlockComponent parent, Direction dir) {
		// Get trees of blocks
		BinaryTree<BlockComponent> parentTree = treeMap.get(parent);
		BinaryTree<BlockComponent> blockToAddTree = treeMap.get(blockToAdd);
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
	public void removeFromTree(BlockComponent blockToRemove) {
		// Get relevant tree
		BinaryTree<BlockComponent> tree = treeMap.get(blockToRemove);
		List<BlockComponent> children = tree.getChildren(blockToRemove);
		
		// Delete element from tree, get branch as newTree
		BinaryTree<BlockComponent> newTree = tree.removeElement(blockToRemove);
		
		//Remove actual element
		treeMap.remove(blockToRemove);
		treeMap.put(blockToRemove, newTree);
		
		// Delete children of block from treeMap and add to same seperate tree
		for(BlockComponent c : children) {
			treeMap.remove(c);
			treeMap.put(c, newTree);
		}
	}
	
	public int getDepth(BlockComponent block) {
		BinaryTree<BlockComponent> tree = treeMap.get(block);
		return tree.getDepth(block, PrimShapeBlock.class);
	}
	
	/**
	 * Returns a list of all children below specified block.
	 * @param block Parent block of which to get all children of.
	 * @return
	 */
	public List<BlockComponent> getChildren(BlockComponent block) {
		BinaryTree<BlockComponent> tree = treeMap.get(block);
		List<BlockComponent> childrenAsBlock = new ArrayList<>();
		for(BlockComponent e : tree.getChildren(block)) {
			childrenAsBlock.add(e);
		}
		return childrenAsBlock;
	}
	
	public BlockComponent getRoot(BlockComponent block) {
		BinaryTree<BlockComponent> tree = treeMap.get(block);
		return tree.getRoot();
	}
	
	public BlockComponent getLeft(BlockComponent block) {
		BinaryTree<BlockComponent> tree = treeMap.get(block);
		return tree.getLeft(block);
	}
	
	public BlockComponent getRight(BlockComponent block) {
		BinaryTree<BlockComponent> tree = treeMap.get(block);
		return tree.getRight(block);
	}
	
	public boolean hasTree(BlockComponent block) {
		return treeMap.containsKey(block);
	}
}
