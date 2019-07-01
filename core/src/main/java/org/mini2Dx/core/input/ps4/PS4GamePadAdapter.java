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
package org.mini2Dx.core.input.ps4;

import org.mini2Dx.core.input.button.PS4Button;

public class PS4GamePadAdapter implements PS4GamePadListener {
	@Override
	public void connected(PS4GamePad gamePad) {

	}

	@Override
	public void disconnected(PS4GamePad gamePad) {

	}

	@Override
	public boolean buttonDown(PS4GamePad gamePad, PS4Button button) {
		return false;
	}

	@Override
	public boolean buttonUp(PS4GamePad gamePad, PS4Button button) {
		return false;
	}

	@Override
	public boolean leftStickXMoved(PS4GamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean leftStickYMoved(PS4GamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean rightStickXMoved(PS4GamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean rightStickYMoved(PS4GamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean l2Moved(PS4GamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean r2Moved(PS4GamePad gamePad, float value) {
		return false;
	}
}
