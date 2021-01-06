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

public enum PS4Button implements GamePadButton {
	UP("ps4-up"),
	DOWN("ps4-down"),
	LEFT("ps4-left"),
	RIGHT("ps4-right"),

	CROSS("ps4-cross"),
	SQUARE("ps4-square"),
	CIRCLE("ps4-circle"),
	TRIANGLE("ps4-triangle"),

	L1("ps4-l1"),
	R1("ps4-r1"),
	L2("ps4-l2"),
	R2("ps4-r2"),
	L3("ps4-l3"),
	R3("ps4-r3"),

	PS("ps4-ps"),
	SHARE("ps4-share"),
	OPTIONS("ps4-options"),
	TOUCHPAD("ps4-touchpad");

	private final String internalName;

	PS4Button(String internalName) {
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

	public static PS4Button fromInternalName(String value) {
		for(PS4Button button : PS4Button.values()) {
			if(button.getInternalName().equals(value)) {
				return button;
			}
		}
		return null;
	}
}
