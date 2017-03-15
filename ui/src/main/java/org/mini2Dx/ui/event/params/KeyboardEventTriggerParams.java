/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.ui.event.params;

/**
 *
 */
public class KeyboardEventTriggerParams implements EventTriggerParams {
	private int key;

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}
}
