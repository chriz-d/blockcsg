package view.block.blockHandler;

import java.awt.event.MouseEvent;
import java.util.List;

import view.View;
import view.block.BlockComponent;

/**
 * An event handler responsible for clicking and dragging BlockComponents around.
 * @author chriz
 *
 */
public class DragHandler implements ICustomHandler {
	
	private View view;
	/** The component the event handler is attached to. */
	private BlockComponent attachedComponent;
	
	/** Helper variable for dragging */
	private int screenX = 0;
	/** Helper variable for dragging */
	private int screenY = 0;
	
	public DragHandler(BlockComponent componentToDrag, View view) {
		this.attachedComponent = componentToDrag;
		this.view = view;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		int deltaX = e.getXOnScreen() - screenX;
		int deltaY = e.getYOnScreen() - screenY;
		// Move component
		attachedComponent.setLocation(attachedComponent.getX() + deltaX, attachedComponent.getY() + deltaY);
		// Move children
		List<BlockComponent> children = view.getTreeManager().getChildren(attachedComponent);
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
		// Selection color
		view.unHighlightBlocks();
		view.highlightBlocks(attachedComponent);
		
		// Recalculate prev connected CSG without this component
		view.getCSGModelManager().displayCSGModel(attachedComponent);

		// Save position for dragging
		screenX = e.getXOnScreen();
		screenY = e.getYOnScreen();
		
		// Disconnect all sockets
		attachedComponent.disconnectSockets();
		
		view.getFrame().repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
}
