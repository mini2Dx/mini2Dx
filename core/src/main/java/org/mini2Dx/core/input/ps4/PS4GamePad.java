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
package org.mini2Dx.core.input.ps4;

import org.mini2Dx.core.input.GamePad;
import org.mini2Dx.core.input.GamePadListener;
import org.mini2Dx.core.input.button.PS4Button;
import org.mini2Dx.core.input.deadzone.DeadZone;
import org.mini2Dx.core.input.deadzone.NoopDeadZone;
import org.mini2Dx.core.input.deadzone.RadialDeadZone;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Disposable;

public abstract class PS4GamePad implements GamePadListener, Disposable {
	public static final String [] ID = new String [] {
		"Sony DualShock 4".toLowerCase(),
		"PS4".toLowerCase()
	};

	private final Array<PS4GamePadListener> listeners = new Array<PS4GamePadListener>(true, 2, PS4GamePadListener.class);
	private final GamePad gamePad;

	private DeadZone leftStickDeadZone, rightStickDeadZone;
	private DeadZone l2DeadZone, r2DeadZone;
	private boolean l2, r2;

	public PS4GamePad(GamePad gamePad) {
		this(gamePad, new NoopDeadZone(), new NoopDeadZone());
	}

	public PS4GamePad(GamePad gamePad, DeadZone leftStickDeadZone, DeadZone rightStickDeadZone) {
		super();
		this.gamePad = gamePad;
		this.leftStickDeadZone = leftStickDeadZone;
		this.rightStickDeadZone = rightStickDeadZone;
		this.l2DeadZone = new RadialDeadZone();
		this.r2DeadZone = new RadialDeadZone();

		gamePad.addListener(this);
	}

	protected boolean notifyConnected() {
		for(PS4GamePadListener listener : listeners) {
			listener.connected(this);
		}
		return false;
	}

	protected boolean notifyDisconnected() {
		for(PS4GamePadListener listener : listeners) {
			listener.disconnected(this);
		}
		return false;
	}

	protected boolean notifyButtonDown(PS4Button button) {
		for(PS4GamePadListener listener : listeners) {
			if(listener.buttonDown(this, button)) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyButtonUp(PS4Button button) {
		for(PS4GamePadListener listener : listeners) {
			if(listener.buttonUp(this, button)) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyLeftStickXMoved(float value) {
		leftStickDeadZone.updateX(value);
		for(PS4GamePadListener listener : listeners) {
			if(listener.leftStickXMoved(this, leftStickDeadZone.getX())) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyLeftStickYMoved(float value) {
		leftStickDeadZone.updateY(value);
		for(PS4GamePadListener listener : listeners) {
			if(listener.leftStickYMoved(this, leftStickDeadZone.getY())) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyRightStickXMoved(float value) {
		rightStickDeadZone.updateX(value);
		for(PS4GamePadListener listener : listeners) {
			if(listener.rightStickXMoved(this, rightStickDeadZone.getX())) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyRightStickYMoved(float value) {
		rightStickDeadZone.updateY(value);
		for(PS4GamePadListener listener : listeners) {
			if(listener.rightStickYMoved(this, rightStickDeadZone.getY())) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyL2Moved(float value) {
		l2DeadZone.updateY(value);
		if(l2DeadZone.getY() >= 0.5f && !l2) {
			notifyButtonDown(PS4Button.L2);
			l2 = true;
		} else if(l2DeadZone.getY() < 0.5f && l2) {
			notifyButtonUp(PS4Button.L2);
			l2 = false;
		}
		for(PS4GamePadListener listener : listeners) {
			if(listener.l2Moved(this, value)) {
				return true;
			}
		}
		return false;
	}

	protected boolean notifyR2Moved(float value) {
		r2DeadZone.updateY(value);
		if(r2DeadZone.getY() >= 0.5f && !r2) {
			notifyButtonDown(PS4Button.R2);
			r2 = true;
		} else if(r2DeadZone.getY() < 0.5f && r2) {
			notifyButtonUp(PS4Button.R2);
			r2 = false;
		}
		for(PS4GamePadListener listener : listeners) {
			if(listener.r2Moved(this, value)) {
				return true;
			}
		}
		return false;
	}

	public void addListener(PS4GamePadListener listener) {
		listeners.add(listener);
	}

	public void removeListener(PS4GamePadListener listener) {
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

	public DeadZone getL2DeadZone() {
		return l2DeadZone;
	}

	public void setL2DeadZone(DeadZone l2DeadZone) {
		if(l2DeadZone == null) {
			l2DeadZone = new NoopDeadZone();
		}
		this.l2DeadZone = l2DeadZone;
	}

	public DeadZone getR2DeadZone() {
		return r2DeadZone;
	}

	public void setR2DeadZone(DeadZone r2DeadZone) {
		if(r2DeadZone == null) {
			r2DeadZone = new NoopDeadZone();
		}
		this.r2DeadZone = r2DeadZone;
	}
}
