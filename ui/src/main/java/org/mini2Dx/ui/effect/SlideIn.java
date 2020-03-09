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
package org.mini2Dx.ui.effect;

import org.mini2Dx.core.collision.CollisionArea;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.render.UiContainerRenderTree;

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

	private UiElement element;

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
	public boolean update(UiContainerRenderTree uiContainer, CollisionArea currentArea, Rectangle targetArea,
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
				element.setVisibility(Visibility.VISIBLE);
				currentArea.forceTo(targetX, uiContainer.getOuterRenderY() + uiContainer.getOuterRenderHeight() + 1f,
						targetArea.getWidth(), targetArea.getHeight());
				started = true;
			}
			if (!speedCalculated) {
				speed = Math.abs(currentArea.getY() - targetY) / remainingDuration;
				speedCalculated = true;
			}
			currentArea.setTo(targetX, Math.max(targetY, currentArea.getY() - (speed * delta)),
					targetArea.getWidth(), targetArea.getHeight());

			if (MathUtils.isEqual(currentArea.getY(), targetY, 0.1f)) {
				finished = true;
			}
			break;
		case DOWN:
			if (!started) {
				element.setVisibility(Visibility.VISIBLE);
				currentArea.forceTo(targetX, uiContainer.getOuterRenderY() - targetArea.getHeight() - 1f,
						targetArea.getWidth(), targetArea.getHeight());
				started = true;
			}
			if (!speedCalculated) {
				speed = Math.abs(targetY - currentArea.getY()) / remainingDuration;
				speedCalculated = true;
			}
			currentArea.setTo(targetX, Math.min(targetY, currentArea.getY() + (speed * delta)),
					targetArea.getWidth(), targetArea.getHeight());

			if (MathUtils.isEqual(currentArea.getY(), targetY, 0.1f)) {
				finished = true;
			}
			break;
		case LEFT:
			if (!started) {
				element.setVisibility(Visibility.VISIBLE);
				currentArea.forceTo(uiContainer.getOuterRenderX() + uiContainer.getOuterRenderWidth() + 1f, targetY,
						targetArea.getWidth(), targetArea.getHeight());
				started = true;
			}
			if (!speedCalculated) {
				speed = Math.abs(currentArea.getX() - targetX) / remainingDuration;
				speedCalculated = true;
			}
			currentArea.setTo(Math.max(targetX, currentArea.getX() - (speed * delta)), targetY,
					targetArea.getWidth(), targetArea.getHeight());

			if (MathUtils.isEqual(currentArea.getX(), targetX, 0.1f)) {
				finished = true;
			}
			break;
		case RIGHT:
			if (!started) {
				element.setVisibility(Visibility.VISIBLE);
				currentArea.forceTo(uiContainer.getOuterRenderX() - targetArea.getWidth() - 1f, targetY,
						targetArea.getWidth(), targetArea.getHeight());
				started = true;
			}
			if (!speedCalculated) {
				speed = Math.abs(targetX - currentArea.getX()) / remainingDuration;
				speedCalculated = true;
			}
			currentArea.setTo(Math.min(targetX, currentArea.getX() + (speed * delta)), targetY,
					targetArea.getWidth(), targetArea.getHeight());

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
