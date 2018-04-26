package GUI;

import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import weka.core.Instances;

public class Panela extends Panel {
	
	private Ksustav ks;
	public static String filepath;
	public static String filename;
	public Instances data = null;
	
	public Panela() {
		
		setLayout(new GridLayout(1, 0, 20, 20));
		
		
		JButton ucitaj = new JButton("Dodaj datoteku");
		
		JButton nacrtaj = new JButton("Izracunaj");
		
		ucitaj.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ae) {
		    	  Input.readFile();
		      }
		    });
		
		
		Panel podaci = new Panel();
		podaci.setLayout(new GridLayout(0, 1, 0, 0));
	
		setBounds(0, 30, 30, 60);
		
		podaci.add(ucitaj);
		setBounds(0, 30, 30, 60);
		podaci.add(nacrtaj);
		add(podaci);
		
		ks = new Ksustav(40, 450);
		add(ks);
		
		
	}
}
	

	
	
