/*******************************************************************************
 * Copyright 2019 See AUTHORS file
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
package org.mini2Dx.ui.gamepad;

import org.mini2Dx.core.input.GamePad;
import org.mini2Dx.core.input.button.GamePadButton;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.element.Actionable;

/**
 * Common interface for implementing {@link GamePad} based UI input
 */
public interface GamePadUiInput<T extends GamePadButton> {
	/**
	 * Updates the input for repeating presses
	 * @param delta The time since the last frame (in seconds)
	 */
	public void update(float delta);

	/**
	 * Returns if the gamepad input is enabled for the {@link UiContainer}
	 * @return True if enabled (enabled by default)
	 */
	public boolean isEnabled();

	/**
	 * Enables this gamepad input for the {@link UiContainer}
	 */
	public void enable();

	/**
	 * Disables this gamepad input for the {@link UiContainer}
	 */
	public void disable();

	/**
	 * Returns the button used to trigger {@link Actionable} instances
	 * @return
	 */
	public T getActionButton();

	/**
	 * Sets the button used for triggering {@link Actionable} instances
	 * @param actionButton
	 */
	public void setActionButton(T actionButton);
	
	/**
	 * Cleans up and de-registers this instance from the associated {@link UiContainer}
	 */
	public void dispose();
	
	/**
	 * Returns the unique id of this {@link GamePadUiInput}
	 * @return A non-null {@link String}
	 */
	public String getId();
}
