package view;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ResizeHandler extends ComponentAdapter {
	
	private View view;
	
	public ResizeHandler(View view) {
		this.view = view;
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		view.blockViewPanel.setBounds(0, 0, (view.frame.getWidth() + 
				view.blockViewPanel.getComponent(0).getWidth() )/ 2, view.frame.getHeight()- 30);
		view.transferPanel.setBounds(0, 0, (view.frame.getWidth() + 
				view.blockViewPanel.getComponent(0).getWidth() )/ 2, view.frame.getHeight()- 30);
	}
}
