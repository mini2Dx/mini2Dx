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

import junit.framework.Assert;
import org.junit.Test;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.math.Vector2;

public class ScreenViewportTest {
	private static final int VIEWPORT_WIDTH = 320;
	private static final int VIEWPORT_HEIGHT = 240;

	private final Viewport viewport = new ScreenViewport();

	@Test
	public void testOnResize() {
		viewport.onResize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		Assert.assertEquals(0, viewport.getX());
		Assert.assertEquals(0, viewport.getY());
		Assert.assertEquals(VIEWPORT_WIDTH, viewport.getWidth());
		Assert.assertEquals(VIEWPORT_HEIGHT, viewport.getHeight());
		Assert.assertEquals(1f, viewport.getScaleX(), 0.01f);
		Assert.assertEquals(1f, viewport.getScaleY(), 0.01f);

		viewport.onResize(MathUtils.round(VIEWPORT_WIDTH * 1.5f), MathUtils.round(VIEWPORT_HEIGHT * 1.5f));
		Assert.assertEquals(0, viewport.getX());
		Assert.assertEquals(0, viewport.getY());
		Assert.assertEquals(MathUtils.round(VIEWPORT_WIDTH * 1.5f), viewport.getWidth());
		Assert.assertEquals(MathUtils.round(VIEWPORT_HEIGHT * 1.5f), viewport.getHeight());
		Assert.assertEquals(1f, viewport.getScaleX(), 0.01f);
		Assert.assertEquals(1f, viewport.getScaleY(), 0.01f);

		viewport.onResize(VIEWPORT_WIDTH * 2, VIEWPORT_HEIGHT * 2);
		Assert.assertEquals(0, viewport.getX());
		Assert.assertEquals(0, viewport.getY());
		Assert.assertEquals(VIEWPORT_WIDTH * 2, viewport.getWidth());
		Assert.assertEquals(VIEWPORT_HEIGHT * 2, viewport.getHeight());
		Assert.assertEquals(1f, viewport.getScaleX(), 0.01f);
		Assert.assertEquals(1f, viewport.getScaleY(), 0.01f);
	}

	@Test
	public void testToScreenCoordinates() {
		final Vector2 coordinates = new Vector2();

		viewport.onResize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		coordinates.set(1f, 1f);
		viewport.toScreenCoordinates(coordinates);
		Assert.assertEquals(1f, coordinates.x, 0.001f);
		Assert.assertEquals(1f, coordinates.y, 0.001f);

		viewport.onResize(MathUtils.round(VIEWPORT_WIDTH * 1.5f), MathUtils.round(VIEWPORT_HEIGHT * 1.5f));
		coordinates.set(1f, 1f);
		viewport.toScreenCoordinates(coordinates);
		Assert.assertEquals(1f, coordinates.x, 0.001f);
		Assert.assertEquals(1f, coordinates.y, 0.001f);

		viewport.onResize(VIEWPORT_WIDTH * 2, VIEWPORT_HEIGHT * 2);
		coordinates.set(1f, 1f);
		viewport.toScreenCoordinates(coordinates);
		Assert.assertEquals(1f, coordinates.x, 0.001f);
		Assert.assertEquals(1f, coordinates.y, 0.001f);
	}

	@Test
	public void testToWorldCoordinates() {
		final Vector2 coordinates = new Vector2();

		viewport.onResize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		coordinates.set(1f, 1f);
		viewport.toWorldCoordinates(coordinates);
		Assert.assertEquals(1f, coordinates.x, 0.001f);
		Assert.assertEquals(1f, coordinates.y, 0.001f);

		viewport.onResize(MathUtils.round(VIEWPORT_WIDTH * 1.5f), MathUtils.round(VIEWPORT_HEIGHT * 1.5f));
		coordinates.set(1f, 1f);
		viewport.toWorldCoordinates(coordinates);
		Assert.assertEquals(1f, coordinates.x, 0.001f);
		Assert.assertEquals(1f, coordinates.y, 0.001f);

		viewport.onResize(VIEWPORT_WIDTH * 2, VIEWPORT_HEIGHT * 2);
		coordinates.set(1f, 1f);
		viewport.toWorldCoordinates(coordinates);
		Assert.assertEquals(1f, coordinates.x, 0.001f);
		Assert.assertEquals(1f, coordinates.y, 0.001f);
	}
}
