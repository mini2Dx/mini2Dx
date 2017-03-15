/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.ui.event.params;

import org.mini2Dx.core.controller.button.ControllerButton;

/**
 *
 */
public class ControllerEventTriggerParams implements EventTriggerParams {
	private ControllerButton controllerButton;

	public ControllerButton getControllerButton() {
		return controllerButton;
	}

	public void setControllerButton(ControllerButton controllerButton) {
		this.controllerButton = controllerButton;
	}
}
