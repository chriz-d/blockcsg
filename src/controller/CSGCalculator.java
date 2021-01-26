package controller;


import model.CSGModel;
/**
 * Class for calculating csg in seperate thread to prevent GUI lockup.
 * After calculation is done the calculated mesh is inserted into scene graph
 * @author chriz
 *
 */
public class CSGCalculator implements Runnable {

	private CSGModel shape;
	private ICSGModelManager modelMan;
	
	public CSGCalculator(CSGModel shape, ICSGModelManager modelMan) {
		this.shape = shape;
		this.modelMan = modelMan;
	}
	
	@Override
	public void run() {
		// Hide mesh because editing scene graph from another is forbidden
		modelMan.undisplayCSGModel(shape.getBlock());
		// Generate CSG recursively
		shape.startCSGGeneration();
		// Display generated mesh
		modelMan.displayCSGModel(shape.getBlock());
	}
	
}
