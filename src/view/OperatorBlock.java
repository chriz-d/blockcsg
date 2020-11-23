package view;

import java.awt.Dimension;
import java.awt.Point;

import support.Support;
import view.BlockSocket.SocketDir;
import view.BlockSocket.SocketType;

public class OperatorBlock extends BlockComponent {
	
	public enum OperatorBlockType {
		UNION, DIFFERENCE, INTERSECT;
	}
	
	private static final long serialVersionUID = -1526119201511001957L;
	
	// Coordinates of connector shape
	final private int shapeConnectorCoordinatesX[] = {10, 45, 45, 40, 50, 60, 55, 55, 89, 89, 94, 94, 99, 99, 94, 94, 89, 89, 10, 10,  6,  6,  1,  1,  6,  6, 10};
	final private int shapeConnectorCoordinatesY[] = {10, 10,  5,  5,  0,  5,  5, 10, 10, 25, 25, 20, 20, 40, 40, 35, 35, 49, 49, 35, 35, 40, 40, 20, 20, 25, 25};
	
	// Coordinates of snap points
	final private Point[] snapPoints = {new Point(0, 35), new Point(50, 0), new Point(90, 30)};
	final private Point[] snapPointOffsetVector = {new Point(11, -25), new Point(-40, 11), new Point(-1, -20)};
	final private SocketType[] socketType = {SocketType.RECTANGLE_PLUG, SocketType.TRIANGLE_PLUG, SocketType.RECTANGLE_PLUG};
	final private SocketDir[] socketPos = {SocketDir.LEFT, SocketDir.TOP, SocketDir.RIGHT};
	
	public OperatorBlock(OperatorBlockType operatorBlockType) {
		label = Support.capitalizeNormal(operatorBlockType.toString());
		color = 0xf8961e;
		blockType = BlockType.Operator;
		blockCornerCoorX = shapeConnectorCoordinatesX;
		blockCornerCoorY = shapeConnectorCoordinatesY;
		socketArr = new BlockSocket[snapPoints.length];
		for(int i = 0; i < snapPoints.length; i++) {
			socketArr[i] = new BlockSocket(snapPoints[i], socketType[i], socketPos[i], snapPointOffsetVector[i]);
		}
		
		// Set bounds
		this.setMinimumSize(new Dimension(100, 50));
		this.setPreferredSize(new Dimension(100, 50));
		this.setMaximumSize(new Dimension(100, 50));
	}
}
