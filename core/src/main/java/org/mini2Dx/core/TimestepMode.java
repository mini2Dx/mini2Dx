/*******************************************************************************
 * Copyright 2020 See AUTHORS file
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

public enum TimestepMode {
	/**
	 * Default timestep mode where delta is the time between previous frame and current frame
	 */
	DEFAULT,
	/**
	 * Timestep mode oriented towards physics games.<br>
	 * <br>
	 * update() and updatePhysics() must be implemented. The delta value passed to update() will be the time between previous frame and current frame.
	 * The delta value passed to updatePhysics() will always be a fixed amount based on the target FPS configured at launch
	 * Objects will be interpolated when a render occurs between updatePhysics() calls.<br>
	 * <br>
	 * See <a href="https://gafferongames.com/post/fix_your_timestep/">Glenn Fiedler's Fix your timestep</a>
	 */
	PHYSICS
}
