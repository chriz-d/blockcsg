package controller;

import java.util.HashMap;
import java.util.Map;

import model.CSGModel;
import model.SizeMeasurements;
import view.block.BlockComponent;

/**
 * Manages 3D objects of blocks. Enables block to get a {@link model.CSGModel CSGModel} assigned.
 * @author chriz
 *
 */
public class CSGModelManager implements ICSGModelManager {
	
	/** Reference for passing it onto CSGModels for CSG calculation. */
	private ITreeManager treeMan;
	
	/** Instance of JME, enables editing of scene graph. */
	private JME jme;
	
	/** Map containing fitting block and CSGModel pairs. */
	private Map<BlockComponent, CSGModel> modelMap;
	
	/** Reference to the currently highlighted model in scenegraph */
	private CSGModel highlightedModel;
	
	public CSGModelManager(ITreeManager treeMan, JME jme) {
		this.treeMan = treeMan;
		this.jme = jme;
		modelMap = new HashMap<>();
	}
	
	/**
	 * Creates a new CSGModel using given block.
	 * @param block Block to create a CSGModel of.
	 */
	@Override
	public void createCSGModel(BlockComponent block) {
		modelMap.put(block, new CSGModel(treeMan, this, block, jme.getAssetManager()));
	}
	
	/**
	 * Deletes a CSGModel.
	 * @param block Block of which to delete the CSGModel.
	 */
	@Override
	public void deleteCSGModel(BlockComponent block) {
		modelMap.remove(block);
	}
	
	@Override
	public boolean hasCSGModel(BlockComponent block) {
		return modelMap.containsKey(block);
	}
	
	/**
	 * Adds a CSGModel to the scene graph queue.
	 * @param block Block of which the CSGModel should be added to scene graph queue.
	 */
	@Override
	public void displayCSGModel(BlockComponent block) {
		CSGModel model = modelMap.get(block);
		jme.addToSceneGraph(model.getCSG());
	}
	
	/**
	 * Adds a CSGModel to the scene graph removal queue, resulting in the model being removed
	 * from scene graph.
	 * @param block Block of which the CSGModel should be added to scene graph removal queue.
	 */
	@Override
	public void undisplayCSGModel(BlockComponent block) {
		CSGModel model = modelMap.get(block);
		jme.removeFromSceneGraph(model.getCSG());
	}
	
	@Override
	public CSGModel getCSGModel(BlockComponent block) {
		return modelMap.get(block);
	}
	
	/**
	 * Starts a thread and recomputes the CSG of a given block.
	 * @param block Block of which to recompute the CSG.
	 */
	@Override
	public void invokeCSGCalculation(BlockComponent block) {
		new Thread(new CSGCalculator(modelMap.get(block), this)).start();
	}
	
	@Override
	public void resizeCSGModel(BlockComponent block, SizeMeasurements size) {
		undisplayCSGModel(block);
		modelMap.get(block).resizeModel(size);;
		displayCSGModel(block);
	}
	
	@Override
	public void unhighlightModel() {
		if(highlightedModel != null) {
			highlightedModel.unHighlight();
			highlightedModel = null;
		}
	}
	
	@Override
	public void highlightModel(BlockComponent block) {
		if(block == null) {
			highlightedModel = null;
			return;
		}
		
		highlightedModel = modelMap.get(block);
		highlightedModel.doHighlight();
	}
}
