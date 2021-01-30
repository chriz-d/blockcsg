package view.block.blockHandler;

import java.awt.event.MouseEvent;
import java.util.List;

import view.IView;
import view.block.BlockComponent;

/**
 * Event handler for highlighting blocks in the workspace.
 * @author chriz
 *
 */
public class HighlighterHandler extends CustomHandler {
	
	public HighlighterHandler(BlockComponent attachedComponent, IView view) {
		super(attachedComponent, view);
	}

	@Override
	protected void mousePressed(MouseEvent e) {
//		if(e.getButton() != MouseEvent.BUTTON1) {
//			return;
//		}
		List<BlockComponent> children = view.getTreeManager().getChildren(attachedComponent);
		children.add(attachedComponent);
		view.unhighlightBlocks();
		view.getCSGModelManager().unhighlightModel();
		view.highlightBlocks(children);
		view.getCSGModelManager().highlightModel(attachedComponent);
		
	}

	@Override
	protected void mouseDragged(MouseEvent e) {
	}

	@Override
	protected void mouseReleased(MouseEvent e) {
		BlockComponent root = view.getTreeManager().getRoot(attachedComponent);
		List<BlockComponent> children = view.getTreeManager().getChildren(root);
		children.add(root);
		view.unhighlightBlocks();
		view.getCSGModelManager().unhighlightModel();
		view.highlightBlocks(children);
		view.getCSGModelManager().highlightModel(root);
	}
}
