package view.block.blockHandler;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import model.CSGModel;
import model.SizeMeasurements;
import view.IView;
import view.block.BlockComponent;
import view.block.PrimShapeBlock;

/**
 * Event handler for creating a popup on right clicking components.
 * @author chriz
 *
 */
public class PopUpHandler extends CustomHandler {
	
	/** Map containing all GUI elements for easy access by name. */
	private Map<String, Component> components;
	
	private JDialog popup;
	
	public PopUpHandler(BlockComponent attachedComponent,IView view) {
		super(attachedComponent, view);
		components = new HashMap<>();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3) {
			switch(attachedComponent.blockType) {
			case Operator: createCSGPopUp(); break;
			case PrimShape: {
				switch(((PrimShapeBlock) attachedComponent).primType) {
				case CUBE: createCubePopUp();
					break;
				case CYLINDER: createCylinderPopUp();
					break;
				case SPHERE: createSpherePopUp();
					break;
				default:
					break;
				}
			}
			default:
				break;
			}
		}
	}
	
	private void createCSGPopUp() {
		popup = getPopUpEmptyPopUp();
		JPanel mainPanel = getPopUpPanel();
		popup.add(mainPanel);
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(getPositionPanel());
		mainPanel.add(getRotationPanel());
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JSpinner xSpinner = (JSpinner)components.get("xPosSpinner");
				JSpinner ySpinner = (JSpinner)components.get("yPosSpinner");
				JSpinner zSpinner = (JSpinner)components.get("zPosSpinner");
				float xPos = Float.valueOf(xSpinner.getValue().toString());
				float yPos = Float.valueOf(ySpinner.getValue().toString());
				float zPos = Float.valueOf(zSpinner.getValue().toString());
				
				Vector3f newPos = new Vector3f(xPos, yPos, zPos);
				view.getCSGModelManager().getCSGModel(attachedComponent).getCSG().setLocalTranslation(newPos);
				xSpinner = (JSpinner)components.get("xRotSpinner");
				ySpinner = (JSpinner)components.get("yRotSpinner");
				zSpinner = (JSpinner)components.get("zRotSpinner");
				float xRot = Float.valueOf(xSpinner.getValue().toString());
				float yRot = Float.valueOf(ySpinner.getValue().toString());
				float zRot = Float.valueOf(zSpinner.getValue().toString());
				
				Quaternion xQuat = new Quaternion();
				Quaternion yQuat = new Quaternion();
				Quaternion zQuat = new Quaternion();
				xQuat.fromAngleAxis(xRot * (FastMath.PI / 180), new Vector3f(0, 1, 0));
				yQuat.fromAngleAxis(yRot * (FastMath.PI / 180), new Vector3f(1, 0, 0));
				zQuat.fromAngleAxis(zRot * (FastMath.PI / 180), new Vector3f(0, 0, 1));
				Quaternion rotation = xQuat.mult(yQuat);
				rotation = rotation.mult(zQuat);
				view.getCSGModelManager().getCSGModel(attachedComponent).getCSG().setLocalRotation(rotation);
				// Move children aswell
				List<BlockComponent> children = view.getTreeManager().getChildren(attachedComponent);
				for(BlockComponent block : children) {
					view.getCSGModelManager().getCSGModel(block).getCSG().setLocalTranslation(newPos);
					view.getCSGModelManager().getCSGModel(block).getCSG().setLocalRotation(rotation);
				}
				view.getCSGModelManager().invokeCSGCalculation(view.getTreeManager().getRoot(attachedComponent));
				popup.setVisible(false);
			}
		});
		mainPanel.add(applyButton);
		popup.pack();
		popup.setVisible(true);
	}
	
	private void createCubePopUp() {
		popup = getPopUpEmptyPopUp();
		JPanel mainPanel = getPopUpPanel();
		popup.add(mainPanel);
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(getCubeSizePanel());
		mainPanel.add(getPositionPanel());
		mainPanel.add(getRotationPanel());
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JSpinner lengthSpinner = (JSpinner)components.get("lengthSpinner");
				JSpinner heightSpinner = (JSpinner)components.get("heightSpinner");
				JSpinner widthSpinner = (JSpinner)components.get("widthSpinner");
				SizeMeasurements size = new SizeMeasurements();
				
				size.length = Float.valueOf(lengthSpinner.getValue().toString());
				size.height = Float.valueOf(heightSpinner.getValue().toString());
				size.width = Float.valueOf(widthSpinner.getValue().toString());
				view.getCSGModelManager().resizeCSGModel(attachedComponent, size);
				
				JSpinner xSpinner = (JSpinner)components.get("xPosSpinner");
				JSpinner ySpinner = (JSpinner)components.get("yPosSpinner");
				JSpinner zSpinner = (JSpinner)components.get("zPosSpinner");
				float xPos = Float.valueOf(xSpinner.getValue().toString());
				float yPos = Float.valueOf(ySpinner.getValue().toString());
				float zPos = Float.valueOf(zSpinner.getValue().toString());
				
				Vector3f newPos = new Vector3f(xPos, yPos, zPos);
				view.getCSGModelManager().getCSGModel(attachedComponent).getCSG().setLocalTranslation(newPos);
				xSpinner = (JSpinner)components.get("xRotSpinner");
				ySpinner = (JSpinner)components.get("yRotSpinner");
				zSpinner = (JSpinner)components.get("zRotSpinner");
				float xRot = Float.valueOf(xSpinner.getValue().toString());
				float yRot = Float.valueOf(ySpinner.getValue().toString());
				float zRot = Float.valueOf(zSpinner.getValue().toString());

				Quaternion xQuat = new Quaternion();
				Quaternion yQuat = new Quaternion();
				Quaternion zQuat = new Quaternion();
				xQuat.fromAngleAxis(xRot * (FastMath.PI / 180), new Vector3f(0, 1, 0));
				yQuat.fromAngleAxis(yRot * (FastMath.PI / 180), new Vector3f(1, 0, 0));
				zQuat.fromAngleAxis(zRot * (FastMath.PI / 180), new Vector3f(0, 0, 1));
				Quaternion rotation = xQuat.mult(yQuat);
				rotation = rotation.mult(zQuat);
				view.getCSGModelManager().getCSGModel(attachedComponent).getCSG().setLocalRotation(rotation);
				view.getCSGModelManager().invokeCSGCalculation(view.getTreeManager().getRoot(attachedComponent));
				popup.setVisible(false);
			}
		});
		mainPanel.add(applyButton);
		popup.pack();
		popup.setVisible(true);
	}
	
	private void createSpherePopUp() {
		popup = getPopUpEmptyPopUp();
		JPanel mainPanel = getPopUpPanel();
		popup.add(mainPanel);
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(getSphereSizePanel());
		mainPanel.add(getPositionPanel());
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JSpinner radiusSpinner = (JSpinner)components.get("radiusSpinner");
				SizeMeasurements size = new SizeMeasurements();
				
				size.radius = Float.valueOf(radiusSpinner.getValue().toString());
				view.getCSGModelManager().resizeCSGModel(attachedComponent, size);
				
				JSpinner xSpinner = (JSpinner)components.get("xPosSpinner");
				JSpinner ySpinner = (JSpinner)components.get("yPosSpinner");
				JSpinner zSpinner = (JSpinner)components.get("zPosSpinner");
				float xPos = Float.valueOf(xSpinner.getValue().toString());
				float yPos = Float.valueOf(ySpinner.getValue().toString());
				float zPos = Float.valueOf(zSpinner.getValue().toString());
				
				Vector3f newPos = new Vector3f(xPos, yPos, zPos);
				view.getCSGModelManager().getCSGModel(attachedComponent).getCSG().setLocalTranslation(newPos);
				view.getCSGModelManager().invokeCSGCalculation(view.getTreeManager().getRoot(attachedComponent));
				popup.setVisible(false);
			}
		});
		mainPanel.add(applyButton);
		popup.pack();
		popup.setVisible(true);
	}

	private void createCylinderPopUp() {
		popup = getPopUpEmptyPopUp();
		JPanel mainPanel = getPopUpPanel();
		popup.add(mainPanel);
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(getCylinderSizePanel());
		mainPanel.add(getPositionPanel());
		mainPanel.add(getRotationPanel());
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JSpinner lengthSpinner = (JSpinner)components.get("lengthSpinner");
				JSpinner radiusSpinner = (JSpinner)components.get("radiusSpinner");
				SizeMeasurements size = new SizeMeasurements();
				
				size.length = Float.valueOf(lengthSpinner.getValue().toString());
				size.radius = Float.valueOf(radiusSpinner.getValue().toString());
				view.getCSGModelManager().resizeCSGModel(attachedComponent, size);
				
				JSpinner xSpinner = (JSpinner)components.get("xPosSpinner");
				JSpinner ySpinner = (JSpinner)components.get("yPosSpinner");
				JSpinner zSpinner = (JSpinner)components.get("zPosSpinner");
				float xPos = Float.valueOf(xSpinner.getValue().toString());
				float yPos = Float.valueOf(ySpinner.getValue().toString());
				float zPos = Float.valueOf(zSpinner.getValue().toString());
				
				Vector3f newPos = new Vector3f(xPos, yPos, zPos);
				view.getCSGModelManager().getCSGModel(attachedComponent).getCSG().setLocalTranslation(newPos);
				xSpinner = (JSpinner)components.get("xRotSpinner");
				ySpinner = (JSpinner)components.get("yRotSpinner");
				zSpinner = (JSpinner)components.get("zRotSpinner");
				float xRot = Float.valueOf(xSpinner.getValue().toString());
				float yRot = Float.valueOf(ySpinner.getValue().toString());
				float zRot = Float.valueOf(zSpinner.getValue().toString());
				
				Quaternion xQuat = new Quaternion();
				Quaternion yQuat = new Quaternion();
				Quaternion zQuat = new Quaternion();
				xQuat.fromAngleAxis(xRot * (FastMath.PI / 180), new Vector3f(0, 1, 0));
				yQuat.fromAngleAxis(yRot * (FastMath.PI / 180), new Vector3f(1, 0, 0));
				zQuat.fromAngleAxis(zRot * (FastMath.PI / 180), new Vector3f(0, 0, 1));
				Quaternion rotation = xQuat.mult(yQuat);
				rotation = rotation.mult(zQuat);
				view.getCSGModelManager().getCSGModel(attachedComponent).getCSG().setLocalRotation(rotation);
				view.getCSGModelManager().invokeCSGCalculation(view.getTreeManager().getRoot(attachedComponent));
				popup.setVisible(false);
			}
		});
		mainPanel.add(applyButton);
		popup.pack();
		popup.setVisible(true);
	}
	
	private JDialog getPopUpEmptyPopUp() {
		JDialog popup = new JDialog();
		ImageIcon icon = new ImageIcon("assets/icon.png");
		popup.setIconImage(icon.getImage());
		popup.setTitle("Configure Parameters");
		popup.setModal(true);
		popup.setLocationRelativeTo(null);
		popup.setResizable(false);
		//popup.setSize(800, 600);
		return popup;
	}
	
	private JPanel getPopUpPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		return mainPanel;
	}
	
	private JPanel getPositionPanel() {
		JPanel posPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JLabel xPosLabel = new JLabel("X-Pos");
		JLabel yPosLabel = new JLabel("Y-Pos");
		JLabel zPosLabel = new JLabel("Z-Pos");
		JSpinner xPosSpinner = new JSpinner(new SpinnerNumberModel(0f, -50f, 50f, 0.1f));
		JSpinner yPosSpinner = new JSpinner(new SpinnerNumberModel(0f, -50f, 50f, 0.1f));
		JSpinner zPosSpinner = new JSpinner(new SpinnerNumberModel(0f, -50f, 50f, 0.1f));
		components.put("xPosSpinner", xPosSpinner);
		components.put("yPosSpinner", yPosSpinner);
		components.put("zPosSpinner", zPosSpinner);
		
		xPosSpinner.setPreferredSize(new Dimension(120,20));
		yPosSpinner.setPreferredSize(new Dimension(120,20));
		zPosSpinner.setPreferredSize(new Dimension(120,20));
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 10, 0, 10);
		
		posPanel.add(xPosLabel, c);
		c.gridx = 1;
		posPanel.add(yPosLabel, c);
		c.gridx = 2;
		posPanel.add(zPosLabel, c);
		c.gridx = 0;
		c.gridy = 1;
		posPanel.add(xPosSpinner, c);
		c.gridx = 1;
		posPanel.add(yPosSpinner, c);
		c.gridx = 2;
		posPanel.add(zPosSpinner, c);
		
		CSGModel shape = view.getCSGModelManager().getCSGModel(attachedComponent);
		Vector3f pos = shape.getCSG().getLocalTranslation();
		xPosSpinner.setValue(pos.x);
		yPosSpinner.setValue(pos.y);
		zPosSpinner.setValue(pos.z);
		
		return posPanel;
	}
	
	private JPanel getRotationPanel() {
		JPanel rotPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JLabel xRotLabel = new JLabel("X-Rot");
		JLabel yRotLabel = new JLabel("Y-Rot");
		JLabel zRotLabel = new JLabel("Z-Rot");
		JSpinner xRotSpinner = new JSpinner(new SpinnerNumberModel(0f, -360f, 360f, 1));
		JSpinner yRotSpinner = new JSpinner(new SpinnerNumberModel(0f, -360f, 360f, 1));
		JSpinner zRotSpinner = new JSpinner(new SpinnerNumberModel(0f, -360f, 360f, 1));
		components.put("xRotSpinner", xRotSpinner);
		components.put("yRotSpinner", yRotSpinner);
		components.put("zRotSpinner", zRotSpinner);
		
		xRotSpinner.setPreferredSize(new Dimension(120,20));
		yRotSpinner.setPreferredSize(new Dimension(120,20));
		zRotSpinner.setPreferredSize(new Dimension(120,20));
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 10, 0, 10);
		
		rotPanel.add(xRotLabel, c);
		c.gridx = 1;
		rotPanel.add(yRotLabel, c);
		c.gridx = 2;
		rotPanel.add(zRotLabel, c);
		c.gridx = 0;
		c.gridy = 1;
		rotPanel.add(xRotSpinner, c);
		c.gridx = 1;
		rotPanel.add(yRotSpinner, c);
		c.gridx = 2;
		rotPanel.add(zRotSpinner, c);
		
		return rotPanel;
	}
	
	public JPanel getCubeSizePanel() {
		JPanel sizePanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JLabel xPosLabel = new JLabel("Length");
		JLabel yPosLabel = new JLabel("Height");
		JLabel zPosLabel = new JLabel("Width");
		JSpinner xPosSpinner = new JSpinner(new SpinnerNumberModel(0f, -50f, 50f, 0.1f));
		JSpinner yPosSpinner = new JSpinner(new SpinnerNumberModel(0f, -50f, 50f, 0.1f));
		JSpinner zPosSpinner = new JSpinner(new SpinnerNumberModel(0f, -50f, 50f, 0.1f));
		components.put("lengthSpinner", xPosSpinner);
		components.put("heightSpinner", yPosSpinner);
		components.put("widthSpinner", zPosSpinner);
		
		xPosSpinner.setPreferredSize(new Dimension(120,20));
		yPosSpinner.setPreferredSize(new Dimension(120,20));
		zPosSpinner.setPreferredSize(new Dimension(120,20));
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 10, 0, 10);
		
		sizePanel.add(xPosLabel, c);
		c.gridx = 1;
		sizePanel.add(yPosLabel, c);
		c.gridx = 2;
		sizePanel.add(zPosLabel, c);
		c.gridx = 0;
		c.gridy = 1;
		sizePanel.add(xPosSpinner, c);
		c.gridx = 1;
		sizePanel.add(yPosSpinner, c);
		c.gridx = 2;
		sizePanel.add(zPosSpinner, c);
		
		CSGModel shape = view.getCSGModelManager().getCSGModel(attachedComponent);
		SizeMeasurements size = shape.getSize();
		xPosSpinner.setValue(size.length);
		yPosSpinner.setValue(size.height);
		zPosSpinner.setValue(size.width);
		
		return sizePanel;
	}
	
	public JPanel getSphereSizePanel() {
		JPanel sizePanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JLabel radiusLabel = new JLabel("Radius");
		JSpinner radiusSpinner = new JSpinner(new SpinnerNumberModel(0f, -50f, 50f, 0.1f));
		components.put("radiusSpinner", radiusSpinner);
		
		radiusSpinner.setPreferredSize(new Dimension(120,20));
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(0, 10, 0, 10);
		
		c.gridx = 1;
		sizePanel.add(radiusLabel, c);
		c.gridy = 1;
		sizePanel.add(radiusSpinner, c);
		
		CSGModel shape = view.getCSGModelManager().getCSGModel(attachedComponent);
		SizeMeasurements size = shape.getSize();
		radiusSpinner.setValue(size.radius);
		
		return sizePanel;
	}
	
	public JPanel getCylinderSizePanel() {
		JPanel posPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JLabel lengthLabel = new JLabel("Length");
		JLabel radiusLabel = new JLabel("Radius");
		JSpinner lengthSpinner = new JSpinner(new SpinnerNumberModel(0f, -50f, 50f, 0.1f));
		JSpinner radiusSpinner = new JSpinner(new SpinnerNumberModel(0f, -50f, 50f, 0.1f));
		components.put("lengthSpinner", lengthSpinner);
		components.put("radiusSpinner", radiusSpinner);
		
		lengthSpinner.setPreferredSize(new Dimension(120,20));
		radiusSpinner.setPreferredSize(new Dimension(120,20));
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 10, 0, 10);
		
		posPanel.add(lengthLabel, c);
		c.gridx = 1;
		posPanel.add(radiusLabel, c);
		c.gridx = 0;
		c.gridy = 1;
		posPanel.add(lengthSpinner, c);
		c.gridx = 1;
		posPanel.add(radiusSpinner, c);
		
		CSGModel shape = view.getCSGModelManager().getCSGModel(attachedComponent);
		SizeMeasurements size = shape.getSize();
		lengthSpinner.setValue(size.length);
		radiusSpinner.setValue(size.radius);
		
		return posPanel;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
