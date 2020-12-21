package controller;

import java.util.ArrayList;
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
import model.Shape;
import support.Support.Direction;
import view.BlockComponent;
import view.PrimShapeBlock;
import view.View;

public class Controller extends SimpleApplication {
	
	private static Controller controller;
	private View view;

	private Shape lastSelected;
	
	// Map for quick access of trees for specific blocks
	private Map<BlockComponent, BinaryTree<Shape>> treeMap;
	
	public Controller() {
		treeMap = new HashMap<BlockComponent, BinaryTree<Shape>>();
	}
	
	public static Controller getInstance() {
		if(controller == null) {
			controller = new Controller();
		}
		return controller;
	}
	
	public void start() {
		view = new View(this);
		view.initView();
	}
	
	// Create new tree for block
	public void createTree(BlockComponent blockToAdd) {
		BinaryTree<Shape> newTree = new BinaryTree<Shape>(new Shape(blockToAdd));
		treeMap.put(blockToAdd, newTree);
	}
	
	public void deleteTree(BlockComponent blockToDelete) {
		treeMap.remove(blockToDelete);
	}
	
	// Add a new block to required tree by map lookup
	public void addToTree(BlockComponent blockToAdd, BlockComponent parent, Direction dir) {
		// Get trees of blocks
		BinaryTree<Shape> parentTree = treeMap.get(parent);
		BinaryTree<Shape> blockToAddTree = treeMap.get(blockToAdd);
		// Delete tree of child block and insert it into parent tree
		treeMap.remove(blockToAdd);
		treeMap.put(blockToAdd, parentTree);
		parentTree.addElement(blockToAddTree, new Shape(parent), dir);
	}
	
	public void removeFromTree(BlockComponent blockToRemove) {
		// Get relevant tree
		BinaryTree<Shape> tree = treeMap.get(blockToRemove);
		if(tree != null) {
			Shape shapeToRemove = new Shape(blockToRemove);
			List<Shape> children = tree.getChildren(shapeToRemove);
			
			// Delete element from tree, get branch as newTree
			BinaryTree<Shape> newTree = tree.removeElement(shapeToRemove);
			
			//Remove actual element
			treeMap.remove(blockToRemove);
			treeMap.put(blockToRemove, newTree);
			
			// Delete children of block from treeMap and add to same seperate tree
			for(Shape c : children) {
				treeMap.remove(c.getBlock());
				treeMap.put(c.getBlock(), newTree);
			}
		}
	}
	
	public int getDepth(BlockComponent block) {
		BinaryTree<Shape> tree = treeMap.get(block);
		return tree.getDepth(new Shape(block), PrimShapeBlock.class);
	}
	
	public List<BlockComponent> getChildren(BlockComponent block) {
		BinaryTree<Shape> tree = treeMap.get(block);
		if(tree != null) {
			List<BlockComponent> childrenAsBlock = new ArrayList<>();
			for(Shape e : tree.getChildren(new Shape(block))) {
				childrenAsBlock.add(e.getBlock());
			}
			return childrenAsBlock;
		} else {
			return new ArrayList<BlockComponent>();
		}
	}
	
	public BlockComponent getRoot(BlockComponent block) {
		BinaryTree<Shape> tree = treeMap.get(block);
		if(tree != null) {
			return tree.getRoot().getBlock();
		} else {
			return null;
		}
	}
	
	public BlockComponent getLeft(BlockComponent block) {
		BinaryTree<Shape> tree = treeMap.get(block);
		Shape shape = tree.getLeft(new Shape(block));
		if(shape != null) {
			return shape.getBlock();
		} else {
			return null;
		}
	}
	
	public BlockComponent getRight(BlockComponent block) {
		BinaryTree<Shape> tree = treeMap.get(block);

		Shape shape = tree.getRight(new Shape(block));
		if(shape != null) {
			return shape.getBlock();
		} else {
			return null;
		}
	}
	
	public Shape getLeftShape(BlockComponent block) {
		BinaryTree<Shape> tree = treeMap.get(block);
		if(tree != null) {
			return tree.getLeft(new Shape(block));
		} else {
			return null;
		}
	}
	
	public Shape getRightShape(BlockComponent block) {
		BinaryTree<Shape> tree = treeMap.get(block);
		if(tree != null) {
			return tree.getRight(new Shape(block));
		} else {
			return null;
		}
	}
	
	public boolean hasTree(BlockComponent block) {
		return treeMap.containsKey(block);
	}
	
	public void setLastSelected(BlockComponent lastSelected) {
		this.lastSelected = new Shape(lastSelected);
		
		
		this.lastSelected.generateCSGMesh();
		if(this.lastSelected.getCSG().getMesh() != null) {
			rootNode.detachAllChildren();
			rootNode.attachChild(this.lastSelected.getCSG());
		}
	}
	
	public BlockComponent getLastSelected() {
		if(lastSelected != null) {
			return lastSelected.getBlock();
		} else {
			return null;
		}
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
//		Box b = new Box(1, 1, 1);
//		Geometry geom = new Geometry("Box", b);
//		Material mat = new Material(assetManager,"Common/MatDefs/Misc/ShowNormals.j3md");
//		geom.setMaterial(mat);
//		rootNode.attachChild(geom);
		flyCam.setMoveSpeed(200);
	}
}
