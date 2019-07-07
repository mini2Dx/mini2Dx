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
 * Similar to {@link FitViewport} except the viewport will expand its size after scaling to
 * fill the remaining space of the window to avoid black bars. A maxiumum virtual screen size c
 * an be set if black bars are desired after a certain amount of viewport expansion.
 */
public class ExtendViewport extends Viewport {
	private float minWorldWidth, minWorldHeight;
	private float maxWorldWidth, maxWorldHeight;
	private boolean powerOfTwo = false;

	private final Vector2 size = new Vector2();
	private final Vector2 scale = new Vector2();

	/**
	 * Constructor with no maxiumum virtual screen size
	 * @param minWorldWidth Minimum virtual screen width
	 * @param minWorldHeight Minimum virtual screen height
	 */
	public ExtendViewport(float minWorldWidth, float minWorldHeight) {
		this(minWorldWidth, minWorldHeight, 0f, 0f);
	}

	/**
	 * Constructor
	 * @param minWorldWidth Minimum virtual screen width
	 * @param minWorldHeight Minimum virtual screen height
	 * @param maxWorldWidth Maximum virtual screen width
	 * @param maxWorldHeight Maximum virtual screen height
	 */
	public ExtendViewport(float minWorldWidth, float minWorldHeight, float maxWorldWidth, float maxWorldHeight) {
		this(false, minWorldWidth, minWorldHeight, maxWorldWidth, maxWorldHeight);
	}

	/**
	 * Constructor with no maxiumum virtual screen size
	 * @param powerOfTwo True if scaling should only be applied in powers of two
	 * @param minWorldWidth Minimum virtual screen width
	 * @param minWorldHeight Minimum virtual screen height
	 */
	public ExtendViewport(boolean powerOfTwo, float minWorldWidth, float minWorldHeight) {
		this(powerOfTwo, minWorldWidth, minWorldHeight, 0f, 0f);
	}

	/**
	 * Constructor
	 * @param powerOfTwo True if scaling should only be applied in powers of two
	 * @param minWorldWidth Minimum virtual screen width
	 * @param minWorldHeight Minimum virtual screen height
	 * @param maxWorldWidth Maximum virtual screen width
	 * @param maxWorldHeight Maximum virtual screen height
	 */
	public ExtendViewport(boolean powerOfTwo, float minWorldWidth, float minWorldHeight, float maxWorldWidth, float maxWorldHeight) {
		super();
		this.powerOfTwo = powerOfTwo;
		this.minWorldWidth = minWorldWidth;
		this.minWorldHeight = minWorldHeight;
		this.maxWorldWidth = maxWorldWidth;
		this.maxWorldHeight = maxWorldHeight;
	}

	@Override
	public void onResize(int width, int height) {
		float worldWidth = minWorldWidth;
		float worldHeight = minWorldHeight;
		Scaling.FIT.apply(size, scale, powerOfTwo, worldWidth, worldHeight, width, height);

		int viewportWidth = Math.round(size.x);
		int viewportHeight = Math.round(size.y);
		if (viewportWidth < width) {
			float toViewportSpace = viewportHeight / worldHeight;
			float toWorldSpace = worldHeight / viewportHeight;
			float lengthen = (width - viewportWidth) * toWorldSpace;
			if (maxWorldWidth > 0) lengthen = Math.min(lengthen, maxWorldWidth - minWorldWidth);
			worldWidth += lengthen;
			viewportWidth += Math.round(lengthen * toViewportSpace);
		}
		if (viewportHeight < height) {
			float toViewportSpace = viewportWidth / worldWidth;
			float toWorldSpace = worldWidth / viewportWidth;
			float lengthen = (height - viewportHeight) * toWorldSpace;
			if (maxWorldHeight > 0) lengthen = Math.min(lengthen, maxWorldHeight - minWorldHeight);
			worldHeight += lengthen;
			viewportHeight += Math.round(lengthen * toViewportSpace);
		}

		setBounds((width - viewportWidth) / MathUtils.round(2 * scale.x),
				(height - viewportHeight) / MathUtils.round(2 * scale.y),
				MathUtils.round(worldWidth), MathUtils.round(worldHeight),
				scale.x, scale.y);
	}

	public float getMinWorldWidth() {
		return minWorldWidth;
	}

	public void setMinWorldWidth(float minWorldWidth) {
		this.minWorldWidth = minWorldWidth;
	}

	public float getMinWorldHeight() {
		return minWorldHeight;
	}

	public void setMinWorldHeight(float minWorldHeight) {
		this.minWorldHeight = minWorldHeight;
	}

	public float getMaxWorldWidth() {
		return maxWorldWidth;
	}

	public void setMaxWorldWidth(float maxWorldWidth) {
		this.maxWorldWidth = maxWorldWidth;
	}

	public float getMaxWorldHeight() {
		return maxWorldHeight;
	}

	public void setMaxWorldHeight(float maxWorldHeight) {
		this.maxWorldHeight = maxWorldHeight;
	}

	public boolean isPowerOfTwo() {
		return powerOfTwo;
	}

	public void setPowerOfTwo(boolean powerOfTwo) {
		this.powerOfTwo = powerOfTwo;
	}
}
