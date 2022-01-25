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

public enum SwitchDualJoyConButton implements GamePadButton {
	LEFT("switch-dual-joycon-left"),
	RIGHT("switch-dual-joycon-right"),
	UP("switch-dual-joycon-up"),
	DOWN("switch-dual-joycon-down"),

	LEFT_STICK("switch-dual-joycon-left-stick"),
	RIGHT_STICK("switch-dual-joycon-right-stick"),

	A("switch-dual-joycon-a"),
	B("switch-dual-joycon-b"),
	X("switch-dual-joycon-x"),
	Y("switch-dual-joycon-y"),

	L("switch-dual-joycon-l"),
	R("switch-dual-joycon-r"),
	ZL("switch-dual-joycon-zl"),
	ZR("switch-dual-joycon-zr"),

	PLUS("switch-dual-joycon-plus"),
	MINUS("switch-dual-joycon-minus");

	private final String internalName;

	SwitchDualJoyConButton(String internalName) {
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

	public static SwitchDualJoyConButton fromInternalName(String value) {
		for(SwitchDualJoyConButton button : SwitchDualJoyConButton.values()) {
			if(button.getInternalName().equals(value)) {
				return button;
			}
		}
		return null;
	}
}