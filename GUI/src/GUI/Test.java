package GUI;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import weka.core.Instances;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Aplet aplet = new Aplet();
		JFrame myFrame = new JFrame("Test");
	
		myFrame.getContentPane().add(aplet);
		myFrame.pack();
		myFrame.setVisible(true);
		aplet.init();
		
		try {
			CrossValidate.CrossVal();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
