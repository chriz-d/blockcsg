package controller;

import com.jme3.app.SimpleApplication;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

import net.wcomohundro.jme3.csg.CSGShape;

public class JME extends SimpleApplication {
	
	/** Time passed since last update for jMonkey */
	private float updateTime = 0;
	
	/** Mesh for jMonkey to display */
	private CSGShape currentDisplayedObject;
	private CSGShape lastDisplayedObject;
	
	private static JME instance;
	
	public static JME getInstance() {
		if(instance == null) {
			instance = new JME();
		}
		return instance;
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
		JMEKeyListener listener = new JMEKeyListener(node);
		inputManager.addListener(listener, new String[] {"Rotate Left", "Rotate Right", "Rotate Up", "Rotate Down"});
		inputManager.addListener(listener, new String[] {"Click"});
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		updateTime += tpf;
		if(updateTime > 0.2) {
			updateTime = 0;
			if(currentDisplayedObject != null && !rootNode.hasChild(currentDisplayedObject)) {
				if(lastDisplayedObject != null) {
					rootNode.detachChild(lastDisplayedObject);
				}
				rootNode.attachChild(currentDisplayedObject);
				lastDisplayedObject = currentDisplayedObject;
			} else if(currentDisplayedObject == null && currentDisplayedObject != lastDisplayedObject) {
				rootNode.detachChild(lastDisplayedObject);
			}
		}
		super.simpleUpdate(tpf);
	}
	
	/** Used by thread for setting variable */
	public void setcurrentDisplayedObject(CSGShape shape) {
		currentDisplayedObject = shape;
	}
	
	public Mesh getCurrentDisplayedMesh() {
		return currentDisplayedObject.getMesh();
	}
}
