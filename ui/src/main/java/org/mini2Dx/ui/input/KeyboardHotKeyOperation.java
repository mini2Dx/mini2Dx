/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.input;

import org.mini2Dx.ui.element.Actionable;

/**
 *
 */
public class KeyboardHotKeyOperation {
	private final int keycode;
	private final Actionable actionable;
	private final boolean mapOperation;
	
	public KeyboardHotKeyOperation(int keycode, Actionable actionable, boolean mapOperation) {
		this.keycode = keycode;
		this.actionable = actionable;
		this.mapOperation = mapOperation;
	}

	public int getKeycode() {
		return keycode;
	}

	public Actionable getActionable() {
		return actionable;
	}

	public boolean isMapOperation() {
		return mapOperation;
	}
}
