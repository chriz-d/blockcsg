package view;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.GeneralPath;

import support.Support;
import view.BlockSocket.SocketDir;
import view.BlockSocket.SocketType;

public class PrimShapeBlock extends BlockComponent {
	
	public enum PrimShapeType {
		CUBE, SPHERE, PYRAMID, CYLINDER
	}
	
	private static final long serialVersionUID = -5948325703189463847L;
	
	// Coordinates of shape with both sockets open
	final private int shapeBothCoordinatesX[] = {0, 79, 79, 75, 75, 70, 70, 75, 75, 79, 79,  0,  0,  5,  5, 10, 10,  5,  5,  0};
	final private int shapeBothCoordinatesY[] = {0,  0, 15, 15, 10, 10, 30, 30, 25, 25, 39, 39, 25, 25, 30, 30, 10, 10, 15, 15};
	
	// Coordinates of shape with only left socket open
	final private int shapeLeftCoordinatesX[] = {0, 79, 79,  0,  0,  5,  5, 10, 10,  5,  5,  0};
	final private int shapeLeftCoordinatesY[] = {0,  0, 39, 39, 25, 25, 30, 30, 10, 10, 15, 15};
	
	// Coordinates of shape with only right socket open
	final private int shapeRightCoordinatesX[] = {0, 79, 79, 75, 75, 70, 70, 75, 75, 79, 79,  0};
	final private int shapeRightCoordinatesY[] = {0,  0, 15, 15, 10, 10, 30, 30, 25, 25, 39, 39};
	
	// Coordinates of snap points
	final private Point[] snapPoints = {new Point(5, 20), new Point(74, 20)};
	final private SocketDir[] socketPos = {SocketDir.LEFT, SocketDir.RIGHT};
	
	public PrimShapeBlock(PrimShapeType primShapeType) {
		label = Support.capitalizeNormal(primShapeType.toString());
		labelHeight = 25;
		color = 0x90be6d;
		blockType = BlockType.PrimShape;
		blockCornerCoorX = shapeBothCoordinatesX;
		blockCornerCoorY = shapeBothCoordinatesY;
		socketArr = new BlockSocket[snapPoints.length];
		for(int i = 0; i < snapPoints.length; i++) {
			socketArr[i] = new BlockSocket(snapPoints[i], SocketType.RECTANGLE_SOCKET, socketPos[i]);
		}
		
		// Set bounds
		this.setMinimumSize(new Dimension(81, 40));
		this.setPreferredSize(new Dimension(81, 40));
		this.setMaximumSize(new Dimension(81, 40));
	}
	
	// returns the drawn path of needed shape
	@Override
	public GeneralPath getGeneralPath() {
		GeneralPath path = new GeneralPath();
		int reqCoordinatesX[] = null;
		int reqCoordinatesY[] = null;
		path.moveTo(0, 0);
		if(!socketArr[0].isUsed && !socketArr[1].isUsed) {
			reqCoordinatesX = shapeBothCoordinatesX;
			reqCoordinatesY = shapeBothCoordinatesY;
		} else if(!socketArr[0].isUsed) {
			reqCoordinatesX = shapeRightCoordinatesX;
			reqCoordinatesY = shapeRightCoordinatesY;
		} else if(!socketArr[1].isUsed) {
			reqCoordinatesX = shapeLeftCoordinatesX;
			reqCoordinatesY = shapeLeftCoordinatesY;
		}

		for(int i = 0; i < reqCoordinatesX.length; i++) {
			path.lineTo(reqCoordinatesX[i], reqCoordinatesY[i]);
		}
		path.closePath();
		return path;
	}
}
