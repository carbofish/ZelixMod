package net.utils.slick.tests;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import net.utils.slick.BasicGame;
import net.utils.slick.CanvasGameContainer;
import net.utils.slick.GameContainer;
import net.utils.slick.Graphics;
import net.utils.slick.SlickException;
import net.utils.slick.util.Log;

/**
 * Quick test to confirm canvas size is reported correctly
 * 
 * @author kevin
 */
public class CanvasSizeTest extends BasicGame {
	
	/**
	 * Create test
	 */
	public CanvasSizeTest() {
		super("Test");
	}

	/**
	 * @see net.utils.slick.BasicGame#init(net.utils.slick.GameContainer)
	 */
	public void init(GameContainer container) throws SlickException {
		System.out.println(container.getWidth() + ", " + container.getHeight());
	}

	/**
	 * @see net.utils.slick.Game#render(net.utils.slick.GameContainer, net.utils.slick.Graphics)
	 */
	public void render(GameContainer container, Graphics g)
			throws SlickException {
	}

	/**
	 * @see net.utils.slick.BasicGame#update(net.utils.slick.GameContainer, int)
	 */
	public void update(GameContainer container, int delta)
			throws SlickException {
	}

	/**
	 * Entry point to the test
	 * 
	 * @param args The command line arguments passed in (none honoured)
	 */
	public static void main(String[] args) {
		try {
			CanvasGameContainer container = new CanvasGameContainer(
					new CanvasSizeTest());
			container.setSize(640,480);
			Frame frame = new Frame("Test");
			frame.setLayout(new GridLayout(1,2));
			frame.add(container);
			frame.pack();
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			frame.setVisible(true);
	
			container.start();
		} catch (Exception e) {
			Log.error(e);
		}
	}
}
