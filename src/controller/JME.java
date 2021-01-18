package controller;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jme3.app.SimpleApplication;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.util.SkyFactory;

public class JME extends SimpleApplication {
	
	/** Mesh for jMonkey to display */
	private Queue<Geometry> meshesToAdd;
	private Queue<Geometry> meshesToRemove;
	
	public JME() {
		meshesToAdd = new ConcurrentLinkedQueue<>();
		meshesToRemove = new ConcurrentLinkedQueue<>();
	}
	
	@Override
	public void simpleInitApp() {
		setPauseOnLostFocus(false);
		flyCam.setEnabled(false);
		flyCam.setDragToRotate(true);
		CameraNode camNode = new CameraNode("Camera", cam);
		camNode.setLocalTranslation(0, 0, -8);
		Node node = new Node("pivot");
		node.attachChild(camNode);
		camNode.getCamera().lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
		rootNode.attachChild(node);
		enableCameraControls(node);
	}
	
	/**
	 * Adds camera controls to JMonkeys input manager (Rotation around center 0,0,0)
	 */
	private void enableCameraControls(Node node) {
		inputManager.addMapping("Rotate Left", new MouseAxisTrigger(MouseInput.AXIS_X, true));
		inputManager.addMapping("Rotate Right", new MouseAxisTrigger(MouseInput.AXIS_X, false));
		inputManager.addMapping("Rotate Up", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
		inputManager.addMapping("Rotate Down", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
		inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		JMEKeyListener listener = new JMEKeyListener(getInputManager(), node);
		inputManager.addListener(listener, new String[] {"Rotate Left", "Rotate Right", "Rotate Up", "Rotate Down"});
		inputManager.addListener(listener, new String[] {"Click"});
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		for(Geometry geom : meshesToRemove) {
			rootNode.detachChild(geom);
		}
		meshesToRemove.clear();
		for(Geometry geom : meshesToAdd) {
			rootNode.attachChild(geom);
		}
		meshesToAdd.clear();
		
		super.simpleUpdate(tpf);
	}
	
	public void addToSceneGraph(Geometry geom) {
		meshesToAdd.add(geom);
	}
	
	public void removeFromSceneGraph(Geometry geom) {
		meshesToRemove.add(geom);
	}
	
	public void highlightObject() {
		
	}
}
