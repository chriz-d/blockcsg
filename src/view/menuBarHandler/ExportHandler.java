package view.menuBarHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import controller.ITreeManager;
import controller.JME;
import controller.TreeManager;
import support.ObjExporter;
/**
 * Creates the save dialog and invokes exporter to specified path.
 * @author chriz
 *
 */
public class ExportHandler implements ActionListener {
	
	private ITreeManager treeMan;
	
	public ExportHandler(ITreeManager controller) {
		this.treeMan = controller;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
//		JFileChooser fc = new JFileChooser();
//		int val = fc.showSaveDialog(null);
//		if(val == JFileChooser.APPROVE_OPTION) {
//			File file = fc.getSelectedFile();
//			ObjExporter.exportMesh(file.getAbsolutePath() + ".obj", 
//					JME.getInstance().getCurrentDisplayedMesh());
//		}
	}

}
