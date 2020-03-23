package net.utils.slick.tests;

import net.utils.slick.AppGameContainer;
import net.utils.slick.BasicGame;
import net.utils.slick.GameContainer;
import net.utils.slick.Graphics;
import net.utils.slick.Input;
import net.utils.slick.Music;
import net.utils.slick.MusicListener;
import net.utils.slick.SlickException;

/**
 * A test for music listeners which notify you when the music has eneded
 *
 * @author kevin
 */
public class MusicListenerTest extends BasicGame implements MusicListener {
	/** True if we should display the music ended message */
	private boolean musicEnded = false;
	/** True if we should display the music swapped message */
	private boolean musicSwapped = false;
	/** The music to be played */
	private Music music;
	/** The music to be streamed */
	private Music stream;
	
	/**
	 * Create a new test
	 */
	public MusicListenerTest() {
		super("Music Listener Test");
	}

	/**
	 * @see net.utils.slick.BasicGame#init(net.utils.slick.GameContainer)
	 */
	public void init(GameContainer container) throws SlickException {
		music = new Music("testdata/restart.ogg", false);
		stream = new Music("testdata/restart.ogg", false);
		
		music.addListener(this);
		stream.addListener(this);
	}

	/**
	 * @see net.utils.slick.BasicGame#update(net.utils.slick.GameContainer, int)
	 */
	public void update(GameContainer container, int delta) throws SlickException {
	}

	/**
	 * @see net.utils.slick.MusicListener#musicEnded(net.utils.slick.Music)
	 */
	public void musicEnded(Music music) {
		musicEnded = true;
	}

	/**
	 * @see net.utils.slick.MusicListener#musicSwapped(net.utils.slick.Music, net.utils.slick.Music)
	 */
	public void musicSwapped(Music music, Music newMusic) {
		musicSwapped = true;
	}
	
	/**
	 * @see net.utils.slick.Game#render(net.utils.slick.GameContainer, net.utils.slick.Graphics)
	 */
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.drawString("Press M to play music", 100, 100);
		g.drawString("Press S to stream music", 100, 150);
		if (musicEnded) {
			g.drawString("Music Ended", 100, 200);
		}
		if (musicSwapped) {
			g.drawString("Music Swapped", 100, 250);
		}
	}

	/**
	 * @see net.utils.slick.BasicGame#keyPressed(int, char)
	 */
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_M) {
			musicEnded = false;
			musicSwapped = false;
			music.play();
		}
		if (key == Input.KEY_S) {
			musicEnded = false;
			musicSwapped = false;
			stream.play();
		}
	}
	
	/**
	 * Entry point to the sound test
	 * 
	 * @param argv The arguments provided to the test
	 */
	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new MusicListenerTest());
			container.setDisplayMode(800,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
