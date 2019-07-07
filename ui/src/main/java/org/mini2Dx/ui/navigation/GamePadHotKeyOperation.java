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

import org.mini2Dx.core.input.button.GamePadButton;
import org.mini2Dx.ui.element.Actionable;

/**
 * Internal class for setting/unsetting {@link GamePadButton} hotkeys
 */
public class GamePadHotKeyOperation {
	private final GamePadButton controllerButton;
	private final Actionable actionable;
	private final boolean mapOperation;
	
	public GamePadHotKeyOperation(GamePadButton controllerButton, Actionable actionable, boolean mapOperation) {
		this.controllerButton = controllerButton;
		this.actionable = actionable;
		this.mapOperation = mapOperation;
	}

	public GamePadButton getGamePadButton() {
		return controllerButton;
	}

	public Actionable getActionable() {
		return actionable;
	}

	public boolean isMapOperation() {
		return mapOperation;
	}
}
