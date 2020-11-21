package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

import support.Support;

public class OperatorBlock extends BlockComponent {
	
	public enum OperatorBlockType {
		UNION, DIFFERENCE, INTERSECT;
	}
	
	private static final long serialVersionUID = -1526119201511001957L;
	
	private OperatorBlockType operatorBlockType;
	
	// Coordinates of connector shape
	final private int shapeConnectorCoordinatesX[] = {10, 45, 45, 40, 50, 60, 55, 55, 89, 89, 94, 94, 99, 99, 94, 94, 89, 89, 10, 10,  6,  6,  1,  1,  6,  6, 10};
	final private int shapeConnectorCoordinatesY[] = {10, 10,  5,  5,  0,  5,  5, 10, 10, 25, 25, 20, 20, 40, 40, 35, 35, 49, 49, 35, 35, 40, 40, 20, 20, 25, 25};
	
	public OperatorBlock(OperatorBlockType operatorBlockType) {
		this.operatorBlockType = operatorBlockType;
		label = Support.capitalizeNormal(operatorBlockType.toString());
		color = 0xf8961e;
		blockCornerCoorX = shapeConnectorCoordinatesX;
		blockCornerCoorY = shapeConnectorCoordinatesY;
		
		blockType = BlockType.Operator;
		snapPoints = new Point[]{new Point(18, 25), new  Point(82, 25)};
		snapPointUsed = new boolean[] {false, false};
		this.setMinimumSize(new Dimension(100, 50));
		this.setPreferredSize(new Dimension(100, 50));
		this.setMaximumSize(new Dimension(100, 50));
	}
}
