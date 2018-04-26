package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Panel;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Ksustav extends Panel {
	
	private int pocx, pocy;
	public ArrayList<Pravac> getPravci() {
		return pravci;
	}

	private ArrayList<Pravac> pravci;
	
	public Ksustav(int x, int y) {
		
		pocx = x;
		pocy = y;
		setPreferredSize(new Dimension(500, 500));
		setBackground(Color.WHITE);
		pravci = new ArrayList<Pravac>();
		
		
		
	}
	
	public void paint(Graphics g) {
		
		nacrtaj_graf(g);
		for (int i = 0; i < pravci.size(); i++)
			pravci.get(i).nacrtaj(g, pocx, pocy);
		
	}
	
	public void nacrtaj_graf(Graphics g) {
		
		g.drawLine(pocx, pocy, pocx, pocy-400);
		g.drawLine(pocx, pocy, pocx+400, pocy);
		
		float br = 0;
		for (int i = pocx+40; i < pocx+400; i+=40){
			DecimalFormat df = new DecimalFormat("#.#");
			br += 0.1;
			
			//String.valueOf(br)
			g.drawString(df.format(br), i, pocy+20);
		}
		br = 0;
		for (int i = pocy-40; i > pocy-400; i-=40){
			DecimalFormat df = new DecimalFormat("#.#");
			br += 0.1;
			g.drawString(df.format(br), pocx-20, i);
		}
	}

}
