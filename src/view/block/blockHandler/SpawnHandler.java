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
public class SpawnHandler implements ICustomHandler {
	
	private BlockComponent spawnableComponent;
	private View view;
	
	public SpawnHandler(BlockComponent spawnableComponent, View view) {
		this.spawnableComponent = spawnableComponent;
		this.view = view;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// Clone component and place at old components position
        BlockComponent spawnedComp = (BlockComponent) Support.deepCopy(spawnableComponent);
        spawnedComp.setBounds(spawnableComponent.getX(), spawnableComponent.getY(), spawnableComponent.getWidth(), spawnableComponent.getHeight());
        
        Container parent = spawnableComponent.getParent();
        // Get index of old element for inserting clone into same position
        int index = Arrays.asList(parent.getComponents()).indexOf(spawnableComponent);
        parent.remove(spawnableComponent);
        view.getTransferPanel().add(spawnableComponent);

        HandlerManager hm = (HandlerManager) spawnableComponent.getMouseListeners()[0];
        hm.addHandler(new PopUpHandler(spawnableComponent, view));
        hm.addHandler(new ControllerHandler(spawnableComponent, view));
        hm.addHandler(new LayerSwitchHandler(spawnableComponent, view));
        hm.addHandler(new DragHandler(spawnableComponent, view));
        hm.addHandler(new SnapHandler(spawnableComponent, view));
        hm.addHandler(new ResizeHandler(spawnableComponent, view));
        hm.addHandler(new DeletionHandler(spawnableComponent, view));
        
        spawnableComponent.addMouseMotionListener(hm);
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
