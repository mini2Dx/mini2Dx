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
package org.mini2Dx.core.input;

public enum GamePadType {
	UNKNOWN("unknown"),
	SWITCH_DUAL_JOYCON("switchDualJoyCon"),
	SWITCH_JOYCON_L("switchJoyConL"),
	SWITCH_JOYCON_R("switchJoyConR"),
	PS4("ps4"),
	XBOX_360("xbox360"),
	XBOX_ONE("xboxone");

	private final String friendlyString;

	GamePadType(String friendlyString) {
		this.friendlyString = friendlyString;
	}

	public String toFriendlyString() {
		return friendlyString;
	}

	public static GamePadType fromFriendlyString(String value) {
		value = value.toLowerCase();
		for(GamePadType gamePadType : GamePadType.values()) {
			if(gamePadType.friendlyString.equals(value)) {
				return gamePadType;
			}
		}
		return GamePadType.UNKNOWN;
	}
}
