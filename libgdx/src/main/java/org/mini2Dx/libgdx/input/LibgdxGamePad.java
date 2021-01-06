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
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector3;
import org.mini2Dx.core.input.GamePad;
import org.mini2Dx.core.input.GamePadListener;
import org.mini2Dx.core.input.GamePadType;
import org.mini2Dx.core.input.PovState;
import org.mini2Dx.core.input.ps4.PS4GamePad;
import org.mini2Dx.core.input.xbox.XboxGamePad;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.IntFloatMap;
import org.mini2Dx.gdx.utils.IntMap;
import org.mini2Dx.gdx.utils.IntSet;

public class LibgdxGamePad implements GamePad, ControllerListener {
	private static final int VIBRATE_DURATION = 1000 * 60 * 60;

	private final Array<GamePadListener> listeners = new Array<GamePadListener>();

	private final IntSet downButtons = new IntSet();
	private final IntFloatMap axes = new IntFloatMap();
	private final IntMap<org.mini2Dx.gdx.math.Vector3> accelerometers = new IntMap<org.mini2Dx.gdx.math.Vector3>();

	protected String uniqueId;
	protected int playerIndex;
	protected Controller controller;
	protected GamePadType gamePadType = null;
	protected boolean connected = true;

	protected float vibrateStrength = 0f;

	public LibgdxGamePad(Controller controller) {
		this.controller = controller;
		this.playerIndex = controller.getPlayerIndex();
		this.uniqueId = controller.getUniqueId();

		Controllers.addListener(this);
	}

	public void init() {
		this.playerIndex = controller.getPlayerIndex();
		this.uniqueId = controller.getUniqueId();
		this.controller.addListener(this);
	}

	@Override
	public void connected(Controller controller) {
		if (controller.getPlayerIndex() == playerIndex) {
			this.controller = controller;
			init();

			connected = true;
			notifyConnected();
		}
	}

	@Override
	public void disconnected(Controller controller) {
		if (controller.getUniqueId().equals(uniqueId)) {
			connected = false;
			notifyDisconnected();
		}
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
	public GamePadType getGamePadType() {
		if(gamePadType == null) {
			final String name = controller.getName().toLowerCase();
			for(int i = 0; i < XboxGamePad.ID.length; i++) {
				if(name.contains(XboxGamePad.ID[i])) {
					gamePadType = GamePadType.XBOX;
					return GamePadType.XBOX;
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
		if(controller.getUniqueId() != null) {
			return controller.getUniqueId();
		}
		return controller.getName();
	}

	@Override
	public String getModelInfo() {
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
		return controller.supportsPlayerIndex();
	}

	@Override
	public int getPlayerIndex() {
		if(!isPlayerIndicesSupported()) {
			return -1;
		}
		return controller.getPlayerIndex();
	}

	@Override
	public void setPlayerIndex(int playerIndex) {
		if(!isPlayerIndicesSupported()) {
			return;
		}
		controller.setPlayerIndex(playerIndex);
	}

	@Override
	public boolean isVibrateSupported() {
		return controller.canVibrate();
	}

	@Override
	public boolean isVibrating() {
		return controller.isVibrating() && vibrateStrength > 0f;
	}

	@Override
	public float getVibrationStrength() {
		return vibrateStrength;
	}

	@Override
	public void startVibration(float strength) {
		if(!isVibrateSupported()) {
			return;
		}
		vibrateStrength = MathUtils.clamp(strength, 0f, 1f);
		controller.startVibration(VIBRATE_DURATION, vibrateStrength);
	}

	@Override
	public void stopVibration() {
		vibrateStrength = 0f;
		//Workaround to Jamepad issue
		controller.startVibration(VIBRATE_DURATION, 0f);
		//controller.cancelVibration();
	}
}
