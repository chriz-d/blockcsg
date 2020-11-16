package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import controller.Controller;
import view.BlockComponent.BlockType;
import view.PrimShapeBlock.PrimShapeSocket;

public class View {
	
	// Main window and panels for easy access
	private JFrame frame;
	private Controller controller;
	private JPanel transferPanel;
	private JPanel blockViewPanel;
	
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
		
		frame.add(getBlockViewPanel());
		frame.add(getJMonkeyPanel());
		
		
		frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setTitle("CSG Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private JLayeredPane getBlockViewPanel() {
		JLayeredPane layeredPane = new JLayeredPane();
		transferPanel = new JPanel(null);
		blockViewPanel = new JPanel(new BorderLayout());
		JPanel workspacePanel = new JPanel(null);
				
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
		return layeredPane;
	}
	
	private JPanel getJMonkeyPanel() {
		JPanel jMonkeyViewPanel = new JPanel();
		jMonkeyViewPanel.setBackground(Color.DARK_GRAY);
		return jMonkeyViewPanel;
	}
	
	private JPanel getDrawerPanel() {
		JPanel drawerPanel = new JPanel(new BorderLayout());
		Box vBox = Box.createVerticalBox();
		JScrollPane scrollPane = new JScrollPane(vBox, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		//scrollPane.setPreferredSize(new Dimension(130, 650));
		drawerPanel.add(scrollPane, BorderLayout.CENTER);
		vBox.add(new PrimShapeBlock(BlockType.CUBE, PrimShapeSocket.BOTH));
		vBox.add(new PrimShapeBlock(BlockType.SPHERE, PrimShapeSocket.BOTH));
		vBox.add(new PrimShapeBlock(BlockType.CYLINDER, PrimShapeSocket.BOTH));
		vBox.add(new PrimShapeBlock(BlockType.PYRAMID, PrimShapeSocket.BOTH));
		vBox.add(new OperatorComponent(BlockType.DIFFERENCE));
		vBox.add(new OperatorComponent(BlockType.INTERSECT));
		vBox.add(new OperatorComponent(BlockType.UNION));

		return drawerPanel;
	}
	
	// Resizes BlockView, because no layout manager
	private void addBlockViewResizeHandler(JPanel blockview) {
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				blockview.setBounds(0, 0, (frame.getWidth() + blockview.getComponent(0).getWidth() )/ 2, frame.getHeight()- 30);
			}
		});
	}
	
	private void addDragHandler(JComponent component) {
		component.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
		        screenX = e.getXOnScreen();
		        screenY = e.getYOnScreen();

		        myX = component.getX();
		        myY = component.getY();
			}
		});
		component.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
		        int deltaX = e.getXOnScreen() - screenX;
		        int deltaY = e.getYOnScreen() - screenY;

		        component.setLocation(myX + deltaX, myY + deltaY);
			}
		});
	}
}
