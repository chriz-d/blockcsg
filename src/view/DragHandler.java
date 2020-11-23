package view;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import support.Support;
import view.SnapPoint.SocketPos;

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
			componentToDrag.setBounds(componentToDrag.getX() + view.blockViewPanel.getComponent(0).getWidth(), componentToDrag.getY(), componentToDrag.getWidth(), componentToDrag.getHeight());
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
			componentToDrag.setBounds(componentToDrag.getX() - view.blockViewPanel.getComponent(0).getWidth(), componentToDrag.getY(), componentToDrag.getWidth(), componentToDrag.getHeight());
			System.out.println("Inside!");
			
			
			
			// Find closest snapping point
			Point pointToSnap = getClosestSnappingPoint();
			if(pointToSnap != null) {
				componentToDrag.setBounds(pointToSnap.x, pointToSnap.y, componentToDrag.getWidth(), componentToDrag.getHeight());
			}
			
			
			view.workspacePanel.add(componentToDrag);
		} else {
			System.out.println("Outside!");
			componentToDrag.getParent().remove(componentToDrag);
			componentToDrag.removeMouseMotionListener(this);
			componentToDrag.removeMouseListener(this);
		}
		view.frame.repaint();
	}
	
	// Searches all components for closest snapping point and calculates position
	private Point getClosestSnappingPoint() {
		Component[] componentList = view.workspacePanel.getComponents();
		BlockComponent draggedBlock = (BlockComponent) componentToDrag;
		double closestDistance = Double.MAX_VALUE;
		Point closestPoint = null;
		BlockComponent closestBlock = null;
		int idx = 0;
		SnapPoint[] draggedBlockSP = draggedBlock.snapPointArr;
		// Iterate over sockets of dragged block, over all components and their own sockets
		for(int i = 0; i < draggedBlockSP.length; i++) {
			for(Component blockc : componentList) {
				BlockComponent potClosest = (BlockComponent) blockc;
				SnapPoint[] potClosestSP = potClosest.snapPointArr;
				for(int j = 0; j < potClosest.snapPointArr.length; j++) {
					if(isValidSocket(draggedBlockSP[i], potClosestSP[j])) {
						double distance = Support.getDistance(
								Support.addPoints(draggedBlockSP[i].position, draggedBlock.getLocation()), 
								Support.addPoints(potClosestSP[j].position, potClosest.getLocation()));
						if(closestDistance > distance) {
							closestDistance = distance;
							closestPoint = potClosestSP[j].position;
							idx = j;
							closestBlock = potClosest;
						}
					}
				}
			}
		}
		if(closestPoint != null && closestDistance < 25) {
			Point spPos = Support.addPoints(closestPoint, closestBlock.getLocation());
			if(closestBlock.snapPointArr[idx].pos == SocketPos.LEFT) {
				spPos = Support.subPoints(spPos, new Point(draggedBlock.getWidth(), 0));
			} else if(closestBlock.snapPointArr[idx].pos == SocketPos.TOP) {
				spPos = Support.subPoints(spPos, new Point(0, draggedBlock.getHeight()));
			}
			return Support.addPoints(spPos, closestBlock.snapPointArr[idx].offsetVector);
		} else {
			return null;
		}
	}
	
	private boolean isValidSocket(SnapPoint s1, SnapPoint s2) {
		boolean isNotUsed = !s1.isUsed && !s2.isUsed;
		boolean isFittingSocket = SnapPoint.isFitting(s1.type, s2.type);
		boolean isNotOnSameSide = s1.pos != s2.pos;
		
		return isNotUsed && isFittingSocket && isNotOnSameSide;
	}
	
}
