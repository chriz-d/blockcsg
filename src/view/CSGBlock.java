package view;

import java.awt.Dimension;
import java.awt.Point;

public class CSGBlock extends BlockComponent {

	private static final long serialVersionUID = -2621290844629216821L;

	// Coordinates of CSG shape block (has 3 sockets)
	final private int shapeCSGCoordinatesX[] = {0, 79, 79, 75, 75, 70, 70, 75, 75, 79, 79, 45, 45, 50, 39, 30, 35, 35,  0,  0,  5,  5, 10, 10,  5,  5,  0};
	final private int shapeCSGCoordinatesY[] = {0,  0, 15, 15, 10, 10, 30, 30, 25, 25, 49, 49, 44, 44, 39, 44, 44, 49, 49, 25, 25, 30, 30, 10, 10, 15, 15};

	// Coordinates of snap points
	final private Point[] snapPoints = {new Point(0, 0), new Point(0, 0)};
	final private Point[] snapPointOffsetVector = {new Point(0, 0), new Point(0, 0)};
	
	public CSGBlock() {
		blockType = BlockType.CSG;
		label = "CSG";
		color = 0x577590;
		blockCornerCoorX = shapeCSGCoordinatesX;
		blockCornerCoorY = shapeCSGCoordinatesY;
		//snapPoints = new Point[]{new Point(18, 25), new  Point(82, 25)};
		//snapPointUsed = new boolean[] {false, false};
		this.setMinimumSize(new Dimension(80, 50));
		this.setPreferredSize(new Dimension(80, 50));
		this.setMaximumSize(new Dimension(80, 50));
	}
}
