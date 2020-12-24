package view;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import support.Support.Direction;
/**
 * Contains information about socket of a block and helper methods.
 * @author chriz
 *
 */
public class BlockSocket implements Serializable {
	
	private static final long serialVersionUID = 5343205691855604722L;

	public enum SocketType {
		RECTANGLE_SOCKET, RECTANGLE_PLUG
	}
	
	/** Map describing which SocketType fits what */
	private static Map<SocketType, SocketType> socketFit;
	
	/** Relative position of socket to BlockComponent */
	public Point position;
	public SocketType type;
	/** Direction the socket is facing */
	public Direction direction;
	/** Flag if socket is usable */
	public boolean isDisabled;
	
	/** Reference to connected Socket, used for setting fields when disconnecting */
	public BlockSocket connectedSocket;
	/** Reference to socket on the other side of block. Used for disabling */
	public BlockSocket opposite;
	
	public BlockSocket(Point position, SocketType type, Direction direction) {
		this.position = position;
		this.type = type;
		this.direction = direction;
		this.isDisabled = false;
		connectedSocket = null;
	}
	
	/**
	 * On first time run, creates a static map for lookup. Then returns if
	 * sockets are compatible.
	 */
	private static boolean isFitting(SocketType s1, SocketType s2) {
		// First time use, init list
		if(socketFit == null) {
			socketFit = new HashMap<SocketType, SocketType>();
			socketFit.put(SocketType.RECTANGLE_SOCKET, SocketType.RECTANGLE_PLUG);
			socketFit.put(SocketType.RECTANGLE_PLUG, SocketType.RECTANGLE_SOCKET);
		}
		return socketFit.get(s1) == s2;
	}
	
	/**
	 * Checks multiple flags and determines if sockets are allowed to snap to each other.
	 */
	public static boolean isValidSocket(BlockSocket s1, BlockSocket s2) {
		boolean isNotUsed = !s1.isDisabled && !s2.isDisabled;
		boolean isFittingSocket = BlockSocket.isFitting(s1.type, s2.type);
		boolean isNotOnSameSide = s1.direction != s2.direction;
		
		return isNotUsed && isFittingSocket && isNotOnSameSide;
	}
	
	public void setPosition(int x, int y) {
		position = new Point(x, y);
	}
}
