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
package org.mini2Dx.core.input.xbox360;

import org.mini2Dx.core.input.GamePad;
import org.mini2Dx.core.input.GamePadListener;
import org.mini2Dx.core.input.button.Xbox360Button;
import org.mini2Dx.core.input.deadzone.DeadZone;
import org.mini2Dx.core.input.deadzone.NoopDeadZone;
import org.mini2Dx.core.input.deadzone.RadialDeadZone;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Disposable;

public abstract class Xbox360GamePad implements GamePadListener, Disposable {
	public static final String ID = "360";

	private final Array<Xbox360GamePadListener> listeners = new Array<Xbox360GamePadListener>(true, 2, Xbox360GamePadListener.class);
	private final GamePad gamePad;

	private DeadZone leftStickDeadZone, rightStickDeadZone;
	private DeadZone leftTriggerDeadZone, rightTriggerDeadZone;
	private boolean leftTrigger, rightTrigger;

	public Xbox360GamePad(GamePad gamePad) {
		this(gamePad, new NoopDeadZone(), new NoopDeadZone());
	}

	public Xbox360GamePad(GamePad gamePad, DeadZone leftStickDeadZone, DeadZone rightStickDeadZone) {
		super();
		this.gamePad = gamePad;
		this.leftStickDeadZone = leftStickDeadZone;
		this.rightStickDeadZone = rightStickDeadZone;
		this.leftTriggerDeadZone = new RadialDeadZone();
		this.rightTriggerDeadZone = new RadialDeadZone();

		gamePad.addListener(this);
	}

	protected boolean notifyConnected() {
		for(Xbox360GamePadListener listener : listeners) {
			listener.connected(this);
		}
		return false;
	}

	protected boolean notifyDisconnected() {
		for(Xbox360GamePadListener listener : listeners) {
			listener.disconnected(this);
		}
		return false;
	}

	protected boolean notifyButtonDown(Xbox360Button button) {
		for(Xbox360GamePadListener listener : listeners) {
			if(listener.buttonDown(this, button)) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyButtonUp(Xbox360Button button) {
		for(Xbox360GamePadListener listener : listeners) {
			if(listener.buttonUp(this, button)) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyLeftTriggerMoved(float value) {
		leftTriggerDeadZone.updateY(value);
		if(leftTriggerDeadZone.getY() >= 0.5f && !leftTrigger) {
			notifyButtonDown(Xbox360Button.LEFT_TRIGGER);
			leftTrigger = true;
		} else if(leftTriggerDeadZone.getY() < 0.5f && leftTrigger) {
			notifyButtonUp(Xbox360Button.LEFT_TRIGGER);
			leftTrigger = false;
		}
		for(Xbox360GamePadListener listener : listeners) {
			if(listener.leftTriggerMoved(this, value)) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyRightTriggerMoved(float value) {
		rightTriggerDeadZone.updateY(value);
		if(rightTriggerDeadZone.getY() >= 0.5f && !rightTrigger) {
			notifyButtonDown(Xbox360Button.RIGHT_TRIGGER);
			rightTrigger = true;
		} else if(rightTriggerDeadZone.getY() < 0.5f && rightTrigger) {
			notifyButtonUp(Xbox360Button.RIGHT_TRIGGER);
			rightTrigger = false;
		}
		for(Xbox360GamePadListener listener : listeners) {
			if(listener.rightTriggerMoved(this, value)) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyLeftStickXMoved(float value) {
		leftStickDeadZone.updateX(value);
		for(Xbox360GamePadListener listener : listeners) {
			if(listener.leftStickXMoved(this, leftStickDeadZone.getX())) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyLeftStickYMoved(float value) {
		leftStickDeadZone.updateY(value);
		for(Xbox360GamePadListener listener : listeners) {
			if(listener.leftStickYMoved(this, leftStickDeadZone.getY())) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyRightStickXMoved(float value) {
		rightStickDeadZone.updateX(value);
		for(Xbox360GamePadListener listener : listeners) {
			if(listener.rightStickXMoved(this, rightStickDeadZone.getX())) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyRightStickYMoved(float value) {
		rightStickDeadZone.updateY(value);
		for(Xbox360GamePadListener listener : listeners) {
			if(listener.rightStickYMoved(this, rightStickDeadZone.getY())) {
				return true;
			}
		}
		return false;
	}

	public void addListener(Xbox360GamePadListener listener) {
		listeners.add(listener);
	}

	public void removeListener(Xbox360GamePadListener listener) {
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
}
