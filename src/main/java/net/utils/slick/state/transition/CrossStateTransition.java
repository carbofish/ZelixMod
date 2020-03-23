package net.utils.slick.state.transition;

import net.utils.slick.GameContainer;
import net.utils.slick.Graphics;
import net.utils.slick.SlickException;
import net.utils.slick.state.GameState;
import net.utils.slick.state.StateBasedGame;

/**
 * A transition that will combine two states into one effect. The first state is
 * the one we're transitioning from. The second state is specified in the constructor. 
 * 
 * By default one state will simply be rendered over the other. Subclass this transition
 * overriding the preRenderFirstState and preRenderSecondState to setup the rendering
 * for each state (alpha or what ever). Note that it's also possible to use the 
 * postRenderSecondState method to clean up your OpenGL setup. 
 * 
 * So these methods are called like so:
 * 
 * preRenderFirstState()
 * = the first state is rendered
 * preRenderSecondState()
 * = the second state is rendered
 * postRenderSecondState()
 *
 * @author kevin
 */
public abstract class CrossStateTransition implements Transition {
	/** The second state to cross with */
	private GameState secondState;
	
	/**
	 * Create a cross state transitions
	 * 
	 * @param secondState The secondary state with combining with the 
	 * source state.
	 */
	public CrossStateTransition(GameState secondState) {
		this.secondState = secondState;
	}
	
	/**
	 * @see net.utils.slick.state.transition.Transition#isComplete()
	 */
	public abstract boolean isComplete();

	/**
	 * @see net.utils.slick.state.transition.Transition#postRender(net.utils.slick.state.StateBasedGame, net.utils.slick.GameContainer, net.utils.slick.Graphics)
	 */
	public void postRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
		preRenderSecondState(game, container, g);
		secondState.render(container, game, g);
		postRenderSecondState(game, container, g);
	}

	/**
	 * @see net.utils.slick.state.transition.Transition#preRender(net.utils.slick.state.StateBasedGame, net.utils.slick.GameContainer, net.utils.slick.Graphics)
	 */
	public void preRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
		preRenderFirstState(game, container, g);
	}

	/**
	 * @see net.utils.slick.state.transition.Transition#update(net.utils.slick.state.StateBasedGame, net.utils.slick.GameContainer, int)
	 */
	public void update(StateBasedGame game, GameContainer container, int delta) throws SlickException {
	}

	/**
	 * Notification that the transition is about to render the first state is the cross
	 * transition.
	 * 
	 * @param game The game being rendered
	 * @param container The container holding the game
	 * @param g The graphic context used to render
	 * @throws SlickException Indicates a failure to setup the rendering state - throw for anything that goes wrong
	 */
	public void preRenderFirstState(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
	}
	
	/**
	 * Notification that the transition is about to render the second state is the cross
	 * transition.
	 * 
	 * @param game The game being rendered
	 * @param container The container holding the game
	 * @param g The graphic context used to render
	 * @throws SlickException Indicates a failure to setup the rendering state - throw for anything that goes wrong
	 */
	public void preRenderSecondState(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
	}

	/**
	 * Notification that the transition is has just rendered the second state is the cross
	 * transition.
	 * 
	 * @param game The game being rendered
	 * @param container The container holding the game
	 * @param g The graphic context used to render
	 * @throws SlickException Indicates a failure to setup the rendering state - throw for anything that goes wrong
	 */	
	public void postRenderSecondState(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
	}
	
}
