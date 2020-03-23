package net.utils.slick.tests;

import net.utils.slick.AppGameContainer;
import net.utils.slick.BasicGame;
import net.utils.slick.Color;
import net.utils.slick.GameContainer;
import net.utils.slick.Graphics;
import net.utils.slick.SlickException;
import net.utils.slick.geom.Polygon;

/**
 * A test for polygon collision
 *
 * @author kevin
 */
public class PolygonTest extends BasicGame {
	/** The polygon we're going to test against */
	private Polygon poly;
	/** True if the mouse is in the polygon */
	private boolean in;
	/** The y offset */
	private float y;
	
	/**
	 * Create the test
	 */
	public PolygonTest() {
		super("Polygon Test");
	}

	/**
	 * @see net.utils.slick.BasicGame#init(net.utils.slick.GameContainer)
	 */
	public void init(GameContainer container) throws SlickException {
		poly = new Polygon();
		poly.addPoint(300, 100);
		poly.addPoint(320, 200);
		poly.addPoint(350, 210);
		poly.addPoint(280, 250);
		poly.addPoint(300, 200);
		poly.addPoint(240, 150);
		
	}

	/**
	 * @see net.utils.slick.BasicGame#update(net.utils.slick.GameContainer, int)
	 */
	public void update(GameContainer container, int delta) throws SlickException {
		in = poly.contains(container.getInput().getMouseX(), container.getInput().getMouseY());
		
		poly.setCenterY(0);
	}

	/**
	 * @see net.utils.slick.Game#render(net.utils.slick.GameContainer, net.utils.slick.Graphics)
	 */
	public void render(GameContainer container, Graphics g) throws SlickException {
		if (in) {
			g.setColor(Color.red);
			g.fill(poly);
		}
		g.setColor(Color.yellow);
		g.fillOval(poly.getCenterX()-3, poly.getCenterY()-3, 6, 6);
		g.setColor(Color.white);
		g.draw(poly);
	}

	/**
	 * Entry point into our test
	 * 
	 * @param argv The arguments passed on the command line
	 */
	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new PolygonTest(), 640, 480, false);
			container.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
