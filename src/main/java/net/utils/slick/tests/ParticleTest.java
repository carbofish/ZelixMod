package net.utils.slick.tests;

import net.utils.slick.AppGameContainer;
import net.utils.slick.BasicGame;
import net.utils.slick.GameContainer;
import net.utils.slick.Graphics;
import net.utils.slick.Image;
import net.utils.slick.Input;
import net.utils.slick.SlickException;
import net.utils.slick.particles.ParticleSystem;
import net.utils.slick.particles.effects.FireEmitter;

/**
 * A particle test using built in effects
 *
 * @author kevin
 */
public class ParticleTest extends BasicGame {
	/** The particle system running everything */
	private ParticleSystem system;
	/** The particle blending mode */
	private int mode = ParticleSystem.BLEND_COMBINE;
	
	/**
	 * Create a new test of graphics context rendering
	 */
	public ParticleTest() {
		super("Particle Test");
	}
	
	/**
	 * @see net.utils.slick.BasicGame#init(net.utils.slick.GameContainer)
	 */
	public void init(GameContainer container) throws SlickException {
		Image image = new Image("testdata/particle.tga", true);
		system = new ParticleSystem(image);
		
		system.addEmitter(new FireEmitter(400,300,45));
		system.addEmitter(new FireEmitter(200,300,60));
		system.addEmitter(new FireEmitter(600,300,30));
		
		//system.setUsePoints(true);
	}

	/**
	 * @see net.utils.slick.BasicGame#render(net.utils.slick.GameContainer, net.utils.slick.Graphics)
	 */
	public void render(GameContainer container, Graphics g) {
		for (int i=0;i<100;i++) {
			g.translate(1,1);
			system.render();
		}
		g.resetTransform();
		g.drawString("Press space to toggle blending mode", 200, 500);
		g.drawString("Particle Count: "+(system.getParticleCount()*100), 200, 520);
	}

	/**
	 * @see net.utils.slick.BasicGame#update(net.utils.slick.GameContainer, int)
	 */
	public void update(GameContainer container, int delta) {
		system.update(delta);
	}

	/**
	 * @see net.utils.slick.BasicGame#keyPressed(int, char)
	 */
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			System.exit(0);
		}
		if (key == Input.KEY_SPACE) {
			mode = ParticleSystem.BLEND_ADDITIVE == mode ? ParticleSystem.BLEND_COMBINE : ParticleSystem.BLEND_ADDITIVE;
			system.setBlendingMode(mode);
		}
	}
	
	/**
	 * Entry point to our test
	 * 
	 * @param argv The arguments passed to the test
	 */
	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new ParticleTest());
			container.setDisplayMode(800,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
