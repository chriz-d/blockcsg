package view;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SnapPoint implements Serializable {
	
	private static final long serialVersionUID = 5343205691855604722L;

	public enum SocketType {
		RECTANGLE_SOCKET, RECTANGLE_PLUG, TRIANGLE_SOCKET, TRIANGLE_PLUG
	}
	
	public enum SocketPos {
		LEFT, RIGHT, TOP, BOTTOM
	}
	
	private static Map<SocketType, SocketType> socketFit;
	
	public Point position;
	public SocketType type;
	public SocketPos pos;
	public boolean isUsed;
	// Offset vector for snapPoints of SAME index (blockPos + snapPoint + OffsetVector = Correct pos for block)
	public Point offsetVector;
	
	public SnapPoint(Point position, SocketType type, SocketPos pos, Point offsetVector) {
		this.position = position;
		this.type = type;
		this.pos = pos;
		this.isUsed = false;
		this.offsetVector = offsetVector;
	}
	
	public static boolean isFitting(SocketType s1, SocketType s2) {
		// First time use, init list
		if(socketFit == null) {
			socketFit = new HashMap<SocketType, SocketType>();
			socketFit.put(SocketType.RECTANGLE_SOCKET, SocketType.RECTANGLE_PLUG);
			socketFit.put(SocketType.RECTANGLE_PLUG, SocketType.RECTANGLE_SOCKET);
			socketFit.put(SocketType.TRIANGLE_SOCKET, SocketType.TRIANGLE_PLUG);
			socketFit.put(SocketType.TRIANGLE_PLUG, SocketType.TRIANGLE_SOCKET);
		}
		return socketFit.get(s1) == s2;
	}
}
