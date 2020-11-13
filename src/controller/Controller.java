package controller;

import model.Model;
import view.View;

public class Controller {
	
	private View view;
	private Model model;
	
	public void start() {
		view = new View(this);
		view.initView();
		model = new Model();
	}
	
	public static void main(String[] args) {
		Controller controller = new Controller();
		controller.start();
	}
}
