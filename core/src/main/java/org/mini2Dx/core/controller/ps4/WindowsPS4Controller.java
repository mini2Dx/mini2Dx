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
package org.mini2Dx.core.controller.ps4;

import org.mini2Dx.core.controller.PS4Controller;
import org.mini2Dx.core.controller.button.PS4Button;
import org.mini2Dx.core.controller.deadzone.DeadZone;
import org.mini2Dx.core.controller.deadzone.NoopDeadZone;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

/**
 * Windows bindings for PS4 controller
 */
public class WindowsPS4Controller extends PS4Controller {
	public static final String ID = "Wireless Controller".toLowerCase();
	
	public static final int BUTTON_TRIANGLE = 3;
	public static final int BUTTON_SQUARE = 0;
	public static final int BUTTON_CIRCLE = 2;
	public static final int BUTTON_X = 1;
	
	public static final int BUTTON_L1 = 4;
	public static final int BUTTON_L2 = 6;
	public static final int BUTTON_L3 = 10;
	public static final int BUTTON_R1 = 5;
	public static final int BUTTON_R2 = 7;
	public static final int BUTTON_R3 = 11;
	
	public static final int BUTTON_SHARE = 8;
	public static final int BUTTON_OPTIONS = 9;
	public static final int BUTTON_TOUCHPAD= 13;
	public static final int BUTTON_PS = 12;
	
	public static final int AXIS_LEFT_STICK_Y = 2;
	public static final int AXIS_LEFT_STICK_X = 3;
	
	public static final int AXIS_RIGHT_STICK_Y = 0;
	public static final int AXIS_RIGHT_STICK_X = 1;
	
	public static final int AXIS_L2 = 5;
	public static final int AXIS_R2 = 4;
	
	public static final int POV_DIRECTIONS = 0;
	
	private boolean up, down, left, right;
	
	public WindowsPS4Controller(Controller controller) {
		this(controller, new NoopDeadZone(), new NoopDeadZone());
	}

	public WindowsPS4Controller(Controller controller, DeadZone leftStickDeadZone, DeadZone rightStickDeadZone) {
		super(controller, leftStickDeadZone, rightStickDeadZone);
	}

	@Override
	public void connected(Controller controller) {
	}

	@Override
	public void disconnected(Controller controller) {
		notifyDisconnected();
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		switch(buttonCode) {
		case BUTTON_CIRCLE:
			return notifyButtonDown(PS4Button.CIRCLE);
		case BUTTON_TRIANGLE:
			return notifyButtonDown(PS4Button.TRIANGLE);
		case BUTTON_SQUARE:
			return notifyButtonDown(PS4Button.SQUARE);
		case BUTTON_X:
			return notifyButtonDown(PS4Button.X);
		case BUTTON_L1:
			return notifyButtonDown(PS4Button.L1);
		case BUTTON_L2:
			return notifyButtonDown(PS4Button.L2);
		case BUTTON_L3:
			return notifyButtonDown(PS4Button.L3);
		case BUTTON_R1:
			return notifyButtonDown(PS4Button.R1);
		case BUTTON_R2:
			return notifyButtonDown(PS4Button.R2);
		case BUTTON_R3:
			return notifyButtonDown(PS4Button.R3);
		case BUTTON_SHARE:
			return notifyButtonDown(PS4Button.SHARE);
		case BUTTON_OPTIONS:
			return notifyButtonDown(PS4Button.OPTIONS);
		case BUTTON_TOUCHPAD:
			return notifyButtonDown(PS4Button.TOUCHPAD);
		case BUTTON_PS:
			return notifyButtonDown(PS4Button.PS);
		}
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		switch(buttonCode) {
		case BUTTON_CIRCLE:
			return notifyButtonUp(PS4Button.CIRCLE);
		case BUTTON_TRIANGLE:
			return notifyButtonUp(PS4Button.TRIANGLE);
		case BUTTON_SQUARE:
			return notifyButtonUp(PS4Button.SQUARE);
		case BUTTON_X:
			return notifyButtonUp(PS4Button.X);
		case BUTTON_L1:
			return notifyButtonUp(PS4Button.L1);
		case BUTTON_L2:
			return notifyButtonUp(PS4Button.L2);
		case BUTTON_L3:
			return notifyButtonUp(PS4Button.L3);
		case BUTTON_R1:
			return notifyButtonUp(PS4Button.R1);
		case BUTTON_R2:
			return notifyButtonUp(PS4Button.R2);
		case BUTTON_R3:
			return notifyButtonUp(PS4Button.R3);
		case BUTTON_SHARE:
			return notifyButtonUp(PS4Button.SHARE);
		case BUTTON_OPTIONS:
			return notifyButtonUp(PS4Button.OPTIONS);
		case BUTTON_TOUCHPAD:
			return notifyButtonUp(PS4Button.TOUCHPAD);
		case BUTTON_PS:
			return notifyButtonUp(PS4Button.PS);
		}
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		switch(axisCode) {
		case AXIS_LEFT_STICK_X:
			return notifyLeftStickXMoved(value);
		case AXIS_LEFT_STICK_Y:
			return notifyLeftStickYMoved(value);
		case AXIS_RIGHT_STICK_X:
			return notifyRightStickXMoved(value);
		case AXIS_RIGHT_STICK_Y:
			return notifyRightStickYMoved(value);
		case AXIS_L2:
			return notifyL2Moved(value);
		case AXIS_R2:
			return notifyR2Moved(value);
		}
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		switch(povCode) {
		case POV_DIRECTIONS:
			switch(value) {
			case center:
				if(up) {
					notifyButtonUp(PS4Button.UP);
					up = false;
				}
				if(down) {
					notifyButtonUp(PS4Button.DOWN);
					down = false;
				}
				if(left) {
					notifyButtonUp(PS4Button.LEFT);
					left = false;
				}
				if(right) {
					notifyButtonUp(PS4Button.RIGHT);
					right = false;
				}
				break;
			case east:
				if(up) {
					notifyButtonUp(PS4Button.UP);
					up = false;
				}
				if(down) {
					notifyButtonUp(PS4Button.DOWN);
					down = false;
				}
				if(left) {
					notifyButtonUp(PS4Button.LEFT);
					left = false;
				}
				if(!right) {
					right = true;
					notifyButtonDown(PS4Button.RIGHT);
				}
				break;
			case north:
				if(left) {
					notifyButtonUp(PS4Button.LEFT);
					left = false;
				}
				if(right) {
					notifyButtonUp(PS4Button.RIGHT);
					right = false;
				}
				if(down) {
					notifyButtonUp(PS4Button.DOWN);
					down = false;
				}
				if(!up) {
					up = true;
					notifyButtonDown(PS4Button.UP);
				}
				break;
			case northEast:
				if(left) {
					notifyButtonUp(PS4Button.LEFT);
					left = false;
				}
				if(down) {
					notifyButtonUp(PS4Button.DOWN);
					down = false;
				}
				if(!right) {
					notifyButtonDown(PS4Button.RIGHT);
					right = true;
				}
				if(!up) {
					up = true;
					notifyButtonDown(PS4Button.UP);
				}
				break;
			case northWest:
				if(right) {
					notifyButtonUp(PS4Button.RIGHT);
					right = false;
				}
				if(down) {
					notifyButtonUp(PS4Button.DOWN);
					down = false;
				}
				if(!left) {
					notifyButtonDown(PS4Button.LEFT);
					left = true;
				}
				if(!up) {
					up = true;
					notifyButtonDown(PS4Button.UP);
				}
				break;
			case south:
				if(left) {
					notifyButtonUp(PS4Button.LEFT);
					left = false;
				}
				if(right) {
					notifyButtonUp(PS4Button.RIGHT);
					right = false;
				}
				if(up) {
					notifyButtonUp(PS4Button.UP);
					up = false;
				}
				if(!down) {
					notifyButtonDown(PS4Button.DOWN);
					down = true;
				}
				break;
			case southEast:
				if(left) {
					notifyButtonUp(PS4Button.LEFT);
					left = false;
				}
				if(up) {
					notifyButtonUp(PS4Button.UP);
					up = false;
				}
				if(!right) {
					notifyButtonDown(PS4Button.RIGHT);
					right = true;
				}
				if(!down) {
					notifyButtonDown(PS4Button.DOWN);
					down = true;
				}
				break;
			case southWest:
				if(right) {
					notifyButtonUp(PS4Button.RIGHT);
					right = false;
				}
				if(up) {
					notifyButtonUp(PS4Button.UP);
					up = false;
				}
				if(!left) {
					notifyButtonDown(PS4Button.LEFT);
					left = true;
				}
				if(!down) {
					notifyButtonDown(PS4Button.DOWN);
					down = true;
				}
				break;
			case west:
				if(up) {
					notifyButtonUp(PS4Button.UP);
					up = false;
				}
				if(down) {
					notifyButtonUp(PS4Button.DOWN);
					down = false;
				}
				if(right) {
					notifyButtonUp(PS4Button.LEFT);
					right = false;
				}
				if(!left) {
					left = true;
					notifyButtonDown(PS4Button.LEFT);
				}
				break;
			default:
				break;
			}
			break;
		}
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		return false;
	}
	
}
