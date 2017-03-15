/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.ui.event.params;

/**
 *
 */
public class MouseEventTriggerParams implements EventTriggerParams {
	private int mouseX, mouseY;

	public int getMouseX() {
		return mouseX;
	}

	public void setMouseX(int mouseX) {
		this.mouseX = mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public void setMouseY(int mouseY) {
		this.mouseY = mouseY;
	}
}
