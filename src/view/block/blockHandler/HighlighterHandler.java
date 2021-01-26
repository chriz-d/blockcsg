package view.block.blockHandler;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import view.View;
import view.block.BlockComponent;

/**
 * Event handler for highlighting blocks in the workspace.
 * @author chriz
 *
 */
public class HighlighterHandler extends CustomHandler {
	
	public HighlighterHandler(BlockComponent attachedComponent, View view) {
		super(attachedComponent, view);
	}

	@Override
	protected void mousePressed(MouseEvent e) {
		List<BlockComponent> children = view.getTreeManager().getChildren(attachedComponent);
		children.add(attachedComponent);
		unhighlightBlocks();
		unhighlightModel();
		highlightBlocks(children);
		highlightModel(attachedComponent);
		view.getFrame().repaint();
	}

	@Override
	protected void mouseDragged(MouseEvent e) {
	}

	@Override
	protected void mouseReleased(MouseEvent e) {
		BlockComponent root = view.getTreeManager().getRoot(attachedComponent);
		List<BlockComponent> children = view.getTreeManager().getChildren(root);
		children.add(root);
		unhighlightBlocks();
		unhighlightModel();
		highlightBlocks(children);
		highlightModel(root);
		view.getFrame().repaint();
	}
	
	private void highlightBlocks(List<BlockComponent> blocksToHighlight) {
		for(BlockComponent block : blocksToHighlight) {
			block.color += 10000;
		}
		view.setHighlightedBlocks(blocksToHighlight);
	}
	
	private void unhighlightBlocks() {
		for(BlockComponent block : view.getHighlightedBlocks()) {
			block.color -= 10000;
		}
	}
	
	private void unhighlightModel() {
		view.getCSGModelManager().unhighlightModel();
	}
	
	private void highlightModel(BlockComponent block) {
		view.getCSGModelManager().highlightModel(block);
	}
}
