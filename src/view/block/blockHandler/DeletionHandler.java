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
public class DeletionHandler extends CustomHandler {

	public DeletionHandler(BlockComponent attachedComponent, View view) {
		super(attachedComponent, view);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(Support.isOutOfBounds(view.getFrame().getMousePosition(), 
				view.getWorkspacePanel().getBounds())) {
			
			attachedComponent.getParent().remove(attachedComponent);
			// delete children aswell
			List<BlockComponent> children = view.getTreeManager().getChildren(attachedComponent);
			view.getTreeManager().deleteTree(attachedComponent);
			view.getCSGModelManager().undisplayCSGModel(attachedComponent);
			view.getCSGModelManager().deleteCSGModel(attachedComponent);
			for(BlockComponent child : children) {
				child.getParent().remove(child);
				view.getTreeManager().deleteTree(child);
				view.getCSGModelManager().undisplayCSGModel(child);
				view.getCSGModelManager().deleteCSGModel(child);
			}
		}
	}

}
