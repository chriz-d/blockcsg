package controller;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class JMEKeyListener implements AnalogListener, ActionListener {

	private boolean mouseButtonPressed;
	private Node node;
	
	public JMEKeyListener(Node node) {
		this.node = node;
	}
	
	@Override
	public void onAction(String name, boolean keyPressed, float tpf) {
		if(name.equals("Click")) {
			mouseButtonPressed = keyPressed;
			if(keyPressed) {
			}
		}
	}

	@Override
	public void onAnalog(String name, float keyPressed, float tpf) {
		Quaternion newRotation = null;
		if(name.equals("Rotate Left") && mouseButtonPressed) {
			newRotation = new Quaternion();
			newRotation.fromAngleAxis(FastMath.HALF_PI/80, Vector3f.UNIT_Y);
		} else if(name.equals("Rotate Right") && mouseButtonPressed) {
			newRotation = new Quaternion();
			newRotation.fromAngleAxis(-FastMath.HALF_PI/80, Vector3f.UNIT_Y);
		} else if(name.equals("Rotate Up") && mouseButtonPressed) {
			newRotation = new Quaternion();
			newRotation.fromAngleAxis(FastMath.HALF_PI/80, Vector3f.UNIT_X);
		} else if(name.equals("Rotate Down") && mouseButtonPressed) {
			newRotation = new Quaternion();
			newRotation.fromAngleAxis(-FastMath.HALF_PI/80, Vector3f.UNIT_X);
		}
		if(newRotation != null) {
			node.setLocalRotation(node.getLocalRotation().mult(newRotation));
		}
	}

}
