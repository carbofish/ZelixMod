package net.utils.slick.tests.states;

import net.utils.slick.AngelCodeFont;
import net.utils.slick.Color;
import net.utils.slick.Font;
import net.utils.slick.GameContainer;
import net.utils.slick.Graphics;
import net.utils.slick.Image;
import net.utils.slick.Input;
import net.utils.slick.SlickException;
import net.utils.slick.state.BasicGameState;
import net.utils.slick.state.StateBasedGame;
import net.utils.slick.state.transition.FadeInTransition;
import net.utils.slick.state.transition.FadeOutTransition;

/**
 * A simple test state to display an image and rotate it
 *
 * @author kevin
 */
public class TestState2 extends BasicGameState {
	/** The ID given to this state */
	public static final int ID = 2;
	/** The font to write the message with */
	private Font font;
	/** The image to be display */
	private Image image;
	/** The angle we'll rotate by */
	private float ang;
	/** The game holding this state */
	private StateBasedGame game;
	
	/**
	 * @see net.utils.slick.state.BasicGameState#getID()
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @see net.utils.slick.state.BasicGameState#init(net.utils.slick.GameContainer, net.utils.slick.state.StateBasedGame)
	 */
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.game = game;
		font = new AngelCodeFont("testdata/demo2.fnt","testdata/demo2_00.tga");
		image = new Image("testdata/logo.tga");
	}

	/**
	 * @see net.utils.slick.state.BasicGameState#render(net.utils.slick.GameContainer, net.utils.slick.state.StateBasedGame, net.utils.slick.Graphics)
	 */
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.setFont(font);
		g.setColor(Color.green);
		g.drawString("This is State 2", 200, 50);
		
		g.rotate(400,300,ang);
		g.drawImage(image,400-(image.getWidth()/2),300-(image.getHeight()/2));
	}

	/**
	 * @see net.utils.slick.state.BasicGameState#update(net.utils.slick.GameContainer, net.utils.slick.state.StateBasedGame, int)
	 */
	public void update(GameContainer container, StateBasedGame game, int delta) {
		ang += delta * 0.1f;
	}
	
	/**
	 * @see net.utils.slick.state.BasicGameState#keyReleased(int, char)
	 */
	public void keyReleased(int key, char c) {
		if (key == Input.KEY_1) {
			game.enterState(TestState1.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
		}
		if (key == Input.KEY_3) {
			game.enterState(TestState3.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
		}
	}
}
