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

import com.badlogic.gdx.controllers.AdvancedController;
import org.mini2Dx.gdx.math.MathUtils;

public class LibgdxAdvancedGamePad extends LibgdxGamePad {
	public final AdvancedController advancedController;

	protected float vibrateStrength = 0f;

	public LibgdxAdvancedGamePad(AdvancedController controller) {
		super(controller);
		advancedController = controller;
	}

	@Override
	public String getInstanceId() {
		if(advancedController.getUniqueId() != null) {
			return advancedController.getUniqueId();
		}
		return super.getInstanceId();
	}

	@Override
	public boolean isPlayerIndicesSupported() {
		return advancedController.supportsPlayerIndex();
	}

	@Override
	public int getPlayerIndex() {
		return advancedController.getPlayerIndex();
	}

	@Override
	public void setPlayerIndex(int playerIndex) {
		advancedController.setPlayerIndex(playerIndex);
	}

	@Override
	public boolean isVibrateSupported() {
		return advancedController.canVibrate();
	}

	@Override
	public boolean isVibrating() {
		return advancedController.isVibrating();
	}

	@Override
	public float getVibrationStrength() {
		return vibrateStrength;
	}

	@Override
	public void startVibration(float strength) {
		vibrateStrength = MathUtils.clamp(strength, 0f, 1f);
		advancedController.startVibration(vibrateStrength);
	}

	@Override
	public void stopVibration() {
		vibrateStrength = 0f;
		advancedController.stopVibration();
	}
}
