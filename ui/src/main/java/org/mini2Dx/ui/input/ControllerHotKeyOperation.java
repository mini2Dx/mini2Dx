/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.input;

import org.mini2Dx.core.controller.button.ControllerButton;
import org.mini2Dx.ui.element.Actionable;

/**
 *
 */
public class ControllerHotKeyOperation {
	private final ControllerButton controllerButton;
	private final Actionable actionable;
	private final boolean mapOperation;
	
	public ControllerHotKeyOperation(ControllerButton controllerButton, Actionable actionable, boolean mapOperation) {
		this.controllerButton = controllerButton;
		this.actionable = actionable;
		this.mapOperation = mapOperation;
	}

	public ControllerButton getControllerButton() {
		return controllerButton;
	}

	public Actionable getActionable() {
		return actionable;
	}

	public boolean isMapOperation() {
		return mapOperation;
	}
}
