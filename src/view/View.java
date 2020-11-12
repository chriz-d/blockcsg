package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
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
		JFrame frame = new JFrame();
		JPanel workspacePanel = new JPanel();
		JPanel jMonkeyPanel = new JPanel();
		frame.setLayout(new GridLayout(0, 2));
		workspacePanel.setLayout(null);
		
		frame.add(workspacePanel);
		frame.add(jMonkeyPanel);
		
		Square s = new Square();
		s.setBounds(0, 0, 200, 200);
		s.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
		        screenX = e.getXOnScreen();
		        screenY = e.getYOnScreen();

		        myX = s.getX();
		        myY = s.getY();
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		s.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
		        int deltaX = e.getXOnScreen() - screenX;
		        int deltaY = e.getYOnScreen() - screenY;

		        s.setLocation(myX + deltaX, myY + deltaY);
				
			}
		});
		workspacePanel.add(s);
		
		frame.setSize(1280, 720);
		frame.setTitle("CSG Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
