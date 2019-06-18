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
package org.mini2Dx.core.input.deadzone;

/**
 * Common interface for gamepad dead zone handling implementations
 */
public interface DeadZone {
	/**
	 * Updates the x axis value
	 * @param x The x axis value
	 */
	public void updateX(float x);

	/**
	 * Updates the y axis value
	 * @param y The y axis value
	 */
	public void updateY(float y);

	/**
	 * Returns the x axis value with the dead zone applied
	 * @return
	 */
	public float getX();

	/**
	 * Returns the y axis value with the dead zone applied
	 * @return
	 */
	public float getY();

	/**
	 * Returns the dead zone threshold
	 * @return The threshold - defaults to 0.25f
	 */
	public float getDeadZone();

	/**
	 * Sets the dead zone threshold
	 * @param deadZone The threshold
	 */
	public void setDeadZone(float deadZone);

	/**
	 * Creates a copy of this {@link DeadZone}
	 * @return A new {@link DeadZone} instance
	 */
	public DeadZone copy();
}
