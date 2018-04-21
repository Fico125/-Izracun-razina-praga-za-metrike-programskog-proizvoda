package GUI;

import java.awt.Choice;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;

public class Panela extends Panel {
	
	private TextField k,l;
	private Choice boja;
	private Ksustav ks;
	
	public Panela() {
		
		setLayout(new GridLayout(1, 0, 20, 20));
		
		
		JButton ucitaj = new JButton("Dodaj datoteku");
		
		JButton nacrtaj = new JButton("Izracunaj");
		
		ucitaj.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ae) {
		        JFileChooser fileChooser = new JFileChooser();
		        int returnValue = fileChooser.showOpenDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
		          File selectedFile = fileChooser.getSelectedFile();
		          System.out.println(selectedFile.getName());
		        }
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
