package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import model.BinaryTree;
import model.Shape;
import net.wcomohundro.jme3.csg.CSGShape;
import support.Support.Direction;
import view.BlockComponent;
import view.PrimShapeBlock;
import view.View;
/**
 * Controls the application. Creates the view and jMonkey viewport.
 * Organizes the model (BinaryTrees).
 * @author chriz
 *
 */
public class Controller extends SimpleApplication {
	/** Singleton */
	private static Controller controller;
	/** GUI */
	private View view;
	/** Mesh for jMonkey to display */
	private CSGShape csg;
	/** Time passed since last update for jMonkey */
	private float updateTime = 0;
	
	/** Map containing trees of blocks. Every block has a tree. */
	private Map<BlockComponent, BinaryTree<BlockComponent>> treeMap;
	
	/** Map for assigning each block a csg mesh */
	private Map<BlockComponent, Shape> shapeMap;
	
	public Controller() {
		treeMap = new HashMap<BlockComponent, BinaryTree<BlockComponent>>();
		shapeMap = new HashMap<BlockComponent, Shape>();
	}
	
	/** Singleton */
	public static Controller getInstance() {
		if(controller == null) {
			controller = new Controller();
		}
		return controller;
	}
	
	/**
	 * Starts application
	 */
	public void start() {
		view = new View(this);
		view.initView();
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
		shapeMap.remove(blockToDelete);
		rootNode.detachAllChildren();
		csg = null;
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
	
	public Shape getLeftShape(BlockComponent block) {
		BinaryTree<BlockComponent> tree = treeMap.get(block);
		return shapeMap.get(tree.getLeft(block));
	}
	
	public Shape getRightShape(BlockComponent block) {
		BinaryTree<BlockComponent> tree = treeMap.get(block);
		return shapeMap.get(tree.getRight(block));
	}
	
	public boolean hasTree(BlockComponent block) {
		return treeMap.containsKey(block);
	}
	
	public void setDisplayedMesh(BlockComponent block) {
		Shape shape = shapeMap.get(block);
		csg = shape.generateCSGMesh();
	}
	
	public void addShape(BlockComponent block) {
		shapeMap.put(block, new Shape(block));
	}
	
	public static void main(String[] args) {
		//org.swingexplorer.Launcher.launch();
		Controller controller = Controller.getInstance();
		controller.start();
		java.awt.EventQueue.invokeLater(new Runnable() {
	    	public void run() {
	    		AppSettings settings = new AppSettings(true);
	    		settings.setWidth(640);
	    		settings.setHeight(480);
	    		controller.createCanvas();
	    		JmeCanvasContext ctx = (JmeCanvasContext) controller.getContext();
	    		ctx.setSystemListener(controller);
	    		controller.view.setJMonkeyWindow(ctx.getCanvas());
	    		controller.startCanvas();
	    	}
	    });
	}

	@Override
	public void simpleInitApp() {
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(200);
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		updateTime += tpf;
		if(updateTime > 0.2) {
			updateTime = 0;
			if(csg != null || rootNode.hasChild(csg)) {
				rootNode.detachAllChildren();
				rootNode.attachChild(csg);
			}
		}
		super.simpleUpdate(tpf);
	}
}
