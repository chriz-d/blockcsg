package controller;

import view.View;

public class Controller {
	
	private View view;
	private Model model;
	
	public void start() {
		view = new View();
		view.initView();
	}
	
	public static void main(String[] args) {
		//org.swingexplorer.Launcher.launch();
		Controller controller = new Controller();
		controller.start();
	}
}
