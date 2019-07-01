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
package org.mini2Dx.ui.effect;

import org.mini2Dx.core.collision.CollisionBox;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.render.UiContainerRenderTree;

import org.mini2Dx.gdx.math.MathUtils;

/**
 * A {@link UiEffect} that moves a {@link UiElement} off the screen from its
 * current position
 */
public class SlideOut implements UiEffect {
	public static final float DEFAULT_DURATION = 1f;

	private final SlideDirection direction;
	private final float duration;

	private float speed;
	private boolean started = false;
	private boolean finished = false;

	private UiElement element;

	/**
	 * Slide out via top of screen with {@link #DEFAULT_DURATION}
	 */
	public SlideOut() {
		this(DEFAULT_DURATION);
	}

	/**
	 * Slide out via top of screen with specific duration
	 * 
	 * @param duration
	 *            The duration of the animation
	 */
	public SlideOut(float duration) {
		this(SlideDirection.UP, duration);
	}

	/**
	 * Slide out via one side of screen with {@link #DEFAULT_DURATION}
	 * 
	 * @param direction
	 *            The direction to slide out. Note that this is the direction that
	 *            the {@link UiElement} will move.
	 */
	public SlideOut(SlideDirection direction) {
		this(direction, DEFAULT_DURATION);
	}

	/**
	 * Slide out via one side of screen with specific duration
	 * 
	 * @param direction
	 *            The direction to slide out. Note that this is the direction that
	 *            the {@link UiElement} will move.
	 * @param duration
	 *            The duration of the animation
	 */
	public SlideOut(SlideDirection direction, float duration) {
		super();
		this.direction = direction;
		this.duration = duration;
	}

	@Override
	public boolean update(UiContainerRenderTree uiContainer, CollisionBox currentArea, Rectangle targetArea,
			float delta) {
		if (finished) {
			return false;
		}

		switch (direction) {
		case UP:
			if (!started) {
				speed = Math.abs(currentArea.getY() + currentArea.getHeight()) / duration;
				started = true;
			}

			if (currentArea.getY() + currentArea.getHeight() > 0f) {
				currentArea.setY(currentArea.getY() - (speed * delta));
			}
			if (currentArea.getY() + currentArea.getHeight() <= 0f) {
				finished = true;
				element.setVisibility(Visibility.HIDDEN);
			} else if (MathUtils.isEqual(currentArea.getY() + currentArea.getHeight(), 0f, 0.1f)) {
				finished = true;
				element.setVisibility(Visibility.HIDDEN);
			}
			break;
		case DOWN:
			if (!started) {
				speed = Math
						.abs((uiContainer.getOuterRenderY() + uiContainer.getOuterRenderHeight()) - currentArea.getY())
						/ duration;
				started = true;
			}
			if (currentArea.getY() < uiContainer.getOuterRenderHeight()) {
				currentArea.setY(currentArea.getY() + (speed * delta));
			}
			if (currentArea.getY() >= uiContainer.getOuterRenderHeight()) {
				finished = true;
				element.setVisibility(Visibility.HIDDEN);
			} else if (MathUtils.isEqual(currentArea.getY(), uiContainer.getOuterRenderHeight(), 0.1f)) {
				finished = true;
				element.setVisibility(Visibility.HIDDEN);
			}
			break;
		case LEFT:
			if (!started) {
				speed = Math.abs(currentArea.getX() + currentArea.getWidth()) / duration;
				started = true;
			}
			if (currentArea.getX() + currentArea.getWidth() > 0f) {
				currentArea.setX(currentArea.getX() - (speed * delta));
			}
			if (currentArea.getX() + currentArea.getWidth() <= 0f) {
				finished = true;
				element.setVisibility(Visibility.HIDDEN);
			} else if (MathUtils.isEqual(currentArea.getX() + currentArea.getWidth(), 0f, 0.1f)) {
				finished = true;
				element.setVisibility(Visibility.HIDDEN);
			}
			break;
		case RIGHT:
			if (!started) {
				speed = Math
						.abs((uiContainer.getOuterRenderX() + uiContainer.getOuterRenderWidth()) - currentArea.getX())
						/ duration;
				started = true;
			}
			if (currentArea.getX() < uiContainer.getOuterRenderWidth()) {
				currentArea.setX(currentArea.getX() + (speed * delta));
			} 
			if (currentArea.getX() >= uiContainer.getOuterRenderWidth()) {
				finished = true;
				element.setVisibility(Visibility.HIDDEN);
			} else if (MathUtils.isEqual(currentArea.getX(), uiContainer.getOuterRenderWidth(), 0.1f)) {
				finished = true;
				element.setVisibility(Visibility.HIDDEN);
			}
			break;
		default:
			break;
		}
		return !finished;
	}

	@Override
	public void preBegin(UiElement element) {
		this.element = element;
	}

	@Override
	public void postEnd(UiElement element) {
	}

	@Override
	public void preRender(Graphics g) {
	}

	@Override
	public void postRender(Graphics g) {
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	public float getDuration() {
		return duration;
	}

	public float getSpeed() {
		return speed;
	}

}
