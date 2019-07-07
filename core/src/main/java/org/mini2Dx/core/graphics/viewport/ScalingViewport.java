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
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.math.Vector2;

/**
 * Base class for implementing {@link Viewport} instances that use {@link Scaling}
 */
public class ScalingViewport extends Viewport {
	private final Scaling scaling;
	private final float worldWidth, worldHeight;
	private final Vector2 size = new Vector2();
	private final Vector2 scale = new Vector2();

	private boolean powerOfTwo;

	public ScalingViewport(Scaling scaling, boolean powerOfTwo, float worldWidth, float worldHeight) {
		super();
		this.scaling = scaling;
		this.powerOfTwo = powerOfTwo;
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
	}

	@Override
	public void onResize(int width, int height) {
		scaling.apply(size, scale, powerOfTwo, worldWidth, worldHeight, width, height);

		final int viewWidth = MathUtils.round(size.x);
		final int viewHeight = MathUtils.round(size.y);

		setBounds((width - viewWidth) / MathUtils.round(2 * scale.x), (height - viewHeight) / MathUtils.round(2 * scale.y),
				MathUtils.round(worldWidth), MathUtils.round(worldHeight), scale.x, scale.y);
	}

	public boolean isPowerOfTwo() {
		return powerOfTwo;
	}

	public void setPowerOfTwo(boolean powerOfTwo) {
		this.powerOfTwo = powerOfTwo;
	}
}
