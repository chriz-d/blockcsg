package view.block.blockHandler;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import support.Support;
import view.View;
import view.block.BlockComponent;
/**
 * Adds event handlers to component and enables cloning when clicking on it.
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
		System.out.println("Spawn!");
		// Clone component and place at old components position
        BlockComponent spawnedComp = (BlockComponent) Support.deepCopy(spawnableComponent);
        spawnedComp.setBounds(spawnableComponent.getX(), spawnableComponent.getY(), spawnableComponent.getWidth(), spawnableComponent.getHeight());
        
        Container parent = spawnableComponent.getParent();
        // Get index of old element for inserting clone into same position
        int index = Arrays.asList(parent.getComponents()).indexOf(spawnableComponent);
        parent.remove(spawnableComponent);
        view.getTransferPanel().add(spawnableComponent);

        HandlerManager hm = (HandlerManager) spawnableComponent.getMouseListeners()[0];
        hm.addDragHandler();
        spawnableComponent.addMouseMotionListener(hm);
        parent.add(spawnedComp, index);
        HandlerManager newHm = new HandlerManager(spawnedComp, view);
        newHm.addSpawnHandler();
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
