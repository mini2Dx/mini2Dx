/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.engine;

import org.mini2Dx.core.geom.Point;

import com.badlogic.gdx.math.Vector2;

/**
 * A common interface for objects that can be positioned with an x and y
 * coordinate
 */
public interface Positionable extends Updatable {
	/**
	 * Returns the unique id of this object
	 * 
	 * @return
	 */
	public int getId();

	/**
	 * Returns the x coordinate of this object
	 * 
	 * @return 0 by default
	 */
	public float getX();

	/**
	 * Returns the y coordinate of this object
	 * 
	 * @return 0 by default
	 */
	public float getY();
	
	/**
	 * Sets the x coordinate of this object
	 * 
	 * @param x The x coordinate
	 */
	public void setX(float x);

	/**
	 * Sets the y coordinate of this object
	 * 
	 * @param y The y coordinate
	 */
	public void setY(float y);
	
	/**
	 * Returns the render x coordinate of this object
	 * @return
	 */
	public int getRenderX();
	
	/**
	 * Returns the render y coordinate of this object
	 * @return
	 */
	public int getRenderY();

	/**
	 * Returns this distance between this object's x,y coordinates and the
	 * provided {@link Positionable}'s xy coordinates
	 * 
	 * @param positionable
	 *            The {@link Positionable} to retrieve the distance from
	 * @return 0 if the xy coordinates are the same
	 */
	public float getDistanceTo(Positionable positionable);
	
	/**
	 * Returns this distance between this object's x,y coordinates and the
	 * provided {@link Point}'s xy coordinates
	 * 
	 * @param point
	 *            The {@link Point} to retrieve the distance from
	 * @return 0 if the xy coordinates are the same
	 */
	public float getDistanceTo(Point point);
	
	/**
	 * Moves this {@link Positionable} towards a coordinate
	 * @param x The target x coordinate
	 * @param y The target y coordinate
	 * @param speed The amount to move by
	 */
	public void moveTowards(float x, float y, float speed);
	
	/**
	 * Moves this {@link Positionable} towards another {@link Positionable}
	 * @param positionable The target {@link Positionable}
	 * @param speed The amount to move by
	 */
	public void moveTowards(Positionable positionable, float speed);

	/**
	 * Adds a {@link PositionChangeListener} to be notified of coordinate
	 * changes
	 * 
	 * @param listener
	 *            The {@link PositionChangeListener} to add
	 */
	public <T extends Positionable> void addPostionChangeListener(PositionChangeListener<T> listener);

	/**
	 * Removes a {@link PositionChangeListener} to stop it being notified of
	 * coordinate changes
	 * 
	 * @param listener
	 *            The {@link PositionChangeListener} to remove
	 */
	public <T extends Positionable> void removePositionChangeListener(PositionChangeListener<T> listener);
}
