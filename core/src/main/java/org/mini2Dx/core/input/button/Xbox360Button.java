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

public enum Xbox360Button implements GamePadButton {
	UP("xbox360-up"),
	DOWN("xbox360-down"),
	LEFT("xbox360-left"),
	RIGHT("xbox360-right"),

	START("xbox360-start"),
	BACK("xbox360-back"),
	GUIDE("xbox360-guide"),

	LEFT_STICK("xbox360-left-stick"),
	RIGHT_STICK("xbox360-right-stick"),

	LEFT_SHOULDER("xbox360-left-shoulder"),
	RIGHT_SHOULDER("xbox360-right-shoulder"),
	LEFT_TRIGGER("xbox360-left-trigger"),
	RIGHT_TRIGGER("xbox360-right-trigger"),

	A("xbox360-a"),
	B("xbox360-b"),
	X("xbox360-x"),
	Y("xbox360-y");

	private final String internalName;

	Xbox360Button(String internalName) {
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

	public static Xbox360Button fromInternalName(String value) {
		for(Xbox360Button button : Xbox360Button.values()) {
			if(button.getInternalName().equals(value)) {
				return button;
			}
		}
		return null;
	}
}
