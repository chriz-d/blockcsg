package view;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import support.Support;
import view.BlockSocket.SocketDir;

// Not able to use MouseAdapter, thanks diamond of death...
public class DragHandler implements MouseListener, MouseMotionListener {
	
	private View view;
	private Component componentToDrag;
	
	private int screenX = 0;
	private int screenY = 0;
	private int componentX = 0;
	private int componentY = 0;
	
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
		componentToDrag.getParent().remove(componentToDrag);
		view.transferPanel.add(componentToDrag);
		if(!isFreshlySpawned) {
			componentToDrag.setLocation(componentToDrag.getX() + view.blockViewPanel.getComponent(0).getWidth(), componentToDrag.getY());
		}
        screenX = e.getXOnScreen();
        screenY = e.getYOnScreen();
        System.out.println("Fired!");
        componentX = componentToDrag.getX();
        componentY = componentToDrag.getY();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(isFreshlySpawned) {
			isFreshlySpawned = false;
		}
		System.out.println("Release!");
		if(!Support.isOutOfBounds(view.frame.getMousePosition(), view.workspacePanel)) {
			componentToDrag.getParent().remove(componentToDrag);
			componentToDrag.setLocation(componentToDrag.getX() - view.blockViewPanel.getComponent(0).getWidth(), componentToDrag.getY());
			System.out.println("Inside!");
			
			// Find closest snapping point and set component position to it if necessary
			snapToClosestBlock();
			
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
	private void snapToClosestBlock() {
		Component[] componentList = view.workspacePanel.getComponents();
		BlockComponent draggedBlock = (BlockComponent) componentToDrag;
		double closestDistance = Double.MAX_VALUE;
		Point closestPoint = null;
		BlockComponent closestBlock = null;
		int idx = 0;
		BlockSocket[] dragBlockSckt = draggedBlock.socketArr;
		// Iterate over sockets of dragged block, over all components and their own sockets
		for(int i = 0; i < dragBlockSckt.length; i++) {
			for(Component blockc : componentList) {
				BlockComponent potClosest = (BlockComponent) blockc;
				BlockSocket[] potClosestScktArr = potClosest.socketArr;
				for(int j = 0; j < potClosest.socketArr.length; j++) {
					if(isValidSocket(dragBlockSckt[i], potClosestScktArr[j])) {
						double distance = Support.getDistance(
								Support.addPoints(dragBlockSckt[i].position, draggedBlock.getLocation()), 
								Support.addPoints(potClosestScktArr[j].position, potClosest.getLocation()));
						if(closestDistance > distance) {
							closestDistance = distance;
							closestPoint = potClosestScktArr[j].position;
							idx = j;
							closestBlock = potClosest;
						}
					}
				}
			}
		}
		// Check distance to found point
		if(closestPoint != null && closestDistance < 25) {
			Point spPos = Support.addPoints(closestPoint, closestBlock.getLocation());
			// Compensate for width or height when snapping to left or top side of block
			if(closestBlock.socketArr[idx].direction == SocketDir.LEFT) {
				spPos = Support.subPoints(spPos, new Point(draggedBlock.getWidth(), 0));
			} else if(closestBlock.socketArr[idx].direction == SocketDir.TOP) {
				spPos = Support.subPoints(spPos, new Point(0, draggedBlock.getHeight()));
			}
			// Add offset vector for precise placement
			spPos = Support.addPoints(spPos, closestBlock.socketArr[idx].offsetVector);
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
