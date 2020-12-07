/*******************************************************************************
 * Copyright 2020 See AUTHORS file
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
package org.mini2Dx.ui.listener;

import org.mini2Dx.ui.element.UiElement;

public interface NodeMouseListener {

	/**
	 * Event sent on mouse moved
	 * @param element The {@link UiElement} sending the event
	 * @param elementContainsMouse True if the {@link UiElement} contains the mouse
	 */
	public void onMouseMoved(UiElement element, boolean elementContainsMouse);

	/**
	 * Event sent on mouse down
	 * @param element The {@link UiElement} sending the event
	 * @param elementContainsMouse True if the {@link UiElement} contains the mouse
	 */
	public void onMouseDown(UiElement element, boolean elementContainsMouse);

	/**
	 * Event sent on mouse up. Note: This event will only be sent if the element was an {@link org.mini2Dx.ui.element.Actionable} triggered by a mouse down event
	 * @param element The {@link UiElement} sending the event
	 * @param elementContainsMouse True if the {@link UiElement} contains the mouse
	 */
	public void onMouseUp(UiElement element, boolean elementContainsMouse);
}
