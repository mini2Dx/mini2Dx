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
import org.mini2Dx.core.input.GamePadMapping;
import org.mini2Dx.core.input.button.SwitchDualJoyConButton;
import org.mini2Dx.core.input.deadzone.DeadZone;
import org.mini2Dx.core.input.deadzone.NoopDeadZone;
import org.mini2Dx.core.input.deadzone.RadialDeadZone;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Disposable;

public abstract class SwitchDualJoyConGamePad implements GamePadMapping<SwitchDualJoyConGamePadListener>, Disposable {

	private final Array<SwitchDualJoyConGamePadListener> listeners = new Array<SwitchDualJoyConGamePadListener>(true, 2, SwitchDualJoyConGamePadListener.class);
	private final GamePad gamePad;

	private DeadZone leftStickDeadZone, rightStickDeadZone;
	private DeadZone zlDeadZone, zrDeadZone;
	private boolean zl, zr;

	public SwitchDualJoyConGamePad(GamePad gamePad) {
		this(gamePad, new NoopDeadZone(), new NoopDeadZone());
	}

	public SwitchDualJoyConGamePad(GamePad gamePad, DeadZone leftStickDeadZone, DeadZone rightStickDeadZone) {
		super();
		this.gamePad = gamePad;
		this.leftStickDeadZone = leftStickDeadZone;
		this.rightStickDeadZone = rightStickDeadZone;
		this.zlDeadZone = new RadialDeadZone();
		this.zrDeadZone = new RadialDeadZone();

		gamePad.addListener(this);
	}

	protected boolean notifyConnected() {
		for(SwitchDualJoyConGamePadListener listener : listeners) {
			listener.connected(this);
		}
		return false;
	}

	protected boolean notifyDisconnected() {
		for(SwitchDualJoyConGamePadListener listener : listeners) {
			listener.disconnected(this);
		}
		return false;
	}

	protected boolean notifyButtonDown(SwitchDualJoyConButton button) {
		for(SwitchDualJoyConGamePadListener listener : listeners) {
			if(listener.buttonDown(this, button)) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyButtonUp(SwitchDualJoyConButton button) {
		for(SwitchDualJoyConGamePadListener listener : listeners) {
			if(listener.buttonUp(this, button)) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyLeftStickXMoved(float value) {
		leftStickDeadZone.updateX(value);
		for(SwitchDualJoyConGamePadListener listener : listeners) {
			if(listener.leftStickXMoved(this, leftStickDeadZone.getX())) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyLeftStickYMoved(float value) {
		leftStickDeadZone.updateY(value);
		for(SwitchDualJoyConGamePadListener listener : listeners) {
			if(listener.leftStickYMoved(this, leftStickDeadZone.getY())) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyRightStickXMoved(float value) {
		rightStickDeadZone.updateX(value);
		for(SwitchDualJoyConGamePadListener listener : listeners) {
			if(listener.rightStickXMoved(this, rightStickDeadZone.getX())) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyRightStickYMoved(float value) {
		rightStickDeadZone.updateY(value);
		for(SwitchDualJoyConGamePadListener listener : listeners) {
			if(listener.rightStickYMoved(this, rightStickDeadZone.getY())) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyZLMoved(float value) {
		zlDeadZone.updateY(value);
		if(zlDeadZone.getY() >= 0.5f && !zl) {
			notifyButtonDown(SwitchDualJoyConButton.ZL);
			zl = true;
		} else if(zlDeadZone.getY() < 0.5f && zl) {
			notifyButtonUp(SwitchDualJoyConButton.ZL);
			zl = false;
		}
		for(SwitchDualJoyConGamePadListener listener : listeners) {
			if(listener.zlMoved(this, value)) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyZRMoved(float value) {
		zrDeadZone.updateY(value);
		if(zrDeadZone.getY() >= 0.5f && !zr) {
			notifyButtonDown(SwitchDualJoyConButton.ZR);
			zr = true;
		} else if(zrDeadZone.getY() < 0.5f && zr) {
			notifyButtonUp(SwitchDualJoyConButton.ZR);
			zr = false;
		}
		for(SwitchDualJoyConGamePadListener listener : listeners) {
			if(listener.zrMoved(this, value)) {
				return true;
			}
		}
		return false;
	}

	public void addListener(SwitchDualJoyConGamePadListener listener) {
		listeners.add(listener);
	}

	public void removeListener(SwitchDualJoyConGamePadListener listener) {
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

	public DeadZone getRightStickDeadZone() {
		return rightStickDeadZone;
	}

	public void setRightStickDeadZone(DeadZone rightStickDeadZone) {
		if(rightStickDeadZone == null) {
			rightStickDeadZone = new NoopDeadZone();
		}
		this.rightStickDeadZone = rightStickDeadZone;
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

	public DeadZone getZRDeadZone() {
		return zrDeadZone;
	}

	public void setZRDeadZone(DeadZone zrDeadZone) {
		if(zrDeadZone == null) {
			zrDeadZone = new NoopDeadZone();
		}
		this.zrDeadZone = zrDeadZone;
	}
}
