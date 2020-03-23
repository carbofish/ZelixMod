package net.utils.slick.tests;

import net.utils.slick.AppGameContainer;
import net.utils.slick.GameContainer;
import net.utils.slick.SlickException;
import net.utils.slick.state.StateBasedGame;
import net.utils.slick.tests.states.TestState1;
import net.utils.slick.tests.states.TestState2;
import net.utils.slick.tests.states.TestState3;

/**
 * A test for the multi-state based functionality
 *
 * @author kevin
 */
public class StateBasedTest extends StateBasedGame {

	/**
	 * Create a new test
	 */
	public StateBasedTest() {
		super("State Based Test");
	}
	
	/**
	 * @see net.utils.slick.state.StateBasedGame#initStatesList(net.utils.slick.GameContainer)
	 */
	public void initStatesList(GameContainer container) {
		addState(new TestState1());
		addState(new TestState2());
		addState(new TestState3());
	}
	
	/**
	 * Entry point to our test
	 * 
	 * @param argv The arguments to pass into the test
	 */
	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new StateBasedTest());
			container.setDisplayMode(800,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
