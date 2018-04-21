package GUI;

import java.applet.Applet;
import java.awt.Toolkit;
/*Promatramo pravce y=kx+l, gdje su k i l integeri. Napišite applet koji uèitava u jedan textfield parametar
 *  k, a u drugi parametar l i tako crta pravce u ravnini sa mogouènošæu odabira boje za svaki od pravaca.
 *   Nacrtajte i koordinatne osi.*/
public class Aplet extends Applet {
	
	public void init() {
		
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		add(new Panela());
		
	}

}
