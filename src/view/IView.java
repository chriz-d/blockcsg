package view;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.ICSGModelManager;
import controller.ITreeManager;
import view.block.BlockComponent;

public interface IView {

	/**
	 * Creates and displays GUI.
	 */
	void initView();

	ITreeManager getTreeManager();

	ICSGModelManager getCSGModelManager();

	JFrame getFrame();

	JPanel getWorkspacePanel();

	JPanel getBlockViewPanel();

	JPanel getTransferPanel();

	List<BlockComponent> getHighlightedBlocks();

	void setHighlightedBlocks(List<BlockComponent> highlightedBlocks);

}