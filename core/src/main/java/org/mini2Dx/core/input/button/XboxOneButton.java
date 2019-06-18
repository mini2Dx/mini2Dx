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

public enum XboxOneButton implements GamePadButton {
	UP("xboxOne-up"),
	DOWN("xboxOne-down"),
	LEFT("xboxOne-left"),
	RIGHT("xboxOne-right"),

	MENU("xboxOne-menu"),
	VIEW("xboxOne-view"),
	HOME("xboxOne-home"),

	LEFT_STICK("xboxOne-left-stick"),
	RIGHT_STICK("xboxOne-right-stick"),

	LEFT_SHOULDER("xboxOne-left-shoulder"),
	RIGHT_SHOULDER("xboxOne-right-shoulder"),
	LEFT_TRIGGER("xboxOne-left-trigger"),
	RIGHT_TRIGGER("xboxOne-right-trigger"),

	A("xboxOne-a"),
	B("xboxOne-b"),
	X("xboxOne-x"),
	Y("xboxOne-y");

	private final String internalName;

	XboxOneButton(String internalName) {
		this.internalName = internalName;
	}

	public static XboxOneButton fromInternalName(String value) {
		for(XboxOneButton button : XboxOneButton.values()) {
			if(button.getInternalName().equals(value)) {
				return button;
			}
		}
		return null;
	}

	@Override
	public String getInternalName() {
		return internalName;
	}

	@Override
	public boolean equals(GamePadButton gamePadButton) {
		return internalName.equals(gamePadButton.getInternalName());
	}
}
