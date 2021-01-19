package controller;

import com.jme3.math.Vector3f;

import model.CSGModel;
import net.wcomohundro.jme3.csg.CSGShape;
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
