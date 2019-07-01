/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.ui.event;

import org.mini2Dx.gdx.Input;

/**
 *
 */
public enum EventTrigger {
	LEFT_MOUSE_CLICK,
	RIGHT_MOUSE_CLICK,
	MIDDLE_MOUSE_CLICK,
	KEYBOARD,
	CONTROLLER;
	
	public static EventTrigger getTriggerForMouseClick(int button) {
		switch(button) {
		case Input.Buttons.LEFT:
			return LEFT_MOUSE_CLICK;
		case Input.Buttons.RIGHT:
			return EventTrigger.RIGHT_MOUSE_CLICK;
		case Input.Buttons.MIDDLE:
			return EventTrigger.MIDDLE_MOUSE_CLICK;
		}
		return null;
	}
}
