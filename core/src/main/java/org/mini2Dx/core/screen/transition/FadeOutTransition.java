/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.screen.transition;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.util.ColorUtils;

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
		this(ColorUtils.rgbToColor("1,1,1"));
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
