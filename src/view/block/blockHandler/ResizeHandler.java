package view.block.blockHandler;

import java.awt.event.MouseEvent;
import java.util.List;

import controller.TreeManager;
import view.View;
import view.block.BlockComponent;
import view.block.OperatorBlock;
/**
 * An Event handler responsible for resizing the blocks of a tree, when a given size is reached.
 * @author chriz
 *
 */
public class ResizeHandler extends CustomHandler {
	
	private HandlerMemory mem;
	
	public ResizeHandler(BlockComponent attachedComponent, View view, HandlerMemory mem) {
		super(attachedComponent, view);
		this.mem = mem;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Resize components
		// Remove from tree
		BlockComponent root = mem.getOldRoot();
		view.getTreeManager().removeFromTree(attachedComponent);
		resizeTree(root);
		// Repeat resize, this time only for dragged opponent and children
		resizeTree(attachedComponent);
		//view.getCSGModelManager().invokeCSGCalculation(attachedComponent);
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Resize tree
		resizeTree(attachedComponent);
	}

	/**
	 * Resizes the operatorblocks of a tree.
	 * @param block Block if which tree gets resized.
	 * @param doDelete Flag for deleting the specified block beforehand.
	 */
	public void resizeTree(BlockComponent block) {
		TreeManager controller =  view.getTreeManager();
		BlockComponent root = controller.getRoot(block);
		// Get every node in tree
		List<BlockComponent> allNodes = controller.getChildren(root);
		allNodes.add(root);
		// Iterate over list and fix width of each component and translate children
		for(BlockComponent e : allNodes) {
			if(e instanceof OperatorBlock) {
				int width = ((OperatorBlock) e).correctWidth(controller.getDepth(e));
				// Translate children to the left (<--)
				if(controller.getLeft(e) != null) {
					List<BlockComponent> childrenToMoveLeft = controller.getChildren(controller.getLeft(e));
					childrenToMoveLeft.add(controller.getLeft(e));
					for(BlockComponent f : childrenToMoveLeft) {
						f.setLocation((int)f.getLocation().getX() - width, (int)f.getLocation().getY());
					}
				}
				// Translate children to the right (-->)
				if(controller.getRight(e) != null) {
					List<BlockComponent> childrenToMoveRight = controller.getChildren(controller.getRight(e));
					childrenToMoveRight.add(controller.getRight(e));
					for(BlockComponent f : childrenToMoveRight) {
						f.setLocation((int)f.getLocation().getX() + width, (int)f.getLocation().getY());
					}
				}
			}
		}
	}
}
