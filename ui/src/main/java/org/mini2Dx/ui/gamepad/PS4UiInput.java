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
package org.mini2Dx.ui.gamepad;

import org.mini2Dx.core.input.GamePadType;
import org.mini2Dx.core.input.button.PS4Button;
import org.mini2Dx.core.input.ps4.PS4GamePad;
import org.mini2Dx.core.input.ps4.PS4GamePadAdapter;
import org.mini2Dx.gdx.Input.Keys;
import org.mini2Dx.ui.InputSource;
import org.mini2Dx.ui.UiContainer;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * {@link PS4GamePad} implementation of {@link GamePadUiInput}
 */
public class PS4UiInput extends PS4GamePadAdapter implements GamePadUiInput<PS4Button> {
	private static final String ID_PREFIX = PS4UiInput.class.getSimpleName() + "-";
	private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
	
	private final String id;
	private final UiContainer uiContainer;
	
	/* User configurable fields */
	private float stickRepeatTimer = 0.25f;
	private float stickThreshold = 0.25f;
	private boolean enabled = true;
	private PS4Button actionButton = PS4Button.CROSS;
	
	/* Internal fields */
	private float leftTimer = stickRepeatTimer;
	private float rightTimer = stickRepeatTimer;
	private float upTimer = stickRepeatTimer;
	private float downTimer = stickRepeatTimer;
	private boolean left, right, up, down;
	private boolean navigateWithDPad;
	
	public PS4UiInput(UiContainer uiContainer) {
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
				uiContainer.keyDown(Keys.LEFT);
				uiContainer.keyUp(Keys.LEFT);
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
				uiContainer.keyDown(Keys.RIGHT);
				uiContainer.keyUp(Keys.RIGHT);
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
				uiContainer.keyDown(Keys.UP);
				uiContainer.keyUp(Keys.UP);
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
				uiContainer.keyDown(Keys.DOWN);
				uiContainer.keyUp(Keys.DOWN);
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
	public boolean leftStickXMoved(PS4GamePad controller, float value) {
		if(value < -stickThreshold) {
			left = true;
			right = false;
		} else if(value > stickThreshold) {
			right = true;
			left = false;
		} else {
			left = false;
			right = false;
		}
		if(enabled) {
			uiContainer.setLastInputSource(InputSource.CONTROLLER);
			uiContainer.setLastGamePadType(GamePadType.PS4);
			return uiContainer.getActiveNavigation() != null;
		}
		return false;
	}
	
	@Override
	public boolean leftStickYMoved(PS4GamePad controller, float value) {
		if(value < -stickThreshold) {
			up = true;
			down = false;
		} else if(value > stickThreshold) {
			down = true;
			up = false;
		} else {
			down = false;
			up = false;
		}
		if(enabled) {
			uiContainer.setLastInputSource(InputSource.CONTROLLER);
			uiContainer.setLastGamePadType(GamePadType.PS4);
			return uiContainer.getActiveNavigation() != null;
		}
		return false;
	}
	
	@Override
	public boolean buttonDown(PS4GamePad controller, PS4Button button) {
		if(!enabled) {
			return false;
		}
		uiContainer.setLastInputSource(InputSource.CONTROLLER);
		uiContainer.setLastGamePadType(GamePadType.PS4);

		if(navigateWithDPad) {
			switch (button) {
			case UP:
				return uiContainer.keyDown(Keys.UP);
			case DOWN:
				return uiContainer.keyDown(Keys.DOWN);
			case LEFT:
				return uiContainer.keyDown(Keys.LEFT);
			case RIGHT:
				return uiContainer.keyDown(Keys.RIGHT);
			}
		}

		return uiContainer.buttonDown(this, button);
	}
	
	@Override
	public boolean buttonUp(PS4GamePad controller, PS4Button button) {
		if(!enabled) {
			return false;
		}
		uiContainer.setLastInputSource(InputSource.CONTROLLER);
		uiContainer.setLastGamePadType(GamePadType.PS4);

		if(navigateWithDPad) {
			switch (button) {
			case UP:
				return uiContainer.keyUp(Keys.UP);
			case DOWN:
				return uiContainer.keyUp(Keys.DOWN);
			case LEFT:
				return uiContainer.keyUp(Keys.LEFT);
			case RIGHT:
				return uiContainer.keyUp(Keys.RIGHT);
			}
		}

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

	/**
	 * Returns if {@link org.mini2Dx.ui.navigation.UiNavigation} events should be triggered by the D-PAD
	 * @return False by default
	 */
	public boolean isNavigateWithDPad() {
		return navigateWithDPad;
	}

	/**
	 * Sets if {@link org.mini2Dx.ui.navigation.UiNavigation} events should be triggered by the D-PAD
	 * @param navigateWithDPad True if events should be triggered
	 */
	public void setNavigateWithDPad(boolean navigateWithDPad) {
		this.navigateWithDPad = navigateWithDPad;
	}

	@Override
	public PS4Button getActionButton() {
		return actionButton;
	}

	@Override
	public void setActionButton(PS4Button actionButton) {
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
		PS4UiInput other = (PS4UiInput) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
