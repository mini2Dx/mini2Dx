/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.ui.event.params;

import org.mini2Dx.core.input.button.GamePadButton;

/**
 *
 */
public class GamePadEventTriggerParams implements EventTriggerParams {
	private GamePadButton controllerButton;

	public GamePadButton getGamePadButton() {
		return controllerButton;
	}

	public void setGamePadButton(GamePadButton controllerButton) {
		this.controllerButton = controllerButton;
	}
}
