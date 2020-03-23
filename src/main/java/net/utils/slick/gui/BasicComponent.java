package net.utils.slick.gui;

import net.utils.slick.Graphics;
import net.utils.slick.SlickException;

/**
 * Renamed to provide backwards compatibility
 *
 * @author kevin
 * @deprecated
 */
public abstract class BasicComponent extends AbstractComponent {
	/** The x position of the component */
	protected int x;
	/** The y position of the component */
	protected int y;
	/** The width of the component */
	protected int width;
	/** The height of the component */
	protected int height;

	/**
	 * Create a new component
	 * 
	 * @param container
	 *            The container displaying this component
	 */
	public BasicComponent(GUIContext container) {
		super(container);
	}
	
	/**
	 * @see net.utils.slick.gui.AbstractComponent#getHeight()
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @see net.utils.slick.gui.AbstractComponent#getWidth()
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @see net.utils.slick.gui.AbstractComponent#getX()
	 */
	public int getX() {
		return x;
	}

	/**
	 * @see net.utils.slick.gui.AbstractComponent#getY()
	 */
	public int getY() {
		return y;
	}

	/**
	 * Allow the sub-component to render
	 * 
	 * @param container The container holding the GUI
	 * @param g The graphics context into which we should render
	 */
	public abstract void renderImpl(GUIContext container, Graphics g);
	
	/**
	 * @see net.utils.slick.gui.AbstractComponent#render(net.utils.slick.gui.GUIContext, net.utils.slick.Graphics)
	 */
	public void render(GUIContext container, Graphics g) throws SlickException {
		renderImpl(container,g);
	}

	/**
	 * @see net.utils.slick.gui.AbstractComponent#setLocation(int, int)
	 */
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

}
