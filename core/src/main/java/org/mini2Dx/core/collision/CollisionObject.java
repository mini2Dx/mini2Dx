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
package org.mini2Dx.core.collision;

import org.mini2Dx.core.geom.Positionable;
import org.mini2Dx.core.util.Interpolatable;

/**
 * Common interface for collidable points in-game
 */
public interface CollisionObject extends Positionable, Interpolatable {
	/**
	 * Returns the unique id of this object
	 *
	 * @return
	 */
	public int getId();

	/**
	 * Called at the start of each update() before any changes are made to this object
	 */
	public void preUpdate();

	/**
	 * Called during interpolate phase each interpolate()
	 * @param alpha The alpha value between 0.0 and 1.0
	 */
	public void interpolate(float alpha);

	/**
	 * Sets the current x and y coordinate to the specified x and y and force updates the
	 * rendering position to match
	 *
	 * @param x
	 *            The x coordinate to set
	 * @param y
	 *            The y coordinate to set
	 */
	public void forceTo(float x, float y);

	/**
	 * Returns the render x coordinate of this object using the set {@link RenderCoordMode}
	 *
	 * @return
	 */
	public int getRenderX();

	/**
	 * Returns the render y coordinate of this object using the set {@link RenderCoordMode}
	 *
	 * @return
	 */
	public int getRenderY();

	/**
	 * Returns the raw float value used for the render x coordinate
	 * @return
	 */
	public float getRawRenderX();

	/**
	 * Returns the raw float value used for the render y coordinate
	 * @return
	 */
	public float getRawRenderY();

	/**
	 * Returns the mode used for calculating render coordinates
	 * @return Defaults to {@link RenderCoordMode#GLOBAL_DEFAULT}
	 */
	public RenderCoordMode getRenderCoordMode();

	/**
	 * Sets the mode used for calculating render coordinates
	 * @param mode The {@link RenderCoordMode} value
	 */
	public void setRenderCoordMode(RenderCoordMode mode);

	/**
	 * Disposes of this object, returning any pooled references to their corresponding pools
	 */
	public void dispose();
}
