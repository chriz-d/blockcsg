package controller;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jme3.app.SimpleApplication;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.LightControl;
import com.jme3.scene.debug.Grid;

/**
 * Manages all jMonkey related things like scene graphand configuration of viewport.
 * @author chriz
 *
 */
public class JME extends SimpleApplication {
	
	/** Queue which meshes should be added to scene graph. Gets processed and emptied every frame. */
	private Queue<Geometry> meshesToAdd;
	
	/** Queue which meshes should be removed from scene graph. Gets processed and emptied every frame. */
	private Queue<Geometry> meshesToRemove;
	
	public JME() {
		meshesToAdd = new ConcurrentLinkedQueue<>();
		meshesToRemove = new ConcurrentLinkedQueue<>();
	}
	
	/** 
	 * Initializes the JME. Configs camera and prepares viewport with grid. 
	 */
	@Override
	public void simpleInitApp() {
		// Camera settings
		setPauseOnLostFocus(false);
		flyCam.setEnabled(false);
		CameraNode camNode = new CameraNode("Camera", cam);
		camNode.setLocalTranslation(0, 0, -8);
		Node node = new Node("pivot");
		node.attachChild(camNode);
		camNode.getCamera().lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
		rootNode.attachChild(node);
		enableCameraControls(node);
		
		viewPort.setBackgroundColor(ColorRGBA.DarkGray);
		
		// Create Grid
		Grid grid = new Grid(20, 20, 1);
		Geometry geom = new Geometry("grid", grid);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.LightGray);
		geom.setLocalTranslation(-9, -1, -9);
		geom.setMaterial(mat);
		rootNode.attachChild(geom);
		
		// Create lights
		PointLight light = new PointLight(new Vector3f(0, 0, -10), ColorRGBA.White, 50f);
		PointLight light2 = new PointLight(new Vector3f(-10, 10, 0), ColorRGBA.White, 50f);
		PointLight light3 = new PointLight(new Vector3f(10, 10, 0), ColorRGBA.White, 50f);
		rootNode.addLight(light);
		rootNode.addLight(light2);
		rootNode.addLight(light3);
		Node lightNode = new Node("light");
		Node lightNode2 = new Node("light2");
		Node lightNode3 = new Node("light3");
		LightControl lightcontrol = new LightControl(light);
		LightControl lightcontrol2 = new LightControl(light2);
		LightControl lightcontrol3 = new LightControl(light3);
		lightNode.addControl(lightcontrol);
		lightNode2.addControl(lightcontrol2);
		lightNode3.addControl(lightcontrol3);
		lightNode.setLocalTranslation(new Vector3f(0, 0, -10));
		lightNode2.setLocalTranslation(new Vector3f(-10, 10, 0));
		lightNode3.setLocalTranslation(new Vector3f(10, 10, 0));
		node.attachChild(lightNode);
		node.attachChild(lightNode2);
		node.attachChild(lightNode3);
	}
	
	/**
	 * Adds camera controls to JMonkeys input manager (Rotation around center 0,0,0).
	 * @param node Node the camera is attached to.
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
	
	/** 
	 * Gets called every frame and processes the queues of mesh addition 
	 * and removal from scene graph.
	 * @param tpf Time per frame.
	 */
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
	
	/** 
	 * Adds a Geometry to the addition queue.
	 * @param geom Geometry to add.
	 */
	public void addToSceneGraph(Geometry geom) {
		meshesToAdd.add(geom);
	}
	
	/** 
	 * Adds a Geometry to the removal queue.
	 * @param geom Geometry to add.
	 */
	public void removeFromSceneGraph(Geometry geom) {
		meshesToRemove.add(geom);
	}
	
	public boolean isInSceneGraph(Geometry geom) {
		return rootNode.hasChild(geom);
	}
	
	public void highlightObject() {
		
	}
}
