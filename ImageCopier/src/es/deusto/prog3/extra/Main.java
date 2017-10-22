package es.deusto.prog3.extra;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import es.deusto.prog3.extra.utils.Utils;

/**
 * Small project I wanted to make. This is what it does.
 * - Load a source image from file.
 * - Get the list of all colors from the source image
 * - Generate two blank BufferedImages (image1 and image2)
 * - Draw a circle of a random color from the list in a random position on image1
 * - Compare both images to the source image. 
 * - If it's closer to the source image than image2, copy image1 to image2; if not, copy image2 to image1 and repeat the process a number of iterations.
 * 
 * Note: performance is very slow, only use with smaller images
 * @author Pablo Alonso
 *
 */
public class Main {
	
	private static int iterations = 1000000;
	private static int circleRadius = 10;
	static BufferedImage image1;
	static BufferedImage image2;
	static BufferedImage source;
	
	private static ArrayList<Integer> colors = new ArrayList<Integer>();

	public static void main(String[] args) {
		File imageFile = new File("image.jpg");
		source = null;
		try {
			source = ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < source.getWidth(); i++) {
			for (int j = 0; j < source.getHeight(); j++) {
				colors.add(source.getRGB(i, j));
			}
		}
		
		image1 = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
		image2 = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(source.getWidth(), source.getHeight());
		Canvas canvas = new Canvas();
		canvas.setSize(source.getWidth(), source.getHeight());
		frame.getContentPane().add(canvas);
		frame.setVisible(true);
		new Thread() {
			public void run() {
				for (int i = 0; i < iterations; i++) {
					Graphics g1 = image1.getGraphics();
					g1.setColor(new Color(colors.get(Utils.randomInt(colors.size() - 1))));
					g1.fillOval(Utils.randomInt(image1.getWidth()), Utils.randomInt(image1.getHeight()), circleRadius, circleRadius);
					g1.dispose();
					double diffToSource1 = getDiffPercent(image1, source);
					double diffToSource2 = getDiffPercent(image2, source);
					
					if(diffToSource1 < diffToSource2) {
						image2 = new BufferedImage(image1.getWidth(), image1.getHeight(), image1.getType());
						Graphics g = image2.getGraphics();
						g.drawImage(image1, 0, 0, null);
						g.dispose();
					} else {
						image1 = new BufferedImage(image2.getWidth(), image2.getHeight(), image2.getType());
						Graphics g = image1.getGraphics();
						g.drawImage(image2, 0, 0, null);
						g.dispose();
					}
					if(i % 1000 == 0) {
						Graphics canvasG = canvas.getGraphics();
						canvasG.drawImage(image1, 0, 0, null);
						canvasG.dispose();
						System.out.println(i);
					}
					
				}
				System.out.println("Finished! " + iterations + " iterations.");
			}
		}.start();
		
	}
	
	public static double getDiffPercent(BufferedImage img1, BufferedImage img2) {
		// TODO needs optimization
		int height = img1.getHeight();
		int width = img1.getWidth();
		long diff = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				diff += pixelDiff(img1.getRGB(i, j), img2.getRGB(i, j));
			}
		}
		
		long maxDiff = 3L * 255 * width * height;
		
		return 100.0 * diff/maxDiff;
	}
	
	public static int pixelDiff(int a, int b) {
		int r1 = (a >> 16) & 0xff;
		int g1 = (a >> 8) & 0xff;
		int b1 = a & 0xff;
		int r2 = (b >> 16) & 0xff;
		int g2 = (b >> 8) & 0xff;
		int b2 = b & 0xff;
		
		return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
		
	}
	
	

}
