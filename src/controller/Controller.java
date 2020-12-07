package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import model.BinaryTree;
import support.Support.Direction;
import view.BlockComponent;
import view.View;

public class Controller extends SimpleApplication {
	
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
	
	// Add a new block to required tree by map lookup
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
		// Get relevant tree
		BinaryTree<BlockComponent> tree = treeMap.get(blockToRemove);
		if(tree != null) {
			// Delete children from treeMap
			List<BlockComponent> children = tree.getChildren(blockToRemove);
			for(BlockComponent c : children) {
				treeMap.remove(c);
			}
			//Remove actual element
			tree.removeElement(blockToRemove);
			treeMap.remove(blockToRemove);
		}
	}
	
	public static void main(String[] args) {
		//org.swingexplorer.Launcher.launch();
		Controller controller = new Controller();
		controller.start();
//		java.awt.EventQueue.invokeLater(new Runnable() {
//	    	public void run() {
//	    		AppSettings settings = new AppSettings(true);
//	    		settings.setWidth(640);
//	    		settings.setHeight(480);
//	    		controller.createCanvas();
//	    		JmeCanvasContext ctx = (JmeCanvasContext) controller.getContext();
//	    		ctx.setSystemListener(controller);
//	    		controller.view.setJMonkeyWindow(ctx.getCanvas());
//	    		controller.startCanvas();
//	    	}
//	    });
	}

	@Override
	public void simpleInitApp() {
		flyCam.setDragToRotate(true);
	}
}
