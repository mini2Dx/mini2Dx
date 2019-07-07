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
package org.mini2Dx.core.input.deadzone;

import org.mini2Dx.gdx.math.Vector2;

/**
 * A {@link DeadZone} implementation that returns the raw gamepad values
 */
public class NoopDeadZone implements DeadZone {
	private final Vector2 rawValues = new Vector2(0f, 0f);

	@Override
	public void updateX(float x) {
		rawValues.x = x;
	}

	@Override
	public void updateY(float y) {
		rawValues.y = y;
	}

	@Override
	public float getX() {
		return rawValues.x;
	}

	@Override
	public float getY() {
		return rawValues.y;
	}

	@Override
	public float getDeadZone() {
		return 0f;
	}

	@Override
	public void setDeadZone(float deadZone) {}

	@Override
	public DeadZone copy() {
		return new NoopDeadZone();
	}

}
