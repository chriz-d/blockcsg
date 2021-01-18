package view.block.blockHandler;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.SwingUtilities;

import view.View;
import view.block.BlockComponent;
/**
 * An event handler for switching between swing layers to enable a smooth drag operation.
 * @author chriz
 *
 */
public class LayerSwitchHandler implements ICustomHandler {

	private View view;
	private BlockComponent attachedComponent;
	
	/** Flag for initial offset when spawning, needed for compensating the offset of the drawer. */
	private boolean isFreshlySpawned;
	
	public LayerSwitchHandler(BlockComponent attachedComponent,View view) {
		this.attachedComponent = attachedComponent;
		this.view = view;
		isFreshlySpawned = true;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Switch layers
		attachedComponent.getParent().remove(attachedComponent);
		view.getTransferPanel().add(attachedComponent);
		List<BlockComponent> children = view.getController().getChildren(attachedComponent);
		for(BlockComponent child : children) {
			child.getParent().remove(child);
			view.getTransferPanel().add(child);
			if(!isFreshlySpawned) {
				child.setLocation(child.getX() + view.getBlockViewPanel().getComponent(0).getWidth(), child.getY());
			}
		}
		
		if(!isFreshlySpawned) {
			attachedComponent.setLocation(attachedComponent.getX() + view.getBlockViewPanel().getComponent(0).getWidth(), attachedComponent.getY());
		} else {
			// Snap component to mouse pos, needed because scrollpane changes Y coordinate offset
			Point mousePos = MouseInfo.getPointerInfo().getLocation();
			SwingUtilities.convertPointFromScreen(mousePos, view.getTransferPanel());
			// Minus 30 for middle of block and not edge
			attachedComponent.setLocation(attachedComponent.getX(), (int)mousePos.getY() - 30);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(isFreshlySpawned) {
			isFreshlySpawned = false;
		}
		// Correct for drawer width
		attachedComponent.setLocation(
				attachedComponent.getX() - view.getBlockViewPanel().getComponent(0).getWidth(), 
				attachedComponent.getY());
		List<BlockComponent> children = view.getController().getChildren(attachedComponent);
		for(BlockComponent child : children) {
			child.setLocation(
					child.getX() - view.getBlockViewPanel().getComponent(0).getWidth(), 
					child.getY());
		}
		// fix z ordering of other elements
		Component[] components = view.getWorkspacePanel().getComponents();
		if(components.length > 1) {
			for(int i = 1; i < components.length; i++) {
				view.getWorkspacePanel().setComponentZOrder(components[i], i);
			}
		}
		// Finally add to panel and give last element highest z order
		attachedComponent.getParent().remove(attachedComponent);
		view.getWorkspacePanel().add(attachedComponent);
		view.getWorkspacePanel().setComponentZOrder(attachedComponent, 0);
		for(BlockComponent child : children) {
			view.getWorkspacePanel().add(child);
			view.getWorkspacePanel().setComponentZOrder(child, 0);
		}
		
		view.getFrame().repaint();
	}
	
}
