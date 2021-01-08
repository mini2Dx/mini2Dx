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
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import org.mini2Dx.core.Input;
import org.mini2Dx.core.input.GamePad;
import org.mini2Dx.core.input.GamePadConnectionListener;
import org.mini2Dx.core.input.nswitch.SwitchDualJoyConGamePad;
import org.mini2Dx.core.input.nswitch.SwitchJoyConLGamePad;
import org.mini2Dx.core.input.nswitch.SwitchJoyConRGamePad;
import org.mini2Dx.core.input.ps4.PS4GamePad;
import org.mini2Dx.core.input.xbox.XboxGamePad;
import org.mini2Dx.gdx.InputProcessor;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Queue;
import org.mini2Dx.libgdx.input.*;

public class LibgdxInput implements Input, ControllerListener {
	private final Array<GamePad> connectedGamePads = new Array<GamePad>();
	private final Array<GamePad> disconnectedGamePads = new Array<GamePad>();
	private final Queue<Controller> connectedGamePadQueue = new Queue<>();

	private LibgdxInputProcessor gdxInputProcessor = null;
	private GamePadConnectionListener connectionListener = null;

	private boolean initialised = false;

	public LibgdxInput() {
		super();
		Controllers.addListener(this);
	}

	public void updateGamePads() {
		init();
		reconnectGamePads();
		connectNewGamePads();
	}

	private void init() {
		if(initialised) {
			return;
		}

		for(Controller controller : Controllers.getControllers()) {
			connectedGamePadQueue.addLast(controller);
		}
		initialised = true;
	}

	private void reconnectGamePads() {
		for(int i = disconnectedGamePads.size - 1; i >= 0; i--) {
			final LibgdxGamePad gamePad = (LibgdxGamePad) disconnectedGamePads.get(i);
			if(!gamePad.isConnected()) {
				continue;
			}
			connectedGamePads.add(gamePad);
			disconnectedGamePads.removeIndex(i);

			notifyGamePadConnected(gamePad);
		}
	}

	private void connectNewGamePads() {
		while(!connectedGamePadQueue.isEmpty()) {
			final Controller controller = connectedGamePadQueue.removeFirst();

			boolean existingController = false;
			for(int i = 0; i < connectedGamePads.size; i++) {
				if(LibgdxGamePad.getInstanceId(controller).equals(connectedGamePads.get(i).getInstanceId())) {
					//Already connected
					existingController = true;
					break;
				}
			}

			if(existingController) {
				continue;
			}
			final GamePad gamePad = new LibgdxGamePad(controller);
			connectedGamePads.add(gamePad);
			notifyGamePadConnected(gamePad);
		}
	}

	private void notifyGamePadConnected(GamePad gamePad) {
		if(connectionListener == null) {
			return;
		}
		connectionListener.onConnect(gamePad);
	}

	private void notifyGamePadDisconnected(GamePad gamePad) {
		if(connectionListener == null) {
			return;
		}
		connectionListener.onDisconnect(gamePad);
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
	public void setGamePadConnectionListener(GamePadConnectionListener listener, boolean notifyExisting) {
		this.connectionListener = listener;

		if(!notifyExisting) {
			return;
		}
		for(int i = connectedGamePads.size - 1; i >= 0; i--) {
			listener.onConnect(connectedGamePads.get(i));
		}
	}

	@Override
	public void setOnScreenKeyboardVisible(boolean visible) {
		Gdx.input.setOnscreenKeyboardVisible(visible);
	}

	@Override
	public Array<GamePad> getGamePads() {
		return connectedGamePads;
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
	public boolean isKeyDown(int key) {
		return Gdx.input.isKeyPressed(key);
	}

	@Override
	public boolean isKeyUp(int key) {
		return !Gdx.input.isKeyPressed(key);
	}

	@Override
	public boolean justTouched() {
		return Gdx.input.justTouched();
	}

	@Override
	public void connected(Controller controller) {
		connectedGamePadQueue.addLast(controller);
	}

	@Override
	public void disconnected(Controller controller) {
		for(int i = connectedGamePads.size - 1; i >= 0; i--) {
			final LibgdxGamePad gamePad = (LibgdxGamePad) connectedGamePads.get(i);
			if(gamePad.getInstanceId().equals(LibgdxGamePad.getInstanceId(controller))) {
				disconnectedGamePads.add(gamePad);
				connectedGamePads.removeIndex(i);

				notifyGamePadDisconnected(gamePad);
				break;
			}
		}
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		return false;
	}
}
