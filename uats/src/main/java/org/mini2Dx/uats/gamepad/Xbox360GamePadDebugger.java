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
package org.mini2Dx.uats.gamepad;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.input.button.Xbox360Button;
import org.mini2Dx.core.input.xbox360.Xbox360GamePad;
import org.mini2Dx.core.input.xbox360.Xbox360GamePadListener;
import org.mini2Dx.gdx.utils.ObjectMap;

/**
 * Shows input from {@link org.mini2Dx.core.input.xbox360.Xbox360GamePad}
 */
public class Xbox360GamePadDebugger implements Xbox360GamePadListener, GamePadDebugger {
	private ObjectMap<Xbox360Button, String> buttonMessages = new ObjectMap<Xbox360Button, String>();
	private float leftStickXAxis, leftStickYAxis;
	private float rightStickXAxis, rightStickYAxis;
	private float leftTrigger, rightTrigger;
	private boolean connected = true;
	
	public Xbox360GamePadDebugger() {
		for(Xbox360Button button : Xbox360Button.values()) {
			buttonMessages.put(button, "UP");
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawString("Xbox 360 Controller", 32f, 24f);
		if(connected) {
			g.drawString("Connected", 32f, 48f);
		} else {
			g.drawString("Disconnected", 32f, 48f);
		}
		g.drawString("Left Stick X: " + leftStickXAxis, 32f, 72f);
		g.drawString("Left Stick Y: " + leftStickYAxis, 32f, 96f);
		g.drawString("Right Stick X: " + rightStickXAxis, 32f, 120f);
		g.drawString("Right Stick Y: " + rightStickYAxis, 32f, 144f);
		g.drawString("Left Trigger: " + leftTrigger, 32f, 168f);
		g.drawString("Right Trigger: " + rightTrigger, 32f, 192f);
		
		float y = 72f;
		for(Xbox360Button button : buttonMessages.keys()) {
			g.drawString(button.name() + ": " + buttonMessages.get(button), 256f, y);
			y += 24f;
		}
	}

	@Override
	public void connected(Xbox360GamePad gamePad) {
		connected = true;
	}

	@Override
	public void disconnected(Xbox360GamePad controller) {
		connected = false;
	}

	@Override
	public boolean buttonDown(Xbox360GamePad controller, Xbox360Button button) {
		buttonMessages.put(button, "DOWN");
		return false;
	}

	@Override
	public boolean buttonUp(Xbox360GamePad controller, Xbox360Button button) {
		buttonMessages.put(button, "UP");
		return false;
	}

	@Override
	public boolean leftTriggerMoved(Xbox360GamePad controller, float value) {
		leftTrigger = value;
		return false;
	}

	@Override
	public boolean rightTriggerMoved(Xbox360GamePad controller, float value) {
		rightTrigger = value;
		return false;
	}

	@Override
	public boolean leftStickXMoved(Xbox360GamePad controller, float value) {
		leftStickXAxis = value;
		return false;
	}

	@Override
	public boolean leftStickYMoved(Xbox360GamePad controller, float value) {
		leftStickYAxis = value;
		return false;
	}

	@Override
	public boolean rightStickXMoved(Xbox360GamePad controller, float value) {
		rightStickXAxis = value;
		return false;
	}

	@Override
	public boolean rightStickYMoved(Xbox360GamePad controller, float value) {
		rightStickYAxis = value;
		return false;
	}

}
