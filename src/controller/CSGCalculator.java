package controller;


import model.ICSGModel;
/**
 * Class for calculating csg in seperate thread to prevent GUI lockup.
 * After calculation is done the calculated mesh is inserted into scene graph
 * @author chriz
 *
 */
public class CSGCalculator implements Runnable {

	private ICSGModel shape;
	private ICSGModelManager modelMan;
	
	public CSGCalculator(ICSGModel shape, ICSGModelManager modelMan) {
		this.shape = shape;
		this.modelMan = modelMan;
	}
	
	@Override
	public void run() {
		// Hide mesh because editing scene graph from another thread is forbidden
		modelMan.undisplayCSGModel(shape.getBlock());
		// Generate CSG recursively
		shape.generateCSGMesh();
		// Display generated mesh
		modelMan.displayCSGModel(shape.getBlock());
	}
	
}
