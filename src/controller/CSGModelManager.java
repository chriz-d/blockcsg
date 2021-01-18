package controller;

import java.util.HashMap;
import java.util.Map;

import model.CSGModel;
import view.block.BlockComponent;

public class CSGModelManager {
	
	private TreeManager treeMan;
	private Map<BlockComponent, CSGModel> modelMap;
	
	public CSGModelManager(TreeManager treeMan) {
		modelMap = new HashMap<>();
		this.treeMan = treeMan;
	}
	
	public void createCSGModel(BlockComponent block) {
		modelMap.put(block, new CSGModel(treeMan, this, block));
	}
	
	public void deleteCSGModel(BlockComponent block) {
		modelMap.remove(block);
	}
	
	public boolean hasCSGModel(BlockComponent block) {
		return modelMap.containsKey(block);
	}
	
	public void displayCSGModel(BlockComponent block) {
		CSGModel model = modelMap.get(block);
		JME.getInstance().addToSceneGraph(model.getCSG());
	}
	
	public void undisplayCSGModel(BlockComponent block) {
		CSGModel model = modelMap.get(block);
		JME.getInstance().removeFromSceneGraph(model.getCSG());
	}
	
	public CSGModel getCSG(BlockComponent block) {
		return modelMap.get(block);
	}
	
	public void invokeCSGCalculation(BlockComponent block) {
		new Thread(new CSGCalculator(modelMap.get(block), this)).start();
	}
}