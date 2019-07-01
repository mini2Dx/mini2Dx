/**
 * Copyright (c) 2016 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui.controller;

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
	 * Returns if the controller input is enabled for the {@link UiContainer}
	 * @return True if enabled (enabled by default)
	 */
	public boolean isEnabled();

	/**
	 * Enables this controller input for the {@link UiContainer}
	 */
	public void enable();

	/**
	 * Disables this controller input for the {@link UiContainer}
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
