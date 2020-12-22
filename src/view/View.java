package view;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import controller.Controller;
import model.Shape;
import view.OperatorBlock.OperatorBlockType;
import view.PrimShapeBlock.PrimShapeType;

public class View {
	
	private Controller controller;
	
	// Main window and panels for easy access
	private JFrame frame;
	private JPanel transferPanel;
	private JPanel blockViewPanel;
	private JPanel workspacePanel;
	private JPanel jMonkeyPanel;
	
	private BlockComponent lastSelected;
	
	private final int WINDOW_WIDTH = 1280;
	private final int WINDOW_HEIGHT = 720;
	
	public View(Controller controller) {
		this.controller = controller;
	}
	
	public void initView() {
		// Main JFrame
		frame = new JFrame();
		frame.setLayout(new GridLayout(2, 1));
		
		frame.add(createBlockViewPanel());
		frame.add(createJMonkeyPanel());
		
		frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setTitle("CSG Editor");
		frame.setMinimumSize(new Dimension(800, 300));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public Controller getController() {
		return controller;
	}
	
	public void setJMonkeyWindow(Canvas canvas) {
		jMonkeyPanel.add(canvas, BorderLayout.CENTER);
	}
	
	private JLayeredPane createBlockViewPanel() {
		JLayeredPane layeredPane = new JLayeredPane();
		transferPanel = new JPanel(null);
		transferPanel.setBounds(0, 0, 400, 400);
		blockViewPanel = new JPanel(new BorderLayout());
		workspacePanel = new JPanel(null);
		workspacePanel.setBackground(new Color(0xedf6f9));
		// Layered pane does not have layout manager, hence manual position and size
		// needed
		addBlockViewResizeHandler(blockViewPanel);
		transferPanel.setOpaque(false);
		
		// Inserting panels to each other and JFrame
		blockViewPanel.add(createDrawerPanel(), BorderLayout.LINE_START);
		blockViewPanel.add(workspacePanel, BorderLayout.CENTER);
		layeredPane.add(blockViewPanel, JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(transferPanel, JLayeredPane.DRAG_LAYER);
		return layeredPane;
	}
	
	private JPanel createJMonkeyPanel() {
		jMonkeyPanel = new JPanel(new BorderLayout());
		jMonkeyPanel.setBackground(Color.DARK_GRAY);
		return jMonkeyPanel;
	}
	
	private JPanel createDrawerPanel() {
		JPanel drawerPanel = new JPanel(new BorderLayout());
		Box vBox = Box.createVerticalBox();
		JScrollPane scrollPane = new JScrollPane(vBox, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		drawerPanel.add(scrollPane, BorderLayout.CENTER);
		for(PrimShapeType e : PrimShapeType.values()) {
			vBox.add(Box.createRigidArea(new Dimension(150, 30)));
			PrimShapeBlock block = new PrimShapeBlock(e);
			block.addMouseListener(new SpawnHandler(block, this));
			vBox.add(block);
		}
		for(OperatorBlockType e : OperatorBlockType.values()) {
			vBox.add(Box.createRigidArea(new Dimension(150, 30)));
			OperatorBlock block = new OperatorBlock(e);
			block.addMouseListener(new SpawnHandler(block, this));
			vBox.add(block);
		}
		vBox.add(Box.createRigidArea(new Dimension(150, 30)));
		return drawerPanel;
	}
	
	// Resizes BlockView, because no layout manager in LayeredPane
	private void addBlockViewResizeHandler(JPanel blockview) {
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				blockview.setBounds(0, 0, frame.getWidth() + 30, frame.getHeight()/ 2);
				transferPanel.setBounds(0, 0, frame.getWidth() + 30, frame.getHeight()/ 2);
				frame.repaint();
			}
		});
	}
	
	/*
	 * Resizes all leaf nodes of tree. Tree is determined by given block by controller.
	 * doDelete determines if the given block is deleted beforehand, for a correct depth
	 */
	public void resizeTree(BlockComponent block, boolean doDelete) {
		BlockComponent root = controller.getRoot(block);
		if(root == null) {
			return; // Block not existing
		}
		// Get every node in tree
		List<BlockComponent> allNodes = controller.getChildren(root);
		allNodes.add(root);
		if(doDelete) {
			allNodes.remove(block);
			controller.removeFromTree(block);
		}
		// Iterate over list and fix width of each component and translate children
		for(BlockComponent e : allNodes) {
			if(e instanceof OperatorBlock) {
				int width = ((OperatorBlock) e).correctWidth(controller.getDepth(e));
				// Translate children to the left (<--)
				if(controller.getLeft(e) != null) {
					List<BlockComponent> childrenToMoveLeft = controller.getChildren(controller.getLeft(e));
					childrenToMoveLeft.add(controller.getLeft(e));
					for(BlockComponent f : childrenToMoveLeft) {
						f.setLocation((int)f.getLocation().getX() - width, (int)f.getLocation().getY());
					}
				}
				// Translate children to the right (-->)
				if(controller.getRight(e) != null) {
					List<BlockComponent> childrenToMoveRight = controller.getChildren(controller.getRight(e));
					childrenToMoveRight.add(controller.getRight(e));
					for(BlockComponent f : childrenToMoveRight) {
						f.setLocation((int)f.getLocation().getX() + width, (int)f.getLocation().getY());
					}
				}
			}
		}
	}
	
	
	public void setLastSelected(BlockComponent lastSelected) {
		this.lastSelected = lastSelected;
	}
	
	public BlockComponent getLastSelected() {
		return lastSelected;
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public JPanel getWorkspacePanel() {
		return workspacePanel;
	}
	
	public JPanel getBlockViewPanel() {
		return blockViewPanel;
	}
	
	public JPanel getTransferPanel() {
		return transferPanel;
	}
}
