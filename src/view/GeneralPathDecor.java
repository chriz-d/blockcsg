package view;

import java.awt.Point;
import java.awt.geom.GeneralPath;

import support.Support;

public class GeneralPathDecor {
	
	private GeneralPath path;
	
	public GeneralPathDecor(GeneralPath path) {
		this.path = path;
	}
	
	public void lineToRelative(int x, int y) {
		Point currentPos = Support.point2DToPoint(path.getCurrentPoint());
		Point relativeMove = new Point(x, y);
		Point goal = Support.addPoints(currentPos, relativeMove);
		path.lineTo(goal.getX(), goal.getY());
	}
	
	public void moveTo(int x, int y) {
		path.moveTo(x, y);
	}
	
	public void closePath() {
		path.closePath();
	}
	
	public GeneralPath getPath() {
		return path;
	}
}
