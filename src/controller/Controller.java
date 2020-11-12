package controller;

import view.View;

public class Controller {
	
	private View view;
	
	public void start() {
		view = new View(this);
		view.initView();
	}
	
	public static void main(String[] args) {
		Controller controller = new Controller();
		controller.start();
	}
}
