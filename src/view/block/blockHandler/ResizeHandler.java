package view.block.blockHandler;

import java.awt.event.MouseEvent;

import view.View;
import view.block.BlockComponent;
/**
 * An Event handler responsible for resizing the blocks of a tree, when a given size is reached.
 * @author chriz
 *
 */
public class ResizeHandler implements ICustomHandler {
	
	private View view;
	private BlockComponent attachedComponent;
	
	public ResizeHandler(BlockComponent attachedComponent, View view) {
		this.attachedComponent = attachedComponent;
		this.view = view;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Resize components
		// Remove from tree
		view.resizeTree(attachedComponent, true);
		
		// Repeat resize, this time only for dragged opponent and children
		view.resizeTree(attachedComponent, false);
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Resize tree
		view.resizeTree(attachedComponent, false);
		
	}

}
