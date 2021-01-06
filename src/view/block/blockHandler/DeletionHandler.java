package view.block.blockHandler;

import java.awt.event.MouseEvent;
import java.util.List;

import support.Support;
import view.View;
import view.block.BlockComponent;
/**
 * An event handler responsible for deletion of a block.
 * @author chriz
 *
 */
public class DeletionHandler implements ICustomHandler {

	private View view;
	private BlockComponent attachedComponent;
	
	public DeletionHandler(BlockComponent attachedComponent,View view) {
		this.attachedComponent = attachedComponent;
		this.view = view;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(Support.isOutOfBounds(view.getFrame().getMousePosition(), 
				view.getWorkspacePanel().getBounds())) {
			view.setLastSelected(null);
			attachedComponent.getParent().remove(attachedComponent);
			// delete children aswell
			List<BlockComponent> children = view.getController().getChildren(attachedComponent);
			view.getController().deleteTree(attachedComponent);
			for(BlockComponent child : children) {
				child.getParent().remove(child);
				view.getController().deleteTree(child);
			}
		}
	}

}
