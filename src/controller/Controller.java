package controller;

import java.util.HashMap;
import java.util.Map;

import model.BinaryTree;
import support.Support.Direction;
import view.BlockComponent;
import view.View;

public class Controller {
	
	private View view;
	
	// Map for quick access of trees for specific blocks
	private Map<BlockComponent, BinaryTree<BlockComponent>> treeMap;
	
	public Controller() {
		treeMap = new HashMap<BlockComponent, BinaryTree<BlockComponent>>();
	}
	
	public void start() {
		view = new View(this);
		view.initView();
	}
	
	public void addToTree(BlockComponent blockToAdd, BlockComponent parent, Direction dir) {
		BinaryTree<BlockComponent> parentTree = treeMap.get(parent);
		BinaryTree<BlockComponent> blockToAddTree = treeMap.get(blockToAdd);
		if(parentTree == null && blockToAddTree == null) {
			// Both components have no tree yet, create one for parent
			treeMap.put(parent, new BinaryTree<BlockComponent>());
			parentTree = treeMap.get(parent);
		}
		if(parentTree == null && blockToAddTree != null) {
			// New node gets inserted above root
			blockToAddTree.addElement(blockToAdd, parent, dir);
			treeMap.put(parent, blockToAddTree);
		} else {
			// normal insert
			parentTree.addElement(blockToAdd, parent, dir);
			treeMap.put(blockToAdd, parentTree);
		}
	}
	
	public void removeFromTree(BlockComponent blockToRemove) {
		BinaryTree<BlockComponent> tree = treeMap.get(blockToRemove);
		tree.removeElement(blockToRemove);
		treeMap.remove(blockToRemove);
	}
	
	public void deleteFromTreeList() {
		
	}
	
	public static void main(String[] args) {
		//org.swingexplorer.Launcher.launch();
		Controller controller = new Controller();
		controller.start();
	}
}
