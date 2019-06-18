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
 * Implements an axis-based dead zone - suitable for tile-based games. See
 * <a href=
 * "http://www.gamasutra.com/blogs/JoshSutphin/20130416/190541/Doing_Thumbstick_Dead_Zones_Right.php">
 * Doing Thumbstick Dead Zones Right</a>.
 */
public class AxialDeadZone implements DeadZone {
	private final Vector2 rawValues = new Vector2(0f, 0f);
	private final Vector2 filteredValues = new Vector2(0f, 0f);

	private float threshold = 0.25f;
	private boolean dirty = true;

	@Override
	public void updateX(float x) {
		rawValues.x = x;
		dirty = true;
	}

	@Override
	public void updateY(float y) {
		rawValues.y = y;
		dirty = true;
	}

	@Override
	public float getX() {
		dirtyCheck();
		return filteredValues.x;
	}

	@Override
	public float getY() {
		dirtyCheck();
		return filteredValues.y;
	}

	@Override
	public float getDeadZone() {
		return threshold;
	}

	@Override
	public void setDeadZone(float deadZone) {
		this.threshold = deadZone;
	}

	private void dirtyCheck() {
		if (!dirty) {
			return;
		}
		if (Math.abs(rawValues.x) < threshold) {
			filteredValues.x = 0f;
		} else {
			filteredValues.x = rawValues.x;
		}
		if (Math.abs(rawValues.y) < threshold) {
			filteredValues.y = 0f;
		} else {
			filteredValues.y = rawValues.y;
		}
		dirty = false;
	}

	@Override
	public DeadZone copy() {
		return new AxialDeadZone();
	}
}
