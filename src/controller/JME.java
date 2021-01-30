package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jme3.app.SimpleApplication;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LightControl;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Dome;

import net.wcomohundro.jme3.csg.shape.CSGCylinder;


/**
 * Manages all jMonkey related things like scene graph and configuration of viewport.
 * @author chriz
 *
 */
public class JME extends SimpleApplication {
	
	/** Queue which meshes should be added to scene graph. Gets processed and emptied every frame. */
	private Queue<Spatial> meshesToAdd;
	
	/** Queue which meshes should be removed from scene graph. Gets processed and emptied every frame. */
	private Queue<Spatial> meshesToRemove;
	
	/** Node where all csg meshes get attached */
	public Node csgNode;
	
	/** Node where drag and rotate arrows are attached */
	public Node interactionNode;
	
	public Geometry selectedObject;
	
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
		
		//Misc Settings
		setDisplayFps(false);
		setDisplayStatView(false);
		viewPort.setBackgroundColor(ColorRGBA.DarkGray);
		
		// Create Grid
		Grid grid = new Grid(20, 20, 1);
		Geometry geom = new Geometry("grid", grid);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.LightGray);
		geom.setLocalTranslation(-9, -1, -9);
		geom.setMaterial(mat);
		geom.setQueueBucket(Bucket.Opaque);
		rootNode.attachChild(geom);
		
		// Create lights and attach them to camera
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
		
		// Init csgNode
		csgNode = new Node();
		rootNode.attachChild(csgNode);
		
		// Init interactionNode
		interactionNode = new Node();
		//rootNode.attachChild(interactionNode);
		
		// Create interactable geometries for translation and rotation
		Geometry xAxis = new Geometry("xAxis", new CSGCylinder(20, 20, 0.1f, 0.3f));
		Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat2.setColor("Color", ColorRGBA.Blue);
		mat2.getAdditionalRenderState().setDepthTest(false);
		mat2.getAdditionalRenderState().setDepthWrite(false);
		Quaternion rot90 = new Quaternion();
		rot90.fromAngleAxis(90 * (FastMath.PI / 180), new Vector3f(0, 1, 0));
		xAxis.setLocalRotation(rot90);
		xAxis.setMaterial(mat2);
		interactionNode.attachChild(xAxis);
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
		inputManager.addMapping("Right Click", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		inputManager.addMapping("Left Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		JMEKeyListener listener = new JMEKeyListener(this, node);
		inputManager.addListener(listener, new String[] {"Rotate Left", "Rotate Right", "Rotate Up", "Rotate Down"});
		inputManager.addListener(listener, new String[] {"Right Click", "Left Click"});
	}
	
	/** 
	 * Gets called every frame and processes the queues of mesh addition 
	 * and removal from scene graph.
	 * @param tpf Time per frame.
	 */
	@Override
	public void simpleUpdate(float tpf) {
		for(Spatial geom : meshesToRemove) {
			csgNode.detachChild(geom);
		}
		meshesToRemove.clear();
		for(Spatial geom : meshesToAdd) {
			csgNode.attachChild(geom);
		}
		meshesToAdd.clear();
		
		super.simpleUpdate(tpf);
	}
	
	/** 
	 * Adds a Geometry to the addition queue.
	 * @param geom Geometry to add.
	 */
	public void addToSceneGraph(Spatial geom) {
		meshesToAdd.add(geom);
	}
	
	/** 
	 * Adds a Geometry to the removal queue.
	 * @param geom Geometry to add.
	 */
	public void removeFromSceneGraph(Spatial geom) {
		meshesToRemove.add(geom);
	}
	
	public boolean isInSceneGraph(Geometry geom) {
		return csgNode.hasChild(geom);
	}
	
	public void setInteraction(Geometry geom) {
		Vector3f pos = geom.getLocalTranslation();
		for(Spatial interactable : interactionNode.getChildren()) {
			interactable.setLocalTranslation(pos);
		}
		addToSceneGraph(interactionNode);
		selectedObject = geom;
	}
	
	public void removeInteraction() {
		removeFromSceneGraph(interactionNode);
		selectedObject = null;
	}
}