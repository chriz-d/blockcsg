package view.block.blockHandler;

import java.awt.event.MouseEvent;

import view.View;
import view.block.BlockComponent;
/**
 * An event handler for managing tree creation within the controller.
 * @author chriz
 *
 */
public class ControllerHandler implements ICustomHandler {

	private View view;
	private BlockComponent attachedComponent;
	
	public ControllerHandler(BlockComponent attachedComponent, View view) {
		this.attachedComponent = attachedComponent;
		this.view = view;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// If first time placing, create tree for block
		if(!view.getController().hasTree(attachedComponent)) {
			view.getController().createTree(attachedComponent);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
