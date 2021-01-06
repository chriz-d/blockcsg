package controller;

import model.Shape;
/**
 * Class for calculating csg in seperate thread to prevent GUI lockup
 * @author chriz
 *
 */
public class CSGCalculator implements Runnable {

	private Controller controller;
	private Shape shape;
	
	public CSGCalculator(Controller controller, Shape shape) {
		this.controller = controller;
		this.shape = shape;
	}
	
	@Override
	public void run() {
		controller.setcurrentDisplayedObject(shape.generateCSGMesh());
	}

}
