package view.block;

import java.awt.Point;
import java.awt.geom.GeneralPath;

import support.Support;
/**
 * Decorator class for GeneralPath to enable relative drawing.
 * @author chriz
 *
 */
public class GeneralPathDecor {
	
	/** Original GeneralPath */
	private GeneralPath path;
	
	public GeneralPathDecor(GeneralPath path) {
		this.path = path;
	}
	
	/** Draws line relative to current position of GeneralPath */
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
