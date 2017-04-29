/**
 * Copyright (c) 2017 See AUTHORS file
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

import java.util.concurrent.atomic.AtomicInteger;

import org.mini2Dx.core.controller.ControllerType;
import org.mini2Dx.core.controller.PS4Controller;
import org.mini2Dx.core.controller.button.PS4Button;
import org.mini2Dx.core.controller.ps4.PS4ControllerAdapter;
import org.mini2Dx.ui.InputSource;
import org.mini2Dx.ui.UiContainer;

import com.badlogic.gdx.Input.Keys;

/**
 * {@link PS4Controller} implementation of {@link ControllerUiInput}
 */
public class PS4UiInput extends PS4ControllerAdapter implements ControllerUiInput<PS4Button> {
	private static final String ID_PREFIX = PS4UiInput.class.getSimpleName() + "-";
	private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
	
	private final String id;
	private final UiContainer uiContainer;
	
	/* User configurable fields */
	private float buttonRepeatTimer = 0.25f;
	private boolean enabled = true;
	private PS4Button actionButton = PS4Button.X;
	
	/* Internal fields */
	private float leftTimer = buttonRepeatTimer;
	private float rightTimer = buttonRepeatTimer;
	private float upTimer = buttonRepeatTimer;
	private float downTimer = buttonRepeatTimer;
	private boolean left, right, up, down;
	
	public PS4UiInput(UiContainer uiContainer) {
		super();
		this.id = ID_PREFIX + ID_GENERATOR.incrementAndGet();
		
		this.uiContainer = uiContainer;
		this.uiContainer.addControllerInput(this);
	}
	
	@Override
	public void update(float delta) {
		if(!enabled) {
			return;
		}
		
		if(left) {
			if(leftTimer == buttonRepeatTimer) {
				uiContainer.keyDown(Keys.LEFT);
				uiContainer.keyUp(Keys.LEFT);
			}
			leftTimer -= delta;
			if(leftTimer <= 0f) {
				leftTimer = buttonRepeatTimer;
			}
		} else {
			leftTimer = buttonRepeatTimer;
		}
		
		if(right) {
			if(rightTimer == buttonRepeatTimer) {
				uiContainer.keyDown(Keys.RIGHT);
				uiContainer.keyUp(Keys.RIGHT);
			}
			rightTimer -= delta;
			if(rightTimer <= 0f) {
				rightTimer = buttonRepeatTimer;
			}
		} else {
			rightTimer = buttonRepeatTimer;
		}
		
		if(up) {
			if(upTimer == buttonRepeatTimer) {
				uiContainer.keyDown(Keys.UP);
				uiContainer.keyUp(Keys.UP);
			}
			upTimer -= delta;
			if(upTimer <= 0f) {
				upTimer = buttonRepeatTimer;
			}
		} else {
			upTimer = buttonRepeatTimer;
		}
		
		if(down) {
			if(downTimer == buttonRepeatTimer) {
				uiContainer.keyDown(Keys.DOWN);
				uiContainer.keyUp(Keys.DOWN);
			}
			downTimer -= delta;
			if(downTimer <= 0f) {
				downTimer = buttonRepeatTimer;
			}
		} else {
			downTimer = buttonRepeatTimer;
		}
	}
	
	@Override
	public boolean leftStickXMoved(PS4Controller controller, float value) {
		if(value < 0f) {
			left = true;
			right = false;
		} else if(value > 0f) {
			right = true;
			left = false;
		} else {
			left = false;
			right = false;
		}
		if(enabled) {
			uiContainer.setLastInputSource(InputSource.CONTROLLER);
			uiContainer.setLastControllerType(ControllerType.PS4);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean leftStickYMoved(PS4Controller controller, float value) {
		if(value < 0f) {
			up = true;
			down = false;
		} else if(value > 0f) {
			down = true;
			up = false;
		} else {
			down = false;
			up = false;
		}
		if(enabled) {
			uiContainer.setLastInputSource(InputSource.CONTROLLER);
			uiContainer.setLastControllerType(ControllerType.PS4);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean buttonDown(PS4Controller controller, PS4Button button) {
		if(!enabled) {
			return false;
		}
		uiContainer.buttonDown(this, button);
		uiContainer.setLastInputSource(InputSource.CONTROLLER);
		uiContainer.setLastControllerType(ControllerType.PS4);
		return true;
	}
	
	@Override
	public boolean buttonUp(PS4Controller controller, PS4Button button) {
		if(!enabled) {
			return false;
		}
		uiContainer.buttonUp(this, button);
		uiContainer.setLastInputSource(InputSource.CONTROLLER);
		uiContainer.setLastControllerType(ControllerType.PS4);
		return true;
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

	public void setButtonRepeatTimer(float buttonRepeatTimer) {
		this.buttonRepeatTimer = buttonRepeatTimer;
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
		uiContainer.removeControllerInput(this);
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
