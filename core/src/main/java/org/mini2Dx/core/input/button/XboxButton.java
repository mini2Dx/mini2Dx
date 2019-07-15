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

public enum XboxButton implements GamePadButton {
	UP("xbox-up"),
	DOWN("xbox-down"),
	LEFT("xbox-left"),
	RIGHT("xbox-right"),

	START("xbox-start"),
	BACK("xbox-back"),
	GUIDE("xbox-guide"),

	LEFT_STICK("xbox-left-stick"),
	RIGHT_STICK("xbox-right-stick"),

	LEFT_SHOULDER("xbox-left-shoulder"),
	RIGHT_SHOULDER("xbox-right-shoulder"),
	LEFT_TRIGGER("xbox-left-trigger"),
	RIGHT_TRIGGER("xbox-right-trigger"),

	A("xbox-a"),
	B("xbox-b"),
	X("xbox-x"),
	Y("xbox-y");

	private final String internalName;

	XboxButton(String internalName) {
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

	public static XboxButton fromInternalName(String value) {
		for(XboxButton button : XboxButton.values()) {
			if(button.getInternalName().equals(value)) {
				return button;
			}
		}
		return null;
	}
}
