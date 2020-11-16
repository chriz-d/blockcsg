package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import controller.Controller;
import view.PrimitiveShapeComponent.PrimitiveShapeComponentSocket;

public class View {
	
	// Main window
	private JFrame frame;
	private Controller controller;
	
	// Misc var for event handler
	private int screenX = 0;
	private int screenY = 0;
	private int myX = 0;
	private int myY = 0;
	
	private final int WINDOW_WIDTH = 1280;
	private final int WINDOW_HEIGHT = 720;
	
	public View(Controller controller) {
		this.controller = controller;
	}
	
	public void initView() {
		// Main JFrame
		frame = new JFrame();
		frame.setLayout(new GridLayout(1, 3));
		
		// Every panel of GUI
		JPanel jMonkeyViewPanel = new JPanel();
		JLayeredPane layeredPane = new JLayeredPane();
		JPanel transferPanel = new JPanel(null);
		JPanel blockViewPanel = new JPanel(new BorderLayout());
		JPanel workspacePanel = new JPanel(null);
				
		jMonkeyViewPanel.setBackground(Color.DARK_GRAY);
		workspacePanel.setBackground(Color.LIGHT_GRAY);
		// Layered pane does not have layout manager, hence manual position and size
		// needed
		addBlockViewResizeHandler(blockViewPanel);
		transferPanel.setOpaque(false);
		
		// Inserting panels to each other and JFrame
		blockViewPanel.add(getDrawerPanel(), BorderLayout.LINE_START);
		blockViewPanel.add(workspacePanel, BorderLayout.CENTER);
		layeredPane.add(blockViewPanel, JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(transferPanel, JLayeredPane.DRAG_LAYER);
		
		frame.add(layeredPane);
		frame.add(jMonkeyViewPanel);
		
		
		
		frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setTitle("CSG Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private JPanel getDrawerPanel() {
		JPanel drawerPanel = new JPanel(new BorderLayout());
		Box vBox = Box.createVerticalBox();
		JScrollPane scrollPane = new JScrollPane(vBox, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		//scrollPane.setPreferredSize(new Dimension(130, 650));
		drawerPanel.add(scrollPane, BorderLayout.CENTER);
		vBox.add(new PrimitiveShapeComponent("Cube", PrimitiveShapeComponentSocket.BOTH));
		vBox.add(new PrimitiveShapeComponent("Sphere", PrimitiveShapeComponentSocket.BOTH));
		vBox.add(new PrimitiveShapeComponent("Cylinder", PrimitiveShapeComponentSocket.BOTH));
		vBox.add(new PrimitiveShapeComponent("Pyramid", PrimitiveShapeComponentSocket.BOTH));
		vBox.add(new OperatorComponent("Union"));
		vBox.add(new OperatorComponent("Difference"));
		vBox.add(new OperatorComponent("Intersection"));

		return drawerPanel;
	}
	
	// Resizes BlockView, because no layout manager
	private void addBlockViewResizeHandler(JPanel blockview) {
		frame.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentResized(ComponentEvent arg0) {
				blockview.setBounds(0, 0, (frame.getWidth() + blockview.getComponent(0).getWidth() )/ 2, frame.getHeight()- 30);
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
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
