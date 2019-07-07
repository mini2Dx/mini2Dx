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
package org.mini2Dx.ui.listener;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.input.GamePadType;
import org.mini2Dx.ui.InputSource;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.element.UiElement;

/**
 * Interface for listening to {@link UiContainer} events
 */
public interface UiContainerListener extends ScreenSizeListener, UiInputSourceListener {
	/**
	 * Called before the {@link UiContainer} is updated
	 * @param container The {@link UiContainer} that will be updated
	 * @param delta The frame delta value
	 */
	public void preUpdate(UiContainer container, float delta);
	
	/**
	 * Called after the {@link UiContainer} is updated
	 * @param container The {@link UiContainer} that was updated
	 * @param delta The frame delta value
	 */
	public void postUpdate(UiContainer container, float delta);
	
	/**
	 * Called before the {@link UiContainer} is rendered
	 * @param container The {@link UiContainer} that will be rendered
	 * @param g The {@link Graphics} context
	 */
	public void preRender(UiContainer container, Graphics g);
	
	/**
	 * Called after the {@link UiContainer} is rendered
	 * @param container The {@link UiContainer} that was rendered
	 * @param g The {@link Graphics} context
	 */
	public void postRender(UiContainer container, Graphics g);
	
	/**
	 * Called when a {@link UiElement} becomes active from user input (e.g. click events, etc.)
	 * @param container The {@link UiContainer} the element became active on
	 * @param element The {@link UiElement} that became active
	 */
	public void onElementAction(UiContainer container, UiElement element);
}
