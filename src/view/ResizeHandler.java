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
		view.getBlockViewPanel().setBounds(0, 0, (view.getFrame().getWidth() + 
				view.getBlockViewPanel().getComponent(0).getWidth() )/ 2, view.getFrame().getHeight()- 30);
		view.getTransferPanel().setBounds(0, 0, (view.getFrame().getWidth() + 
				view.getBlockViewPanel().getComponent(0).getWidth() )/ 2, view.getFrame().getHeight()- 30);
	}
}
