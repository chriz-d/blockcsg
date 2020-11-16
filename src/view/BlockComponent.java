package view;

import java.awt.Graphics;

import javax.swing.JComponent;

public class BlockComponent extends JComponent {
	
	public enum BlockType {
		CUBE, SPHERE, PYRAMID, CYLINDER, UNION, DIFFERENCE, INTERSECT;
	}

	private static final long serialVersionUID = -973194356557981053L;

	private BlockType type;
	
	
	public BlockComponent(BlockType type) {
		this.type = type;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
	
	public void setDraggable(boolean draggable) {
		if(draggable) {
			// add to event handler
		} else {
			// remove event handler
		}
	}
	
	public BlockType getType() {
		return type;
	}
}
