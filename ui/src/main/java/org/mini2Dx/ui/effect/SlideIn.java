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

import org.mini2Dx.core.engine.geom.CollisionBox;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.render.UiContainerRenderTree;

import com.badlogic.gdx.math.MathUtils;

/**
 * A {@link UiEffect} that moves a {@link UiElement} from off-screen to its
 * target position
 */
public class SlideIn implements UiEffect {
	public static final float DEFAULT_DURATION = 1f;

	private final SlideDirection direction;
	private final float duration;

	private float speed = 0f;
	private float remainingDuration;
	private float previousTargetX, previousTargetY;

	private boolean started = false;
	private boolean speedCalculated = false;
	private boolean finished = false;

	/**
	 * Slide in from bottom of screen with {@link #DEFAULT_DURATION}
	 */
	public SlideIn() {
		this(DEFAULT_DURATION);
	}

	/**
	 * Slide in from bottom of screen with specific duration
	 * 
	 * @param duration
	 *            The duration of the animation in seconds
	 */
	public SlideIn(float duration) {
		this(SlideDirection.UP, duration);
	}

	/**
	 * Slide in from outside of the screen with {@link #DEFAULT_DURATION}
	 * 
	 * @param direction
	 *            The slide direction. Note that this is the direction that the
	 *            {@link UiElement} moves
	 */
	public SlideIn(SlideDirection direction) {
		this(direction, DEFAULT_DURATION);
	}

	/**
	 * Slide in from outside of the screen with specific duration
	 * 
	 * @param direction
	 *            The slide direction. Note that this is the direction that the
	 *            {@link UiElement} moves
	 * @param duration
	 *            The duration of the animation in seconds
	 */
	public SlideIn(SlideDirection direction, float duration) {
		super();
		this.direction = direction;
		this.duration = duration;
		this.remainingDuration = duration;
	}

	@Override
	public boolean update(UiContainerRenderTree uiContainer, CollisionBox currentArea, Rectangle targetArea,
			float delta) {
		if (finished) {
			return true;
		}

		float targetX = targetArea.getX();
		float targetY = targetArea.getY();

		if (!MathUtils.isEqual(targetX, previousTargetX, 0.1f) || !MathUtils.isEqual(targetY, previousTargetY, 0.1f)) {
			speedCalculated = false;
		}
		previousTargetX = targetX;
		previousTargetY = targetY;

		switch (direction) {
		case UP:
			if (!started) {
				currentArea.forceTo(targetX, uiContainer.getOuterRenderY() + uiContainer.getOuterRenderHeight() + 1f,
						targetArea.getWidth(), targetArea.getHeight());
				started = true;
			}
			if (!speedCalculated) {
				speed = Math.abs(currentArea.getY() - targetY) / remainingDuration;
				speedCalculated = true;
			}
			currentArea.setWidth(targetArea.getWidth());
			currentArea.setHeight(targetArea.getHeight());
			currentArea.setX(targetX);
			currentArea.setY(Math.max(targetY, currentArea.getY() - (speed * delta)));

			if (MathUtils.isEqual(currentArea.getY(), targetY, 0.1f)) {
				finished = true;
			}
			break;
		case DOWN:
			if (!started) {
				currentArea.forceTo(targetX, uiContainer.getOuterRenderY() - targetArea.getHeight() - 1f,
						targetArea.getWidth(), targetArea.getHeight());
				started = true;
			}
			if (!speedCalculated) {
				speed = Math.abs(targetY - currentArea.getY()) / remainingDuration;
				speedCalculated = true;
			}
			currentArea.setWidth(targetArea.getWidth());
			currentArea.setHeight(targetArea.getHeight());
			currentArea.setX(targetX);
			currentArea.setY(Math.min(targetY, currentArea.getY() + (speed * delta)));

			if (MathUtils.isEqual(currentArea.getY(), targetY, 0.1f)) {
				finished = true;
			}
			break;
		case LEFT:
			if (!started) {
				currentArea.forceTo(uiContainer.getOuterRenderX() + uiContainer.getOuterRenderWidth() + 1f, targetY,
						targetArea.getWidth(), targetArea.getHeight());
				started = true;
			}
			if (!speedCalculated) {
				speed = Math.abs(currentArea.getX() - targetX) / remainingDuration;
				speedCalculated = true;
			}
			currentArea.setWidth(targetArea.getWidth());
			currentArea.setHeight(targetArea.getHeight());
			currentArea.setX(Math.max(targetX, currentArea.getX() - (speed * delta)));
			currentArea.setY(targetY);

			if (MathUtils.isEqual(currentArea.getX(), targetX, 0.1f)) {
				finished = true;
			}
			break;
		case RIGHT:
			if (!started) {
				currentArea.forceTo(uiContainer.getOuterRenderX() - targetArea.getWidth() - 1f, targetY,
						targetArea.getWidth(), targetArea.getHeight());
				started = true;
			}
			if (!speedCalculated) {
				speed = Math.abs(targetX - currentArea.getX()) / remainingDuration;
				speedCalculated = true;
			}
			currentArea.setWidth(targetArea.getWidth());
			currentArea.setHeight(targetArea.getHeight());
			currentArea.setX(Math.min(targetX, currentArea.getX() + (speed * delta)));
			currentArea.setY(targetY);

			if (MathUtils.isEqual(currentArea.getX(), targetX, 0.1f)) {
				finished = true;
			}
			break;
		}
		
		remainingDuration -= delta;
		return true;
	}

	@Override
	public void preBegin(UiElement element) {
		element.setVisibility(Visibility.VISIBLE);
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
