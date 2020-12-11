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
	
	// Coordinates of lower operator block (relative)
	private int operatorBlockCoordinatesX[] = {25, 55,  0,  0, -80,   0};
	private int operatorBlockCoordinatesY[] = { 0,  0, 15, 24,   0, -14};
	
	// Coordinates of upper operator block (relative)
	final private int towerShapeX[] = {0,   0,   0, 30, 0,   0};
	final private int towerShapeY[] = {0, -14, -15,  0, 15, 14};
	final private int towerWidth = 30;	
	final private int defaultSize = 100;
	
	// Coordinates of snap points
	private Point[] snapPoints = {new Point(5, 59), new Point(40, 20), new Point(59, 20), new Point(93, 59)};
	final private SocketType[] socketType = {SocketType.RECTANGLE_PLUG, SocketType.RECTANGLE_SOCKET, SocketType.RECTANGLE_SOCKET, SocketType.RECTANGLE_PLUG};
	final private Direction[] socketPos = {Direction.LEFT, Direction.LEFT, Direction.RIGHT, Direction.RIGHT};
	
	public OperatorBlock(OperatorBlockType operatorBlockType) {
		label = Support.capitalizeNormal(operatorBlockType.toString());
		labelHeight = 65;
		color = 0xf8961e;
		blockType = BlockType.Operator;
		socketArr = new BlockSocket[snapPoints.length];
		for(int i = 0; i < snapPoints.length; i++) {
			socketArr[i] = new BlockSocket(snapPoints[i], socketType[i], socketPos[i]);
		}
		
		correctWidth(1);
		// Set bounds
		this.setMinimumSize(new Dimension(100, 79));
		this.setPreferredSize(new Dimension(100, 79));
		this.setMaximumSize(new Dimension(100, 79));
	}
	
	// returns the drawn path of needed shape
	@Override
	public GeneralPath getGeneralPath() {
		GeneralPathDecor path = new GeneralPathDecor(new GeneralPath());
		path.moveTo(10, 39);

		for(int i = 0; i < operatorBlockCoordinatesX.length; i++) {
			path.lineToRelative(operatorBlockCoordinatesX[i], operatorBlockCoordinatesY[i]);
			if(i == 0) {
				drawTower(path);
				// Tower has been drawn, correct path (subtract towerWidth from next value)
				i++;
				path.lineToRelative(operatorBlockCoordinatesX[i] - towerWidth, 0);
			} else if(i == 2) {
				drawPlug(path, -1);
				i++;
				path.lineToRelative(0, operatorBlockCoordinatesY[i] - 10);
			} else if(i == 5) {
				drawPlug(path, 1);
			}
		}
		path.closePath();
		return path.getPath();
	}
	
	// Draw the upper part of block
	private void drawTower(GeneralPathDecor path) {
		for(int i = 0; i < towerShapeX.length; i++) {
			path.lineToRelative(towerShapeX[i], towerShapeY[i]);
			if(i == 1 && !socketArr[2].isUsed) {
				drawSocket(path, 1);
			} else if(i == 1 && socketArr[2].isUsed) { // Skip socket
				path.lineToRelative(0, -10);
			}
			if(i == 4 && !socketArr[1].isUsed) {
				drawSocket(path, -1);
			} else if(i == 4 && socketArr[1].isUsed){ // Skip socket
				path.lineToRelative(0, 10);
			}
		}
	}
	
	public int correctWidth(int depth) {
		int totalWidth = (depth * defaultSize);
		int offset =  totalWidth - getPreferredSize().width;
		int middlePartWidth = totalWidth - socketWidth * 2;
		int towerStartPos = (middlePartWidth / 2) - (towerWidth / 2);
		operatorBlockCoordinatesX[0] = towerStartPos;
		operatorBlockCoordinatesX[1] = (middlePartWidth / 2) + (towerWidth / 2);
		operatorBlockCoordinatesX[4] = middlePartWidth * -1;
		socketArr[1].setPosition((totalWidth / 2) - (towerWidth / 2) + 5, 20);
		socketArr[2].setPosition((totalWidth / 2) + (towerWidth / 2) - 6, 20);
		socketArr[3].setPosition(totalWidth - 6, 59);
		this.setMinimumSize(new Dimension(totalWidth, 79));
		this.setPreferredSize(new Dimension(totalWidth, 79));
		this.setMaximumSize(new Dimension(totalWidth, 79));
		setBounds(getX(), getY(), totalWidth, 79);
		return offset;
	}
	
	@Override
	public boolean contains(Point point) {
		boolean isInUpperPart = !Support.isOutOfBounds(point, new Rectangle(35, 1, 30, 39));
		boolean isInLowerPart = !Support.isOutOfBounds(point, new Rectangle(0, 40, 99, 40));
		return isInUpperPart || isInLowerPart;
	}
}
