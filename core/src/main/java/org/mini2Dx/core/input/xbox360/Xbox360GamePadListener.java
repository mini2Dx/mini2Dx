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
package org.mini2Dx.core.input.xbox360;

import org.mini2Dx.core.input.button.Xbox360Button;
import org.mini2Dx.core.input.deadzone.DeadZone;

public interface Xbox360GamePadListener {
	/**
	 * Called when a gamepad connects
	 *
	 * @param gamePad
	 *            The gamepad that this event came from
	 */
	public void connected(Xbox360GamePad gamePad);

	/**
	 * Called when a gamepad disconnects
	 *
	 * @param gamePad
	 *            The gamepad that this event came from
	 */
	public void disconnected(Xbox360GamePad gamePad);

	/**
	 * Called when a button is pressed down
	 *
	 * @param gamePad
	 *            The gamePad that this event came from
	 * @param button
	 *            The button that was pressed
	 * @return True if this event should not propagate to other listeners
	 */
	public boolean buttonDown(Xbox360GamePad gamePad, Xbox360Button button);

	/**
	 * Called when a button is released
	 *
	 * @param gamePad
	 *            The gamepad that this event came from
	 * @param button
	 *            The button that was released
	 * @return True if this event should not propagate to other listeners
	 */
	public boolean buttonUp(Xbox360GamePad gamePad, Xbox360Button button);

	/**
	 * Called when the left trigger is moved
	 *
	 * @param gamePad
	 *            The gamepad that this event came from
	 * @param value
	 *            ~0f when released, ~1f when pressed
	 * @return True if this event should not propagate to other listeners
	 */
	public boolean leftTriggerMoved(Xbox360GamePad gamePad, float value);

	/**
	 * Called when the right trigger is moved
	 *
	 * @param gamePad
	 *            The gamepad that this event came from
	 * @param value
	 *            ~0f when released, ~1f when pressed
	 * @return True if this event should not propagate to other listeners
	 */
	public boolean rightTriggerMoved(Xbox360GamePad gamePad, float value);

	/**
	 * Called when left stick moves along its X axis
	 *
	 * @param gamePad
	 *            The gamepad that this event came from
	 * @param value
	 *            ~-1f at left, ~0f at center, ~1f at right (values may never be
	 *            accurate due to dead zones, apply a {@link DeadZone} to the
	 *            gamepad instance to correct this)
	 * @return True if this event should not propagate to other listeners
	 */
	public boolean leftStickXMoved(Xbox360GamePad gamePad, float value);

	/**
	 * Called when left stick moves along its Y axis
	 *
	 * @param gamePad
	 *            The gamepad that this event came from
	 * @param value
	 *            ~-1f at top, ~0f at center, ~1f at bottom (values may never be
	 *            accurate due to dead zones, apply a {@link DeadZone} to the
	 *            gamepad instance to correct this)
	 * @return True if this event should not propagate to other listeners
	 */
	public boolean leftStickYMoved(Xbox360GamePad gamePad, float value);

	/**
	 * Called when right stick moves along its X axis
	 *
	 * @param gamePad
	 *            The gamepad that this event came from
	 * @param value
	 *            ~-1f at left, ~0f at center, ~1f at right (values may never be
	 *            accurate due to dead zones, apply a {@link DeadZone} to the
	 *            gamepad instance to correct this)
	 * @return True if this event should not propagate to other listeners
	 */
	public boolean rightStickXMoved(Xbox360GamePad gamePad, float value);

	/**
	 * Called when left stick moves along its Y axis
	 *
	 * @param gamePad
	 *            The gamepad that this event came from
	 * @param value
	 *            ~-1f at top, ~0f at center, ~1f at bottom (values may never be
	 *            accurate due to dead zones, apply a {@link DeadZone} to the
	 *            gamepad instance to correct this)
	 * @return True if this event should not propagate to other listeners
	 */
	public boolean rightStickYMoved(Xbox360GamePad gamePad, float value);
}
