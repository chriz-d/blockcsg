package controller;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

public class JMEKeyListener implements AnalogListener, ActionListener {

	private boolean mouseButtonPressed;
	private Camera cam;
	
	public JMEKeyListener(Camera cam) {
		this.cam = cam;
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
		if(name.equals("Rotate Left") && mouseButtonPressed) {
			Quaternion leftRotation = new Quaternion();
			leftRotation.fromAngleAxis(FastMath.HALF_PI/80, Vector3f.UNIT_Y);
			Vector3f rotationResult = leftRotation.mult(cam.getLocation());
			cam.setLocation(rotationResult);
			cam.lookAt(new Vector3f(0,0,0), new Vector3f(0,1,0));
		} else if(name.equals("Rotate Right") && mouseButtonPressed) {
			Quaternion rightRotation = new Quaternion();
			rightRotation.fromAngleAxis(-FastMath.HALF_PI/80, Vector3f.UNIT_Y);
			Vector3f rotationResult = rightRotation.mult(cam.getLocation());
			cam.setLocation(rotationResult);
			cam.lookAt(new Vector3f(0,0,0), new Vector3f(0,1,0));
		} else if(name.equals("Rotate Up") && mouseButtonPressed) {
			Quaternion upRotation = new Quaternion();
			Quaternion upRotation2 = new Quaternion();
			upRotation.fromAngleAxis(-FastMath.HALF_PI/80, Vector3f.UNIT_X);
			upRotation2.fromAngleAxis(-FastMath.HALF_PI/80, Vector3f.UNIT_Z);
			Vector3f rotationResult = upRotation.mult(cam.getLocation());
			rotationResult = upRotation2.mult(rotationResult);
			cam.setLocation(rotationResult);
			cam.lookAt(new Vector3f(0,0,0), new Vector3f(0,1,0));
		} else if(name.equals("Rotate Down") && mouseButtonPressed) {
			Quaternion downRotation = new Quaternion();
			Quaternion downRotation2 = new Quaternion();
			downRotation.fromAngleAxis(FastMath.HALF_PI/80, Vector3f.UNIT_X);
			downRotation2.fromAngleAxis(FastMath.HALF_PI/80, Vector3f.UNIT_Z);
			Vector3f rotationResult = downRotation.mult(cam.getLocation());
			rotationResult = downRotation2.mult(rotationResult);
			cam.setLocation(rotationResult);
			cam.lookAt(new Vector3f(0,0,0), new Vector3f(0,1,0));
		}
	}

}
