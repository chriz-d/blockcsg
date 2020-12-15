package view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

import support.Support;
import view.BlockSocket.SocketType;

// Not able to use MouseAdapter, thanks diamond of death...
public class DragHandler implements MouseListener, MouseMotionListener {
	
	private View view;
	private BlockComponent componentToDrag;
	
	// Is the component below componentToDrag, if this is not null
	// all events need to passed to this instead
	private Component componentBelow;
	
	private int screenX = 0;
	private int screenY = 0;
	private int componentX = 0;
	private int componentY = 0;
	
	// Flag for setting component position correctly after inital spawning
	private boolean isFreshlySpawned;
	
	// Flag for fixing dragging inside bounds of block, but still outside of blockshape
	private boolean ignoreClick;
	
	public DragHandler(BlockComponent componentToDrag, View view) {
		this.componentToDrag = componentToDrag;
		this.view = view;
		isFreshlySpawned = true;
		ignoreClick = false;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// Pass on event (if a lower component exists)
		if(componentBelow != null) {
			componentBelow.dispatchEvent(SwingUtilities.convertMouseEvent(
					e.getComponent(), e, componentBelow));
		} else if(!ignoreClick) { // Check if click actually is inside shape and not just bounding box
			int deltaX = e.getXOnScreen() - screenX;
			int deltaY = e.getYOnScreen() - screenY;
			
			componentToDrag.setLocation(componentX + deltaX, componentY + deltaY);
		}
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
		// Check if click actually is inside shape and not just bounding box
		if(!componentToDrag.contains(e.getPoint())) {
			// Pass on event (if a lower component exists)
			componentBelow = getLowerComponent(e.getPoint());
			ignoreClick = true;
			if(componentBelow != null) {
				componentBelow.dispatchEvent(SwingUtilities.convertMouseEvent(
						e.getComponent(), e, componentBelow));
			}
		} else {
			// Selection color
			if(view.lastSelected != componentToDrag) {
				if(view.lastSelected != null) {
					view.lastSelected.color -= 10000; 
				}
				view.lastSelected = componentToDrag;
				view.lastSelected.color += 10000;
			}
			
			// Disconnect all sockets
			for(BlockSocket socket : componentToDrag.socketArr) {
				if(socket.connectedSocket != null) {
					socket.connectedSocket.isUsed = false;
					socket.connectedSocket.connectedSocket = null;
				}
				socket.isUsed = false;
				socket.connectedSocket = null;
			}
			// Resize components
			// Remove from tree
			view.resizeTree(componentToDrag, true);
			view.resizeTree(componentToDrag, false);
			
			// Switch layers
			componentToDrag.getParent().remove(componentToDrag);
			view.getTransferPanel().add(componentToDrag);
			if(!isFreshlySpawned) {
				componentToDrag.setLocation(componentToDrag.getX() + view.getBlockViewPanel().getComponent(0).getWidth(), componentToDrag.getY());
			}
			// Save position for dragging
			screenX = e.getXOnScreen();
			screenY = e.getYOnScreen();
			componentX = componentToDrag.getX();
			componentY = componentToDrag.getY();
			view.getFrame().repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(isFreshlySpawned) {
			isFreshlySpawned = false;
		}
		
		// Check if click actually is inside shape and not just bounding box
		if(componentBelow != null ) {
			// Pass on event (if a lower component exists)
				componentBelow.dispatchEvent(SwingUtilities.convertMouseEvent(
						e.getComponent(), e, componentBelow));
				componentBelow = null;
		} else if(!ignoreClick) {
			// Check if mouse is inside workspace Panel
			if(!Support.isOutOfBounds(view.getFrame().getMousePosition(), 
					view.getWorkspacePanel().getBounds())) {
				componentToDrag.setLocation(
						componentToDrag.getX() - view.getBlockViewPanel().getComponent(0).getWidth(), 
						componentToDrag.getY());
				// If first time placing, create tree for block
				if(!view.getController().hasTree(componentToDrag)) {
					view.getController().createTree(componentToDrag);
				}
				
				// Find closest snapping point and set component position to it if necessary
				snapToClosestBlock();
				
				// fix z ordering of other elements
				Component[] components = view.getWorkspacePanel().getComponents();
				if(components.length > 1) {
					for(int i = 1; i < components.length; i++) {
						view.getWorkspacePanel().setComponentZOrder(components[i], i);
					}
				}
				// Finally add to panel and give last element highest z order
				componentToDrag.getParent().remove(componentToDrag);
				view.getWorkspacePanel().add(componentToDrag);
				view.getWorkspacePanel().setComponentZOrder(componentToDrag, 0);
			} else { // Delete component, cause it's outside
				componentToDrag.getParent().remove(componentToDrag);
				componentToDrag.removeMouseMotionListener(this);
				componentToDrag.removeMouseListener(this);
			}
			view.getFrame().repaint();
		}
		ignoreClick = false;
	}
	
	// Searches all components for closest snapping point and calculates position
	private void snapToClosestBlock() {
		Component[] componentList = view.getWorkspacePanel().getComponents();
		double closestDistance = Double.MAX_VALUE;
		Point closestPoint = null;
		BlockComponent closestBlock = null;
		int dragSocketIndex = 0;
		int closestSocketIndex = 0;
		BlockSocket[] dragBlockSckt = componentToDrag.socketArr;
		// Iterate over sockets of dragged block, over all components and their own sockets
		// and find closest snap point
		for(int i = 0; i < dragBlockSckt.length; i++) {
			for(Component blockc : componentList) {
				BlockComponent potClosest = (BlockComponent) blockc;
				for(int j = 0; j < potClosest.socketArr.length; j++) {
					if(isValidSocket(dragBlockSckt[i], potClosest.socketArr[j])) {
						double distance = Support.getDistance(
								Support.addPoints(dragBlockSckt[i].position, componentToDrag.getLocation()), 
								Support.addPoints(potClosest.socketArr[j].position, potClosest.getLocation()));
						if(closestDistance > distance) {
							closestDistance = distance;
							closestPoint = potClosest.socketArr[j].position;
							closestBlock = potClosest;
							dragSocketIndex = i;
							closestSocketIndex = j;
						}
					}
				}
			}
		}
		// Check distance to found point
		if(closestPoint != null && closestDistance < 25) {
			// update socketState
			// TODO: Close other socket opposite of socket being closed
			BlockSocket toDragSocket = componentToDrag.socketArr[dragSocketIndex];
			BlockSocket closestSocket = closestBlock.socketArr[closestSocketIndex];
			toDragSocket.isUsed = true;
			closestSocket.isUsed = true;
			toDragSocket.connectedSocket = closestSocket;
			closestSocket.connectedSocket = toDragSocket;
		
			// Add to model
			if(toDragSocket.type == SocketType.RECTANGLE_PLUG) {
				view.getController().addToTree(closestBlock, componentToDrag, toDragSocket.direction);
			} else {
				view.getController().addToTree(componentToDrag, closestBlock, closestSocket.direction);
			}
			
			
			// Finally, calculate coordinates for placement
			// Convert found point to workspace coordinates
			Point spPos = Support.addPoints(closestPoint, closestBlock.getLocation());
			
			spPos = Support.subPoints(spPos, dragBlockSckt[dragSocketIndex].position);
			componentToDrag.setLocation(spPos.x, spPos.y);

			view.resizeTree(componentToDrag, false);
		}
	}
	
	// returns if given sockets are compatible
	private boolean isValidSocket(BlockSocket s1, BlockSocket s2) {
		boolean isNotUsed = !s1.isUsed && !s2.isUsed;
		boolean isFittingSocket = BlockSocket.isFitting(s1.type, s2.type);
		boolean isNotOnSameSide = s1.direction != s2.direction;
		
		return isNotUsed && isFittingSocket && isNotOnSameSide;
	}
	
	// Gets lower component with lowest z index
	private Component getLowerComponent(Point p) {
		Container parent = componentToDrag.getParent();
		Component[] allComps = parent.getComponents();
		Component result = null;
		Point pWorkSpace = Support.addPoints(componentToDrag.getLocation(), p);
		int zIndex = Integer.MAX_VALUE;
		
		// Search for component under componentToDrag and pick the one with lowest z index
		for(Component e : allComps) {
			Point pe = Support.subPoints(pWorkSpace, e.getLocation());
			if(!e.equals((Component)componentToDrag) && e.contains(pe)) {
				if(parent.getComponentZOrder(e) < zIndex) {
					result = e;
					zIndex = parent.getComponentZOrder(e);
				}
			}
		}
		return result;
	}
}
