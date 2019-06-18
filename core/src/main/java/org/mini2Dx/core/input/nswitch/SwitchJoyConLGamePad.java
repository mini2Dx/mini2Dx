/*******************************************************************************
 * Copyright 2019 Viridian Software Limited
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
package org.mini2Dx.core.input.nswitch;

import org.mini2Dx.core.input.GamePad;
import org.mini2Dx.core.input.GamePadListener;
import org.mini2Dx.core.input.button.SwitchJoyConLButton;
import org.mini2Dx.core.input.deadzone.DeadZone;
import org.mini2Dx.core.input.deadzone.NoopDeadZone;
import org.mini2Dx.core.input.deadzone.RadialDeadZone;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Disposable;

public abstract class SwitchJoyConLGamePad implements GamePadListener, Disposable {

	private final Array<SwitchJoyConLGamePadListener> listeners = new Array<SwitchJoyConLGamePadListener>(true, 2, SwitchJoyConLGamePadListener.class);
	private final GamePad gamePad;

	private DeadZone leftStickDeadZone;
	private DeadZone zlDeadZone;
	private boolean zl;

	public SwitchJoyConLGamePad(GamePad gamePad) {
		this(gamePad, new NoopDeadZone());
	}

	public SwitchJoyConLGamePad(GamePad gamePad, DeadZone leftStickDeadZone) {
		super();
		this.gamePad = gamePad;
		this.leftStickDeadZone = leftStickDeadZone;
		this.zlDeadZone = new RadialDeadZone();

		gamePad.addListener(this);
	}

	protected boolean notifyConnected() {
		for(SwitchJoyConLGamePadListener listener : listeners) {
			listener.connected(this);
		}
		return false;
	}

	protected boolean notifyDisconnected() {
		for(SwitchJoyConLGamePadListener listener : listeners) {
			listener.disconnected(this);
		}
		return false;
	}

	protected boolean notifyButtonDown(SwitchJoyConLButton button) {
		for(SwitchJoyConLGamePadListener listener : listeners) {
			if(listener.buttonDown(this, button)) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyButtonUp(SwitchJoyConLButton button) {
		for(SwitchJoyConLGamePadListener listener : listeners) {
			if(listener.buttonUp(this, button)) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyLeftStickXMoved(float value) {
		leftStickDeadZone.updateX(value);
		for(SwitchJoyConLGamePadListener listener : listeners) {
			if(listener.leftStickXMoved(this, leftStickDeadZone.getX())) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyLeftStickYMoved(float value) {
		leftStickDeadZone.updateY(value);
		for(SwitchJoyConLGamePadListener listener : listeners) {
			if(listener.leftStickYMoved(this, leftStickDeadZone.getY())) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyZLMoved(float value) {
		zlDeadZone.updateY(value);
		if(zlDeadZone.getY() >= 0.5f && !zl) {
			notifyButtonDown(SwitchJoyConLButton.ZL);
			zl = true;
		} else if(zlDeadZone.getY() < 0.5f && zl) {
			notifyButtonUp(SwitchJoyConLButton.ZL);
			zl = false;
		}
		for(SwitchJoyConLGamePadListener listener : listeners) {
			if(listener.zlMoved(this, value)) {
				return true;
			}
		}
		return false;
	}

	public void addListener(SwitchJoyConLGamePadListener listener) {
		listeners.add(listener);
	}

	public void removeListener(SwitchJoyConLGamePadListener listener) {
		listeners.removeValue(listener, false);
	}

	public void clearListeners() {
		listeners.clear();
	}

	public int getTotalListeners() {
		return listeners.size;
	}

	@Override
	public void dispose() {
		gamePad.removeListener(this);
		listeners.clear();
	}

	public DeadZone getLeftStickDeadZone() {
		return leftStickDeadZone;
	}

	public void setLeftStickDeadZone(DeadZone leftStickDeadZone) {
		if(leftStickDeadZone == null) {
			leftStickDeadZone = new NoopDeadZone();
		}
		this.leftStickDeadZone = leftStickDeadZone;
	}

	public DeadZone getZLDeadZone() {
		return zlDeadZone;
	}

	public void setZLDeadZone(DeadZone zlDeadZone) {
		if(zlDeadZone == null) {
			zlDeadZone = new NoopDeadZone();
		}
		this.zlDeadZone = zlDeadZone;
	}
}
