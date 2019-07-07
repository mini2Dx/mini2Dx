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

import org.mini2Dx.core.input.GamePadType;
import org.mini2Dx.ui.InputSource;
import org.mini2Dx.ui.UiContainer;

/**
 * Interface for listening to {@link InputSource} changes
 */
public interface UiInputSourceListener {

	/**
	 * Called when the {@link InputSource} changes
	 * @param container The {@link UiContainer} that the {@link InputSource} changed on
	 * @param oldInputSource The previous {@link InputSource}
	 * @param newInputSource The new {@link InputSource}
	 */
	public void inputSourceChanged(UiContainer container, InputSource oldInputSource, InputSource newInputSource);

	/**
	 * Called when the {@link GamePadType} changes
	 * @param container The {@link UiContainer} that the {@link GamePadType} changed on
	 * @param oldGamePadType The previous {@link GamePadType}
	 * @param newGamePadType The new {@link GamePadType}
	 */
	public void gamePadTypeChanged(UiContainer container, GamePadType oldGamePadType, GamePadType newGamePadType);
}
