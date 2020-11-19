package view;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JComponent;

public class BlockComponent extends JComponent {

	private static final long serialVersionUID = -973194356557981053L;
	
	public enum BlockType {
		PrimShape, Operator
	}
	
	public BlockType blockType;
	public Point snapPoints[];
	// Offset vector for snapPoints of SAME index
	public Point snapPointOffsetVector[];
	public boolean snapPointUsed[];
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	} 
}
