package support;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Support {
	
	public enum Direction {
		LEFT, RIGHT
	}
	
	// Creates deep copy of an object using java serialization
	public static Object deepCopy(Object object) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);
			outputStrm.writeObject(object);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
			return objInputStream.readObject();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Checks if point is inside of container
	public static boolean isOutOfBounds(Point point, Rectangle rectangle) {
		if(point == null) {
			return true;
		}
		boolean x = point.getX() < rectangle.getX() || 
				point.getX() > rectangle.getX() + rectangle.getWidth();
		boolean y = point.getY() < rectangle.getY() || 
				point.getY() > rectangle.getY() + rectangle.getHeight();
		return x || y;
	}
	
	public static double getDistance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	}
	
	public static Point addPoints(Point p1, Point p2) {
		return new Point(p1.x + p2.x, p1.y + p2.y);
	}
	
	public static Point subPoints(Point p1, Point p2) {
		return new Point(p1.x - p2.x, p1.y - p2.y);
	}
	
	public static String capitalizeNormal(String s) {
		return s.substring(0, 1) + s.substring(1, s.length()).toLowerCase();
	}
	
	public static Point point2DToPoint(Point2D point2D) {
		return new Point((int) point2D.getX(), (int) point2D.getY());
	}
}
