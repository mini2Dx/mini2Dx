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
package org.mini2Dx.libgdx;

import com.badlogic.gdx.Gdx;
import org.mini2Dx.core.Input;
import org.mini2Dx.core.input.GamePad;
import org.mini2Dx.core.input.nswitch.SwitchDualJoyConGamePad;
import org.mini2Dx.core.input.nswitch.SwitchJoyConLGamePad;
import org.mini2Dx.core.input.nswitch.SwitchJoyConRGamePad;
import org.mini2Dx.core.input.ps4.PS4GamePad;
import org.mini2Dx.core.input.xbox360.Xbox360GamePad;
import org.mini2Dx.core.input.xboxOne.XboxOneGamePad;
import org.mini2Dx.gdx.InputProcessor;
import org.mini2Dx.gdx.utils.Array;

public class LibgdxInput implements Input {
	@Override
	public void setInputProcessor(InputProcessor inputProcessor) {

	}

	@Override
	public void setOnScreenKeyboardVisible(boolean visible) {
		Gdx.input.setOnscreenKeyboardVisible(visible);
	}

	@Override
	public Array<GamePad> getGamePads() {
		return null;
	}

	@Override
	public PS4GamePad newPS4GamePad(GamePad gamePad) {
		return null;
	}

	@Override
	public SwitchDualJoyConGamePad newSwitchDualJoyConGamePad(GamePad gamePad) {
		return null;
	}

	@Override
	public SwitchJoyConLGamePad newSwitchJoyConLGamePad(GamePad gamePad) {
		return null;
	}

	@Override
	public SwitchJoyConRGamePad newSwitchJoyConRGamePad(GamePad gamePad) {
		return null;
	}

	@Override
	public XboxOneGamePad newXboxOneGamePad(GamePad gamePad) {
		return null;
	}

	@Override
	public Xbox360GamePad newXbox360GamePad(GamePad gamePad) {
		return null;
	}

	@Override
	public int getX() {
		return Gdx.input.getX();
	}

	@Override
	public int getY() {
		return Gdx.input.getY();
	}
}
