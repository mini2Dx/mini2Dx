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
package org.mini2Dx.core.input.nswitch;

import org.mini2Dx.core.input.button.SwitchDualJoyConButton;

public class SwitchDualJoyConGamePadAdapter implements SwitchDualJoyConGamePadListener {
	@Override
	public void connected(SwitchDualJoyConGamePad gamePad) {

	}

	@Override
	public void disconnected(SwitchDualJoyConGamePad gamePad) {

	}

	@Override
	public boolean buttonDown(SwitchDualJoyConGamePad gamePad, SwitchDualJoyConButton button) {
		return false;
	}

	@Override
	public boolean buttonUp(SwitchDualJoyConGamePad gamePad, SwitchDualJoyConButton button) {
		return false;
	}

	@Override
	public boolean leftStickXMoved(SwitchDualJoyConGamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean leftStickYMoved(SwitchDualJoyConGamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean rightStickXMoved(SwitchDualJoyConGamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean rightStickYMoved(SwitchDualJoyConGamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean zlMoved(SwitchDualJoyConGamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean zrMoved(SwitchDualJoyConGamePad gamePad, float value) {
		return false;
	}
}
