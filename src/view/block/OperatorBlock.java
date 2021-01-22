package view.block;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;

import support.Support;
import support.Support.Direction;
import view.block.BlockSocket.SocketType;
/**
 * Describes the operator block of the GUI.
 * @author chriz
 *
 */
public class OperatorBlock extends BlockComponent {
	
	public enum OperatorBlockType {
		UNION, DIFFERENCE, INTERSECT;
	}
	
	private static final long serialVersionUID = -1526119201511001957L;
	
	/** Relative X-Coordinates of corners */
	private int operatorBlockCoordinatesX[] = {25, 55,  0,  0, -80,   0};
	/** Relative Y-Coordinates of corners */
	private int operatorBlockCoordinatesY[] = { 0,  0, 15, 24,   0, -14};
	
	/** Relative X-Coordinates of "Tower" (upper) part of the block */
	final private int towerShapeX[] = {0,   0,   0, 30, 0,   0};
	/** Relative Y-Coordinates of "Tower" (upper) part of the block */
	final private int towerShapeY[] = {0, -14, -15,  0, 15, 14};
	final private int towerWidth = 30;
	/** Size used when computing new width, should be always bigger than 2 times primshape + towerwidth */
	final private int defaultSize = (85 * 2) + towerWidth + (towerWidth / 2 - 1);
	
	public OperatorBlockType opType;
	
	public OperatorBlock(OperatorBlockType operatorBlockType) {
		label = Support.capitalizeNormal(operatorBlockType.toString());
		labelHeight = 65;
		color = 0x118ab2;
		blockType = BlockType.Operator;
		opType = operatorBlockType;
		BlockSocket socket1 = new BlockSocket(new Point(5, 59), SocketType.RECTANGLE_PLUG, Direction.LEFT);
		BlockSocket socket2 = new BlockSocket(new Point(40, 20), SocketType.RECTANGLE_SOCKET, Direction.LEFT);
		BlockSocket socket3 = new BlockSocket(new Point(59, 20), SocketType.RECTANGLE_SOCKET, Direction.RIGHT);
		BlockSocket socket4 = new BlockSocket(new Point(93, 59), SocketType.RECTANGLE_PLUG, Direction.RIGHT);
		socket2.opposite = socket3;
		socket3.opposite = socket2;
		
		socketArr = new BlockSocket[]{socket1, socket2, socket3, socket4};
		
		// Set bounds
		this.setMinimumSize(new Dimension(100, 79));
		this.setPreferredSize(new Dimension(100, 79));
		this.setMaximumSize(new Dimension(100, 79));

		correctWidth(1);
	}
	
	/**
	 * Draws the block depending on state of the block
	 * @return Returns GeneralPath which drew component.
	 */
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
	
	/**
	 * Draws the upper part of the block depending on state
	 * @param path GeneralPath object for drawing.
	 */
	private void drawTower(GeneralPathDecor path) {
		for(int i = 0; i < towerShapeX.length; i++) {
			path.lineToRelative(towerShapeX[i], towerShapeY[i]);
			if(i == 1 && socketArr[2].connectedSocket == null) {
				drawSocket(path, 1);
			} else if(i == 1 && socketArr[2].connectedSocket != null) { // Skip socket
				path.lineToRelative(0, -10);
			}
			if(i == 4 && socketArr[1].connectedSocket == null) {
				drawSocket(path, -1);
			} else if(i == 4 && socketArr[1].connectedSocket != null){ // Skip socket
				path.lineToRelative(0, 10);
			}
		}
	}
	
	/**
	 * Recalculates block width using given parameter. When changes to width are made
	 * the socket positions and tower part need to be moved aswell.
	 * @param depth Depth of the component inside a tree.
	 * @return Returns the distance the block was move by when growing, used for compensating.
	 */
	public int correctWidth(int depth) {
		int newWidth = ((depth- 1) * defaultSize);
		if(depth == 1) { // Set to minimal width
			newWidth = 80;
		}
		int offset =  (newWidth - getPreferredSize().width) / 2;
		int middlePartNewWidth = newWidth - socketWidth * 2;
		int towerStartPos = (middlePartNewWidth / 2) - (towerWidth / 2);
		int towerEndPos = (middlePartNewWidth / 2) + (towerWidth / 2);
		int oldTowerStartPos = operatorBlockCoordinatesX[0];
		operatorBlockCoordinatesX[0] = towerStartPos;
		operatorBlockCoordinatesX[1] = towerEndPos;
		operatorBlockCoordinatesX[4] = middlePartNewWidth * -1;
		
		// Change socket coordinates
		socketArr[1].setPosition((newWidth / 2) - (towerWidth / 2) + 5, 20);
		socketArr[2].setPosition((newWidth / 2) + (towerWidth / 2) - 6, 20);
		socketArr[3].setPosition(newWidth - 6, 59);
		
		// Set component size and bounds
		this.setMinimumSize(new Dimension(newWidth, 79));
		this.setPreferredSize(new Dimension(newWidth, 79));
		this.setMaximumSize(new Dimension(newWidth, 79));
		setBounds(getX() - (towerStartPos - oldTowerStartPos), getY(), newWidth, 79);
		return offset;
	}
	
	/** Overridden contains() method for supporting more than only rectangular shapes */
	@Override
	public boolean contains(Point point) {
		boolean isInUpperPart = !Support.isOutOfBounds(point, 
				new Rectangle(operatorBlockCoordinatesX[0] + socketWidth, 1, 30, 39));
		boolean isInLowerPart = !Support.isOutOfBounds(point, 
				new Rectangle(0, 40, (int) getPreferredSize().getWidth(), 40));
		return isInUpperPart || isInLowerPart;
	}

	@Override
	public void disconnectSockets() {
		socketArr[1].isDisabled = false;
		socketArr[2].isDisabled = false;
		if(socketArr[1].connectedSocket != null) {
			socketArr[1].connectedSocket.isDisabled = false;
			socketArr[1].connectedSocket.connectedSocket = null;
			socketArr[1].connectedSocket = null;
		}
		if(socketArr[2].connectedSocket != null) {
			socketArr[2].connectedSocket.isDisabled = false;
			socketArr[2].connectedSocket.connectedSocket = null;
			socketArr[2].connectedSocket = null;
		}
		if(socketArr[2].connectedSocket != null && socketArr[2].connectedSocket.opposite != null) {
			socketArr[2].connectedSocket.opposite.isDisabled = false;
		}
		if(socketArr[1].connectedSocket != null && socketArr[1].connectedSocket.opposite != null) {
			socketArr[1].connectedSocket.opposite.isDisabled = false;
		}
	}

	@Override
	public void connectSocket(BlockSocket socket1, BlockSocket socket2) {
		socket1.isDisabled = true;
		socket1.connectedSocket = socket2;
		socket2.connectedSocket = socket1;
		if(socket1.opposite != null) {
			socket1.opposite.isDisabled = true;
		}
	}
}
