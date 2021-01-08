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
import org.mini2Dx.core.input.GamePad;
import org.mini2Dx.core.input.button.PS4Button;
import org.mini2Dx.core.input.ps4.PS4GamePad;
import org.mini2Dx.core.input.ps4.PS4GamePadListener;
import org.mini2Dx.gdx.utils.ObjectMap;

/**
 * Shows input from {@link org.mini2Dx.core.input.ps4.PS4GamePad}
 */
public class PS4GamePadDebugger implements PS4GamePadListener, GamePadDebugger {
	private ObjectMap<PS4Button, String> buttonMessages = new ObjectMap<PS4Button, String>();
	private float leftStickXAxis, leftStickYAxis;
	private float rightStickXAxis, rightStickYAxis;
	private float l2, r2;
	private boolean connected = true;
	
	@Override
	public void render(Graphics g) {
		g.drawString("PS4 Controller", 32f, 24f);
		if(connected) {
			g.drawString("Connected", 32f, 48f);
		} else {
			g.drawString("Disconnected", 32f, 48f);
		}
		g.drawString("Left Stick X: " + leftStickXAxis, 32f, 72f);
		g.drawString("Left Stick Y: " + leftStickYAxis, 32f, 96f);
		g.drawString("Right Stick X: " + rightStickXAxis, 32f, 120f);
		g.drawString("Right Stick Y: " + rightStickYAxis, 32f, 144f);
		g.drawString("L2: " + l2, 32f, 168f);
		g.drawString("R2: " + r2, 32f, 192f);
		
		float y = 72f;
		for(PS4Button button : buttonMessages.keys()) {
			g.drawString(button.name() + ": " + buttonMessages.get(button), 256f, y);
			y += 24f;
		}
	}

	@Override
	public void connected(PS4GamePad gamePad) {
		gamePad.addListener(this);
		connected = true;
	}

	@Override
	public void disconnected(PS4GamePad gamePad) {
		gamePad.removeListener(this);
		connected = false;
	}
	
	@Override
	public boolean buttonDown(PS4GamePad controller, PS4Button button) {
		buttonMessages.put(button, "DOWN");
		return false;
	}
	
	@Override
	public boolean buttonUp(PS4GamePad controller, PS4Button button) {
		buttonMessages.put(button, "UP");

		switch(button) {
		case CROSS:
			if(controller.isVibrating()) {
				controller.stopVibration();
			} else {
				controller.startVibration(0.5f);
			}
			break;
		}
		return false;
	}
	
	@Override
	public boolean leftStickXMoved(PS4GamePad controller, float value) {
		leftStickXAxis = value;
		return false;
	}

	@Override
	public boolean leftStickYMoved(PS4GamePad controller, float value) {
		leftStickYAxis = value;
		return false;
	}

	@Override
	public boolean rightStickXMoved(PS4GamePad controller, float value) {
		rightStickXAxis = value;
		return false;
	}

	@Override
	public boolean rightStickYMoved(PS4GamePad controller, float value) {
		rightStickYAxis = value;
		return false;
	}
	
	@Override
	public boolean l2Moved(PS4GamePad controller, float value) {
		l2 = value;
		return false;
	}
	
	@Override
	public boolean r2Moved(PS4GamePad controller, float value) {
		r2 = value;
		return false;
	}
}
