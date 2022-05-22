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
		return LEFT_MOUSE_CLICK;
	}
}
