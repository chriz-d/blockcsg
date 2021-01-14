package controller;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class JMEKeyListener implements AnalogListener, ActionListener {

	private boolean mouseButtonPressed;
	private Node node;
	private Vector2f oldMousePos;
	
	public JMEKeyListener(Node node) {
		this.node = node;
	}
	
	@Override
	public void onAction(String name, boolean keyPressed, float tpf) {
		if(name.equals("Click")) {
			mouseButtonPressed = keyPressed;
			if(keyPressed) {
				JME.getInstance().getInputManager().setCursorVisible(false);
			} else {
				JME.getInstance().getInputManager().setCursorVisible(true);
			}
		}
	}

	@Override
	public void onAnalog(String name, float keyPressed, float tpf) {
		Vector2f newMousePos = JME.getInstance().getInputManager().getCursorPosition();
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
