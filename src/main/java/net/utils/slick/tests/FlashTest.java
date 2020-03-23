package net.utils.slick.tests;

import net.utils.slick.AppGameContainer;
import net.utils.slick.BasicGame;
import net.utils.slick.Color;
import net.utils.slick.GameContainer;
import net.utils.slick.Graphics;
import net.utils.slick.Image;
import net.utils.slick.Input;
import net.utils.slick.SlickException;

/**
 * A test for image flashes
 *
 * @author kevin
 */
public class FlashTest extends BasicGame {
	/** The TGA image loaded */
	private Image image;
	/** True if the image is rendered flashed */
	private boolean flash;
	/** The container for the test */
	private GameContainer container;
	
	/**
	 * Create a new image rendering test
	 */
	public FlashTest() {
		super("Flash Test");
	}
	
	/**
	 * @see net.utils.slick.BasicGame#init(net.utils.slick.GameContainer)
	 */
	public void init(GameContainer container) throws SlickException {
		this.container = container;
		
		image = new Image("testdata/logo.tga");
	}

	/**
	 * @see net.utils.slick.BasicGame#render(net.utils.slick.GameContainer, net.utils.slick.Graphics)
	 */
	public void render(GameContainer container, Graphics g) {
		g.drawString("Press space to toggle",10,50);
		if (flash) {
			image.draw(100,100);
		} else {
			image.drawFlash(100,100,image.getWidth(), image.getHeight(), new Color(1,0,1f,1f));
		}
	}

	/**
	 * @see net.utils.slick.BasicGame#update(net.utils.slick.GameContainer, int)
	 */
	public void update(GameContainer container, int delta) {
	}

	/**
	 * Entry point to our test
	 * 
	 * @param argv The arguments to pass into the test
	 */
	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new FlashTest());
			container.setDisplayMode(800,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see net.utils.slick.BasicGame#keyPressed(int, char)
	 */
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_SPACE) {
			flash = !flash;
		}
		if (key == Input.KEY_ESCAPE) {
			container.exit();
		}
	}
}
