package GUI;

import java.applet.Applet;
import java.awt.Toolkit;
/*Promatramo pravce y=kx+l, gdje su k i l integeri. Napi�ite applet koji u�itava u jedan textfield parametar
 *  k, a u drugi parametar l i tako crta pravce u ravnini sa mogou�no��u odabira boje za svaki od pravaca.
 *   Nacrtajte i koordinatne osi.*/
public class Aplet extends Applet {
	
	public void init() {
		
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		add(new Panela());
		
	}

}
