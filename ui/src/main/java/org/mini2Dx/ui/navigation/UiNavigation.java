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
package org.mini2Dx.ui.navigation;

import org.mini2Dx.ui.element.Actionable;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.listener.HoverListener;

/**
 * Common interface for user interface navigation
 */
public interface UiNavigation extends HoverListener {
	/**
	 * Re-orders elements based on screen size if necessary
	 * 
	 * @param screenSize
	 *            The current {@link ScreenSize}
	 */
	public void layout(ScreenSize screenSize);

	/**
	 * Resets the selection back to the first {@link Actionable}
	 * @return The first {@link Actionable}
	 */
	public Actionable resetCursor();

	/**
	 * Resets the selection back to the first {@link Actionable}
	 * @param triggerHoverEvent True if a hover event should be triggered. Typically used when a gamepad is being used for navigation.
	 * @return The first {@link Actionable}
	 */
	public Actionable resetCursor(boolean triggerHoverEvent);
	
	/**
	 * Returns the {@link Actionable} highlighted by the cursor
	 * @return Null if there is no {@link Actionable}
	 */
	public Actionable getCursor();

	/**
	 * Adds a {@link Actionable} to end of the navigation
	 * 
	 * @param actionable The {@link Actionable} to add
	 */
	public void add(Actionable actionable);

	/**
	 * Removes a {@link Actionable} from the navigation and shifts any following
	 * {@link Actionable}s by 1 place
	 * 
	 * @param actionable The {@link Actionable} to remove
	 */
	public void remove(Actionable actionable);
	
	/**
	 * Removes all {@link Actionable} instances from the navigation
	 */
	public void removeAll();

	/**
	 * Replace an {@link Actionable} at a specific index
	 * @param index The index to replace
	 * @param actionable The new {@link Actionable}
	 */
	public void set(int index, Actionable actionable);

	/**
	 * Update (if required) navigation from key input
	 * @param keycode The key that was pressed
	 * @return The currently highlighted {@link Actionable}
	 */
	public Actionable navigate(int keycode);
}
