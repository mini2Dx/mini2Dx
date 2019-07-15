/*******************************************************************************
 * Copyright 2019 Viridian Software Limited
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
package org.mini2Dx.core.input.xbox;

import org.mini2Dx.core.input.button.XboxButton;

public class XboxGamePadAdapter implements XboxGamePadListener {
	@Override
	public void connected(XboxGamePad gamePad) {
		
	}

	@Override
	public void disconnected(XboxGamePad gamePad) {

	}

	@Override
	public boolean buttonDown(XboxGamePad gamePad, XboxButton button) {
		return false;
	}

	@Override
	public boolean buttonUp(XboxGamePad gamePad, XboxButton button) {
		return false;
	}

	@Override
	public boolean leftTriggerMoved(XboxGamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean rightTriggerMoved(XboxGamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean leftStickXMoved(XboxGamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean leftStickYMoved(XboxGamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean rightStickXMoved(XboxGamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean rightStickYMoved(XboxGamePad gamePad, float value) {
		return false;
	}
}
