package es.deusto.prog3.extra;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import es.deusto.prog3.extra.utils.Utils;

/**
 * Small project I wanted to make. This is what it does.
 * <ul>
 * <li>Load a source image from file (not too big, as it will take long).
 * <li>Get the list of all colors from the source image
 * <li>Generate two blank BufferedImages (image1 and image2)
 * <li>Draw a line of a random color from the list in a random position on
 * image1
 * <li>Compare both images to the source image.
 * <li>If image1 is closer to the source image than image2, copy image1 to
 * image2; if not, copy image2 to image1 and repeat the process a number of
 * iterations.
 * </ul>
 * 
 * @author Pablo Alonso
 *
 */
public class Main {

	private static int iterations = 1000000;
	private static int maxLength;
	private static BufferedImage image1;
	private static BufferedImage image2;
	private static BufferedImage source;

	private static ArrayList<Integer> colors = new ArrayList<Integer>();

	public static void main(String[] args) {
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(null);
		File imageFile = fc.getSelectedFile();
		source = null;
		try {
			source = ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.exit(1);
		}

		try {
			maxLength = Utils.max(source.getHeight(), source.getWidth()) / 50;
		} catch (NullPointerException e) {
<<<<<<< HEAD
=======
			// TODO Auto-generated catch block
>>>>>>> branch 'master' of https://github.com/kuu22/random-image-copier.git
			JOptionPane.showMessageDialog(null, "That is not a valid image", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
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
		DrawingPanel canvas = new DrawingPanel(image1);
		canvas.setPreferredSize(new Dimension(source.getWidth(), source.getHeight()));
		frame.getContentPane().add(canvas);
		frame.pack();
		frame.setVisible(true);
		Thread updater = new Thread() {
			public void run() {
				long start = System.currentTimeMillis();
				for (int i = 1; i <= iterations; i++) {

					Graphics2D g1 = (Graphics2D) image1.getGraphics();
					g1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g1.setColor(new Color(colors.get(Utils.randomInt(colors.size() - 1))));
					g1.setStroke(new BasicStroke(maxLength / 4f));
					int x = Utils.randomInt(image1.getWidth() - 1);
					int y = Utils.randomInt(image1.getHeight() - 1);
					int x2 = Utils.randomInt(x - maxLength, x + maxLength);
					int y2 = Utils.randomInt(y - maxLength, y + maxLength);
					g1.drawLine(x, y, x2, y2);
					g1.dispose();
					long diffToSource1 = getDiff(image1, source, x, y, x2, y2);
					long diffToSource2 = getDiff(image2, source, x, y, x2, y2);
					if (diffToSource1 < diffToSource2) {
						Graphics g = image2.getGraphics();
						g.drawImage(image1, 0, 0, null);
						g.dispose();
					} else {
						Graphics g = image1.getGraphics();
						g.drawImage(image2, 0, 0, null);
						g.dispose();
					}

					if (i % 100 == 0) {
						canvas.repaint();
						if (i % 100000 == 0 && maxLength > 4) {
							maxLength -= 1;
						}
					}

				}
				JOptionPane.showMessageDialog(frame, "Finished! " + iterations + " iterations in "
						+ (System.currentTimeMillis() - start) / 1000 + " seconds.");
			}
		};
		
		updater.start();
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				updater.interrupt();
			}
			
		});

	}

	public static long getDiff(BufferedImage img1, BufferedImage img2, int x, int y, int x2, int y2) {
		long diff = 0;
		if (x2 < x) {
			int temp = x2;
			x2 = x;
			x = temp;

		}

		if (y2 < y) {
			int temp = y2;
			y2 = y;
			y = temp;

		}
		for (int i = x; i < x2; i++) {
			if (i >= img1.getWidth() || i <= 0) {
				break;
			}
			for (int j = y; j < y2; j++) {
				if (j >= img1.getHeight() || j <= 0) {
					break;
				}
				diff += pixelDiff(img1.getRGB(i, j), img2.getRGB(i, j));

			}
		}

		return diff;
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
