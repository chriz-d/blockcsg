package controller;

import java.util.HashMap;
import java.util.Map;

import com.jme3.renderer.queue.RenderQueue.Bucket;

import model.CSGModel;
import model.ICSGModel;
import model.SizeMeasurements;
import support.Support;
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
	private Map<BlockComponent, ICSGModel> modelMap;
	
	/** Reference to the currently highlighted model in scenegraph */
	private ICSGModel highlightedModel;
	
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
		ICSGModel model = modelMap.get(block);
		jme.addToSceneGraph(model.getCSG());
	}
	
	/**
	 * Adds a CSGModel to the scene graph removal queue, resulting in the model being removed
	 * from scene graph.
	 * @param block Block of which the CSGModel should be added to scene graph removal queue.
	 */
	@Override
	public void undisplayCSGModel(BlockComponent block) {
		ICSGModel model = modelMap.get(block);
		jme.removeFromSceneGraph(model.getCSG());
	}
	
	@Override
	public ICSGModel getCSGModel(BlockComponent block) {
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
		ICSGModel model = modelMap.get(block);
		model.resizeModel(size);
	}
	
	@Override
	public void unhighlightModel() {
		if(highlightedModel != null) {
			highlightedModel.getCSG().setMaterial(Support.getTransparentMaterial(jme.getAssetManager()));
			highlightedModel.getCSG().setQueueBucket(Bucket.Translucent);
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
		highlightedModel.getCSG().setMaterial(Support.getHighlightMaterial(jme.getAssetManager()));
		highlightedModel.getCSG().setQueueBucket(Bucket.Opaque);
	}

	@Override
	public ICSGModel getHighlightedModel() {
		return highlightedModel;
	}
}
