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
package org.mini2Dx.libgdx.input;

import org.mini2Dx.core.input.GamePad;
import org.mini2Dx.core.input.PovState;
import org.mini2Dx.core.input.button.XboxButton;
import org.mini2Dx.core.input.deadzone.DeadZone;
import org.mini2Dx.core.input.xbox.XboxGamePad;
import org.mini2Dx.gdx.math.Vector3;
import org.mini2Dx.gdx.utils.IntMap;

public class LibgdxXboxGamePad extends XboxGamePad {

	public LibgdxXboxGamePad(GamePad gamePad) {
		super(gamePad);
	}

	public LibgdxXboxGamePad(GamePad gamePad, DeadZone leftStickDeadZone, DeadZone rightStickDeadZone) {
		super(gamePad, leftStickDeadZone, rightStickDeadZone);
	}

	@Override
	public void onConnect(GamePad gamePad) {
		notifyConnected();
	}

	@Override
	public void onDisconnect(GamePad gamePad) {
		notifyDisconnected();
	}

	@Override
	public void onButtonDown(GamePad gamePad, int buttonCode) {
		if(!BUTTON_MAPPINGS.containsKey(buttonCode)) {
			return;
		}
		notifyButtonDown(BUTTON_MAPPINGS.get(buttonCode));
	}

	@Override
	public void onButtonUp(GamePad gamePad, int buttonCode) {
		if(!BUTTON_MAPPINGS.containsKey(buttonCode)) {
			return;
		}
		notifyButtonUp(BUTTON_MAPPINGS.get(buttonCode));
	}

	@Override
	public void onPovChanged(GamePad gamePad, int povCode, PovState povState) {
	}

	@Override
	public void onAxisChanged(GamePad gamePad, int axisCode, float axisValue) {
		switch(axisCode) {
		case LEFT_STICK_X:
			notifyLeftStickXMoved(axisValue);
			break;
		case LEFT_STICK_Y:
			notifyLeftStickYMoved(axisValue);
			break;
		case RIGHT_STICK_X:
			notifyRightStickXMoved(axisValue);
			break;
		case RIGHT_STICK_Y:
			notifyRightStickYMoved(axisValue);
			break;
		case LEFT_TRIGGER:
			notifyLeftTriggerMoved(axisValue);
			break;
		case RIGHT_TRIGGER:
			notifyRightTriggerMoved(axisValue);
			break;
		}
	}

	@Override
	public void onAccelerometerChanged(GamePad gamePad, int accelerometerCode, Vector3 value) {
	}

	private static final int LEFT_STICK_X = 0;
	private static final int LEFT_STICK_Y = 1;

	private static final int RIGHT_STICK_X = 2;
	private static final int RIGHT_STICK_Y = 3;

	private static final int LEFT_TRIGGER = 4;
	private static final int RIGHT_TRIGGER = 5;

	private static final IntMap<XboxButton> BUTTON_MAPPINGS = new IntMap<XboxButton>() {
		{
			put(0, XboxButton.A);
			put(1, XboxButton.B);
			put(2, XboxButton.X);
			put(3, XboxButton.Y);

			put(4, XboxButton.BACK);
			put(5, XboxButton.GUIDE);
			put(6, XboxButton.START);

			put(7, XboxButton.LEFT_STICK);
			put(8, XboxButton.RIGHT_STICK);

			put(9, XboxButton.LEFT_SHOULDER);
			put(10, XboxButton.RIGHT_SHOULDER);

			put(11, XboxButton.UP);
			put(12, XboxButton.DOWN);
			put(13, XboxButton.LEFT);
			put(14, XboxButton.RIGHT);
		}
	};
}
