package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import javax.swing.JComponent;

public class PrimitiveShapeComponent extends JComponent {
	
	private static final long serialVersionUID = -5948325703189463847L;
	
	private String type;
	
	private boolean showLeftSocket;
	private boolean showRightSocket;
	
	// Coordinates of shape with both sockets open
	final private int shapeBothCoordinatesX[] = {0, 100, 75, 100, 0, 25};
	final private int shapeBothCoordinatesY[] = {0, 0, 25, 50, 50, 25};
	
	// Coordinates of shape with only left socket open
	final private int shapeLeftCoordinatesX[] = {0, 100, 100, 0, 25};
	final private int shapeLeftCoordinatesY[] = {0, 0, 50, 50, 25};
	
	// Coordinates of shape with only right socket open
	final private int shapeRightCoordinatesX[] = {0, 100, 75, 100, 0};
	final private int shapeRightCoordinatesY[] = {0, 0, 25, 50, 50};

	
	public PrimitiveShapeComponent(String type, boolean showLeftSocket, boolean showRightSocket) {
		this.type = type;
		this.showLeftSocket = showLeftSocket;
		this.showRightSocket = showRightSocket;
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
		if(showLeftSocket && showRightSocket) {
			reqCoordinatesX = shapeBothCoordinatesX;
			reqCoordinatesY = shapeBothCoordinatesY;
		} else if(showRightSocket && !showLeftSocket) {
			reqCoordinatesX = shapeRightCoordinatesX;
			reqCoordinatesY = shapeRightCoordinatesY;
		} else if(!showRightSocket && showLeftSocket) {
			reqCoordinatesX = shapeLeftCoordinatesX;
			reqCoordinatesY = shapeLeftCoordinatesY;
		} else {
			System.out.println("Both sockets disabled?!");
		}
		for(int i = 0; i < reqCoordinatesX.length; i++) {
			path.lineTo(reqCoordinatesX[i], reqCoordinatesY[i]);
		}
		path.closePath();
		return path;
	}
	
	public void enableLeftSocket() {
		showLeftSocket = true;
	}
	
	public void enableRightSocket() {
		showRightSocket = true;
	}
	
	public void disableLeftSocket() {
		showLeftSocket = false;
	}
	
	public void disableRightSocket() {
		showLeftSocket = false;
	}
	
	public String getType() {
		return type;
	}
}
