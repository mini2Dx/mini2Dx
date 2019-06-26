/*******************************************************************************
 * Copyright 2019 Viridian Software Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.core.geom;

/**
 * A common interface for objects that can be positioned with an x and y
 * coordinate
 */
public interface Positionable {

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
	 * @param x
	 *            The x coordinate
	 */
	public void setX(float x);

	/**
	 * Sets the y coordinate of this object
	 *
	 * @param y
	 *            The y coordinate
	 */
	public void setY(float y);

	/**
	 * Sets the x and y coordinate of this object
	 *
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 */
	public void setXY(float x, float y);

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
	 * provided xy coordinates
	 *
	 * @param x
	 *            The x coordinate to measure distance to
	 * @param y
	 *            The y coordinate to measure distance to
	 * @return 0 if the xy coordinates are the same
	 */
	public float getDistanceTo(float x, float y);

	/**
	 * Moves this {@link Positionable} towards a coordinate
	 *
	 * @param x
	 *            The target x coordinate
	 * @param y
	 *            The target y coordinate
	 * @param speed
	 *            The amount to move by
	 */
	public void moveTowards(float x, float y, float speed);

	/**
	 * Moves this {@link Positionable} towards another {@link Positionable}
	 *
	 * @param positionable
	 *            The target {@link Positionable}
	 * @param speed
	 *            The amount to move by
	 */
	public void moveTowards(Positionable positionable, float speed);

	/**
	 * Adds a {@link Positionable} to be notified of coordinate
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
