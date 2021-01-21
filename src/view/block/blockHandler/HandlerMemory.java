package view.block.blockHandler;

import java.util.ArrayList;
import java.util.List;

import view.block.BlockComponent;

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
