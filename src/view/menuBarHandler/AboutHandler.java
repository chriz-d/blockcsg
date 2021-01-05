package view.menuBarHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
/**
 * Shows a dialog containing miscellaneous info.
 * @author chriz
 *
 */
public class AboutHandler implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(null, "This software was created in the scope of a bachelor thesis by Christian Dorn.");
	}

}
