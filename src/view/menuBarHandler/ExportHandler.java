package view.menuBarHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import controller.ICSGModelManager;
import support.ObjExporter;
/**
 * Creates the save dialog and invokes exporter to specified path.
 * @author chriz
 *
 */
public class ExportHandler implements ActionListener {
	
	private ICSGModelManager modelMan;
	
	public ExportHandler(ICSGModelManager modelMan) {
		this.modelMan = modelMan;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		int val = fc.showSaveDialog(null);
		if(val == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			ObjExporter.exportMesh(file.getAbsolutePath() + ".obj", 
					modelMan.getHighlightedModel().getCSG().getMesh());
		}
	}

}
