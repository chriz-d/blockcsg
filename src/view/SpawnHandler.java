package view;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JComponent;

import support.Support;

public class SpawnHandler extends MouseAdapter {
	
	private Component spawnableComponent;
	private View view;
	
	public SpawnHandler(Component spawnableComponent, View view) {
		this.spawnableComponent = spawnableComponent;
		this.view = view;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("Spawn!");
		// Clone component and place at old components position
        JComponent spawnedComp = (JComponent) Support.deepCopy(spawnableComponent);
        spawnedComp.setBounds(spawnableComponent.getX(), spawnableComponent.getY(), spawnableComponent.getWidth(), spawnableComponent.getHeight());
        
        Container parent = spawnableComponent.getParent();
        // Get index of old element for inserting clone into same position
        int index = Arrays.asList(parent.getComponents()).indexOf(spawnableComponent);
        parent.remove(spawnableComponent);
        view.transferPanel.add(spawnableComponent);
//        addDragHandler(component);
        DragHandler dragger = new DragHandler(spawnableComponent, view);
        spawnableComponent.addMouseListener(dragger);
        spawnableComponent.addMouseMotionListener(dragger);
        parent.add(spawnedComp, index);
        spawnedComp.addMouseListener(new SpawnHandler(spawnedComp, view));
        
		view.frame.repaint();
		// Delegate initial mouse press for drag operation
		spawnableComponent.getMouseListeners()[1].mousePressed(e);
		removeBlockSpawnHandler(spawnableComponent);
	}
	
	private void removeBlockSpawnHandler(Component component) {
		component.removeMouseListener(component.getMouseListeners()[0]);
	}
}
