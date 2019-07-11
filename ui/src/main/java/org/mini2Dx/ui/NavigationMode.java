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
package org.mini2Dx.ui;

public enum NavigationMode {
	/**
	 * UI Navigation can only use buttons (i.e. keyboard or gamepad)
	 */
	BUTTON_ONLY,
	/**
	 * UI Navigation can use buttons (i.e. keyboard or gamepad) or pointers (i.e. mouse or touch input)
	 */
	BUTTON_OR_POINTER,
	/**
	 * UI Navigation can only use pointers (i.e. mouse or touch input)
	 */
	POINTER_ONLY
}
