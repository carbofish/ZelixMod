package net.utils.slick.tests;
	
import net.utils.slick.AngelCodeFont;
import net.utils.slick.AppGameContainer;
import net.utils.slick.BasicGame;
import net.utils.slick.Font;
import net.utils.slick.GameContainer;
import net.utils.slick.Graphics;
import net.utils.slick.Image;
import net.utils.slick.Input;
import net.utils.slick.SlickException;

/**
 * A test of the font rendering capabilities
 *
 * @author kevin
 */
public class PureFontTest extends BasicGame {
	/** The font we're going to use to render */
	private Font font;
	/** The image */
	private Image image;
	
	/**
	 * Create a new test for font rendering
	 */
	public PureFontTest() {
		super("Hiero Font Test");
	}
	
	/**
	 * @see net.utils.slick.Game#init(net.utils.slick.GameContainer)
	 */
	public void init(GameContainer container) throws SlickException {
		image = new Image("testdata/sky.jpg");
		font = new AngelCodeFont("testdata/hiero.fnt","testdata/hiero.png");
	}

	/**
	 * @see net.utils.slick.BasicGame#render(net.utils.slick.GameContainer, net.utils.slick.Graphics)
	 */
	public void render(GameContainer container, Graphics g) {
		image.draw(0,0,800,600);
		font.drawString(100, 32, "On top of old smokey, all");
		font.drawString(100, 80, "covered with sand..");
	}

	/**
	 * @see net.utils.slick.BasicGame#update(net.utils.slick.GameContainer, int)
	 */
	public void update(GameContainer container, int delta) throws SlickException {
	}
	
	/**
	 * @see net.utils.slick.BasicGame#keyPressed(int, char)
	 */
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			System.exit(0);
		}
	}
	
	/** The container we're using */
	private static AppGameContainer container;
	
	/**
	 * Entry point to our test
	 * 
	 * @param argv The arguments passed in the test
	 */
	public static void main(String[] argv) {
		try {
			container = new AppGameContainer(new PureFontTest());
			container.setDisplayMode(800,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
