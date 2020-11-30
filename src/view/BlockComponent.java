package view;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

import javax.swing.JComponent;

public class BlockComponent extends JComponent {

	private static final long serialVersionUID = -973194356557981053L;
	
	public enum BlockType {
		PrimShape, Operator, CSG
	}
	// Position of corners for paint()
	public int[] blockCornerCoorX;
	public int[] blockCornerCoorY;
	
	public String label;
	public int labelHeight;
	public int color;
	public BlockType blockType;
	public BlockSocket[] socketArr;
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		// Enable Anti-Aliasing for text
		g2.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		// Draw shape
		GeneralPath path = getGeneralPath();
		g2.setColor(new Color(color));
		g2.fill(path);
		g2.draw(path);
		
		// Draw black outline
		g2.setColor(Color.BLACK);
		g2.draw(path);
		
		// Draw snap points
		g2.setColor(Color.BLUE);
		for(BlockSocket e : socketArr) {
			g2.drawRect(e.position.x, e.position.y, 1, 1);
		}
		
		// Draw Label centered
		g2.setColor(Color.WHITE);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int x = (int) ((getPreferredSize().getWidth() - metrics.getStringBounds(label, g).getWidth()) / 2);
		g2.drawString(label, x, labelHeight);
	}
	
	// returns the drawn path of needed shape
	public GeneralPath getGeneralPath() {
		GeneralPath path = new GeneralPath();
		path.moveTo(blockCornerCoorX[0], blockCornerCoorY[0]);
		for(int i = 0; i < blockCornerCoorX.length; i++) {
			path.lineTo(blockCornerCoorX[i], blockCornerCoorY[i]);
		}
		path.closePath();
		return path;
	}
}
