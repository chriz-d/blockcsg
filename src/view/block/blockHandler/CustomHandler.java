package view.block.blockHandler;

import java.awt.event.MouseEvent;

import view.View;
import view.block.BlockComponent;

/**
 * Generic template for all handler classes except the {@link view.block.blockHandler.HandlerManager HandlerManager}..
 * @author chriz
 *
 */
public abstract class CustomHandler {
	
	protected View view;
	protected BlockComponent attachedComponent;
	
	public CustomHandler(BlockComponent attachedComponent,View view) {
		this.attachedComponent = attachedComponent;
		this.view = view;
	}
	
	protected abstract void mousePressed(MouseEvent e);

	protected abstract void mouseDragged(MouseEvent e);
	
	protected abstract void mouseReleased(MouseEvent e);
}
