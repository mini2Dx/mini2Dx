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
import org.mini2Dx.core.input.button.SwitchJoyConRButton;
import org.mini2Dx.core.input.deadzone.DeadZone;
import org.mini2Dx.core.input.deadzone.NoopDeadZone;
import org.mini2Dx.core.input.deadzone.RadialDeadZone;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Disposable;

public abstract class SwitchJoyConRGamePad implements GamePadListener, Disposable {
	public static final String [] ID = new String [] {
			"Joy-Con (R)".toLowerCase()
	};

	private final Array<SwitchJoyConRGamePadListener> listeners = new Array<SwitchJoyConRGamePadListener>(true, 2, SwitchJoyConRGamePadListener.class);
	private final GamePad gamePad;

	private DeadZone rightStickDeadZone;
	private DeadZone zrDeadZone;
	private boolean zr;

	public SwitchJoyConRGamePad(GamePad gamePad) {
		this(gamePad, new NoopDeadZone());
	}

	public SwitchJoyConRGamePad(GamePad gamePad, DeadZone rightStickDeadZone) {
		super();
		this.gamePad = gamePad;
		this.rightStickDeadZone = rightStickDeadZone;
		this.zrDeadZone = new RadialDeadZone();

		gamePad.addListener(this);
	}

	public abstract boolean isButtonDown(SwitchJoyConRButton button);

	public abstract boolean isButtonUp(SwitchJoyConRButton button);

	protected boolean notifyConnected() {
		for(SwitchJoyConRGamePadListener listener : listeners) {
			listener.connected(this);
		}
		return false;
	}

	protected boolean notifyDisconnected() {
		for(SwitchJoyConRGamePadListener listener : listeners) {
			listener.disconnected(this);
		}
		return false;
	}

	protected boolean notifyButtonDown(SwitchJoyConRButton button) {
		for(SwitchJoyConRGamePadListener listener : listeners) {
			if(listener.buttonDown(this, button)) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyButtonUp(SwitchJoyConRButton button) {
		for(SwitchJoyConRGamePadListener listener : listeners) {
			if(listener.buttonUp(this, button)) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyRightStickXMoved(float value) {
		rightStickDeadZone.updateX(value);
		for(SwitchJoyConRGamePadListener listener : listeners) {
			if(listener.rightStickXMoved(this, rightStickDeadZone.getX())) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyRightStickYMoved(float value) {
		rightStickDeadZone.updateY(value);
		for(SwitchJoyConRGamePadListener listener : listeners) {
			if(listener.rightStickYMoved(this, rightStickDeadZone.getY())) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyZRMoved(float value) {
		zrDeadZone.updateY(value);
		if(zrDeadZone.getY() >= 0.5f && !zr) {
			notifyButtonDown(SwitchJoyConRButton.ZR);
			zr = true;
		} else if(zrDeadZone.getY() < 0.5f && zr) {
			notifyButtonUp(SwitchJoyConRButton.ZR);
			zr = false;
		}
		for(SwitchJoyConRGamePadListener listener : listeners) {
			if(listener.zrMoved(this, value)) {
				return true;
			}
		}
		return false;
	}

	public void addListener(SwitchJoyConRGamePadListener listener) {
		listeners.add(listener);
	}

	public void removeListener(SwitchJoyConRGamePadListener listener) {
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

	public DeadZone getRightStickDeadZone() {
		return rightStickDeadZone;
	}

	public void setRightStickDeadZone(DeadZone rightStickDeadZone) {
		if(rightStickDeadZone == null) {
			rightStickDeadZone = new NoopDeadZone();
		}
		this.rightStickDeadZone = rightStickDeadZone;
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

	public boolean isConnected() {
		return gamePad.isConnected();
	}

	public String getInstanceId() {
		return gamePad.getInstanceId();
	}

	public String getModelInfo() {
		return gamePad.getModelInfo();
	}

	public boolean isPlayerIndicesSupported() {
		return gamePad.isPlayerIndicesSupported();
	}

	public int getPlayerIndex() {
		return gamePad.getPlayerIndex();
	}

	public void setPlayerIndex(int playerIndex) {
		gamePad.setPlayerIndex(playerIndex);
	}

	public boolean isVibrateSupported() {
		return gamePad.isVibrateSupported();
	}

	public boolean isVibrating() {
		return gamePad.isVibrating();
	}

	public float getVibrationStrength() {
		return gamePad.getVibrationStrength();
	}

	public void startVibration(float strength) {
		gamePad.startVibration(strength);
	}

	public void stopVibration() {
		gamePad.stopVibration();
	}
}
