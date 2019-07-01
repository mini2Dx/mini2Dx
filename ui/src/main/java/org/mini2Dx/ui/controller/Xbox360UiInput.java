/**
 * Copyright (c) 2016 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui.controller;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.input.GamePadType;
import org.mini2Dx.core.input.button.Xbox360Button;
import org.mini2Dx.core.input.xbox360.Xbox360GamePad;
import org.mini2Dx.core.input.xbox360.Xbox360GamePadAdapter;
import org.mini2Dx.gdx.Input.Keys;
import org.mini2Dx.ui.InputSource;
import org.mini2Dx.ui.UiContainer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link Xbox360GamePad} implementation of {@link GamePadUiInput}
 */
public class Xbox360UiInput extends Xbox360GamePadAdapter implements GamePadUiInput<Xbox360Button> {
	private static final String LOGGING_TAG = Xbox360UiInput.class.getSimpleName();
	private static final String ID_PREFIX = Xbox360UiInput.class.getSimpleName() + "-";
	private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
	
	private final String id;
	private final UiContainer uiContainer;
	
	/* User configurable fields */
	private float stickRepeatTimer = 0.25f;
	private float stickThreshold = 0.25f;
	private boolean enabled = true;
	private Xbox360Button actionButton = Xbox360Button.A;
	private boolean debug = false;
	
	/* Internal fields */
	private float leftTimer = stickRepeatTimer;
	private float rightTimer = stickRepeatTimer;
	private float upTimer = stickRepeatTimer;
	private float downTimer = stickRepeatTimer;
	private boolean left, right, up, down;
	private boolean navigateWithDPad;

	public Xbox360UiInput(UiContainer uiContainer) {
		super();
		this.id = ID_PREFIX + ID_GENERATOR.incrementAndGet();
		
		this.uiContainer = uiContainer;
		this.uiContainer.addGamePadInput(this);
	}
	
	private void debugLog(String message) {
		if(!debug) {
			return;
		}
		Mdx.log.info(LOGGING_TAG, message);
		
		if(uiContainer.getActiveNavigation() == null) {
			return;
		}
		Mdx.log.info(LOGGING_TAG, message + " " + uiContainer.getActiveNavigation().getNavigation().getCursor());
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
				debugLog("Navigate left");
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
				debugLog("Navigate right");
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
				debugLog("Navigate up");
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
				debugLog("Navigate down");
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
	public boolean leftStickXMoved(Xbox360GamePad controller, float value) {
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
			uiContainer.setLastGamePadType(GamePadType.XBOX_360);
			return uiContainer.getActiveNavigation() != null;
		}
		return false;
	}
	
	@Override
	public boolean leftStickYMoved(Xbox360GamePad controller, float value) {
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
			uiContainer.setLastGamePadType(GamePadType.XBOX_360);
			return uiContainer.getActiveNavigation() != null;
		}
		return false;
	}
	
	@Override
	public boolean buttonDown(Xbox360GamePad controller, Xbox360Button button) {
		if(!enabled) {
			return false;
		}
		uiContainer.setLastInputSource(InputSource.CONTROLLER);
		uiContainer.setLastGamePadType(GamePadType.XBOX_360);

		if(navigateWithDPad) {
			switch(button) {
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

		boolean result = uiContainer.buttonDown(this, button);
		debugLog(button.name() + " " + result);
		return result;
	}
	
	@Override
	public boolean buttonUp(Xbox360GamePad controller, Xbox360Button button) {
		if(!enabled) {
			return false;
		}
		uiContainer.setLastInputSource(InputSource.CONTROLLER);
		uiContainer.setLastGamePadType(GamePadType.XBOX_360);

		if(navigateWithDPad) {
			switch(button) {
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

		boolean result = uiContainer.buttonUp(this, button);
		debugLog(button.name() + " " + result);
		return result;
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

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	@Override
	public Xbox360Button getActionButton() {
		return actionButton;
	}

	@Override
	public void setActionButton(Xbox360Button actionButton) {
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
		Xbox360UiInput other = (Xbox360UiInput) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
