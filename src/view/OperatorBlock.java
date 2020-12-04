package view;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;

import support.Support;
import support.Support.Direction;
import view.BlockSocket.SocketType;

public class OperatorBlock extends BlockComponent {
	
	public enum OperatorBlockType {
		UNION, DIFFERENCE, INTERSECT;
	}
	
	private static final long serialVersionUID = -1526119201511001957L;
	
	// Coordinates of connector shape
	final private int shapeConnectorBothCoordinatesX[] = {10, 35, 35, 40, 40, 45, 45, 40, 40, 35, 35, 65, 65, 60, 60, 55, 55, 60, 60, 65, 65, 89, 89, 94, 94, 99, 99, 94, 94, 89, 89, 10, 10,  6,  6,  1,  1,  6,  6, 10};
	final private int shapeConnectorBothCoordinatesY[] = {40, 40, 26, 26, 31, 31, 11, 11, 16, 16,  1,  1, 16, 16, 11, 11, 31, 31, 26, 26, 40, 40, 55, 55, 50, 50, 70, 70, 65, 65, 79, 79, 65, 65, 70, 70, 50, 50, 55, 55};
	
	final private int shapeConnectorLeftCoordinatesX[] = {10, 35, 35, 40, 40, 45, 45, 40, 40, 35, 35, 65, 65, 89, 89, 94, 94, 99, 99, 94, 94, 89, 89, 10, 10,  6,  6,  1,  1,  6,  6, 10};
	final private int shapeConnectorLeftCoordinatesY[] = {40, 40, 26, 26, 31, 31, 11, 11, 16, 16,  1,  1, 40, 40, 55, 55, 50, 50, 70, 70, 65, 65, 79, 79, 65, 65, 70, 70, 50, 50, 55, 55};
	
	final private int shapeConnectorRightCoordinatesX[] = {10, 35, 35, 65, 65, 60, 60, 55, 55, 60, 60, 65, 65, 89, 89, 94, 94, 99, 99, 94, 94, 89, 89, 10, 10,  6,  6,  1,  1,  6,  6, 10};
	final private int shapeConnectorRightCoordinatesY[] = {40, 40,  1,  1, 16, 16, 11, 11, 31, 31, 26, 26, 40, 40, 55, 55, 50, 50, 70, 70, 65, 65, 79, 79, 65, 65, 70, 70, 50, 50, 55, 55};
	
	// Coordinates of snap points
	final private Point[] snapPoints = {new Point(5, 60), new Point(40, 21), new Point(59, 21), new Point(94, 60)};
	final private SocketType[] socketType = {SocketType.RECTANGLE_PLUG, SocketType.RECTANGLE_SOCKET, SocketType.RECTANGLE_SOCKET, SocketType.RECTANGLE_PLUG};
	final private Direction[] socketPos = {Direction.LEFT, Direction.LEFT, Direction.RIGHT, Direction.RIGHT};
	
	public OperatorBlock(OperatorBlockType operatorBlockType) {
		label = Support.capitalizeNormal(operatorBlockType.toString());
		labelHeight = 65;
		color = 0xf8961e;
		blockType = BlockType.Operator;
		blockCornerCoorX = shapeConnectorBothCoordinatesX;
		blockCornerCoorY = shapeConnectorBothCoordinatesY;
		socketArr = new BlockSocket[snapPoints.length];
		for(int i = 0; i < snapPoints.length; i++) {
			socketArr[i] = new BlockSocket(snapPoints[i], socketType[i], socketPos[i]);
		}
		
		// Set bounds
		this.setMinimumSize(new Dimension(101, 80));
		this.setPreferredSize(new Dimension(101, 80));
		this.setMaximumSize(new Dimension(101, 80));
	}
	
	// returns the drawn path of needed shape
	@Override
	public GeneralPath getGeneralPath() {
		GeneralPath path = new GeneralPath();
		int reqCoordinatesX[] = null;
		int reqCoordinatesY[] = null;
		path.moveTo(shapeConnectorBothCoordinatesX[0], shapeConnectorBothCoordinatesY[0]);
		if(!socketArr[1].isUsed && !socketArr[2].isUsed) {
			reqCoordinatesX = shapeConnectorBothCoordinatesX;
			reqCoordinatesY = shapeConnectorBothCoordinatesY;
		} else if(!socketArr[1].isUsed) {
			reqCoordinatesX = shapeConnectorRightCoordinatesX;
			reqCoordinatesY = shapeConnectorRightCoordinatesY;
		} else if(!socketArr[2].isUsed) {
			reqCoordinatesX = shapeConnectorLeftCoordinatesX;
			reqCoordinatesY = shapeConnectorLeftCoordinatesY;
		}

		for(int i = 0; i < reqCoordinatesX.length; i++) {
			path.lineTo(reqCoordinatesX[i], reqCoordinatesY[i]);
		}
		path.closePath();
		return path;
	}
	
	@Override
	public boolean contains(Point point) {
		boolean isInUpperPart = !Support.isOutOfBounds(point, new Rectangle(35, 1, 30, 39));
		boolean isInLowerPart = !Support.isOutOfBounds(point, new Rectangle(0, 40, 99, 40));
		return isInUpperPart || isInLowerPart;
	}
}
