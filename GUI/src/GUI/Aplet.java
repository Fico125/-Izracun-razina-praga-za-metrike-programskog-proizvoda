package GUI;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Toolkit;

/* 
 * Klasa Aplet slu�i samo kako bi se u njoj inicializirala nova panela
 * */

public class Aplet extends Applet {
	
	public void init() {
		add(new Panela());		
	}



}
