package controller;


import model.CSGModel;
/**
 * Class for calculating csg in seperate thread to prevent GUI lockup
 * @author chriz
 *
 */
public class CSGCalculator implements Runnable {

	private CSGModel shape;
	private CSGModelManager modelMan;
	
	public CSGCalculator(CSGModel shape, CSGModelManager modelMan) {
		this.shape = shape;
		this.modelMan = modelMan;
	}
	
	@Override
	public void run() {
		modelMan.undisplayCSGModel(shape.getBlock());
		shape.startCSGGeneration();
		modelMan.displayCSGModel(shape.getBlock());
		//JME.getInstance().addToSceneGraph(shape.getCSG());
	}
	
}
