package view.menuBarHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import controller.Controller;
import support.ObjExporter;
/**
 * Creates the save dialog and invokes exporter to specified path.
 * @author chriz
 *
 */
public class ExportHandler implements ActionListener {
	
	private Controller controller;
	
	public ExportHandler(Controller controller) {
		this.controller = controller;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		int val = fc.showSaveDialog(null);
		if(val == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			ObjExporter.exportMesh(file.getAbsolutePath() + ".obj", 
					controller.getCurrentDisplayedMesh());
		}
	}

}
