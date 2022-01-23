/*******************************************************************************
 * Copyright 2022 See AUTHORS file
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
package org.mini2Dx.ui.gamepad;

import org.mini2Dx.core.input.GamePadType;
import org.mini2Dx.core.input.button.SwitchJoyConLButton;
import org.mini2Dx.core.input.nswitch.SwitchJoyConLGamePad;
import org.mini2Dx.core.input.nswitch.SwitchJoyConLGamePadAdapter;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.ui.InputSource;
import org.mini2Dx.ui.UiContainer;

import java.util.concurrent.atomic.AtomicInteger;

public class SwitchJoyConLUiInput extends SwitchJoyConLGamePadAdapter implements GamePadUiInput<SwitchJoyConLButton> {
	private static final String ID_PREFIX = SwitchJoyConLUiInput.class.getSimpleName() + "-";
	private static final AtomicInteger ID_GENERATOR = new AtomicInteger();

	private final String id;
	private final UiContainer uiContainer;

	/* User configurable fields */
	private float stickRepeatTimer = 0.25f;
	private float stickThreshold = 0.35f;
	private boolean enabled = true;
	private SwitchJoyConLButton actionButton = SwitchJoyConLButton.DOWN;

	/* Internal fields */
	private float leftTimer = stickRepeatTimer;
	private float rightTimer = stickRepeatTimer;
	private float upTimer = stickRepeatTimer;
	private float downTimer = stickRepeatTimer;
	private boolean left, right, up, down;

	public SwitchJoyConLUiInput(UiContainer uiContainer) {
		super();
		this.id = ID_PREFIX + ID_GENERATOR.incrementAndGet();

		this.uiContainer = uiContainer;
		this.uiContainer.addGamePadInput(this);
	}

	@Override
	public void update(float delta) {
		if(!enabled) {
			return;
		}

		if(left) {
			if(leftTimer == stickRepeatTimer) {
				uiContainer.keyDownNoInputChange(Input.Keys.LEFT);
				uiContainer.keyUp(Input.Keys.LEFT);
			}
			leftTimer -= delta;
			if(leftTimer <= 0f) {
				leftTimer = stickRepeatTimer;
			}
		} else {
			leftTimer = stickRepeatTimer;
		}

		if(right) {
			if(rightTimer == stickRepeatTimer) {
				uiContainer.keyDownNoInputChange(Input.Keys.RIGHT);
				uiContainer.keyUp(Input.Keys.RIGHT);
			}
			rightTimer -= delta;
			if(rightTimer <= 0f) {
				rightTimer = stickRepeatTimer;
			}
		} else {
			rightTimer = stickRepeatTimer;
		}

		if(up) {
			if(upTimer == stickRepeatTimer) {
				uiContainer.keyDownNoInputChange(Input.Keys.UP);
				uiContainer.keyUp(Input.Keys.UP);
			}
			upTimer -= delta;
			if(upTimer <= 0f) {
				upTimer = stickRepeatTimer;
			}
		} else {
			upTimer = stickRepeatTimer;
		}

		if(down) {
			if(downTimer == stickRepeatTimer) {
				uiContainer.keyDownNoInputChange(Input.Keys.DOWN);
				uiContainer.keyUp(Input.Keys.DOWN);
			}
			downTimer -= delta;
			if(downTimer <= 0f) {
				downTimer = stickRepeatTimer;
			}
		} else {
			downTimer = stickRepeatTimer;
		}
	}

	@Override
	public boolean leftStickXMoved(SwitchJoyConLGamePad controller, float value) {
		if(!enabled) {
			return false;
		}
		if(value < -stickThreshold) {
			uiContainer.setLastInputSource(InputSource.CONTROLLER);
			uiContainer.setLastGamePadType(GamePadType.SWITCH_JOYCON_L);
			left = true;
			right = false;
		} else if(value > stickThreshold) {
			uiContainer.setLastInputSource(InputSource.CONTROLLER);
			uiContainer.setLastGamePadType(GamePadType.SWITCH_JOYCON_L);
			right = true;
			left = false;
		} else {
			left = false;
			right = false;
		}
		return uiContainer.getActiveNavigation() != null;
	}

	@Override
	public boolean leftStickYMoved(SwitchJoyConLGamePad controller, float value) {
		if(!enabled) {
			return false;
		}
		if(value < -stickThreshold) {
			uiContainer.setLastInputSource(InputSource.CONTROLLER);
			uiContainer.setLastGamePadType(GamePadType.SWITCH_JOYCON_L);
			up = true;
			down = false;
		} else if(value > stickThreshold) {
			uiContainer.setLastInputSource(InputSource.CONTROLLER);
			uiContainer.setLastGamePadType(GamePadType.SWITCH_JOYCON_L);
			down = true;
			up = false;
		} else {
			down = false;
			up = false;
		}
		return uiContainer.getActiveNavigation() != null;
	}

	@Override
	public boolean buttonDown(SwitchJoyConLGamePad controller, SwitchJoyConLButton button) {
		if(!enabled) {
			return false;
		}
		uiContainer.setLastInputSource(InputSource.CONTROLLER);
		uiContainer.setLastGamePadType(GamePadType.SWITCH_JOYCON_L);
		return uiContainer.buttonDown(this, button);
	}

	@Override
	public boolean buttonUp(SwitchJoyConLGamePad controller, SwitchJoyConLButton button) {
		if(!enabled) {
			return false;
		}
		uiContainer.setLastInputSource(InputSource.CONTROLLER);
		uiContainer.setLastGamePadType(GamePadType.SWITCH_JOYCON_L);
		return uiContainer.buttonUp(this, button);
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void enable() {
		enabled = true;
	}

	@Override
	public void disable() {
		enabled = false;
	}

	/**
	 * Sets the amount of time in seconds before the left stick is considered repeating a navigation direction
	 * @param stickRepeatTimer Default value is 0.25 seconds
	 */
	public void setStickRepeatTimer(float stickRepeatTimer) {
		this.stickRepeatTimer = Math.abs(stickRepeatTimer);
	}

	/**
	 * Sets the threshold for stick values before the input is considered as a navigation direction press
	 * @param stickThreshold Default value is 0.25
	 */
	public void setStickThreshold(float stickThreshold) {
		this.stickThreshold = Math.abs(stickThreshold);
	}

	@Override
	public SwitchJoyConLButton getActionButton() {
		return actionButton;
	}

	@Override
	public void setActionButton(SwitchJoyConLButton actionButton) {
		if(actionButton == null) {
			return;
		}
		this.actionButton = actionButton;
	}

	@Override
	public void dispose() {
		uiContainer.removeGamePadInput(this);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SwitchJoyConLUiInput other = (SwitchJoyConLUiInput) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

