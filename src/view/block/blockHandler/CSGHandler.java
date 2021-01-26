package view.block.blockHandler;

import java.awt.event.MouseEvent;
import java.util.List;

import view.IView;
import view.block.BlockComponent;

/**
 * Event handler for managing CSG related events. Looks into 
 * {@link view.block.blockHandler.HandlerMemory HandlerMemory} for occured events and
 * invokes CSG calculations if necessary.
 * @author chriz
 *
 */
public class CSGHandler extends CustomHandler {
	
	/** Handler memory for getting information from other handlers. */
	private HandlerMemory mem;
	
	public CSGHandler(BlockComponent attachedComponent, IView view, HandlerMemory mem) {
		super(attachedComponent, view);
		this.mem = mem;
	}
	
	/**
	 * Checks if component was snapped to another component, if yes, the csg must be recalculated
	 * for both trees. (If attachedComponent is not equal to oldRoot, meaning component was
	 * disattached). Furthermore hides an element if needed.
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
	
	/** Iterates through list and invokes a csg calculation for each element. */
	private void startInvocation() {
		List<BlockComponent> invokeList = mem.getInvokeList();
		for(BlockComponent block : invokeList) {
			view.getCSGModelManager().invokeCSGCalculation(block);
		}
		invokeList.clear();
	}
}
