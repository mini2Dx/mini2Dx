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
import com.badlogic.gdx.controllers.AdvancedController;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import org.mini2Dx.core.Input;
import org.mini2Dx.core.input.GamePad;
import org.mini2Dx.core.input.nswitch.SwitchDualJoyConGamePad;
import org.mini2Dx.core.input.nswitch.SwitchJoyConLGamePad;
import org.mini2Dx.core.input.nswitch.SwitchJoyConRGamePad;
import org.mini2Dx.core.input.ps4.PS4GamePad;
import org.mini2Dx.core.input.xbox.XboxGamePad;
import org.mini2Dx.gdx.InputProcessor;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.libgdx.input.*;

public class LibgdxInput implements Input {
	private final Array<GamePad> gamePads = new Array<GamePad>();
	private final ObjectMap<String, LibgdxGamePad> gamePadsById = new ObjectMap<String, LibgdxGamePad>();

	private LibgdxInputProcessor gdxInputProcessor = null;

	public void updateGamePads() {
		final boolean firstRun = gamePads.size == 0;

		for(int i = 0; i < Controllers.getControllers().size; i++) {
			final Controller controller = Controllers.getControllers().get(i);

			final LibgdxGamePad gamePad;

			if(firstRun) {
				if(controller instanceof AdvancedController) {
					final AdvancedController advancedController = (AdvancedController) controller;
					gamePad = new LibgdxAdvancedGamePad(advancedController);
				} else {
					gamePad = new LibgdxGamePad(controller);
				}
			} else {
				if(controller instanceof AdvancedController) {
					final AdvancedController advancedController = (AdvancedController) controller;
					final String instanceId = advancedController.getUniqueId() != null ? advancedController.getUniqueId() : advancedController.getName();

					if(gamePadsById.containsKey(instanceId)) {
						gamePad = gamePadsById.get(instanceId);
					} else {
						gamePad = new LibgdxGamePad(controller);
					}
				} else {
					if(gamePadsById.containsKey(controller.getName())) {
						gamePad = gamePadsById.get(controller.getName());
					} else {
						gamePad = new LibgdxGamePad(controller);
					}
				}
			}

			if(!gamePadsById.containsKey(gamePad.getInstanceId())) {
				gamePads.add(gamePad);
				gamePadsById.put(gamePad.getInstanceId(), gamePad);
				gamePad.init();
			}
		}
	}

	@Override
	public void setInputProcessor(InputProcessor inputProcessor) {
		if(gdxInputProcessor == null) {
			gdxInputProcessor = new LibgdxInputProcessor(inputProcessor);
			Gdx.input.setInputProcessor(gdxInputProcessor);
		} else {
			gdxInputProcessor.setInputProcessor(inputProcessor);
		}
	}

	@Override
	public void setOnScreenKeyboardVisible(boolean visible) {
		Gdx.input.setOnscreenKeyboardVisible(visible);
	}

	@Override
	public Array<GamePad> getGamePads() {
		if(gamePads.size == 0) {
			updateGamePads();
		}
		return gamePads;
	}

	@Override
	public PS4GamePad newPS4GamePad(GamePad gamePad) {
		return new LibgdxPS4GamePad(gamePad);
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
	public XboxGamePad newXboxGamePad(GamePad gamePad) {
		return new LibgdxXboxGamePad(gamePad);
	}

	@Override
	public int getX() {
		return Gdx.input.getX();
	}

	@Override
	public int getY() {
		return Gdx.input.getY();
	}

	@Override
	public boolean isKeyJustPressed(int key) {
		return Gdx.input.isKeyJustPressed(key);
	}

	@Override
	public boolean justTouched() {
		return Gdx.input.justTouched();
	}
}
