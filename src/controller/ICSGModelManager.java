package controller;

import model.CSGModel;
import model.SizeMeasurements;
import view.block.BlockComponent;

public interface ICSGModelManager {

	/**
	 * Creates a new CSGModel using given block.
	 * @param block Block to create a CSGModel of.
	 */
	void createCSGModel(BlockComponent block);

	/**
	 * Deletes a CSGModel.
	 * @param block Block of which to delete the CSGModel.
	 */
	void deleteCSGModel(BlockComponent block);

	boolean hasCSGModel(BlockComponent block);

	/**
	 * Adds a CSGModel to the scene graph queue.
	 * @param block Block of which the CSGModel should be added to scene graph queue.
	 */
	void displayCSGModel(BlockComponent block);

	/**
	 * Adds a CSGModel to the scene graph removal queue, resulting in the model being removed
	 * from scene graph.
	 * @param block Block of which the CSGModel should be added to scene graph removal queue.
	 */
	void undisplayCSGModel(BlockComponent block);

	CSGModel getCSGModel(BlockComponent block);

	/**
	 * Starts a thread and recomputes the CSG of a given block.
	 * @param block Block of which to recompute the CSG.
	 */
	void invokeCSGCalculation(BlockComponent block);

	void resizeCSGModel(BlockComponent block, SizeMeasurements size);

	void unhighlightModel();

	void highlightModel(BlockComponent block);

}