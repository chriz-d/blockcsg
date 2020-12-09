package view;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.GeneralPath;

import support.Support;
import support.Support.Direction;
import view.BlockSocket.SocketType;

public class PrimShapeBlock extends BlockComponent {
	
	public enum PrimShapeType {
		CUBE, SPHERE, PYRAMID, CYLINDER
	}
	
	private static final long serialVersionUID = -5948325703189463847L;
	
	// Coordinates of primitive shape block (relative)
	final private int primShapeCoorX[] = {79,  0,  0, -79,   0};
	final private int primShapeCoorY[] = { 0, 15, 24,   0, -14};
	
	// Coordinates of snap points
	final private Point[] snapPoints = {new Point(5, 20), new Point(73, 20)};
	final private Direction[] socketPos = {Direction.LEFT, Direction.RIGHT};
	
	public PrimShapeBlock(PrimShapeType primShapeType) {
		label = Support.capitalizeNormal(primShapeType.toString());
		labelHeight = 25;
		color = 0x90be6d;
		blockType = BlockType.PrimShape;
		socketArr = new BlockSocket[snapPoints.length];
		for(int i = 0; i < snapPoints.length; i++) {
			socketArr[i] = new BlockSocket(snapPoints[i], SocketType.RECTANGLE_SOCKET, socketPos[i]);
		}
		
		// Set bounds
		this.setMinimumSize(new Dimension(80, 40));
		this.setPreferredSize(new Dimension(80, 40));
		this.setMaximumSize(new Dimension(80, 40));
	}
	
	// returns the drawn path of needed shape
	@Override
	public GeneralPath getGeneralPath() {
		GeneralPathDecor path = new GeneralPathDecor(new GeneralPath());
		path.moveTo(0, 0);
		for(int i = 0; i < primShapeCoorX.length; i++) {
			path.lineToRelative(primShapeCoorX[i], primShapeCoorY[i]);
			if(i == 1 && !socketArr[0].isUsed) {
				drawSocket(path, -1);
				i++;
				path.lineToRelative(0, 14);
			}
			if(i == 4 && !socketArr[1].isUsed) {
				drawSocket(path, 1);
				i++;
				path.lineToRelative(0, -14);
			}
		}
		path.closePath();
		return path.getPath();
	}
}
