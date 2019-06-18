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
package org.mini2Dx.core.input;

import org.mini2Dx.gdx.math.Vector3;

/**
 * No-op implementation of {@link GamePadListener} that can be extended and override individual methods
 */
public class BaseGamePadListener implements GamePadListener {

	@Override
	public void onConnect(GamePad gamePad) {
	}

	@Override
	public void onDisconnect(GamePad gamePad) {
	}

	@Override
	public void onButtonDown(GamePad gamePad, int buttonCode) {
	}

	@Override
	public void onButtonUp(GamePad gamePad, int buttonCode) {
	}

	@Override
	public void onPovChanged(GamePad gamePad, int povCode, PovState povState) {
	}

	@Override
	public void onAxisChanged(GamePad gamePad, int axisCode, float axisValue) {
	}

	@Override
	public void onAccelerometerChanged(GamePad gamePad, int accelerometerCode, Vector3 value) {
	}
}
