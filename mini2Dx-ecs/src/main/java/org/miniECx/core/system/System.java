/*******************************************************
 * Copyright (c) 2013 Thomas Cashman
 * All rights reserved.
 *******************************************************/
package org.miniECx.core.system;

import org.mini2Dx.core.graphics.Graphics;
import org.miniECx.core.entity.Entity;

/**
 *
 * @author Thomas Cashman
 */
public interface System {
	public void addEntity(Entity entity);
	
	public void removeEntity(Entity entity);
	
	public void update(float delta);
	
	public void update(Entity entity, float delta);
	
	public void interpolate(float alpha);
	
	public void interpolate(Entity entity, float alpha);
	
	public void render(Graphics g);
	
	public void render(Entity entity, Graphics g);
	
	public void setDebugging(boolean debugging);
	
	public boolean isDebugging();
}
