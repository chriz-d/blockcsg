package controller;

import view.View;

public class Main {
	public static void main(String[] args) {
		//org.swingexplorer.Launcher.launch();
		TreeManager treeMan = new TreeManager();
		CSGModelManager modelMan = new CSGModelManager(treeMan);
		View view = new View(treeMan, modelMan);
		view.initView();
	}
}
