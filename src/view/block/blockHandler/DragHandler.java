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
public class DragHandler extends CustomHandler {
	
	/** Helper variable for dragging */
	private int screenX = 0;
	/** Helper variable for dragging */
	private int screenY = 0;
	
	public DragHandler(BlockComponent attachedComponent, View view) {
		super(attachedComponent, view);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Recalculate prev connected CSG without this component
		//view.getCSGModelManager().displayCSGModel(attachedComponent);
		
		// Save position for dragging
		screenX = e.getXOnScreen();
		screenY = e.getYOnScreen();
		
		view.getFrame().repaint();
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
	public void mouseReleased(MouseEvent e) {
		
	}
}
