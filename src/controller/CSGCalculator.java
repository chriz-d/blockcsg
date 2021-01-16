package controller;

import model.CSGModel;
import net.wcomohundro.jme3.csg.CSGShape;
/**
 * Class for calculating csg in seperate thread to prevent GUI lockup
 * @author chriz
 *
 */
public class CSGCalculator implements Runnable {

	private CSGModel shape;
	
	public CSGCalculator(CSGModel shape) {
		this.shape = shape;
	}
	
	@Override
	public void run() {
		CSGShape result = shape.generateCSGMesh();
		if(result != null) {
			JME.getInstance().addToSceneGraph(result);
		}
	}

}
