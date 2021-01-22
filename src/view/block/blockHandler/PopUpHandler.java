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
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import controller.TreeManager;
import model.CSGModel;
import view.View;
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
	
	public PopUpHandler(BlockComponent attachedComponent,View view) {
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
				case PYRAMID: 
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
		JDialog popup = getPopUpEmptyPopUp();
		JPanel mainPanel = getPopUpPanel();
		popup.add(mainPanel);
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(getScalePanel());
		mainPanel.add(getPositionPanel());
		mainPanel.add(getRotationPanel());
		popup.pack();
		popup.setVisible(true);
	}
	
	private void createCubePopUp() {
		JDialog popup = getPopUpEmptyPopUp();
		JPanel mainPanel = getPopUpPanel();
		popup.add(mainPanel);
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(getScalePanel());
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
				
				newPos = new Vector3f(xRot, yRot, zRot);
				view.getCSGModelManager().getCSGModel(attachedComponent).getCSG().rotate(xRot, yRot, zRot);
				popup.setVisible(false);
			}
		});
		mainPanel.add(applyButton);
		popup.pack();
		popup.setVisible(true);
	}
	
	private void createSpherePopUp() {
		
	}

	private void createCylinderPopUp() {
	
	}
	
	private JDialog getPopUpEmptyPopUp() {
		JDialog popup = new JDialog();
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
	
	private JPanel getScalePanel() {
		JPanel scalePanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		JLabel scaleLabel = new JLabel("Scale");
		scalePanel.add(scaleLabel, c);
		
		JSpinner scale = new JSpinner();
		scale.setPreferredSize(new Dimension(120,20));
		c.gridy = 1;
		scalePanel.add(scale, c);
		return scalePanel;
	}
	
	private JPanel getPositionPanel() {
		JPanel posPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JLabel xPosLabel = new JLabel("X-Pos");
		JLabel yPosLabel = new JLabel("Y-Pos");
		JLabel zPosLabel = new JLabel("Z-Pos");
		JSpinner xPosSpinner = new JSpinner();
		JSpinner yPosSpinner = new JSpinner();
		JSpinner zPosSpinner = new JSpinner();
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
		JSpinner xRotSpinner = new JSpinner();
		JSpinner yRotSpinner = new JSpinner();
		JSpinner zRotSpinner = new JSpinner();
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
	
	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
