package support;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
/**
 * Contains helper methods for miscellaneous operations not worthy of their own class.
 * @author chriz
 *
 */
public class Support {
	
	public enum Direction {
		LEFT, RIGHT
	}
	
	/**
	 * Creates a deep copy of an object using serialization.
	 * @param object Object to clone.
	 * @return Cloned object.
	 */
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

	/**
	 * Returns if an point is out of bounds from a given rectangle 
	 * @param point Point to check if its inside rectangle
	 * @param rectangle Rectangle describing bounds.
	 * @return If point is not inside rectangle.
	 */
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
	
	/**
	 * Returns distance between two points.
	 * @param p1 Point 1.
	 * @param p2 Point 2.
	 * @return Distance between Point 1 and Point 2.
	 */
	public static double getDistance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	}

	public static Point addPoints(Point p1, Point p2) {
		return new Point(p1.x + p2.x, p1.y + p2.y);
	}
	
	public static Point subPoints(Point p1, Point p2) {
		return new Point(p1.x - p2.x, p1.y - p2.y);
	}
	
	/** 
	 * Capitalizes only the first letter in an all caps string, used for enums toString() 
	 * @param s String to capitalize.
	 * @return Capitalized string.
	 */
	public static String capitalizeNormal(String s) {
		return s.substring(0, 1) + s.substring(1, s.length()).toLowerCase();
	}
	
	public static Point point2DToPoint(Point2D point2D) {
		return new Point((int) point2D.getX(), (int) point2D.getY());
	}
	
	public static Material getTransparentMaterial(AssetManager assetMan) {
		Material mat = new Material(assetMan, "Common/MatDefs/Light/Lighting.j3md");
		mat.setBoolean("UseMaterialColors",true);
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		mat.setColor("Diffuse", new ColorRGBA(1, 1, 1, 0.4f));
		mat.setColor("Ambient", new ColorRGBA(1, 1, 1, 0.4f));
		return mat;
	}
	
	public static Material getHighlightMaterial(AssetManager assetMan) {
		Material mat = new Material(assetMan, "Common/MatDefs/Light/Lighting.j3md");
		mat.setBoolean("UseMaterialColors",true);
		mat.setColor("Diffuse", new ColorRGBA(1, 1, 1, 1));
		mat.setColor("Ambient", new ColorRGBA(1, 1, 1, 1));
		return mat;
	}
}
