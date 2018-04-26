package GUI;

import javax.swing.JFrame;

import weka.core.Instances;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Aplet aplet = new Aplet();
		JFrame myFrame = new JFrame("Test");
		myFrame.add(aplet);
		myFrame.pack();
		myFrame.setVisible(true);
		aplet.init();
		
		Instances data;
		do {
			data = Input.getData();
			}
		while(data == null);
		
		
	}
	
	
}
