/**
 * Copyright 2014 Thomas Cashman
 */
package org.mini2Dx.ecs.system;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ecs.entity.Entity;

/**
 *
 * @author Thomas Cashman
 */
public abstract class GameSystem<T extends Entity> extends System<T> {
	/**
	 * Initialise the {@link System}
	 * @param gc The {@link GameContainer} calling initialise
	 */
	public abstract void initialise(GameContainer gc);
	
	/**
	 * Update the {@link System}
	 * @param gc The {@link GameContainer} calling update
	 * @param delta The time in seconds since the last update
	 */
	public abstract void update(GameContainer gc, float delta);
	
	/**
	 * Interpolate the {@link System}
	 * @param gc The {@link GameContainer} calling interpolate
	 * @param alpha The alpha value to use during interpolation
	 */
	public abstract void interpolate(GameContainer gc, float alpha);
	
	/**
	 * Render the {@link System}
	 * @param gc The {@link GameContainer} calling render
	 * @param g The {@link Graphics} instance
	 */
	public abstract void render(GameContainer gc, Graphics g);

	@Override
	public void update(float delta) {
		throw new RuntimeException("GameSystem should use update(GameContainer gc, float delta)");
	}
}
