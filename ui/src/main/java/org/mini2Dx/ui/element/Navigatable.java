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
package org.mini2Dx.ui.element;

import org.mini2Dx.core.input.button.GamePadButton;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.ui.navigation.UiNavigation;
import org.mini2Dx.ui.render.ActionableRenderNode;

/**
 * Common interface for {@link UiElement}s that can be navigated by keyboard or
 * gamepad
 */
public interface Navigatable {
	/**
	 * Returns the unique id of the {@link Navigatable}
	 * @return A non-null {@link String} that is the id
	 */
	public String getId();
	/**
	 * Triggers a navigation and returns the newly highlighted {@link ActionableRenderNode}
	 * @param keycode The navigation {@link Input.Keys} value
	 * @return Null if no {@link UiNavigation} is available
	 */
	public ActionableRenderNode navigate(int keycode);

	/**
	 * Returns the corresponding {@link ActionableRenderNode} mapped to a keyboard hotkey
	 * @param keycode The {@link Input.Keys} keycode that is the hotkey
	 * @return Null if there is no mapping
	 */
	public ActionableRenderNode hotkey(int keycode);

	/**
	 * Returns the corresponding {@link ActionableRenderNode} mapped to a {@link GamePadButton} hotkey
	 * @param button The {@link GamePadButton} that is the hotkey
	 * @return Null if there is no mapping
	 */
	public ActionableRenderNode hotkey(GamePadButton button);

	/**
	 * Maps a {@link GamePadButton} to a {@link Actionable}
	 * @param button The {@link GamePadButton} that is the hotkey
	 * @param actionable The {@link Actionable} to trigger when the hotkey is pressed
	 */
	public void setHotkey(GamePadButton button, Actionable actionable);

	/**
	 * Maps a keyboard button to a {@link Actionable}
	 * @param keycode The {@link Input.Keys} keycode that is the hotkey
	 * @param actionable The {@link Actionable} to trigger when the key is pressed
	 */
	public void setHotkey(int keycode, Actionable actionable);

	/**
	 * Unmaps a {@link GamePadButton} hotkey
	 * @param button The {@link GamePadButton} that is the hotkey
	 */
	public void unsetHotkey(GamePadButton button);

	/**
	 * Unmaps a keyboard hotkey
	 * @param keycode The {@link Input.Keys} keycode that is the hotkey
	 */
	public void unsetHotkey(int keycode);
	
	/**
	 * Unmaps all hotkeys
	 */
	public void clearHotkeys();
	
	/**
	 * Unmaps all gamepad hotkeys
	 */
	public void clearGamePadHotkeys();
	
	/**
	 * Unmaps all keyboard hotkeys
	 */
	public void clearKeyboardHotkeys();

	/**
	 * Returns the {@link UiNavigation} currently being navigated
	 * @return Null if no navigation is occurring
	 */
	public UiNavigation getNavigation();
}
