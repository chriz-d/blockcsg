package view;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import support.Support.Direction;

public class BlockSocket implements Serializable {
	
	private static final long serialVersionUID = 5343205691855604722L;

	public enum SocketType {
		RECTANGLE_SOCKET, RECTANGLE_PLUG, TRIANGLE_SOCKET, TRIANGLE_PLUG
	}
	
	private static Map<SocketType, SocketType> socketFit;
	
	public Point position;
	public SocketType type;
	public Direction direction;
	public boolean isUsed;
	
	public BlockComponent parent;
	public BlockSocket connectedSocket;
	
	public BlockSocket(Point position, SocketType type, Direction direction) {
		this.position = position;
		this.type = type;
		this.direction = direction;
		this.isUsed = false;
		connectedSocket = null;
	}
	
	// Returns if given socket types are compatible using static map
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
