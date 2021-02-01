package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import controller.ICSGModelManager;
import controller.ITreeManager;
import controller.JME;
import view.block.BlockComponent;
import view.block.OperatorBlock;
import view.block.OperatorBlock.OperatorBlockType;
import view.block.PrimShapeBlock;
import view.block.PrimShapeBlock.PrimShapeType;
import view.block.blockHandler.HandlerManager;
import view.block.blockHandler.SpawnHandler;
import view.menuBarHandler.AboutHandler;
import view.menuBarHandler.ControlsHandler;
import view.menuBarHandler.ExportHandler;
/**
 * Organizes the GUI using Swing.
 * @author chriz
 *
 */
public class View implements IView {
	
	private ITreeManager treeMan;
	
	private ICSGModelManager modelMan;
	
	private JME jme;
	
	/** Main window */
	private JFrame frame;
	/** Transparent panel for dragging elements above other panels */
	private JPanel transferPanel;
	/** Upper part of window, consists of drawer and workspace */
	private JPanel blockViewPanel;
	/** Panel where blocks are dragged around */
	private JPanel workspacePanel;
	/** Hosts jMonkey viewport */
	private JPanel jMonkeyPanel;
	
	private List<BlockComponent> highlightedBlocks;
	
	private final int WINDOW_WIDTH = 1280;
	private final int WINDOW_HEIGHT = 720;
	
	public View(ITreeManager treeMan, ICSGModelManager modelMan, JME jme) {
		this.treeMan = treeMan;
		this.modelMan = modelMan;
		this.jme = jme;
		highlightedBlocks = new ArrayList<>();
	}
	
	/**
	 * Creates and displays GUI.
	 */
	@Override
	public void initView() {
		// Enable Anti Aliasing for GUI Elements
		System.setProperty("awt.useSystemAAFontSettings","on");
		System.setProperty("swing.aatext", "true");
		
		// Main JFrame
		frame = new JFrame();
		addMenuToolbar();
		frame.setLayout(new GridLayout(2, 1));
		
		frame.add(createBlockViewPanel());
		frame.add(createJMonkeyPanel());
		
		ImageIcon icon = new ImageIcon("assets/icon.png");
		frame.setIconImage(icon.getImage());
		frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setTitle("CSG Editor");
		frame.setMinimumSize(new Dimension(800, 300));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startJME();
		frame.setVisible(true);
	}

	@Override
	public ITreeManager getTreeManager() {
		return treeMan;
	}
	
	@Override
	public ICSGModelManager getCSGModelManager() {
		return modelMan;
	}
	
	private void startJME() {
		java.awt.EventQueue.invokeLater(new Runnable() {
	    	@Override
			public void run() {
	    		AppSettings settings = new AppSettings(true);
	    		settings.setWidth(640);
	    		settings.setHeight(480);
	    		settings.setFrameRate(60);
	    		settings.setSamples(4);
	    		jme.setSettings(settings);
	    		jme.createCanvas();
	    		JmeCanvasContext ctx = (JmeCanvasContext) jme.getContext();
	    		ctx.setSystemListener(jme);
	    		jMonkeyPanel.add(ctx.getCanvas(), BorderLayout.CENTER);
	    		jme.startCanvas();
	    	}
	    });
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
		scrollPane.getVerticalScrollBar().setUnitIncrement(12);
		scrollPane.setBorder(null);
		drawerPanel.add(scrollPane, BorderLayout.CENTER);
		// Fill drawer with PrimitiveBlocks
		for(PrimShapeType e : PrimShapeType.values()) {
			vBox.add(Box.createRigidArea(new Dimension(150, 30)));
			PrimShapeBlock block = new PrimShapeBlock(e);
			HandlerManager hm = new HandlerManager(block, this);
			hm.addHandler(new SpawnHandler(block, this));
			block.addMouseListener(hm);
			vBox.add(block);
		}
		// Fill drawer with OperatorBlocks
		for(OperatorBlockType e : OperatorBlockType.values()) {
			vBox.add(Box.createRigidArea(new Dimension(150, 30)));
			OperatorBlock block = new OperatorBlock(e);
			HandlerManager hm = new HandlerManager(block, this);
			hm.addHandler(new SpawnHandler(block, this));
			block.addMouseListener(hm);
			vBox.add(block);
		}
		vBox.add(Box.createRigidArea(new Dimension(150, 30)));
		return drawerPanel;
	}
	
	private void addMenuToolbar() {
		// Create each menu item
		JMenuBar bar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu spacer = new JMenu("|");
		spacer.setEnabled(false);
		JMenu help = new JMenu("Help");
		JMenuItem controls = new JMenuItem("Controls");
		JMenuItem about = new JMenuItem("About");
		JMenuItem export = new JMenuItem("Export highlighted model");
		
		// Add menu items to each other
		help.add(controls);
		help.add(about);
		file.add(export);
		bar.add(file);
		bar.add(spacer);
		bar.add(help);
		
		// Set Menu bar for frame
		frame.setJMenuBar(bar);
		
		// Add event handler for menu items
		about.addActionListener(new AboutHandler());
		controls.addActionListener(new ControlsHandler());
		export.addActionListener(new ExportHandler(modelMan));
	}
	
	/**
	 * Because of swings null window manager, automatic resizing is not working.
	 * Hence, manual correction when resizing is needed.
	 * Listens to componentResized and calculates the new panel size.
	 * @param blockview JPanel to attach the Handler to.
	 */
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
	
	@Override
	public JFrame getFrame() {
		return frame;
	}
	
	@Override
	public JPanel getWorkspacePanel() {
		return workspacePanel;
	}
	
	@Override
	public JPanel getBlockViewPanel() {
		return blockViewPanel;
	}
	
	@Override
	public JPanel getTransferPanel() {
		return transferPanel;
	}
	
	@Override
	public void highlightBlocks(List<BlockComponent> blocksToHighlight) {
		for(BlockComponent block : blocksToHighlight) {
			block.color += 10000;
		}
		highlightedBlocks = blocksToHighlight;
		getFrame().repaint();
	}
	
	@Override
	public void unhighlightBlocks() {
		for(BlockComponent block : highlightedBlocks) {
			block.color -= 10000;
		}
		highlightedBlocks = new ArrayList<>();
		getFrame().repaint();
	}
}
