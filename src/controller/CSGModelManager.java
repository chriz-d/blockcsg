package controller;

import java.util.HashMap;
import java.util.Map;

import model.CSGModel;
import view.block.BlockComponent;

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
}
