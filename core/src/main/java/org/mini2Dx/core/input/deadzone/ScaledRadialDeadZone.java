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
 * Implements a scaling radius-based dead zone. High-precision version of
 * {@link RadialDeadZone}. See <a href=
 * "http://www.gamasutra.com/blogs/JoshSutphin/20130416/190541/Doing_Thumbstick_Dead_Zones_Right.php">
 * Doing Thumbstick Dead Zones Right</a>.
 */
public class ScaledRadialDeadZone implements DeadZone {
	private final Vector2 rawValues = new Vector2(0f, 0f);
	private final Vector2 filteredValues = new Vector2(0f, 0f);

	private float thresholdRaw = 0.25f;
	private double thresholdCast = 0.25;
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
		return thresholdRaw;
	}

	@Override
	public void setDeadZone(float deadZone) {
		this.thresholdRaw = deadZone;
		this.thresholdCast = (double) thresholdRaw;
	}

	private void dirtyCheck() {
		if (!dirty) {
			return;
		}
		double magnitude = Math.sqrt((rawValues.x * rawValues.x) + (rawValues.y * rawValues.y));
		if (magnitude < thresholdCast) {
			filteredValues.x = 0f;
			filteredValues.y = 0f;
		} else {
			float magnitudeF = (float) magnitude;
			float rawX = rawValues.x;
			float rawY = rawValues.y;
			rawValues.nor();
			filteredValues.x = rawValues.x * (magnitudeF - thresholdRaw) / (1f - thresholdRaw);
			filteredValues.y = rawValues.y * (magnitudeF - thresholdRaw) / (1f - thresholdRaw);
			rawValues.set(rawX, rawY);
		}
		dirty = false;
	}

	@Override
	public DeadZone copy() {
		return new ScaledRadialDeadZone();
	}
}
