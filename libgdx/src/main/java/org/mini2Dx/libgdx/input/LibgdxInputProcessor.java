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

import org.mini2Dx.gdx.InputProcessor;

public class LibgdxInputProcessor implements com.badlogic.gdx.InputProcessor {
	private InputProcessor inputProcessor;

	public LibgdxInputProcessor(InputProcessor inputProcessor) {
		this.inputProcessor = inputProcessor;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(inputProcessor == null) {
			return false;
		}
		return inputProcessor.keyDown(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
		if(inputProcessor == null) {
			return false;
		}
		return inputProcessor.keyUp(keycode);
	}

	@Override
	public boolean keyTyped(char character) {
		if(inputProcessor == null) {
			return false;
		}
		return inputProcessor.keyTyped(character);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(inputProcessor == null) {
			return false;
		}
		return inputProcessor.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(inputProcessor == null) {
			return false;
		}
		return inputProcessor.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(inputProcessor == null) {
			return false;
		}
		return inputProcessor.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if(inputProcessor == null) {
			return false;
		}
		return inputProcessor.mouseMoved(screenX, screenY);
	}

	@Override
	public boolean scrolled(int amount) {
		if(inputProcessor == null) {
			return false;
		}
		return inputProcessor.scrolled(amount);
	}

	public void setInputProcessor(InputProcessor inputProcessor) {
		this.inputProcessor = inputProcessor;
	}
}
