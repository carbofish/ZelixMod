package net.utils.slick.tests;

import net.utils.slick.AppGameContainer;
import net.utils.slick.BasicGame;
import net.utils.slick.Color;
import net.utils.slick.GameContainer;
import net.utils.slick.Graphics;
import net.utils.slick.Input;
import net.utils.slick.SlickException;
import net.utils.slick.opengl.renderer.Renderer;
import net.utils.slick.svg.InkscapeLoader;
import net.utils.slick.svg.SimpleDiagramRenderer;

/**
 * A rudimentry test of loading SVG from inkscape
 *
 * @author kevin
 */
public class InkscapeTest extends BasicGame {
	/** The renderer doing the work */
	private SimpleDiagramRenderer[] renderer = new SimpleDiagramRenderer[5];
	/** The zoom */
	private float zoom = 1;
	/** The x location */
	private float x;
	/** The y location */
	private float y;
	
	/**
	 * Create a new test for inkscape loading
	 */
	public InkscapeTest() {
		super("Inkscape Test");
	}

	/**
	 * @see net.utils.slick.BasicGame#init(net.utils.slick.GameContainer)
	 */
	public void init(GameContainer container) throws SlickException {
		container.getGraphics().setBackground(Color.white);
		
		InkscapeLoader.RADIAL_TRIANGULATION_LEVEL = 2;
		
//		renderer[0] = new SimpleDiagramRenderer(InkscapeLoader.load("testdata/svg/orc.svg"));
//		renderer[1] = new SimpleDiagramRenderer(InkscapeLoader.load("testdata/svg/head2.svg"));
//		renderer[2] = new SimpleDiagramRenderer(InkscapeLoader.load("testdata/svg/head3.svg"));
		renderer[3] = new SimpleDiagramRenderer(InkscapeLoader.load("testdata/svg/clonetest.svg"));
//		renderer[4] = new SimpleDiagramRenderer(InkscapeLoader.load("testdata/svg/cow.svg"));
		
		container.getGraphics().setBackground(new Color(0.5f,0.7f,1.0f));
	}

	/**
	 * @see net.utils.slick.BasicGame#update(net.utils.slick.GameContainer, int)
	 */
	public void update(GameContainer container, int delta) throws SlickException {
		if (container.getInput().isKeyDown(Input.KEY_Q)) {
			zoom += (delta * 0.01f);
			if (zoom > 10) {
				zoom = 10;
			}
		}
		if (container.getInput().isKeyDown(Input.KEY_A)) {
			zoom -= (delta * 0.01f);
			if (zoom < 0.1f) {
				zoom = 0.1f;
			}
		}
		if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
			x += (delta * 0.1f);
		}
		if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
			x -= (delta * 0.1f);
		}
		if (container.getInput().isKeyDown(Input.KEY_DOWN)) {
			y += (delta * 0.1f);
		}
		if (container.getInput().isKeyDown(Input.KEY_UP)) {
			y -= (delta * 0.1f);
		}
	}

	/**
	 * @see net.utils.slick.Game#render(net.utils.slick.GameContainer, net.utils.slick.Graphics)
	 */
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.scale(zoom,zoom);
		g.translate(x, y);
		g.scale(0.3f,0.3f);
		//renderer[0].render(g);
		g.scale(1/0.3f,1/0.3f);
		g.translate(400, 0);
		//renderer[1].render(g);
		g.translate(100, 300);
		g.scale(0.7f,0.7f);
		//renderer[2].render(g);
		g.scale(1/0.7f,1/0.7f);
		
		g.scale(0.5f,0.5f);
		g.translate(-1100, -380);
		renderer[3].render(g);
		g.scale(1/0.5f,1/0.5f);
		
//		g.translate(280, 100);
//		g.scale(0.5f,0.5f);
//		renderer[4].render(g);
		
		g.resetTransform();
	}
	
	/**
	 * Entry point to our simple test
	 * 
	 * @param argv The arguments passed in
	 */
	public static void main(String argv[]) {
		try {
			Renderer.setRenderer(Renderer.VERTEX_ARRAY_RENDERER);
			Renderer.setLineStripRenderer(Renderer.QUAD_BASED_LINE_STRIP_RENDERER);
			
			AppGameContainer container = new AppGameContainer(new InkscapeTest());
			container.setDisplayMode(800,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
