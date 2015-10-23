/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.controller.xbox360;

import org.mini2Dx.core.controller.Xbox360Controller;
import org.mini2Dx.core.controller.button.Xbox360Button;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

/**
 * Linux bindings for Xbox 360 controller
 */
public class LinuxXbox360Controller extends Xbox360Controller {
	public static final int BUTTON_UP = 0;
	public static final int BUTTON_DOWN = 1;
	public static final int BUTTON_LEFT = 2;
	public static final int BUTTON_RIGHT = 3;
	
	public static final int BUTTON_BACK = 4;
	public static final int BUTTON_START = 5;
	
	public static final int BUTTON_LEFT_STICK = 6;
	public static final int BUTTON_RIGHT_STICK = 7;
	
	public static final int BUTTON_LEFT_SHOULDER = 8;
	public static final int BUTTON_RIGHT_SHOULDER = 9;
	
	public static final int BUTTON_GUIDE = 10;
	
	public static final int BUTTON_A = 11;
	public static final int BUTTON_B = 12;
	public static final int BUTTON_X = 13;
	public static final int BUTTON_Y = 14;
	
	public static final int AXIS_LEFT_TRIGGER = 0;
	public static final int AXIS_RIGHT_TRIGGER = 1;
	
	public static final int AXIS_LEFT_STICK_X = 2;
	public static final int AXIS_LEFT_STICK_Y = 3;
	
	public static final int AXIS_RIGHT_STICK_X = 4;
	public static final int AXIS_RIGHT_STICK_Y = 5;
	
	public LinuxXbox360Controller(Controller controller) {
		super(controller);
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
		case BUTTON_UP:
			return notifyButtonDown(Xbox360Button.UP);
		case BUTTON_DOWN:
			return notifyButtonDown(Xbox360Button.DOWN);
		case BUTTON_LEFT:
			return notifyButtonDown(Xbox360Button.LEFT);
		case BUTTON_RIGHT:
			return notifyButtonDown(Xbox360Button.RIGHT);
		case BUTTON_START:
			return notifyButtonDown(Xbox360Button.START);
		case BUTTON_BACK:
			return notifyButtonDown(Xbox360Button.BACK);
		case BUTTON_LEFT_STICK:
			return notifyButtonDown(Xbox360Button.LEFT_STICK);
		case BUTTON_RIGHT_STICK:
			return notifyButtonDown(Xbox360Button.RIGHT_STICK);
		case BUTTON_LEFT_SHOULDER:
			return notifyButtonDown(Xbox360Button.LEFT_SHOULDER);
		case BUTTON_RIGHT_SHOULDER:
			return notifyButtonDown(Xbox360Button.RIGHT_SHOULDER);
		case BUTTON_GUIDE:
			return notifyButtonDown(Xbox360Button.GUIDE);
		case BUTTON_A:
			return notifyButtonDown(Xbox360Button.A);
		case BUTTON_B:
			return notifyButtonDown(Xbox360Button.B);
		case BUTTON_X:
			return notifyButtonDown(Xbox360Button.X);
		case BUTTON_Y:
			return notifyButtonDown(Xbox360Button.Y);
		}
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		switch(buttonCode) {
		case BUTTON_UP:
			return notifyButtonUp(Xbox360Button.UP);
		case BUTTON_DOWN:
			return notifyButtonUp(Xbox360Button.DOWN);
		case BUTTON_LEFT:
			return notifyButtonUp(Xbox360Button.LEFT);
		case BUTTON_RIGHT:
			return notifyButtonUp(Xbox360Button.RIGHT);
		case BUTTON_START:
			return notifyButtonUp(Xbox360Button.START);
		case BUTTON_BACK:
			return notifyButtonUp(Xbox360Button.BACK);
		case BUTTON_LEFT_STICK:
			return notifyButtonUp(Xbox360Button.LEFT_STICK);
		case BUTTON_RIGHT_STICK:
			return notifyButtonUp(Xbox360Button.RIGHT_STICK);
		case BUTTON_LEFT_SHOULDER:
			return notifyButtonUp(Xbox360Button.LEFT_SHOULDER);
		case BUTTON_RIGHT_SHOULDER:
			return notifyButtonUp(Xbox360Button.RIGHT_SHOULDER);
		case BUTTON_GUIDE:
			return notifyButtonUp(Xbox360Button.GUIDE);
		case BUTTON_A:
			return notifyButtonUp(Xbox360Button.A);
		case BUTTON_B:
			return notifyButtonUp(Xbox360Button.B);
		case BUTTON_X:
			return notifyButtonUp(Xbox360Button.X);
		case BUTTON_Y:
			return notifyButtonUp(Xbox360Button.Y);
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
		case AXIS_LEFT_TRIGGER:
			return notifyLeftTriggerMoved(value);
		case AXIS_RIGHT_TRIGGER:
			return notifyRightTriggerMoved(value);
		}
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
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
