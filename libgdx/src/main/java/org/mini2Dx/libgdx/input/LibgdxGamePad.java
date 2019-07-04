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

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import org.mini2Dx.core.input.GamePad;
import org.mini2Dx.core.input.GamePadListener;
import org.mini2Dx.core.input.GamePadType;
import org.mini2Dx.core.input.PovState;
import org.mini2Dx.core.input.ps4.PS4GamePad;
import org.mini2Dx.core.input.xbox360.Xbox360GamePad;
import org.mini2Dx.core.input.xboxOne.XboxOneGamePad;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.IntFloatMap;
import org.mini2Dx.gdx.utils.IntMap;
import org.mini2Dx.gdx.utils.IntSet;

public class LibgdxGamePad implements GamePad, ControllerListener {
	private final Controller controller;
	private final Array<GamePadListener> listeners = new Array<GamePadListener>();

	private final IntSet downButtons = new IntSet();
	private final IntFloatMap axes = new IntFloatMap();
	private final IntMap<PovState> povs = new IntMap<PovState>();
	private final IntMap<org.mini2Dx.gdx.math.Vector3> accelerometers = new IntMap<org.mini2Dx.gdx.math.Vector3>();

	protected GamePadType gamePadType = null;
	protected boolean connected = true;

	public LibgdxGamePad(Controller controller) {
		this.controller = controller;
	}

	public void init() {
		this.controller.addListener(this);
	}

	@Override
	public void connected(Controller controller) {
		connected = true;
		notifyConnected();
	}

	@Override
	public void disconnected(Controller controller) {
		connected = false;
		notifyDisconnected();
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		downButtons.add(buttonCode);
		notifyButtonDown(buttonCode);
		return true;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		downButtons.remove(buttonCode);
		notifyButtonUp(buttonCode);
		return true;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		axes.put(axisCode, value);
		notifyAxisChanged(axisCode, value);
		return true;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		switch(value) {
		case center:
			povs.put(povCode, PovState.CENTER);
			break;
		case north:
			povs.put(povCode, PovState.NORTH);
			break;
		case south:
			povs.put(povCode, PovState.SOUTH);
			break;
		case east:
			povs.put(povCode, PovState.EAST);
			break;
		case west:
			povs.put(povCode, PovState.WEST);
			break;
		case northEast:
			povs.put(povCode, PovState.NORTH_EAST);
			break;
		case southEast:
			povs.put(povCode, PovState.SOUTH_EAST);
			break;
		case northWest:
			povs.put(povCode, PovState.NORTH_WEST);
			break;
		case southWest:
			povs.put(povCode, PovState.SOUTH_WEST);
			break;
		}
		notifyPovChanged(povCode, povs.get(povCode, PovState.CENTER));
		return true;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		return true;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		return true;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		if(!accelerometers.containsKey(accelerometerCode)) {
			accelerometers.put(accelerometerCode, new org.mini2Dx.gdx.math.Vector3(value.x, value.y, value.z));
		} else {
			accelerometers.get(accelerometerCode).set(value.x, value.y, value.z);
		}
		notifyAccelerometerChanged(accelerometerCode);
		return true;
	}

	@Override
	public GamePadType getGamePadType() {
		if(gamePadType == null) {
			final String name = controller.getName().toLowerCase();
			for(int i = 0; i < XboxOneGamePad.ID.length; i++) {
				if(name.contains(XboxOneGamePad.ID[i])) {
					gamePadType = GamePadType.XBOX_ONE;
					return GamePadType.XBOX_ONE;
				}
			}
			for(int i = 0; i < Xbox360GamePad.ID.length; i++) {
				if(name.contains(Xbox360GamePad.ID[i])) {
					gamePadType = GamePadType.XBOX_360;
					return GamePadType.XBOX_360;
				}
			}
			for(int i = 0; i < PS4GamePad.ID.length; i++) {
				if(name.contains(PS4GamePad.ID[i])) {
					gamePadType = GamePadType.PS4;
					return GamePadType.PS4;
				}
			}
			gamePadType = GamePadType.UNKNOWN;
		}
		return gamePadType;
	}

	@Override
	public String getInstanceId() {
		return controller.getName();
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public boolean isButtonDown(int buttonCode) {
		return downButtons.contains(buttonCode);
	}

	@Override
	public boolean isButtonUp(int buttonCode) {
		return !downButtons.contains(buttonCode);
	}

	@Override
	public float getAxis(int axisCode) {
		return axes.get(axisCode, 0f);
	}

	@Override
	public PovState getPov(int povCode) {
		return povs.get(povCode, PovState.CENTER);
	}

	@Override
	public boolean isAccelerometerSupported() {
		return false;
	}

	@Override
	public org.mini2Dx.gdx.math.Vector3 getAccelerometer(int accelerometerCode) {
		if(!accelerometers.containsKey(accelerometerCode)) {
			accelerometers.put(accelerometerCode, new org.mini2Dx.gdx.math.Vector3(org.mini2Dx.gdx.math.Vector3.Zero));
		}
		return accelerometers.get(accelerometerCode);
	}

	@Override
	public float getAccelerometerSensitivity() {
		return 0f;
	}

	@Override
	public void setAccelerometerSensitivity(float sensitivity) {
	}

	@Override
	public void addListener(GamePadListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(GamePadListener listener) {
		listeners.removeValue(listener, false);
	}

	protected void notifyConnected() {
		for(int i = listeners.size - 1; i >= 0; i--) {
			listeners.get(i).onConnect(this);
		}
	}

	protected void notifyDisconnected() {
		for(int i = listeners.size - 1; i >= 0; i--) {
			listeners.get(i).onDisconnect(this);
		}
	}

	protected void notifyButtonDown(int buttonCode) {
		for(int i = listeners.size - 1; i >= 0; i--) {
			listeners.get(i).onButtonDown(this, buttonCode);
		}
	}

	protected void notifyButtonUp(int buttonCode) {
		for(int i = listeners.size - 1; i >= 0; i--) {
			listeners.get(i).onButtonUp(this, buttonCode);
		}
	}

	protected void notifyPovChanged(int povCode, PovState state) {
		for(int i = listeners.size - 1; i >= 0; i--) {
			listeners.get(i).onPovChanged(this, povCode, state);
		}
	}

	protected void notifyAxisChanged(int axisCode, float axisValue) {
		for(int i = listeners.size - 1; i >= 0; i--) {
			listeners.get(i).onAxisChanged(this, axisCode, axisValue);
		}
	}

	protected void notifyAccelerometerChanged(int accelerometerCode) {
		for(int i = listeners.size - 1; i >= 0; i--) {
			listeners.get(i).onAccelerometerChanged(this, accelerometerCode, accelerometers.get(accelerometerCode));
		}
	}

	@Override
	public boolean isPlayerIndicesSupported() {
		return false;
	}

	@Override
	public int getPlayerIndex() {
		return -1;
	}

	@Override
	public void setPlayerIndex(int playerIndex) {
	}

	@Override
	public boolean isVibrateSupported() {
		return false;
	}

	@Override
	public boolean isVibrating() {
		return false;
	}

	@Override
	public float getVibrationStrength() {
		return 0f;
	}

	@Override
	public void startVibration(float strength) {
	}

	@Override
	public void stopVibration() {
	}
}
