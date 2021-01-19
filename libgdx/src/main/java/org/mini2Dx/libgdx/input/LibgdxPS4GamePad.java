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
import org.mini2Dx.core.input.button.PS4Button;
import org.mini2Dx.core.input.deadzone.DeadZone;
import org.mini2Dx.core.input.ps4.PS4GamePad;
import org.mini2Dx.gdx.math.Vector3;
import org.mini2Dx.gdx.utils.IntMap;
import org.mini2Dx.gdx.utils.ObjectIntMap;

public class LibgdxPS4GamePad extends PS4GamePad {

	public LibgdxPS4GamePad(GamePad gamePad) {
		super(gamePad);
	}

	public LibgdxPS4GamePad(GamePad gamePad, DeadZone leftStickDeadZone, DeadZone rightStickDeadZone) {
		super(gamePad, leftStickDeadZone, rightStickDeadZone);
	}

	@Override
	public boolean isButtonDown(PS4Button button) {
		return gamePad.isButtonDown(BUTTON_CODES.get(button, -1));
	}

	@Override
	public boolean isButtonUp(PS4Button button) {
		return gamePad.isButtonUp(BUTTON_CODES.get(button, -1));
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
		case L2:
			notifyL2Moved(axisValue);
			break;
		case R2:
			notifyR2Moved(axisValue);
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

	private static final int L2 = 4;
	private static final int R2 = 5;

	private static final IntMap<PS4Button> BUTTON_MAPPINGS = new IntMap<PS4Button>() {
		{
			put(0, PS4Button.CROSS);
			put(1, PS4Button.CIRCLE);
			put(2, PS4Button.SQUARE);
			put(3, PS4Button.TRIANGLE);

			put(4, PS4Button.SHARE);
			put(5, PS4Button.PS);
			put(6, PS4Button.TOUCHPAD);

			put(7, PS4Button.L3);
			put(8, PS4Button.R3);

			put(9, PS4Button.L1);
			put(10, PS4Button.R1);

			put(11, PS4Button.UP);
			put(12, PS4Button.DOWN);
			put(13, PS4Button.LEFT);
			put(14, PS4Button.RIGHT);
		}
	};
	private static final ObjectIntMap<PS4Button> BUTTON_CODES = new ObjectIntMap<PS4Button>() {
		{
			final IntMap.Keys keys = BUTTON_MAPPINGS.keys();
			while(keys.hasNext) {
				final int code = keys.next();
				put(BUTTON_MAPPINGS.get(code), code);
			}
		}
	};
}
