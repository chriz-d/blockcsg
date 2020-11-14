package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import javax.swing.JComponent;

public class PrimitiveShapeComponent extends JComponent {
	
	public enum PrimitiveShapeComponentSocket {
		LEFT, RIGHT, BOTH
	}
	
	private static final long serialVersionUID = -5948325703189463847L;
	
	private String type;
	
	private PrimitiveShapeComponentSocket socketType;
	
	// Coordinates of shape with both sockets open
	final private int shapeBothCoordinatesX[] = {0, 100, 75, 100, 0, 25};
	final private int shapeBothCoordinatesY[] = {0, 0, 25, 50, 50, 25};
	
	// Coordinates of shape with only left socket open
	final private int shapeLeftCoordinatesX[] = {0, 100, 100, 0, 25};
	final private int shapeLeftCoordinatesY[] = {0, 0, 50, 50, 25};
	
	// Coordinates of shape with only right socket open
	final private int shapeRightCoordinatesX[] = {0, 100, 75, 100, 0};
	final private int shapeRightCoordinatesY[] = {0, 0, 25, 50, 50};

	
	public PrimitiveShapeComponent(String type, PrimitiveShapeComponentSocket socketType) {
		this.type = type;
		this.socketType = socketType;
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.RED);

		GeneralPath path = getGeneralPath();
		g2.fill(path);
		g2.draw(path);
		g2.setColor(Color.WHITE);
		g2.drawString(type, 40, 25);
	}
	
	// returns the drawn path of needed shape
	private GeneralPath getGeneralPath() {
		GeneralPath path = new GeneralPath();
		int reqCoordinatesX[] = null;
		int reqCoordinatesY[] = null;
		path.moveTo(0, 0);
		switch(socketType) {
		case BOTH: {
			reqCoordinatesX = shapeBothCoordinatesX;
			reqCoordinatesY = shapeBothCoordinatesY;
		} break;
		case RIGHT: {
			reqCoordinatesX = shapeRightCoordinatesX;
			reqCoordinatesY = shapeRightCoordinatesY;
		} break;
		case LEFT: {
			reqCoordinatesX = shapeLeftCoordinatesX;
			reqCoordinatesY = shapeLeftCoordinatesY;
		} break;
		default: System.out.println("Both sockets disabled?!"); break;
		}
		for(int i = 0; i < reqCoordinatesX.length; i++) {
			path.lineTo(reqCoordinatesX[i], reqCoordinatesY[i]);
		}
		path.closePath();
		return path;
	}
	
	public String getType() {
		return type;
	}
}
