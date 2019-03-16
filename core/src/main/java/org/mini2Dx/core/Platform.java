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
package org.mini2Dx.core;

public enum Platform {
	WINDOWS(true, false, false),
	MAC(true, false, false),
	LINUX(true, false, false),
	ANDROID(false, true, false),
	IOS(false, true, false),
	NINTENDO_SWITCH(false, false, true);

	private final boolean desktop, mobile, console;

	Platform(boolean desktop, boolean mobile, boolean console) {
		this.desktop = desktop;
		this.mobile = mobile;
		this.console = console;
	}

	/**
	 * True if the game is running on desktop
	 * @return
	 */
	public boolean isDesktop() {
		return desktop;
	}

	/**
	 * True if the game is running on mobile
	 * @return
	 */
	public boolean isMobile() {
		return mobile;
	}

	/**
	 * True if the game is running on console
	 * @return
	 */
	public boolean isConsole() {
		return console;
	}
}
