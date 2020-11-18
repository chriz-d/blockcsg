package support;

import java.awt.Container;
import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Support {
	
	// Creates deep copy of an object using java serialization
	static public Object deepCopy(Object object) {
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
	public static boolean isOutOfBounds(Point point, Container container) {
		if(point == null) {
			return true;
		}
		boolean x = point.getX() < container.getX() || 
				point.getX() > container.getX() + container.getWidth();
		boolean y = point.getY() < container.getY() || 
				point.getY() > container.getY() + container.getHeight();
		return x || y;
	}
}
