package view.block.blockHandler;

import java.awt.event.MouseEvent;

import view.View;
import view.block.BlockComponent;
/**
 * An event handler for managing tree and 3D model creation within the controllers.
 * @author chriz
 *
 */
public class ControllerHandler extends CustomHandler {

	private HandlerMemory mem;
	
	public ControllerHandler(BlockComponent attachedComponent, View view, HandlerMemory mem) {
		super(attachedComponent, view);
		this.mem = mem;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// If first time placing, create tree for block
		if(!view.getTreeManager().hasTree(attachedComponent)) {
			view.getTreeManager().createTree(attachedComponent);
		}
		if(!view.getCSGModelManager().hasCSGModel(attachedComponent)) {
			view.getCSGModelManager().createCSGModel(attachedComponent);
			view.getCSGModelManager().displayCSGModel(attachedComponent);
		}
		mem.setOldRoot(view.getTreeManager().getRoot(attachedComponent));
		view.getTreeManager().removeFromTree(attachedComponent);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// Unused
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Unused
	}

}
