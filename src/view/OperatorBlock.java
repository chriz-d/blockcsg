package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

import support.Support;
import view.BlockComponent.BlockType;

public class OperatorBlock extends BlockComponent {
	
	public enum OperatorBlockType {
		UNION, DIFFERENCE, INTERSECT;
	}
	
	private static final long serialVersionUID = -1526119201511001957L;
	
	private OperatorBlockType operatorBlockType;
	
	// Coordinates of connector shape
	final private int shapeConnectorCoordinatesX[] = {10, 90, 90, 95, 95, 99, 99, 95, 95, 90, 90, 10, 10,  5,  5,  0,  0,  5,  5, 10, 10};
	final private int shapeConnectorCoordinatesY[] = { 0,  0, 20, 20, 15, 15, 35, 35, 30, 30, 49, 49, 30, 30, 35, 35, 15, 15, 20, 20, 0};
	
	public OperatorBlock(OperatorBlockType operatorBlockType) {
		this.operatorBlockType = operatorBlockType;
		blockType = BlockType.Operator;
		snapPoints = new Point[]{new Point(18, 25), new  Point(82, 25)};
		snapPointUsed = new boolean[] {false, false};
		this.setMinimumSize(new Dimension(100, 50));
		this.setPreferredSize(new Dimension(100, 50));
		this.setMaximumSize(new Dimension(100, 50));
	}
	
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
		g2.setColor(Color.DARK_GRAY);
		g2.fill(path);
		g2.draw(path);
		// Draw black outline
		g2.setColor(Color.BLACK);
		g2.draw(path);
		g2.setColor(Color.WHITE);
		g2.drawString(Support.capitalizeNormal(getType().toString()), 30, 25);
	}
	
	// returns the drawn path of needed shape
	private GeneralPath getGeneralPath() {
		GeneralPath path = new GeneralPath();
		path.moveTo(10, 0);
		for(int i = 0; i < shapeConnectorCoordinatesX.length; i++) {
			path.lineTo(shapeConnectorCoordinatesX[i], shapeConnectorCoordinatesY[i]);
		}
		path.closePath();
		return path;
	}
	
	public OperatorBlockType getType() {
		return operatorBlockType;
	}
}
