package view.block.blockHandler;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import support.Support;
import view.View;
import view.block.BlockComponent;
/**
 * An event handler enabling the component to be cloned when clicking. Initializes the other
 * event handlers.
 * @author chriz
 *
 */
public class SpawnHandler extends CustomHandler {
	
	public SpawnHandler(BlockComponent attachedComponent, View view) {
		super(attachedComponent, view);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// Clone component and place at old components position
        BlockComponent spawnedComp = (BlockComponent) Support.deepCopy(attachedComponent);
        spawnedComp.setBounds(attachedComponent.getX(), attachedComponent.getY(), attachedComponent.getWidth(), attachedComponent.getHeight());
        
        Container parent = attachedComponent.getParent();
        // Get index of old element for inserting clone into same position
        int index = Arrays.asList(parent.getComponents()).indexOf(attachedComponent);
        parent.remove(attachedComponent);
        view.getTransferPanel().add(attachedComponent);

        HandlerMemory mem = new HandlerMemory();
        HandlerManager hm = (HandlerManager) attachedComponent.getMouseListeners()[0];
        hm.addHandler(new PopUpHandler(attachedComponent, view));
        hm.addHandler(new ControllerHandler(attachedComponent, view, mem));
        hm.addHandler(new LayerSwitchHandler(attachedComponent, view));
        hm.addHandler(new DragHandler(attachedComponent, view));
        hm.addHandler(new SnapHandler(attachedComponent, view));
        hm.addHandler(new ResizeHandler(attachedComponent, view, mem));
        hm.addHandler(new HighlighterHandler(attachedComponent, view));
        hm.addHandler(new DeletionHandler(attachedComponent, view));
        
        attachedComponent.addMouseMotionListener(hm);
        parent.add(spawnedComp, index);
        HandlerManager newHm = new HandlerManager(spawnedComp, view);
        newHm.addHandler(new SpawnHandler(spawnedComp, view));
        spawnedComp.addMouseListener(newHm);
        
		view.getFrame().repaint();
		// Delegate initial mouse press for drag operation
		hm.removeSpawnHandler();
		hm.mousePressed(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
