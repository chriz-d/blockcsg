package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import javax.swing.JComponent;

import view.BlockComponent.BlockType;

public class OperatorComponent extends BlockComponent {

	private static final long serialVersionUID = -1526119201511001957L;
	
	// Coordinates of connector shape
	final private int shapeConnectorCoordinatesX[] = {0, 25, 75, 100, 75, 25};
	final private int shapeConnectorCoordinatesY[] = {25, 0, 0, 25, 50, 50};
	
	public OperatorComponent(BlockType type) {
		super(type);
		this.setPreferredSize(new Dimension(100, 50));
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.DARK_GRAY);

		GeneralPath path = getGeneralPath();
		g2.fill(path);
		g2.draw(path);
		g2.setColor(Color.WHITE);
		g2.drawString(getType().toString(), 40, 25);
	}
	
	// returns the drawn path of needed shape
		private GeneralPath getGeneralPath() {
			GeneralPath path = new GeneralPath();
			path.moveTo(0, 25);
			for(int i = 0; i < shapeConnectorCoordinatesX.length; i++) {
				path.lineTo(shapeConnectorCoordinatesX[i], shapeConnectorCoordinatesY[i]);
			}
			path.closePath();
			return path;
		}
}
