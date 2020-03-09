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
package org.mini2Dx.core.screen.transition;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.Transition;

/**
 * Implements a fade out transition
 * 
 * NOTE: Based on Slick implementation by Kevin Glass
 */
public class FadeOutTransition implements Transition {
	private final Color originalColor;

	private Color color;
	private float duration;

	/**
	 * Default constructor - fade to black in 0.5 seconds
	 */
	public FadeOutTransition() {
		this(Colors.rgbToColor("1,1,1"));
	}

	/**
	 * Constructs a fade out transition that lasts 0.5 seconds
	 * 
	 * @param color
	 *            The {@link Color} to fade to
	 */
	public FadeOutTransition(Color color) {
		this(color, 0.5f);
	}

	/**
	 * Constructs a fade out transition
	 * 
	 * @param color
	 *            The {@link Color} to fade to
	 * @param duration
	 *            The time in seconds to last
	 */
	public FadeOutTransition(Color color, float duration) {
		super();
		this.originalColor = color;
		this.duration = duration;
	}

	@Override
	public void initialise(GameScreen outScreen, GameScreen inScreen) {
		this.color = originalColor.copy();
		this.color.setA(0f);
	}

	@Override
	public void update(GameContainer gc, float delta) {
		color.setA(color.af() + ((delta * 1.0f) / duration));
		if (color.af() > 1f) {
			color.setA(1f);
		}
	}

	@Override
	public void preRender(GameContainer gc, Graphics g) {
	}

	@Override
	public void postRender(GameContainer gc, Graphics g) {
		Color old = g.getColor();
		g.setColor(color);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		g.setColor(old);
	}

	@Override
	public boolean isFinished() {
		return color.af() >= 1f;
	}

}
