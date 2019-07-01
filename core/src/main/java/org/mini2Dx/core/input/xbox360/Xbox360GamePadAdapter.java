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
package org.mini2Dx.core.input.xbox360;

import org.mini2Dx.core.input.button.Xbox360Button;

public class Xbox360GamePadAdapter implements Xbox360GamePadListener {
	@Override
	public void connected(Xbox360GamePad gamePad) {
		
	}

	@Override
	public void disconnected(Xbox360GamePad gamePad) {

	}

	@Override
	public boolean buttonDown(Xbox360GamePad gamePad, Xbox360Button button) {
		return false;
	}

	@Override
	public boolean buttonUp(Xbox360GamePad gamePad, Xbox360Button button) {
		return false;
	}

	@Override
	public boolean leftTriggerMoved(Xbox360GamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean rightTriggerMoved(Xbox360GamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean leftStickXMoved(Xbox360GamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean leftStickYMoved(Xbox360GamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean rightStickXMoved(Xbox360GamePad gamePad, float value) {
		return false;
	}

	@Override
	public boolean rightStickYMoved(Xbox360GamePad gamePad, float value) {
		return false;
	}
}
