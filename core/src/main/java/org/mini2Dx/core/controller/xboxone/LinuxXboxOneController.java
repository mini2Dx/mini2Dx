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
package org.mini2Dx.core.controller.xboxone;

import org.mini2Dx.core.controller.XboxOneController;
import org.mini2Dx.core.controller.button.XboxOneButton;
import org.mini2Dx.core.controller.deadzone.DeadZone;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

/**
 * Linux bindings for Xbox One controller
 */
public class LinuxXboxOneController extends XboxOneController {
	public static final int BUTTON_UP = 0;
	public static final int BUTTON_DOWN = 1;
	public static final int BUTTON_LEFT = 2;
	public static final int BUTTON_RIGHT = 3;
	
	public static final int BUTTON_MENU = 4;
	public static final int BUTTON_VIEW = 5;
	
	public static final int BUTTON_LEFT_STICK = 6;
	public static final int BUTTON_RIGHT_STICK = 7;
	
	public static final int BUTTON_LEFT_SHOULDER = 8;
	public static final int BUTTON_RIGHT_SHOULDER = 9;
	
	public static final int BUTTON_HOME = 10;
	
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
	
	public LinuxXboxOneController(Controller controller) {
		super(controller);
	}
	
	public LinuxXboxOneController(Controller controller, DeadZone leftStickDeadZone, DeadZone rightStickDeadZone) {
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
		case BUTTON_UP:
			return notifyButtonDown(XboxOneButton.UP);
		case BUTTON_DOWN:
			return notifyButtonDown(XboxOneButton.DOWN);
		case BUTTON_LEFT:
			return notifyButtonDown(XboxOneButton.LEFT);
		case BUTTON_RIGHT:
			return notifyButtonDown(XboxOneButton.RIGHT);
		case BUTTON_MENU:
			return notifyButtonDown(XboxOneButton.MENU);
		case BUTTON_VIEW:
			return notifyButtonDown(XboxOneButton.VIEW);
		case BUTTON_LEFT_STICK:
			return notifyButtonDown(XboxOneButton.LEFT_STICK);
		case BUTTON_RIGHT_STICK:
			return notifyButtonDown(XboxOneButton.RIGHT_STICK);
		case BUTTON_LEFT_SHOULDER:
			return notifyButtonDown(XboxOneButton.LEFT_SHOULDER);
		case BUTTON_RIGHT_SHOULDER:
			return notifyButtonDown(XboxOneButton.RIGHT_SHOULDER);
		case BUTTON_HOME:
			return notifyButtonDown(XboxOneButton.HOME);
		case BUTTON_A:
			return notifyButtonDown(XboxOneButton.A);
		case BUTTON_B:
			return notifyButtonDown(XboxOneButton.B);
		case BUTTON_X:
			return notifyButtonDown(XboxOneButton.X);
		case BUTTON_Y:
			return notifyButtonDown(XboxOneButton.Y);
		}
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		switch(buttonCode) {
		case BUTTON_UP:
			return notifyButtonUp(XboxOneButton.UP);
		case BUTTON_DOWN:
			return notifyButtonUp(XboxOneButton.DOWN);
		case BUTTON_LEFT:
			return notifyButtonUp(XboxOneButton.LEFT);
		case BUTTON_RIGHT:
			return notifyButtonUp(XboxOneButton.RIGHT);
		case BUTTON_MENU:
			return notifyButtonUp(XboxOneButton.MENU);
		case BUTTON_VIEW:
			return notifyButtonUp(XboxOneButton.VIEW);
		case BUTTON_LEFT_STICK:
			return notifyButtonUp(XboxOneButton.LEFT_STICK);
		case BUTTON_RIGHT_STICK:
			return notifyButtonUp(XboxOneButton.RIGHT_STICK);
		case BUTTON_LEFT_SHOULDER:
			return notifyButtonUp(XboxOneButton.LEFT_SHOULDER);
		case BUTTON_RIGHT_SHOULDER:
			return notifyButtonUp(XboxOneButton.RIGHT_SHOULDER);
		case BUTTON_HOME:
			return notifyButtonUp(XboxOneButton.HOME);
		case BUTTON_A:
			return notifyButtonUp(XboxOneButton.A);
		case BUTTON_B:
			return notifyButtonUp(XboxOneButton.B);
		case BUTTON_X:
			return notifyButtonUp(XboxOneButton.X);
		case BUTTON_Y:
			return notifyButtonUp(XboxOneButton.Y);
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
