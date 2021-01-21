package view.block.blockHandler;

import java.awt.event.MouseEvent;
import java.util.List;

import view.View;
import view.block.BlockComponent;

public class CSGHandler extends CustomHandler {

	private HandlerMemory mem;
	
	public CSGHandler(BlockComponent attachedComponent, View view, HandlerMemory mem) {
		super(attachedComponent, view);
		this.mem = mem;
	}
	
	/**
	 * Checks if component was snapped to another component, if yes, the csg must be recalculated
	 * for both trees. 
	 */
	@Override
	protected void mousePressed(MouseEvent e) {
		if(mem.getElementToHide() != null) {
			view.getCSGModelManager().undisplayCSGModel(mem.getElementToHide());
			mem.setElementToHide(null);
		}
		if(!attachedComponent.equals(mem.getOldRoot())) {
			view.getCSGModelManager().invokeCSGCalculation(attachedComponent);
			view.getCSGModelManager().invokeCSGCalculation(mem.getOldRoot());
		}
	}

	@Override
	protected void mouseDragged(MouseEvent e) {
	}

	@Override
	protected void mouseReleased(MouseEvent e) {
		startInvocation();
		if(mem.getElementToHide() != null) {
			view.getCSGModelManager().undisplayCSGModel(mem.getElementToHide());
			mem.setElementToHide(null);
		}
	}
	
	private void startInvocation() {
		List<BlockComponent> invokeList = mem.getInvokeList();
		for(BlockComponent block : invokeList) {
			view.getCSGModelManager().invokeCSGCalculation(block);
		}
		invokeList.clear();
	}
}
