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
package org.mini2Dx.core.input.xbox;

import org.mini2Dx.core.input.GamePad;
import org.mini2Dx.core.input.GamePadMapping;
import org.mini2Dx.core.input.button.PS4Button;
import org.mini2Dx.core.input.button.XboxButton;
import org.mini2Dx.core.input.deadzone.DeadZone;
import org.mini2Dx.core.input.deadzone.NoopDeadZone;
import org.mini2Dx.core.input.deadzone.RadialDeadZone;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Disposable;

public abstract class XboxGamePad implements GamePadMapping<XboxGamePadListener>, Disposable {
	public static final String [] ID = new String [] {
			"360".toLowerCase(),
			"Xbox".toLowerCase(),
			"X-Box".toLowerCase(),
			"X360".toLowerCase(),
			"XInput".toLowerCase(),
			"PC Wired GamePad".toLowerCase()
	};

	protected final Array<XboxGamePadListener> listeners = new Array<XboxGamePadListener>(true, 2, XboxGamePadListener.class);
	protected final GamePad gamePad;

	protected DeadZone leftStickDeadZone, rightStickDeadZone;
	protected DeadZone leftTriggerDeadZone, rightTriggerDeadZone;
	protected boolean leftTrigger, rightTrigger;

	public XboxGamePad(GamePad gamePad) {
		this(gamePad, new NoopDeadZone(), new NoopDeadZone());
	}

	public XboxGamePad(GamePad gamePad, DeadZone leftStickDeadZone, DeadZone rightStickDeadZone) {
		super();
		this.gamePad = gamePad;
		this.leftStickDeadZone = leftStickDeadZone;
		this.rightStickDeadZone = rightStickDeadZone;
		this.leftTriggerDeadZone = new RadialDeadZone();
		this.rightTriggerDeadZone = new RadialDeadZone();

		gamePad.addListener(this);
	}

	public abstract boolean isButtonDown(XboxButton button);

	public abstract boolean isButtonUp(XboxButton button);

	protected boolean notifyConnected() {
		for(XboxGamePadListener listener : listeners) {
			listener.connected(this);
		}
		return false;
	}

	protected boolean notifyDisconnected() {
		for(XboxGamePadListener listener : listeners) {
			listener.disconnected(this);
		}
		return false;
	}

	protected boolean notifyButtonDown(XboxButton button) {
		for(XboxGamePadListener listener : listeners) {
			if(listener.buttonDown(this, button)) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyButtonUp(XboxButton button) {
		for(XboxGamePadListener listener : listeners) {
			if(listener.buttonUp(this, button)) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyLeftTriggerMoved(float value) {
		leftTriggerDeadZone.updateY(value);
		if(leftTriggerDeadZone.getY() >= 0.5f && !leftTrigger) {
			notifyButtonDown(XboxButton.LEFT_TRIGGER);
			leftTrigger = true;
		} else if(leftTriggerDeadZone.getY() < 0.5f && leftTrigger) {
			notifyButtonUp(XboxButton.LEFT_TRIGGER);
			leftTrigger = false;
		}
		for(XboxGamePadListener listener : listeners) {
			if(listener.leftTriggerMoved(this, value)) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyRightTriggerMoved(float value) {
		rightTriggerDeadZone.updateY(value);
		if(rightTriggerDeadZone.getY() >= 0.5f && !rightTrigger) {
			notifyButtonDown(XboxButton.RIGHT_TRIGGER);
			rightTrigger = true;
		} else if(rightTriggerDeadZone.getY() < 0.5f && rightTrigger) {
			notifyButtonUp(XboxButton.RIGHT_TRIGGER);
			rightTrigger = false;
		}
		for(XboxGamePadListener listener : listeners) {
			if(listener.rightTriggerMoved(this, value)) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyLeftStickXMoved(float value) {
		leftStickDeadZone.updateX(value);
		for(XboxGamePadListener listener : listeners) {
			if(listener.leftStickXMoved(this, leftStickDeadZone.getX())) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyLeftStickYMoved(float value) {
		leftStickDeadZone.updateY(value);
		for(XboxGamePadListener listener : listeners) {
			if(listener.leftStickYMoved(this, leftStickDeadZone.getY())) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyRightStickXMoved(float value) {
		rightStickDeadZone.updateX(value);
		for(XboxGamePadListener listener : listeners) {
			if(listener.rightStickXMoved(this, rightStickDeadZone.getX())) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyRightStickYMoved(float value) {
		rightStickDeadZone.updateY(value);
		for(XboxGamePadListener listener : listeners) {
			if(listener.rightStickYMoved(this, rightStickDeadZone.getY())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void addListener(XboxGamePadListener listener) {
		listeners.add(listener);
	}

	@Override
	public void addListener(int index, XboxGamePadListener listener) {
		listeners.insert(index, listener);
	}

	@Override
	public void removeListener(int index) {
		listeners.removeIndex(index);
	}

	@Override
	public void removeListener(XboxGamePadListener listener) {
		listeners.removeValue(listener, false);
	}

	@Override
	public void clearListeners() {
		listeners.clear();
	}

	@Override
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
		this.leftStickDeadZone = leftStickDeadZone;
	}

	public DeadZone getRightStickDeadZone() {
		return rightStickDeadZone;
	}

	public void setRightStickDeadZone(DeadZone rightStickDeadZone) {
		this.rightStickDeadZone = rightStickDeadZone;
	}

	public DeadZone getLeftTriggerDeadZone() {
		return leftTriggerDeadZone;
	}

	public void setLeftTriggerDeadZone(DeadZone leftTriggerDeadZone) {
		if(leftTriggerDeadZone == null) {
			leftTriggerDeadZone = new NoopDeadZone();
		}
		this.leftTriggerDeadZone = leftTriggerDeadZone;
	}

	public DeadZone getRightTriggerDeadZone() {
		return rightTriggerDeadZone;
	}

	public void setRightTriggerDeadZone(DeadZone rightTriggerDeadZone) {
		if(rightTriggerDeadZone == null) {
			rightTriggerDeadZone = new NoopDeadZone();
		}
		this.rightTriggerDeadZone = rightTriggerDeadZone;
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
