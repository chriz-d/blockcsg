package controller;

import view.View;

/**
 * Start of application. Instantiates and starts the application.
 * @author chriz
 *
 */
public class Main {
	public static void main(String[] args) {
		//org.swingexplorer.Launcher.launch();
		TreeManager treeMan = new TreeManager();
		JME jme = new JME();
		CSGModelManager modelMan = new CSGModelManager(treeMan, jme);
		View view = new View(treeMan, modelMan, jme);
		view.initView();
	}
}
