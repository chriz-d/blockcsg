package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import model.BinaryTree;
import model.Node;
import support.Support.Direction;
import view.BlockComponent;
import view.PrimShapeBlock;
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
	
	// Create new tree for block
	public void createTree(BlockComponent blockToAdd) {
		BinaryTree<BlockComponent> newTree = new BinaryTree<BlockComponent>(blockToAdd);
		treeMap.put(blockToAdd, newTree);
	}
	
	// Add a new block to required tree by map lookup
	public void addToTree(BlockComponent blockToAdd, BlockComponent parent, Direction dir) {
		// Get trees of blocks
		BinaryTree<BlockComponent> parentTree = treeMap.get(parent);
		BinaryTree<BlockComponent> blockToAddTree = treeMap.get(blockToAdd);
		// Delete tree of child block and insert it into parent tree
		treeMap.remove(blockToAdd);
		treeMap.put(blockToAdd, parentTree);
		parentTree.addElement(blockToAddTree, parent, dir);
	}
	
	public void removeFromTree(BlockComponent blockToRemove) {
		// Get relevant tree
		BinaryTree<BlockComponent> tree = treeMap.get(blockToRemove);
		if(tree != null) {
			// Delete children of block and block itself from treeMap and add to separate trees each
			List<BlockComponent> children = tree.getChildren(blockToRemove);
			for(BlockComponent c : children) {
				treeMap.remove(c);
				treeMap.put(c, new BinaryTree<BlockComponent>(c));
			}
			//Remove actual element
			tree.removeElement(blockToRemove);
			treeMap.remove(blockToRemove);
			treeMap.put(blockToRemove, new BinaryTree<BlockComponent>(blockToRemove));
		}
	}
	
	public int getDepth(BlockComponent block) {
		BinaryTree<BlockComponent> tree = treeMap.get(block);
		if(tree != null) {
			return tree.getDepth(block, PrimShapeBlock.class);
		} else {
			return 1;
		}
	}
	
	public List<BlockComponent> getChildren(BlockComponent block) {
		BinaryTree<BlockComponent> tree = treeMap.get(block);
		if(tree != null) {
			return tree.getChildren(block);
		} else {
			return null;
		}
	}
	
	public BlockComponent getRoot(BlockComponent block) {
		BinaryTree<BlockComponent> tree = treeMap.get(block);
		if(tree != null) {
			return tree.getRoot().getContent();
		} else {
			return null;
		}
	}
	
	public BlockComponent getLeft(BlockComponent block) {
		BinaryTree<BlockComponent> tree = treeMap.get(block);
		if(tree != null) {
			return tree.getLeft(block);
		} else {
			return null;
		}
	}
	
	public BlockComponent getRight(BlockComponent block) {
		BinaryTree<BlockComponent> tree = treeMap.get(block);
		if(tree != null) {
			return tree.getRight(block);
		} else {
			return null;
		}
	}
	
	public boolean hasTree(BlockComponent block) {
		return treeMap.containsKey(block);
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
		Box b = new Box(1, 1, 1);
		Geometry geom = new Geometry("Box", b);
		Material mat = new Material(assetManager,"Common/MatDefs/Misc/ShowNormals.j3md");
		geom.setMaterial(mat);
		rootNode.attachChild(geom);
		flyCam.setMoveSpeed(200);
	}
}
