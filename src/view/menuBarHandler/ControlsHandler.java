package view.menuBarHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
/**
 * Shows a dialog on how to use software.
 * @author chriz
 *
 */
public class ControlsHandler implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(null, "Use left click to drag and drop elements from the left onto the workspace.\n"
				+ " To delete blocks, just drag them out of bounds or back into the drawer.");
		
	}

}
