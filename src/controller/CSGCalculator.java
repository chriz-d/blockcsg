package controller;

import model.Shape;
/**
 * Class for calculating csg in seperate thread to prevent GUI lockup
 * @author chriz
 *
 */
public class CSGCalculator implements Runnable {

	private Shape shape;
	
	public CSGCalculator(Shape shape) {
		this.shape = shape;
	}
	
	@Override
	public void run() {
		Controller.getInstance().setcurrentDisplayedObject(shape.generateCSGMesh());
	}

}
