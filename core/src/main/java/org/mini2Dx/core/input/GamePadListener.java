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
 * Event listener interface for {@link GamePad}
 */
public interface GamePadListener {

	public void onConnect(GamePad gamePad);

	public void onDisconnect(GamePad gamePad);

	public void onButtonDown(GamePad gamePad, int buttonCode);

	public void onButtonUp(GamePad gamePad, int buttonCode);

	public void onPovChanged(GamePad gamePad, int povCode, PovState povState);

	public void onAxisChanged(GamePad gamePad, int axisCode, float axisValue);

	public void onAccelerometerChanged(GamePad gamePad, int accelerometerCode, Vector3 value);
}
