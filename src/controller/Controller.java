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
import model.Shape;
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
public class Controller extends SimpleApplication {
	/** Singleton */
	private static Controller controller;
	/** GUI */
	private View view;
	/** Mesh for jMonkey to display */
	private CSGShape currentDisplayedObject;
	private CSGShape lastDisplayedObject;
	
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
	@Override
	public void start() {
		view = new View(this);
		view.initView();
		java.awt.EventQueue.invokeLater(new Runnable() {
	    	@Override
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
		rootNode.detachChild(lastDisplayedObject);
		currentDisplayedObject = null;
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
	
	/** Gets desired mesh by map lookup and computes complete mesh in thread */
	public void setDisplayedMesh(BlockComponent block) {
		Shape shape = shapeMap.get(block);
		new Thread(new CSGCalculator(shape)).start();
	}
	
	public void addShape(BlockComponent block) {
		shapeMap.put(block, new Shape(block));
	}
	
	public static void main(String[] args) {
		//org.swingexplorer.Launcher.launch();
		Controller controller = Controller.getInstance();
		controller.start();
	}

	@Override
	public void simpleInitApp() {
		setPauseOnLostFocus(false);
		flyCam.setEnabled(false);
		flyCam.setDragToRotate(true);
		CameraNode camNode = new CameraNode("Camera", cam);
		camNode.setLocalTranslation(0, 0, -10);
		Node node = new Node("pivot");
		node.attachChild(camNode);
		camNode.getCamera().lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
		rootNode.attachChild(node);
		enableCameraControls(node);
		System.out.println(cam.getLocation());
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		updateTime += tpf;
		if(updateTime > 0.2) {
			updateTime = 0;
			if(currentDisplayedObject != null && !rootNode.hasChild(currentDisplayedObject)) {
				if(lastDisplayedObject != null) {
					rootNode.detachChild(lastDisplayedObject);
				}
				rootNode.attachChild(currentDisplayedObject);
				lastDisplayedObject = currentDisplayedObject;
			}
		}
		super.simpleUpdate(tpf);
	}
	
	/**
	 * Adds camera controls to JMonkeys input manager (Rotation around center 0,0,0)
	 */
	private void enableCameraControls(Node node) {
		inputManager.addMapping("Rotate Left", new MouseAxisTrigger(MouseInput.AXIS_X, true));
		inputManager.addMapping("Rotate Right", new MouseAxisTrigger(MouseInput.AXIS_X, false));
		inputManager.addMapping("Rotate Up", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
		inputManager.addMapping("Rotate Down", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
		inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		JMEKeyListener listener = new JMEKeyListener(node);
		inputManager.addListener(listener, new String[] {"Rotate Left", "Rotate Right", "Rotate Up", "Rotate Down"});
		inputManager.addListener(listener, new String[] {"Click"});
	}
	
	/** Used by thread for setting variable */
	public void setcurrentDisplayedObject(CSGShape shape) {
		currentDisplayedObject = shape;
	}
	
	public Mesh getCurrentDisplayedMesh() {
		return currentDisplayedObject.getMesh();
	}
}
