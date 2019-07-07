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
package org.mini2Dx.core.graphics.pipeline;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.gdx.math.Vector2;

/**
 * Applies/unapplies {@link Graphics#scale(float, float)}. This class must be
 * extended and implement
 * {@link #updateScale(Vector2, GameContainer, float)}
 */
public abstract class ScaleOperation implements RenderOperation {
	private final Vector2 scale = new Vector2();
	
	private float scaleX, scaleY;
	private float unscaleX, unscaleY;
	
	public abstract void updateScale(Vector2 scale, GameContainer gc, float delta);

	@Override
	public void update(GameContainer gc, float delta) {
		updateScale(scale, gc, delta);
		scaleX = scale.x;
		scaleY = scale.y;
		unscaleX = 1f / scaleX;
		unscaleY = 1f / scaleY;
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
	}

	@Override
	public void apply(GameContainer gc, Graphics g) {
		g.scale(scaleX, scaleY);
	}

	@Override
	public void unapply(GameContainer gc, Graphics g) {
		g.scale(unscaleX, unscaleY);
	}

}
