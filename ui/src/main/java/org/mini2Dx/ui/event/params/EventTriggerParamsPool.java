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
package org.mini2Dx.ui.event.params;

import org.mini2Dx.gdx.utils.Queue;

/**
 *
 */
public class EventTriggerParamsPool {
	private static final Queue<MouseEventTriggerParams> mouseParams = new Queue<MouseEventTriggerParams>();
	private static final Queue<KeyboardEventTriggerParams> keyboardParams = new Queue<KeyboardEventTriggerParams>();
	private static final Queue<GamePadEventTriggerParams> controllerParams = new Queue<GamePadEventTriggerParams>();
	
	public static MouseEventTriggerParams allocateMouseParams() {
		if(mouseParams.size == 0) {
			return new MouseEventTriggerParams();
		}
		return mouseParams.removeFirst();
	}
	
	public static void release(MouseEventTriggerParams params) {
		mouseParams.addLast(params);
	}
	
	public static KeyboardEventTriggerParams allocateKeyboardParams() {
		if(keyboardParams.size == 0) {
			return new KeyboardEventTriggerParams();
		}
		return keyboardParams.removeFirst();
	}
	
	public static void release(KeyboardEventTriggerParams params) {
		keyboardParams.addLast(params);
	}
	
	public static GamePadEventTriggerParams allocateGamePadParams() {
		if(controllerParams.size == 0) {
			return new GamePadEventTriggerParams();
		}
		return controllerParams.removeFirst();
	}
	
	public static void release(GamePadEventTriggerParams params) {
		controllerParams.addLast(params);
	}
}
