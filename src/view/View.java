package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import controller.Controller;
import support.Support;
import view.OperatorBlock.OperatorBlockType;
import view.PrimShapeBlock.PrimShapeType;
import view.PrimShapeBlock.PrimShapeSocket;

public class View {
	
	// Main window and panels for easy access
	public JFrame frame;
	public JPanel transferPanel;
	public JPanel blockViewPanel;
	public JPanel workspacePanel;
	
	private final int WINDOW_WIDTH = 1280;
	private final int WINDOW_HEIGHT = 720;
	
	public void initView() {
		// Main JFrame
		frame = new JFrame();
		frame.setLayout(new GridLayout(1, 2));
		
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
		transferPanel.setBounds(0, 0, 400, 400);
		blockViewPanel = new JPanel(new BorderLayout());
		workspacePanel = new JPanel(null);
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
		drawerPanel.add(scrollPane, BorderLayout.CENTER);
		for(PrimShapeType e : PrimShapeType.values()) {
			vBox.add(Box.createRigidArea(new Dimension(150, 30)));
			PrimShapeBlock block = new PrimShapeBlock(e, PrimShapeSocket.BOTH);
			block.addMouseListener(new SpawnHandler(block, this));
			vBox.add(block);
		}
		for(OperatorBlockType e : OperatorBlockType.values()) {
			vBox.add(Box.createRigidArea(new Dimension(150, 30)));
			OperatorBlock block = new OperatorBlock(e);
			block.addMouseListener(new SpawnHandler(block, this));
			vBox.add(block);
		}

		return drawerPanel;
	}
	
	// Resizes BlockView, because no layout manager in LayeredPane
	private void addBlockViewResizeHandler(JPanel blockview) {
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				blockview.setBounds(0, 0, (frame.getWidth() + blockview.getComponent(0).getWidth() )/ 2, frame.getHeight()- 30);
				transferPanel.setBounds(0, 0, (frame.getWidth() + blockview.getComponent(0).getWidth() )/ 2, frame.getHeight()- 30);
			}
		});
	}
}
