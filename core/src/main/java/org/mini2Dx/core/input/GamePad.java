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
package org.mini2Dx.core.input;

import org.mini2Dx.gdx.math.Vector3;

/**
 * Common interface for gamepads/controllers/joysticks/etc.
 */
public interface GamePad {
	/**
	 * Returns the {@link GamePadType}
	 * @return {@link GamePadType#UNKNOWN} if unknown
	 */
	public GamePadType getGamePadType();

	/**
	 * Returns the ID for this {@link GamePad}.
	 * When supported by the driver, if multiple gamepads are connected each will have a unique instance id.
	 * Otherwise, this will fallback for to the name of the device returned by the driver.
	 * If a gamepad is unplugged and reconnected, it will have the same ID.
	 * @return The unique ID when possible, otherwise the generic ID
	 */
	public String getInstanceId();

	/**
	 * Returns the model information present by the hardware/driver (e.g. Xbox One Wireless Controller)
	 * @return
	 */
	public String getModelInfo();

	/**
	 * Returns if this {@link GamePad} is connected
	 * @return False if it has been disconnected/unplugged
	 */
	public boolean isConnected();

	/**
	 * Returns if this game pad supports assigning player numbers to it
	 * @return True if supported
	 */
	public boolean isPlayerIndicesSupported();

	/**
	 * Returns the player assigned to this gamepad
	 * @return -1 if unassigned
	 */
	public int getPlayerIndex();

	/**
	 * Sets the player assigned to this gamepad
	 * @param playerIndex -1 if unassigned
	 */
	public void setPlayerIndex(int playerIndex);

	/**
	 * Returns if this gamepad has vibrate/rumble functionality
	 * @return True if supported
	 */
	public boolean isVibrateSupported();

	/**
	 * Returns if this gamepad is currently vibrating/rumbling
	 * @return True if vibrating/rumbling
	 */
	public boolean isVibrating();

	/**
	 * Returns the strength of the vibration/rumble
	 * @return -1f if not vibrating/rumbling
	 */
	public float getVibrationStrength();

	/**
	 * Starts vibrating/rumbling the gamepad
	 * @param strength A value between 0f and 1f
	 */
	public void startVibration(float strength);

	/**
	 * Stops the vibrating/rumbling of the gamepad
	 */
	public void stopVibration();

	/**
	 * Returns if a specific button is currently pressed down
	 * @param buttonCode The button code (Note: these may vary per platform)
	 * @return True if pressed down
	 */
	public boolean isButtonDown(int buttonCode);

	/**
	 * Returns if a specific button is currently released (not pressed down)
	 * @param buttonCode The button code (Note: these may vary per platform)
	 * @return True if released (not pressed down)
	 */
	public boolean isButtonUp(int buttonCode);

	/**
	 * Returns the current value of an axis
	 * @param axisCode The axis code (Note: these may vary per platform)
	 * @return The value between -1.0f and 1.0f
	 */
	public float getAxis (int axisCode);

	/**
	 * Returns if this gamepad has accelerometer functionality
	 * @return True if supported
	 */
	public boolean isAccelerometerSupported();

	/**
	 * Returns the current accelerometer value
	 * @param accelerometerCode The accelerometer code (Note: these may vary per platform)
	 * @return The acceleromter value on 3 axis in m/s^2
	 */
	public Vector3 getAccelerometer(int accelerometerCode);

	/**
	 * Returns the accelerometer sensitivity
	 * @return The sensitivity where 0.0 is lowest, 1.0 is highest
	 */
	public float getAccelerometerSensitivity();

	/**
	 * Sets the accelerometer sensitivity
	 * @param sensitivity The sensitivity where 0.0 is lowest, 1.0 is highest
	 */
	public void setAccelerometerSensitivity(float sensitivity);

	/**
	 * Adds a {@link GamePadListener} to listen to state changes
	 * @param listener The {@link GamePadListener} to add
	 */
	public void addListener (GamePadListener listener);

	/**
	 * Removes a {@link GamePadListener}
	 * @param listener The {@link GamePadListener} to remove
	 */
	public void removeListener (GamePadListener listener);
}
