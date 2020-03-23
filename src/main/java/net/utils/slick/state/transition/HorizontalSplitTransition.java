package net.utils.slick.state.transition;

import net.utils.slick.Color;
import net.utils.slick.GameContainer;
import net.utils.slick.Graphics;
import net.utils.slick.SlickException;
import net.utils.slick.opengl.renderer.Renderer;
import net.utils.slick.opengl.renderer.SGL;
import net.utils.slick.state.GameState;
import net.utils.slick.state.StateBasedGame;

/**
 * Horitzonal split transition that causes the previous state to split horizontally
 * revealing the new state underneath.
 * 
 * This state is an enter transition.
 * 
 * @author kevin
 */
public class HorizontalSplitTransition implements Transition {
	/** The renderer to use for all GL operations */
	protected static SGL GL = Renderer.get();

	/** The previous game state */
	private GameState prev;
	/** The current offset */
	private float offset;
	/** True if the transition is finished */
	private boolean finish;
	/** The background to draw underneath the previous state (null for none) */
	private Color background;

	/**
	 * Create a new transition
	 */
	public HorizontalSplitTransition() {
		
	}

	/**
	 * Create a new transition
	 * 
	 * @param background The background colour to draw under the previous state
	 */
	public HorizontalSplitTransition(Color background) {
		this.background = background;
	}
	
	/**
	 * @see net.utils.slick.state.transition.Transition#init(net.utils.slick.state.GameState, net.utils.slick.state.GameState)
	 */
	public void init(GameState firstState, GameState secondState) {
		prev = secondState;
	}

	/**
	 * @see net.utils.slick.state.transition.Transition#isComplete()
	 */
	public boolean isComplete() {
		return finish;
	}

	/**
	 * @see net.utils.slick.state.transition.Transition#postRender(net.utils.slick.state.StateBasedGame, net.utils.slick.GameContainer, net.utils.slick.Graphics)
	 */
	public void postRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
		g.translate(-offset, 0);
		g.setClip((int)-offset,0,container.getWidth()/2,container.getHeight());
		if (background != null) {
			Color c = g.getColor();
			g.setColor(background);
			g.fillRect(0,0,container.getWidth(),container.getHeight());
			g.setColor(c);
		}
		GL.glPushMatrix();
		prev.render(container, game, g);
		GL.glPopMatrix();
		g.clearClip();
		
		g.translate(offset*2, 0);
		g.setClip((int)((container.getWidth()/2)+offset),0,container.getWidth()/2,container.getHeight());
		if (background != null) {
			Color c = g.getColor();
			g.setColor(background);
			g.fillRect(0,0,container.getWidth(),container.getHeight());
			g.setColor(c);
		}
		GL.glPushMatrix();
		prev.render(container, game, g);
		GL.glPopMatrix();
		g.clearClip();
		g.translate(-offset, 0);
	}

	/**
	 * @see net.utils.slick.state.transition.Transition#preRender(net.utils.slick.state.StateBasedGame, net.utils.slick.GameContainer, net.utils.slick.Graphics)
	 */
	public void preRender(StateBasedGame game, GameContainer container,
			Graphics g) throws SlickException {
	}

	/**
	 * @see net.utils.slick.state.transition.Transition#update(net.utils.slick.state.StateBasedGame, net.utils.slick.GameContainer, int)
	 */
	public void update(StateBasedGame game, GameContainer container, int delta)
			throws SlickException {
		offset += delta * 1f;
		if (offset > container.getWidth() / 2) {
			finish = true;
		}
	}
}
