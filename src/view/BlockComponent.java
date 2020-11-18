package view;

import java.awt.Graphics;

import javax.swing.JComponent;

public class BlockComponent extends JComponent {

	private static final long serialVersionUID = -973194356557981053L;
	
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
}
