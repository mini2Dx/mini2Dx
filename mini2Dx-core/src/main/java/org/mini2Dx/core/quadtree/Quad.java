/*******************************************************
 * Copyright (c) 2013 Thomas Cashman
 * All rights reserved.
 *******************************************************/
package org.mini2Dx.core.quadtree;

import org.mini2Dx.core.geom.Positionable;

/**
 *
 * @author Thomas Cashman
 */
public interface Quad<T extends Positionable> {
	
	public int getElementLimit();
	
	public int getNumberOfElements();
	
	public Quad<T> getParent();

	public void add(T object);
	
	public void remove(T object);
}
