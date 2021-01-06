package view.block.blockHandler;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

import support.Support;
import view.View;
import view.block.BlockComponent;

/** 
 * Class for managing all event handlers. Needed for oddly shaped blocks, so events not belonging to
 * component can get passed on.
 * @author chriz
 *
 */
public class HandlerManager implements MouseListener, MouseMotionListener {
	
	/** Component his handler is attached to. */
	private BlockComponent attachedComponent;
	
	/**
	 *  Reference to a component below componentToDrag. If this is not null
	 * all events from componentToDrag need to be passed onto this one. 
	 */
	private Component componentBelow;
	
	/** Flag for ignoring events */
	private boolean ignoreAction;

	
	private View view;
	
	/** List of all active event handlers */
	private List<ICustomHandler> handlers;

	public HandlerManager(BlockComponent attachedComponent, View view) {
		this.attachedComponent = attachedComponent;
		handlers = new ArrayList<ICustomHandler>();
		this.view = view;
		ignoreAction = false;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// No iterator used here due to possible recursive access.
		if(attachedComponent.contains(e.getPoint())) {
			for(int i = 0; i < handlers.size(); i++) {
				handlers.get(i).mousePressed(e);
			}
		} else {
			componentBelow = getLowerComponent(e.getPoint());
			if(componentBelow != null) {
				componentBelow.dispatchEvent(SwingUtilities.convertMouseEvent(
						e.getComponent(), e, componentBelow));
			} else {
				ignoreAction = true;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(ignoreAction) {
			return;
		}
		
		// Check if component below is meant
		if(componentBelow == null) {
			for(ICustomHandler handler : handlers) {
				handler.mouseDragged(e);
			}
		} else {
			componentBelow.dispatchEvent(SwingUtilities.convertMouseEvent(
					e.getComponent(), e, componentBelow));
		}
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if(ignoreAction) {
			ignoreAction = false;
			return;
		}
		
		// Check if component below is meant
		if(componentBelow == null) {
			for(ICustomHandler handler : handlers) {
				handler.mouseReleased(e);
			}
		} else {
			componentBelow.dispatchEvent(SwingUtilities.convertMouseEvent(
					e.getComponent(), e, componentBelow));
			componentBelow = null;
		}
	}

	/**
	 * Searches for component below point and component to drag
	 * @param p
	 * @return
	 */
	private Component getLowerComponent(Point p) {
		Container parent = attachedComponent.getParent();
		Component[] allComps = parent.getComponents();
		Component result = null;
		Point pWorkSpace = Support.addPoints(attachedComponent.getLocation(), p);
		int zIndex = Integer.MAX_VALUE;
		
		// Search for component under componentToDrag and pick the one with lowest z index
		for(Component e : allComps) {
			Point pe = Support.subPoints(pWorkSpace, e.getLocation());
			if(!e.equals(attachedComponent) && e.contains(pe)) {
				if(parent.getComponentZOrder(e) < zIndex) {
					result = e;
					zIndex = parent.getComponentZOrder(e);
				}
			}
		}
		return result;
	}
	
	public void addHandler(ICustomHandler handler) {
		handlers.add(handler);
	}
	
	/** Just removes spawnhandler */
	public void removeSpawnHandler() {
		for(int i = 0; i < handlers.size(); i++) {
			if(handlers.get(i) instanceof SpawnHandler) {
				handlers.remove(i);
			}
		}
	}
	
	// Dead weight
	@Override
	public void mouseMoved(MouseEvent e) {}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
}
