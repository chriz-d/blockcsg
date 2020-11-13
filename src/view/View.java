package view;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.Controller;

public class View {
	
	private JFrame frame;
	private Controller controller;
	private int screenX = 0;
	private int screenY = 0;
	private int myX = 0;
	private int myY = 0;
	
	public View(Controller controller) {
		this.controller = controller;
	}
	
	public void initView() {
		frame = new JFrame();
		JPanel workspacePanel = new JPanel();
		JPanel jMonkeyPanel = new JPanel();
		frame.setLayout(new GridLayout(0, 2));
		workspacePanel.setLayout(null);
		
		frame.add(workspacePanel);
		frame.add(jMonkeyPanel);
		
		PrimitiveShapeComponent s = new PrimitiveShapeComponent("Cube", true, true);
		PrimitiveShapeComponent s2 = new PrimitiveShapeComponent("Cube", false, true);
		PrimitiveShapeComponent s3 = new PrimitiveShapeComponent("Cube", true, false);
		
		OperatorComponent o1 = new OperatorComponent("Union");
		
		o1.setBounds(0, 0, 100, 50);
		
		s.setBounds(0, 0, 100, 50);
		s2.setBounds(0, 0, 100, 50);
		s3.setBounds(0, 0, 100, 50);
		addDragHandler(s);
		addDragHandler(s2);
		addDragHandler(s3);
		addDragHandler(o1);
		
		workspacePanel.add(s);
		workspacePanel.add(s2);
		workspacePanel.add(s3);
		workspacePanel.add(o1);
		
		frame.setSize(1280, 720);
		frame.setTitle("CSG Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private void addDragHandler(JComponent component) {
		component.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
		        screenX = e.getXOnScreen();
		        screenY = e.getYOnScreen();

		        myX = component.getX();
		        myY = component.getY();
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {}
		});
		component.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent arg0) {}
			
			@Override
			public void mouseDragged(MouseEvent e) {
		        int deltaX = e.getXOnScreen() - screenX;
		        int deltaY = e.getYOnScreen() - screenY;

		        component.setLocation(myX + deltaX, myY + deltaY);
				
			}
		});
	}
}
