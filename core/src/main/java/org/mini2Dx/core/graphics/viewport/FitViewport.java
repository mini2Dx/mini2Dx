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
package org.mini2Dx.core.graphics.viewport;

import org.mini2Dx.core.util.Scaling;

/**
 * A {@link Viewport} with a fixed virtual screen size. The virtual size will be scaled to fit in the game window.
 * However, black bars may appear around the viewport depending on the window size. The aspect ratio will always be maintained.
 */
public class FitViewport extends ScalingViewport {

	/**
	 * Constructor
	 * @param worldWidth The virtual screen width
	 * @param worldHeight The virtual screen height
	 */
	public FitViewport(float worldWidth, float worldHeight) {
		this(false ,worldWidth, worldHeight);
	}

	/**
	 * Constructor
	 * @param powerOfTwo True if scaling should only be applied in powers of two
	 * @param worldWidth The virtual screen width
	 * @param worldHeight The virtual screen height
	 */
	public FitViewport(boolean powerOfTwo, float worldWidth, float worldHeight) {
		super(Scaling.FIT, powerOfTwo,worldWidth, worldHeight);
	}
}
