package net.utils.slick.tests;

import net.utils.slick.BasicGame;
import net.utils.slick.GameContainer;
import net.utils.slick.Graphics;
import net.utils.slick.SlickException;
import net.utils.slick.tiled.TiledMap;
import net.utils.slick.util.Bootstrap;

/**
 * Simple test for isometric map rendering
 * 
 * @author kevin
 */
public class IsoTiledTest extends BasicGame {
	/** The tilemap we're going to render */
	private TiledMap tilemap;
	
	/**
	 * Create a new test
	 */
	public IsoTiledTest() {
		super("Isometric Tiled Map Test");
	}

	/*
	 * (non-Javadoc)
	 * @see net.utils.slick.BasicGame#init(net.utils.slick.GameContainer)
	 */
	public void init(GameContainer container) throws SlickException {
		tilemap = new TiledMap("testdata/isoexample.tmx", "testdata/");
	}

	/*
	 * (non-Javadoc)
	 * @see net.utils.slick.BasicGame#update(net.utils.slick.GameContainer, int)
	 */
	public void update(GameContainer container, int delta)
			throws SlickException {
	}

	/*
	 * (non-Javadoc)
	 * @see net.utils.slick.Game#render(net.utils.slick.GameContainer, net.utils.slick.Graphics)
	 */
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		tilemap.render(350,150);
	}

	/**
	 * Entry point to our test
	 * 
	 * @param argv The arguments passed in from the command line
	 */
	public static void main(String[] argv) {
		Bootstrap.runAsApplication(new IsoTiledTest(), 800,600,false);
	}
}
