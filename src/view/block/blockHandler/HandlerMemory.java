package view.block.blockHandler;

import java.util.ArrayList;
import java.util.List;

import view.block.BlockComponent;

/**
 * Class for storing information for other handlers to use. For example if DragHandler
 * disconnects a component from its tree and ResizeHandler needs a reference to the old root.
 * @author chriz
 *
 */
public class HandlerMemory {
	
	private BlockComponent oldRoot;
	private BlockComponent elementToHide;

	private List<BlockComponent> invokeList;
	
	public HandlerMemory() {
		invokeList = new ArrayList<>();
	}

	public BlockComponent getOldRoot() {
		return oldRoot;
	}

	public void setOldRoot(BlockComponent oldRoot) {
		this.oldRoot = oldRoot;
	}
	
	public void addElementToInvokeCSG(BlockComponent block) {
		invokeList.add(block);
	}
	
	public List<BlockComponent> getInvokeList() {
		return invokeList;
	}

	public BlockComponent getElementToHide() {
		return elementToHide;
	}
	
	public void setElementToHide(BlockComponent elementToHide) {
		this.elementToHide = elementToHide;
	}
}
