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
	 * @param triggerHoverEvent True if a hover event should be triggered. Typically used when a controller is being used for navigation.
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
