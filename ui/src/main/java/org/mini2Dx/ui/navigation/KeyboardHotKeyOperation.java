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
package org.mini2Dx.ui.navigation;

import org.mini2Dx.gdx.utils.Queue;
import org.mini2Dx.ui.element.Actionable;

/**
 * Internal class for setting/unsetting keyboard hotkeys
 */
public class KeyboardHotKeyOperation {
	private static final Queue<KeyboardHotKeyOperation> POOL = new Queue<>();

	private int keycode;
	private Actionable actionable;
	private boolean mapOperation;

	private KeyboardHotKeyOperation() {}
	
	private void set(int keycode, Actionable actionable, boolean mapOperation) {
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

	public void dispose() {
		actionable = null;

		synchronized(POOL) {
			POOL.addLast(this);
		}
	}

	public static KeyboardHotKeyOperation allocate(int keycode, Actionable actionable, boolean mapOperation) {
		final KeyboardHotKeyOperation result;
		synchronized(POOL) {
			if(POOL.isEmpty()) {
				result = new KeyboardHotKeyOperation();
			} else {
				result = POOL.removeFirst();
			}
		}
		result.set(keycode, actionable, mapOperation);
		return result;
	}
}
