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

import org.mini2Dx.core.input.button.SwitchJoyConRButton;

public class SwitchJoyConRGamePadAdapter implements SwitchJoyConRGamePadListener {
	@Override
	public void connected(SwitchJoyConRGamePad gamePad) {

	}

	@Override
	public void disconnected(SwitchJoyConRGamePad gamePad) {

	}

	@Override
	public boolean buttonDown(SwitchJoyConRGamePad gamePad, SwitchJoyConRButton button) {
		return false;
	}

	@Override
	public boolean buttonUp(SwitchJoyConRGamePad gamePad, SwitchJoyConRButton button) {
		return false;
	}

	@Override
	public boolean rightStickXMoved(SwitchJoyConRGamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean rightStickYMoved(SwitchJoyConRGamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean zrMoved(SwitchJoyConRGamePad gamePad, float value) {
		return false;
	}
}
