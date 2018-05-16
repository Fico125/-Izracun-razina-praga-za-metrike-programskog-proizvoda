package GUI;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import weka.core.Instances;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Aplet aplet = new Aplet();
		JFrame myFrame = new JFrame("Test");
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.getContentPane().add(aplet);
		aplet.init();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		myFrame.setSize(screenSize.width, screenSize.height);
		myFrame.setVisible(true);
		
		try {
			CrossValidate.CrossVal();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
