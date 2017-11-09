package es.deusto.prog3.extra;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel {
	
	
	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;
	
	public DrawingPanel(BufferedImage image) {
		super();
		this.image = image;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
		
	}
}
