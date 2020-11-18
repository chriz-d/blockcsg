package view;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import support.Support;

// Not able to use MouseAdapter, thanks diamond of death...
public class DragHandler implements MouseListener, MouseMotionListener {
	
	private View view;
	private Component componentToDrag;
	
	private int screenX = 0;
	private int screenY = 0;
	private int myX = 0;
	private int myY = 0;
	
	private boolean isFreshlySpawned;
	
	public DragHandler(Component componentToDrag, View view) {
		this.componentToDrag = componentToDrag;
		this.view = view;
		this.isFreshlySpawned = true;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
        int deltaX = e.getXOnScreen() - screenX;
        int deltaY = e.getYOnScreen() - screenY;

        componentToDrag.setLocation(myX + deltaX, myY + deltaY);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent e) {
		componentToDrag.getParent().remove(componentToDrag);
		view.transferPanel.add(componentToDrag);
		if(!isFreshlySpawned) {
			componentToDrag.setBounds(componentToDrag.getX() + view.blockViewPanel.getComponent(0).getWidth(), componentToDrag.getY(), 100, 50);
		}
        screenX = e.getXOnScreen();
        screenY = e.getYOnScreen();
        System.out.println("Fired!");
        myX = componentToDrag.getX();
        myY = componentToDrag.getY();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(isFreshlySpawned) {
			isFreshlySpawned = false;
		}
		System.out.println("Release!");
		if(!Support.isOutOfBounds(view.frame.getMousePosition(), view.workspacePanel)) {
			componentToDrag.getParent().remove(componentToDrag);
			componentToDrag.setBounds(componentToDrag.getX() - view.blockViewPanel.getComponent(0).getWidth(), componentToDrag.getY(), 100, 50);
			view.workspacePanel.add(componentToDrag);
			System.out.println("Inside!");
		} else {
			System.out.println("Outside!");
			componentToDrag.getParent().remove(componentToDrag);
			componentToDrag.removeMouseMotionListener(this);
			componentToDrag.removeMouseListener(this);
		}
		view.frame.repaint();
	}
	
}
