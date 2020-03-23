package net.utils.slick.tests;

import net.utils.slick.AppGameContainer;
import net.utils.slick.BasicGame;
import net.utils.slick.GameContainer;
import net.utils.slick.Graphics;
import net.utils.slick.Input;
import net.utils.slick.SlickException;

/**
 * A test for basic image rendering
 *
 * @author kevin
 */
public class KeyRepeatTest extends BasicGame {
	/** The number of times the key pressed event has been fired */
	private int count;
	/** The input sub system */
	private Input input;
	
	/**
	 * Create a new image rendering test
	 */
	public KeyRepeatTest() {
		super("KeyRepeatTest");
	}
	
	/**
	 * @see net.utils.slick.BasicGame#init(net.utils.slick.GameContainer)
	 */
	public void init(GameContainer container) throws SlickException {
		input = container.getInput();
		input.enableKeyRepeat(300,100);
	}

	/**
	 * @see net.utils.slick.BasicGame#render(net.utils.slick.GameContainer, net.utils.slick.Graphics)
	 */
	public void render(GameContainer container, Graphics g) {
		g.drawString("Key Press Count: "+count, 100,100);
		g.drawString("Press Space to Toggle Key Repeat", 100,150);
		g.drawString("Key Repeat Enabled: "+input.isKeyRepeatEnabled(), 100,200);
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
			AppGameContainer container = new AppGameContainer(new KeyRepeatTest());
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
		count++;
		if (key == Input.KEY_SPACE) {
			if (input.isKeyRepeatEnabled()) {
				input.disableKeyRepeat();
			} else {
				input.enableKeyRepeat(300,100);
			}
		}
	}
}
