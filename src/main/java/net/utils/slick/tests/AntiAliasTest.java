package net.utils.slick.tests;

import net.utils.slick.AppGameContainer;
import net.utils.slick.BasicGame;
import net.utils.slick.Color;
import net.utils.slick.GameContainer;
import net.utils.slick.Graphics;
import net.utils.slick.SlickException;

/**
 * Test to view the effects of antialiasing on cirles
 *
 * @author kevin
 */
public class AntiAliasTest extends BasicGame {

	/**
	 * Create the test
	 */
	public AntiAliasTest() {
		super("AntiAlias Test");
	}

	/**
	 * @see net.utils.slick.BasicGame#init(net.utils.slick.GameContainer)
	 */
	public void init(GameContainer container) throws SlickException {
		container.getGraphics().setBackground(Color.green);
	}

	/**
	 * @see net.utils.slick.BasicGame#update(net.utils.slick.GameContainer, int)
	 */
	public void update(GameContainer container, int delta) throws SlickException {
	}

	/**
	 * @see net.utils.slick.Game#render(net.utils.slick.GameContainer, net.utils.slick.Graphics)
	 */
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.setAntiAlias(true);
		g.setColor(Color.red);
		g.drawOval(100,100,100,100);
		g.fillOval(300,100,100,100);
		g.setAntiAlias(false);
		g.setColor(Color.red);
		g.drawOval(100,300,100,100);
		g.fillOval(300,300,100,100);
	}

	/**
	 * Entry point to our test
	 * 
	 * @param argv The arguments passed to the test
	 */
	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new AntiAliasTest());
			container.setDisplayMode(800,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
