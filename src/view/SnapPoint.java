package view;

import java.awt.Point;

public class SnapPoint {
	
	public enum SocketType {
		RECTANGLE, TRIANGLE
	}
	
	public Point position;
	public SocketType type;
	public boolean isUsed;
	// Offset vector for snapPoints of SAME index (blockPos + snapPoint + OffsetVector = Correct pos for block)
	public Point offsetVector;
	
	public SnapPoint(Point position, SocketType type, Point offsetVector) {
		this.position = position;
		this.type = type;
		this.isUsed = false;
		this.offsetVector = offsetVector;
	}
}
