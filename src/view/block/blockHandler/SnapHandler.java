package view.block.blockHandler;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import controller.TreeManager;
import support.Support;
import view.View;
import view.block.BlockComponent;
import view.block.BlockSocket;
import view.block.BlockSocket.SocketType;
/**
 * An event handler responsible for snapping blocks together when dropped in range.
 * @author chriz
 *
 */
public class SnapHandler implements ICustomHandler {

	private View view;
	private BlockComponent attachedComponent;
	
	public SnapHandler(BlockComponent attachedComponent,View view) {
		this.attachedComponent = attachedComponent;
		this.view = view;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		List<BlockComponent> children = view.getTreeManager().getChildren(attachedComponent);
		// Find closest snapping point and set component position to it if necessary
		snapToClosestBlock(attachedComponent);
		
		// Check if one of children viable for snapping
		for(BlockComponent child : children) {
			snapToClosestBlock(child);
		}
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
		List<BlockComponent> children = view.getTreeManager().getChildren(componentToSnap);
		for(BlockComponent e : children) {
			componentList.remove(e);
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
			TreeManager controller = view.getTreeManager();
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
				view.getTreeManager().addToTree(closestBlock, componentToSnap, toDragSocket.direction);
				view.highlightBlocks(attachedComponent);
				view.getCSGModelManager().undisplayCSGModel(closestBlock);
				view.getCSGModelManager().invokeCSGCalculation(attachedComponent);
			} else {
				view.getTreeManager().addToTree(componentToSnap, closestBlock, closestSocket.direction);
				BlockComponent root = view.getTreeManager().getRoot(closestBlock);
				view.highlightBlocks(root);
				view.getCSGModelManager().undisplayCSGModel(attachedComponent);
				view.getCSGModelManager().invokeCSGCalculation(root);
			}
		}
	}
}
