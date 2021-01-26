package controller;

import java.util.HashMap;
import java.util.Map;

import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

import model.CSGModel;
import model.SizeMeasurements;
import net.wcomohundro.jme3.csg.CSGShape;
import net.wcomohundro.jme3.csg.shape.CSGCylinder;
import support.Support;
import view.block.BlockComponent;
import view.block.PrimShapeBlock;

/**
 * Manages 3D objects of blocks. Enables block to get a {@link model.CSGModel CSGModel} assigned.
 * @author chriz
 *
 */
public class CSGModelManager {
	
	/** Reference for passing it onto CSGModels for CSG calculation. */
	private TreeManager treeMan;
	
	/** Instance of JME, enables editing of scene graph. */
	private JME jme;
	
	/** Map containing fitting block and CSGModel pairs. */
	private Map<BlockComponent, CSGModel> modelMap;
	
	/** Reference to the currently highlighted model in scenegraph */
	private CSGModel highlightedModel;
	
	public CSGModelManager(TreeManager treeMan, JME jme) {
		this.treeMan = treeMan;
		this.jme = jme;
		modelMap = new HashMap<>();
	}
	
	/**
	 * Creates a new CSGModel using given block.
	 * @param block Block to create a CSGModel of.
	 */
	public void createCSGModel(BlockComponent block) {
		modelMap.put(block, new CSGModel(treeMan, this, block, jme.getAssetManager()));
	}
	
	/**
	 * Deletes a CSGModel.
	 * @param block Block of which to delete the CSGModel.
	 */
	public void deleteCSGModel(BlockComponent block) {
		modelMap.remove(block);
	}
	
	public boolean hasCSGModel(BlockComponent block) {
		return modelMap.containsKey(block);
	}
	
	/**
	 * Adds a CSGModel to the scene graph queue.
	 * @param block Block of which the CSGModel should be added to scene graph queue.
	 */
	public void displayCSGModel(BlockComponent block) {
		CSGModel model = modelMap.get(block);
		jme.addToSceneGraph(model.getCSG());
	}
	
	/**
	 * Adds a CSGModel to the scene graph removal queue, resulting in the model being removed
	 * from scene graph.
	 * @param block Block of which the CSGModel should be added to scene graph removal queue.
	 */
	public void undisplayCSGModel(BlockComponent block) {
		CSGModel model = modelMap.get(block);
		jme.removeFromSceneGraph(model.getCSG());
	}
	
	public CSGModel getCSGModel(BlockComponent block) {
		return modelMap.get(block);
	}
	
	/**
	 * Starts a thread and recomputes the CSG of a given block.
	 * @param block Block of which to recompute the CSG.
	 */
	public void invokeCSGCalculation(BlockComponent block) {
		new Thread(new CSGCalculator(modelMap.get(block), this)).start();
	}
	
	public void resizeCSGModel(BlockComponent block, SizeMeasurements size) {
		undisplayCSGModel(block);
		modelMap.get(block).resizeModel(size);;
		displayCSGModel(block);
	}
	
	public void unhighlightModel() {
		if(highlightedModel != null) {
			highlightedModel.unHighlight();
			highlightedModel = null;
		}
	}
	
	public void highlightModel(BlockComponent block) {
		if(block == null) {
			highlightedModel = null;
			return;
		}
		
		highlightedModel = modelMap.get(block);
		highlightedModel.doHighlight();
	}
}
