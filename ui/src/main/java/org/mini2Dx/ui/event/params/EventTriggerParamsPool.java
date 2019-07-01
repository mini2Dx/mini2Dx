/**
 * Copyright (c) 2017 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
