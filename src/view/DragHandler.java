package view;

import java.awt.Component;
import java.awt.Point;
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
			System.out.println("Inside!");
			
			
			
			// Find closest snapping point
			Component list[] = view.workspacePanel.getComponents();
			BlockComponent draggedBlock = (BlockComponent) componentToDrag;
			double closestDistance = Double.MAX_VALUE;
			Point closestPoint = null;
			BlockComponent closestBlock = null;
//			for(int i = 0; i < draggedBlock.snapPoints.length; i++) {
//				for(Component blockc : view.workspacePanel.getComponents()) {
//					BlockComponent block = (BlockComponent) blockc;
//					for(int j = 0; j < block.snapPoints.length; j++) {
//						if(!block.snapPointUsed[j] && block.blockType != draggedBlock.blockType) {
//							if(closestDistance > Support.getDistance(
//									Support.addPoints(draggedBlock.snapPoints[i], draggedBlock.getLocation()), 
//									Support.addPoints(block.snapPoints[j], block.getLocation()))) {
//								closestDistance = Support.getDistance(
//										Support.addPoints(draggedBlock.snapPoints[i], draggedBlock.getLocation()), 
//										Support.addPoints(block.snapPoints[j], block.getLocation()));
//								closestPoint = block.snapPoints[j];
//								closestBlock = block;
//							}
//						}
//					}
//				}
//			}
//			if(closestPoint != null && closestDistance < 25) {
//				Point absolute = Support.addPoints(closestPoint, closestBlock.getLocation());
//				componentToDrag.setBounds(absolute.x, absolute.y, 100, 50);
//			}
			
			view.workspacePanel.add(componentToDrag);
		} else {
			System.out.println("Outside!");
			componentToDrag.getParent().remove(componentToDrag);
			componentToDrag.removeMouseMotionListener(this);
			componentToDrag.removeMouseListener(this);
		}
		view.frame.repaint();
	}
	
}
