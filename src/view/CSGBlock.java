package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

public class CSGBlock extends BlockComponent {

	private static final long serialVersionUID = -2621290844629216821L;

	// Coordinates of CSG shape block (has 3 sockets)
	final private int shapeCSGCoordinatesX[] = {0, 79, 79, 75, 75, 70, 70, 75, 75, 79, 79, 45, 45, 50, 39, 30, 35, 35,  0,  0,  5,  5, 10, 10,  5,  5,  0};
	final private int shapeCSGCoordinatesY[] = {0,  0, 15, 15, 10, 10, 30, 30, 25, 25, 49, 49, 44, 44, 39, 44, 44, 49, 49, 25, 25, 30, 30, 10, 10, 15, 15};

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

	// returns the drawn path of needed shape
	private GeneralPath getGeneralPath() {
		GeneralPath path = new GeneralPath();
		path.moveTo(0, 0);
		for(int i = 0; i < shapeCSGCoordinatesX.length; i++) {
			path.lineTo(shapeCSGCoordinatesX[i], shapeCSGCoordinatesY[i]);
		}
		path.closePath();
		return path;
	}
}
