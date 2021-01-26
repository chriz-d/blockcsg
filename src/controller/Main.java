package controller;

import view.IView;
import view.View;

/**
 * Start of application. Instantiates and starts the application.
 * @author chriz
 *
 */
public class Main {
	public static void main(String[] args) {
		//org.swingexplorer.Launcher.launch();
		ITreeManager treeMan = new TreeManager();
		JME jme = new JME();
		ICSGModelManager modelMan = new CSGModelManager(treeMan, jme);
		IView view = new View(treeMan, modelMan, jme);
		view.initView();
	}
}
