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
package org.mini2Dx.ui.element;

import org.mini2Dx.core.controller.button.ControllerButton;
import org.mini2Dx.ui.input.UiNavigation;
import org.mini2Dx.ui.render.ActionableRenderNode;

import com.badlogic.gdx.Input.Keys;

/**
 * Common interface for {@link UiElement}s that can be navigated by keyboard or
 * controller
 */
public interface Navigatable {
	/**
	 * Triggers a navigation and returns the newly highlighted {@link ActionableRenderNode}
	 * @param keycode The navigation {@link Keys} value
	 * @return Null if no {@link UiNavigation} is available
	 */
	public ActionableRenderNode navigate(int keycode);

	/**
	 * Returns the corresponding {@Link ActionableRenderNode} mapped to a keyboard hotkey
	 * @param keycode The {@link Keys} keycode that is the hotkey
	 * @return Null if there is no mapping
	 */
	public ActionableRenderNode hotkey(int keycode);

	/**
	 * Returns the corresponding {@Link ActionableRenderNode} mapped to a {@link ControllerButton} hotkey
	 * @param button The {@link ControllerButton} that is the hotkey
	 * @return Null if there is no mapping
	 */
	public ActionableRenderNode hotkey(ControllerButton button);

	/**
	 * Maps a {@link ControllerButton} to a {@link Actionable}
	 * @param button The {@link ControllerButton} that is the hotkey
	 * @param actionable The {@link Actionable} to trigger when the hotkey is pressed
	 */
	public void setHotkey(ControllerButton button, Actionable actionable);

	/**
	 * Maps a keyboard button to a {@link Actionable}
	 * @param keycode The {@link Keys} keycode that is the hotkey
	 * @param actionable The {@link Actionable} to trigger when the key is pressed
	 */
	public void setHotkey(int keycode, Actionable actionable);

	/**
	 * Unmaps a {@link ControllerButton} hotkey
	 * @param button The {@link ControllerButton} that is the hotkey
	 */
	public void unsetHotkey(ControllerButton button);

	/**
	 * Unmaps a keyboard hotkey
	 * @param keycode The {@link Keys} keycode that is the hotkey
	 */
	public void unsetHotkey(int keycode);

	/**
	 * Returns the {@link UiNavigation} currently being navigated
	 * @return Null if no navigation is occurring
	 */
	public UiNavigation getNavigation();
}
