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
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.math.Vector2;

/**
 * Applies/unapplies {@link Graphics#translate(float, float)}. This class must be
 * extended and implement
 * {@link #updateTranslation(Vector2, GameContainer, float)}
 */
public abstract class TranslateOperation implements RenderOperation {
	private final Vector2 translation = new Vector2();
	private int translateX, translateY;

	public abstract void updateTranslation(Vector2 translation, GameContainer gc, float delta);

	@Override
	public void update(GameContainer gc, float delta) {
		updateTranslation(translation, gc, delta);
		translateX = MathUtils.round(translation.x);
		translateY = MathUtils.round(translation.y);
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
	}

	@Override
	public void apply(GameContainer gc, Graphics g) {
		g.translate(translateX, translateY);
	}

	@Override
	public void unapply(GameContainer gc, Graphics g) {
		g.translate(-translateX, -translateY);
	}

}
