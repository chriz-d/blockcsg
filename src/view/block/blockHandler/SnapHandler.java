package view.block.blockHandler;

import java.awt.event.MouseEvent;

import view.block.BlockComponent;

public class SnapHandler implements ICustomHandler {

	/** The component the event handler is attached to. */
	private BlockComponent attachedComponent;
	
	public SnapHandler(BlockComponent attachedComponent) {
		this.attachedComponent = attachedComponent;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
}
