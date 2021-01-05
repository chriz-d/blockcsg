package view.block.blockHandler;

import java.awt.Component;
import java.awt.Container;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;

import controller.Controller;
import support.Support;
import view.View;
import view.block.BlockComponent;
import view.block.BlockSocket;
import view.block.BlockSocket.SocketType;

/**
 * Adds event handlers for dragging and dropping, snapping to fitting blocks.
 * @author chriz
 *
 */
public class DragHandler implements ICustomHandler {
	
	private View view;
	/** The component the event handler is attached to. */
	private BlockComponent componentToDrag;
	
	/** Helper variable for dragging */
	private int screenX = 0;
	/** Helper variable for dragging */
	private int screenY = 0;
	
	/** Flag for initial offset when spawning, needed for compensating the offset of the drawer. */
	private boolean isFreshlySpawned;
	
	public DragHandler(BlockComponent componentToDrag, View view) {
		this.componentToDrag = componentToDrag;
		this.view = view;
		isFreshlySpawned = true;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		int deltaX = e.getXOnScreen() - screenX;
		int deltaY = e.getYOnScreen() - screenY;
		// Move component
		componentToDrag.setLocation(componentToDrag.getX() + deltaX, componentToDrag.getY() + deltaY);
		// Move children
		List<BlockComponent> children = view.getController().getChildren(componentToDrag);
		if(children != null) {
			for(BlockComponent child : children) {
				child.setLocation(child.getX() + deltaX, child.getY() + deltaY);
			}
		}
		screenX = e.getXOnScreen();
		screenY = e.getYOnScreen();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// If first time placing, create tree for block
		if(!view.getController().hasTree(componentToDrag)) {
			view.getController().createTree(componentToDrag);
		}
		
		// Selection color
		view.unHighlightBlocks();
		view.highlightBlocks(componentToDrag);

		// Disconnect all sockets
		componentToDrag.disconnectSockets();
		
		// Resize components
		// Remove from tree
		view.resizeTree(componentToDrag, true);
		
		// Repeat resize, this time only for dragged opponent and children
		view.resizeTree(componentToDrag, false);
		
		// Switch layers
		componentToDrag.getParent().remove(componentToDrag);
		view.getTransferPanel().add(componentToDrag);
		List<BlockComponent> children = view.getController().getChildren(componentToDrag);
		for(BlockComponent child : children) {
			child.getParent().remove(child);
			view.getTransferPanel().add(child);
			if(!isFreshlySpawned) {
				child.setLocation(child.getX() + view.getBlockViewPanel().getComponent(0).getWidth(), child.getY());
			}
		}
		if(!isFreshlySpawned) {
			componentToDrag.setLocation(componentToDrag.getX() + view.getBlockViewPanel().getComponent(0).getWidth(), componentToDrag.getY());
		} else {
			// Snap component to mouse pos, needed because scrollpane changes Y coordinate offset
			Point mousePos = MouseInfo.getPointerInfo().getLocation();
			SwingUtilities.convertPointFromScreen(mousePos, view.getTransferPanel());
			// Minus 30 for middle of block and not edge
			componentToDrag.setLocation(componentToDrag.getX(), (int)mousePos.getY() - 30);
			
			// Add block to shapeMap, so it get's displayed while dragging
			view.getController().addShape(componentToDrag);
		}
		
		// Display mesh in jMonkey
		view.getController().setDisplayedMesh(componentToDrag);

		// Save position for dragging
		screenX = e.getXOnScreen();
		screenY = e.getYOnScreen();
		view.getFrame().repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(isFreshlySpawned) {
			isFreshlySpawned = false;
		}
	
		// Check if mouse is inside workspace Panel
		if(!Support.isOutOfBounds(view.getFrame().getMousePosition(), 
				view.getWorkspacePanel().getBounds())) {
			
			// Correct for drawer width
			componentToDrag.setLocation(
					componentToDrag.getX() - view.getBlockViewPanel().getComponent(0).getWidth(), 
					componentToDrag.getY());
			List<BlockComponent> children = view.getController().getChildren(componentToDrag);
			for(BlockComponent child : children) {
				child.setLocation(
						child.getX() - view.getBlockViewPanel().getComponent(0).getWidth(), 
						child.getY());
			}
			
			// Find closest snapping point and set component position to it if necessary
			snapToClosestBlock(componentToDrag);
			
			// Check if one of children viable for snapping
			for(BlockComponent child : children) {
				snapToClosestBlock(child);
			}
			
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
			for(BlockComponent child : children) {
				view.getWorkspacePanel().add(child);
				view.getWorkspacePanel().setComponentZOrder(child, 0);
			}
			
		} else { // Delete component, cause it's outside
			view.setLastSelected(null);
			componentToDrag.getParent().remove(componentToDrag);
			// delete children aswell
			List<BlockComponent> children = view.getController().getChildren(componentToDrag);
			view.getController().deleteTree(componentToDrag);
			for(BlockComponent child : children) {
				child.getParent().remove(child);
				view.getController().deleteTree(child);
			}
		}
		view.getFrame().repaint();
	}
	
	/**
	 * Iterates through all blocks and their sockets in workspace and measures distance
	 * to socket of given block. If distance is close enough, the given block gets moved
	 * to the socket ("snapped").
	 * @param componentToSnap
	 */
	private void snapToClosestBlock(BlockComponent componentToSnap) {
		// Remove all children of component to drag from list, or else it snaps to itself!
		List<Component> componentList = 
				new ArrayList<Component>(Arrays.asList(view.getWorkspacePanel().getComponents()));
		List<BlockComponent> children = view.getController().getChildren(componentToSnap);
		for(BlockComponent e : children) {
			componentList.remove((Component)e);
		}
		double closestDistance = Double.MAX_VALUE;
		Point closestPoint = null;
		BlockComponent closestBlock = null;
		int dragSocketIndex = 0;
		int closestSocketIndex = 0;
		BlockSocket[] dragBlockSckt = componentToSnap.socketArr;
		// Iterate over sockets of dragged block, over all components and their own sockets
		// and find closest snap point
		for(int i = 0; i < dragBlockSckt.length; i++) {
			for(Component blockc : componentList) {
				BlockComponent potClosest = (BlockComponent) blockc;
				for(int j = 0; j < potClosest.socketArr.length; j++) {
					if(BlockSocket.isValidSocket(dragBlockSckt[i], potClosest.socketArr[j])) {
						double distance = Support.getDistance(
								Support.addPoints(dragBlockSckt[i].position, componentToSnap.getLocation()), 
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
			BlockSocket toDragSocket = componentToSnap.socketArr[dragSocketIndex];
			BlockSocket closestSocket = closestBlock.socketArr[closestSocketIndex];
			componentToSnap.connectSocket(toDragSocket, closestSocket);
			closestBlock.connectSocket(closestSocket, toDragSocket);
			
			
			// Finally, calculate coordinates for placement
			// Convert found point to workspace coordinates
			Point spPos = Support.addPoints(closestPoint, closestBlock.getLocation());
			spPos = Support.subPoints(spPos, dragBlockSckt[dragSocketIndex].position);
			
			// Calculate delta for all nodes repositioning
			Point deltaPos = Support.subPoints(spPos, componentToSnap.getLocation());
			componentToSnap.setLocation(spPos.x, spPos.y);
			
			// Get all nodes and repos
			Controller controller = view.getController();
			List<BlockComponent> allNodes = controller.getChildren(controller.getRoot(componentToSnap));
			allNodes.add(controller.getRoot(componentToSnap));
			for(BlockComponent node : allNodes) {
				if(!node.equals(componentToSnap)) {
					node.setLocation((int)(node.getX() + deltaPos.getX()),(int) (node.getY() + deltaPos.getY()));
				}
			}
			view.unHighlightBlocks();
			// Add to model
			if(toDragSocket.type == SocketType.RECTANGLE_PLUG) {
				view.getController().addToTree(closestBlock, componentToSnap, toDragSocket.direction);
				view.highlightBlocks(componentToDrag);
				view.getController().setDisplayedMesh(componentToDrag);
			} else {
				view.getController().addToTree(componentToSnap, closestBlock, closestSocket.direction);
				view.highlightBlocks(closestBlock);
				view.getController().setDisplayedMesh(closestBlock);
			}

			// Resize tree
			view.resizeTree(componentToSnap, false);
		}
	}
}
