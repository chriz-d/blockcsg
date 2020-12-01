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
	private BlockComponent componentToDrag;
	
	private int screenX = 0;
	private int screenY = 0;
	private int componentX = 0;
	private int componentY = 0;
	
	private boolean isFreshlySpawned;
	
	public DragHandler(BlockComponent componentToDrag, View view) {
		this.componentToDrag = componentToDrag;
		this.view = view;
		this.isFreshlySpawned = true;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
        int deltaX = e.getXOnScreen() - screenX;
        int deltaY = e.getYOnScreen() - screenY;

        componentToDrag.setLocation(componentX + deltaX, componentY + deltaY);
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
		//System.out.println(componentToDrag.contains(e.getPoint()));
		// Selection color
		if(view.lastSelected != componentToDrag) {
			if(view.lastSelected != null) {
				view.lastSelected.color -= 10000; 
			}
			view.lastSelected = componentToDrag;
			view.lastSelected.color += 10000;
		}
		
		// Switch layers
		componentToDrag.getParent().remove(componentToDrag);
		view.transferPanel.add(componentToDrag);
		if(!isFreshlySpawned) {
			componentToDrag.setLocation(componentToDrag.getX() + view.blockViewPanel.getComponent(0).getWidth(), componentToDrag.getY());
		}
		// Save position for dragging
        screenX = e.getXOnScreen();
        screenY = e.getYOnScreen();
        componentX = componentToDrag.getX();
        componentY = componentToDrag.getY();
        view.frame.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(isFreshlySpawned) {
			isFreshlySpawned = false;
		}
		// Check if mouse is inside workspace Panel
		if(!Support.isOutOfBounds(view.frame.getMousePosition(), view.workspacePanel.getBounds())) {
			componentToDrag.getParent().remove(componentToDrag);
			componentToDrag.setLocation(componentToDrag.getX() - view.blockViewPanel.getComponent(0).getWidth(), componentToDrag.getY());
			
			// Find closest snapping point and set component position to it if necessary
			snapToClosestBlock();
			
			// fix z ordering of other elements
			Component[] components = view.workspacePanel.getComponents();
			if(components.length > 1) {
				for(int i = 0; i < components.length; i++) {
					view.workspacePanel.setComponentZOrder(components[i], i);
				}
			}
			// Finally add to panel and give last element highest z order
			view.workspacePanel.add(componentToDrag);
			view.workspacePanel.setComponentZOrder(componentToDrag, 0);
		} else { // Delete component, cause it's outside
			componentToDrag.getParent().remove(componentToDrag);
			componentToDrag.removeMouseMotionListener(this);
			componentToDrag.removeMouseListener(this);
		}
		view.frame.repaint();
	}
	
	// Searches all components for closest snapping point and calculates position
	private void snapToClosestBlock() {
		Component[] componentList = view.workspacePanel.getComponents();
		double closestDistance = Double.MAX_VALUE;
		Point closestPoint = null;
		BlockComponent closestBlock = null;
		int idx = 0;
		int idx2 = 0;
		BlockSocket[] dragBlockSckt = componentToDrag.socketArr;
		// Iterate over sockets of dragged block, over all components and their own sockets
		for(int i = 0; i < dragBlockSckt.length; i++) {
			for(Component blockc : componentList) {
				BlockComponent potClosest = (BlockComponent) blockc;
				BlockSocket[] potClosestScktArr = potClosest.socketArr;
				for(int j = 0; j < potClosest.socketArr.length; j++) {
					if(isValidSocket(dragBlockSckt[i], potClosestScktArr[j])) {
						double distance = Support.getDistance(
								Support.addPoints(dragBlockSckt[i].position, componentToDrag.getLocation()), 
								Support.addPoints(potClosestScktArr[j].position, potClosest.getLocation()));
						if(closestDistance > distance) {
							closestDistance = distance;
							closestPoint = potClosestScktArr[j].position;
							idx = j;
							closestBlock = potClosest;
							idx2 = i;
						}
					}
				}
			}
		}
		// Check distance to found point
		if(closestPoint != null && closestDistance < 25) {
			// Convert found point to workspace coordinates
			Point spPos = Support.addPoints(closestPoint, closestBlock.getLocation());

			spPos = Support.subPoints(spPos, dragBlockSckt[idx2].position);
			componentToDrag.setLocation(spPos.x, spPos.y);
		}
	}
	
	// returns if given sockets are compatible
	private boolean isValidSocket(BlockSocket s1, BlockSocket s2) {
		boolean isNotUsed = !s1.isUsed && !s2.isUsed;
		boolean isFittingSocket = BlockSocket.isFitting(s1.type, s2.type);
		boolean isNotOnSameSide = s1.direction != s2.direction;
		
		return isNotUsed && isFittingSocket && isNotOnSameSide;
	}
	
}
