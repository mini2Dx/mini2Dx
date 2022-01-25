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
package org.mini2Dx.core.input.button;

public enum SwitchJoyConLButton implements GamePadButton {
	LEFT("switch-joyconL-left"),
	RIGHT("switch-joyconL-right"),
	UP("switch-joyconL-up"),
	DOWN("switch-joyconL-down"),

	STICK("switch-joyconL-stick"),

	L("switch-joyconL-l"),
	ZL("switch-joyconL-zl"),
	SL("switch-joyconL-sl"),
	SR("switch-joyconL-sr"),

	MINUS("switch-joyconL-minus");

	private final String internalName;

	SwitchJoyConLButton(String internalName) {
		this.internalName = internalName;
	}

	@Override
	public String getInternalName() {
		return internalName;
	}

	@Override
	public boolean equals(GamePadButton gamePadButton) {
		return internalName.equals(gamePadButton.getInternalName());
	}

	public static SwitchJoyConLButton fromInternalName(String value) {
		for(SwitchJoyConLButton button : SwitchJoyConLButton.values()) {
			if(button.getInternalName().equals(value)) {
				return button;
			}
		}
		return null;
	}
}