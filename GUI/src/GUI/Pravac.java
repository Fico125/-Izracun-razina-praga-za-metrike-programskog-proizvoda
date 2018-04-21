package GUI;

import java.awt.Color;
import java.awt.Graphics;

public class Pravac {
	
	private int k,l;
	private Color b;
	
	public Pravac(int k, int l, Color boja) {
		
		this.k = k;
		this.l = l;
		b = boja;
		
	}
	
	public void nacrtaj(Graphics g, int pocx, int pocy) {
		
		g.setColor(b);
		
		int x1 = -20;
		int y1 = k*x1+l*10;
		int x2 = 420;
		int y2 = k*x2+l*10;
		
		g.drawLine(pocx + x1, pocy - y1, pocx + x2, pocy - y2);
		
	}

}
