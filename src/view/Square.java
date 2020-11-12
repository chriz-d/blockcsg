package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import javax.swing.JComponent;

public class Square extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5948325703189463847L;

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.RED);
		g2.fillRect(0, 0, 200, 200);
//		GeneralPath p = new GeneralPath();
//		p.moveTo(20, 20);
//		p.lineTo(40, 0);
//		p.lineTo(60, 20);
//		p.lineTo(60, 40);
//		p.lineTo(20, 40);
//		p.closePath();
//		g2.fill(p);
//		g2.draw(p);
	}
	
}
