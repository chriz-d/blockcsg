package view.block;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.GeneralPath;

import support.Support;
import support.Support.Direction;
import view.block.BlockSocket.SocketType;
/**
 * Describes the primitve shape block of the GUI.
 * @author chriz
 *
 */
public class PrimShapeBlock extends BlockComponent {
	
	public enum PrimShapeType {
		CUBE, SPHERE, CYLINDER
	}
	
	private static final long serialVersionUID = -5948325703189463847L;
	
	// Coordinates of primitive shape block (relative)
	final private int primShapeCoorX[] = {79,  0,  0, -79,   0};
	final private int primShapeCoorY[] = { 0, 15, 24,   0, -14};
	
	public PrimShapeType primType;
	
	public PrimShapeBlock(PrimShapeType primShapeType) {
		label = Support.capitalizeNormal(primShapeType.toString());
		labelHeight = 25;
		color = 0xef476f;
		blockType = BlockType.PrimShape;
		primType = primShapeType;
		BlockSocket socket1 = new BlockSocket(new Point(5, 20), SocketType.RECTANGLE_SOCKET, Direction.LEFT);
		BlockSocket socket2 = new BlockSocket(new Point(73, 20), SocketType.RECTANGLE_SOCKET, Direction.RIGHT);
		socket1.opposite = socket2;
		socket2.opposite = socket1;
		
		socketArr = new BlockSocket[]{socket1, socket2};
		
		// Set bounds
		this.setMinimumSize(new Dimension(80, 40));
		this.setPreferredSize(new Dimension(80, 40));
		this.setMaximumSize(new Dimension(80, 40));
	}
	
	/**
	 * Draws the block depending on state of the block
	 */
	@Override
	public GeneralPath getGeneralPath() {
		GeneralPathDecor path = new GeneralPathDecor(new GeneralPath());
		path.moveTo(0, 0);
		for(int i = 0; i < primShapeCoorX.length; i++) {
			path.lineToRelative(primShapeCoorX[i], primShapeCoorY[i]);
			if(i == 1 && socketArr[0].connectedSocket == null) {
				drawSocket(path, -1);
				i++;
				path.lineToRelative(0, 14);
			}
			if(i == 4 && socketArr[1].connectedSocket == null) {
				drawSocket(path, 1);
				i++;
				path.lineToRelative(0, -14);
			}
		}
		path.closePath();
		return path.getPath();
	}

	@Override
	public void disconnectSockets() {
		socketArr[0].isDisabled = false;
		socketArr[1].isDisabled = false;
		if(socketArr[0].connectedSocket != null) {
			socketArr[0].connectedSocket.isDisabled = false;
			socketArr[0].connectedSocket.connectedSocket = null;
			socketArr[0].connectedSocket = null;
		}
		if(socketArr[1].connectedSocket != null) {
			socketArr[1].connectedSocket.isDisabled = false;
			socketArr[1].connectedSocket.connectedSocket = null;
			socketArr[1].connectedSocket = null;
		}
		if(socketArr[0].connectedSocket != null && socketArr[0].connectedSocket.opposite != null) {
			socketArr[0].connectedSocket.opposite.isDisabled = false;
		}
		if(socketArr[1].connectedSocket != null && socketArr[1].connectedSocket.opposite != null) {
			socketArr[1].connectedSocket.opposite.isDisabled = false;
		}
	}

	@Override
	public void connectSocket(BlockSocket socket1, BlockSocket socket2) {
		socket1.connectedSocket = socket2;
		socket1.isDisabled = true;
		socket1.opposite.isDisabled = true;
	}
}
