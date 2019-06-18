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

public enum SwitchJoyConRButton implements GamePadButton {
	A("switch-joyconR-a"),
	B("switch-joyconR-b"),
	X("switch-joyconR-x"),
	Y("switch-joyconR-y"),

	R("switch-joyconR-r"),
	ZR("switch-joyconR-zr"),
	SL("switch-joyconR-sl"),
	SR("switch-joyconR-sr"),

	PLUS("switch-joyconR-plus");

	private final String internalName;

	SwitchJoyConRButton(String internalName) {
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

	public static SwitchJoyConRButton fromInternalName(String value) {
		for(SwitchJoyConRButton button : SwitchJoyConRButton.values()) {
			if(button.getInternalName().equals(value)) {
				return button;
			}
		}
		return null;
	}
}