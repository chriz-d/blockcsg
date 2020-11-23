package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

import javax.swing.JComponent;

import support.Support;
import view.SnapPoint.SocketPos;
import view.SnapPoint.SocketType;

public class PrimShapeBlock extends BlockComponent {
	
	public enum PrimShapeType {
		CUBE, SPHERE, PYRAMID, CYLINDER
	}
	
	public enum PrimShapeSocket {
		LEFT, RIGHT, BOTH
	}
	
	private static final long serialVersionUID = -5948325703189463847L;
	
	private PrimShapeSocket socketType;
	
	// Coordinates of shape with both sockets open
	final private int shapeBothCoordinatesX[] = {0, 79, 79, 75, 75, 70, 70, 75, 75, 79, 79,  0,  0,  5,  5, 10, 10,  5,  5,  0};
	final private int shapeBothCoordinatesY[] = {0,  0, 15, 15, 10, 10, 30, 30, 25, 25, 39, 39, 25, 25, 30, 30, 10, 10, 15, 15};
	
	// Coordinates of shape with only left socket open
	final private int shapeLeftCoordinatesX[] = {0, 99, 99,  0,  0,  5,  5, 10, 10,  5,  5,  0};
	final private int shapeLeftCoordinatesY[] = {0,   0,  49, 49, 30, 30, 35, 35, 15, 15, 20, 20};
	
	// Coordinates of shape with only right socket open
	final private int shapeRightCoordinatesX[] = {0, 99, 99, 95, 95, 90, 90, 95, 95, 99, 99, 99,  0};
	final private int shapeRightCoordinatesY[] = {0,  0, 20, 20, 15, 15, 35, 35, 30, 30, 49, 49, 49};
	
	// Coordinates of snap points
	final private Point[] snapPoints = {new Point(0, 25), new Point(79, 25)};
	final private Point[] snapPointOffsetVector = {new Point(11, -35), new Point(-10, -35)};
	final private SocketPos[] socketPos = {SocketPos.LEFT, SocketPos.RIGHT};
	
	public PrimShapeBlock(PrimShapeType primShapeType, PrimShapeSocket socketType) {
		this.socketType = socketType;
		label = Support.capitalizeNormal(primShapeType.toString());
		color = 0x90be6d;
		blockType = BlockType.PrimShape;
		blockCornerCoorX = shapeBothCoordinatesX;
		blockCornerCoorY = shapeBothCoordinatesY;
		snapPointArr = new SnapPoint[snapPoints.length];
		for(int i = 0; i < snapPoints.length; i++) {
			snapPointArr[i] = new SnapPoint(snapPoints[i], SocketType.RECTANGLE_SOCKET, socketPos[i], snapPointOffsetVector[i]);
		}
		
		// Set bounds
		this.setMinimumSize(new Dimension(80, 40));
		this.setPreferredSize(new Dimension(80, 40));
		this.setMaximumSize(new Dimension(80, 40));
	}
	
	// returns the drawn path of needed shape
	public GeneralPath getGeneralPath() {
		GeneralPath path = new GeneralPath();
		int reqCoordinatesX[] = null;
		int reqCoordinatesY[] = null;
		path.moveTo(0, 0);
		switch(socketType) {
		case BOTH: {
			reqCoordinatesX = shapeBothCoordinatesX;
			reqCoordinatesY = shapeBothCoordinatesY;
		} break;
		case RIGHT: {
			reqCoordinatesX = shapeRightCoordinatesX;
			reqCoordinatesY = shapeRightCoordinatesY;
		} break;
		case LEFT: {
			reqCoordinatesX = shapeLeftCoordinatesX;
			reqCoordinatesY = shapeLeftCoordinatesY;
		} break;
		default: System.out.println("Both sockets disabled?!"); break;
		}
		for(int i = 0; i < reqCoordinatesX.length; i++) {
			path.lineTo(reqCoordinatesX[i], reqCoordinatesY[i]);
		}
		path.closePath();
		return path;
	}
}
