package controller;

import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * Listens to key inputs. In this case, clicking on the viewport for camera rotation.
 * @author chriz
 *
 */
public class JMEKeyListener implements AnalogListener, ActionListener {

	/** Reference to the input manager of JME. */
	private InputManager inputMan;
	
	/** 
	 * Camera node for rotation. This is not the camera itself, but instead a node at
	 * (0, 0, 0) of which the camera is a child of.
	 */
	private Node node;
	
	/** Flag for showing if the mouse is currently pressed. */
	private boolean mouseButtonPressed;
	
	/** Mouse position of last frame, used for computing rotation speed. */
	private Vector2f oldMousePos;
	
	public JMEKeyListener(InputManager inputMan, Node node) {
		this.inputMan = inputMan;
		this.node = node;
	}
	
	@Override
	public void onAction(String name, boolean keyPressed, float tpf) {
		if(name.equals("Click")) {
			mouseButtonPressed = keyPressed;
			if(keyPressed) {
				inputMan.setCursorVisible(false);
			} else {
				inputMan.setCursorVisible(true);
			}
		}
	}

	@Override
	public void onAnalog(String name, float keyPressed, float tpf) {
		Vector2f newMousePos = inputMan.getCursorPosition();
		if(oldMousePos == null) {
			oldMousePos = newMousePos;
		}
		Vector2f distance = oldMousePos.subtract(newMousePos);
		oldMousePos = newMousePos.clone();
		if(mouseButtonPressed) {
			node.rotate(distance.y / 100, distance.x / 100, 0);
		}
	}

}
