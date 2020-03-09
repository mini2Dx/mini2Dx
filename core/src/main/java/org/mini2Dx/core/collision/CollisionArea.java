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

import org.mini2Dx.core.geom.Sizeable;

/**
 * Common interface for collidable areas in-game
 */
public interface CollisionArea extends CollisionObject, Sizeable {

	/**
	 * Sets the current x, y, width and height
	 *
	 * @param x
	 *            The x coordinate to set
	 * @param y
	 *            The y coordinate to set
	 * @param width The width to set
	 * @param height The height to set
	 * @return This object
	 */
	public CollisionArea setTo(float x, float y, float width, float height);

	/**
	 * Sets the current x, y, width and height and force updates the
	 * rendering state to match
	 *
	 * @param x
	 *            The x coordinate to set
	 * @param y
	 *            The y coordinate to set
	 * @param width
	 *            The width to set
	 * @param height
	 *            The height to set
	 */
	public void forceTo(float x, float y, float width, float height);

	/**
	 * Returns the render width of this object using the set {@link RenderCoordMode}
	 *
	 * @return
	 */
	public int getRenderWidth();

	/**
	 * Returns the render height of this object using the set {@link RenderCoordMode}
	 *
	 * @return
	 */
	public int getRenderHeight();

	/**
	 * Returns the raw float value used for the render width coordinate
	 * @return
	 */
	public float getRawRenderWidth();

	/**
	 * Returns the raw float value used for the render height coordinate
	 * @return
	 */
	public float getRawRenderHeight();
}
