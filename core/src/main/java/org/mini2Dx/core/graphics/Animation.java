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
package org.mini2Dx.core.graphics;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.gdx.utils.Array;

/**
 * Implements an animation with frames of variable or fixed duration
 */
public class Animation<T extends Sprite> {
	private Array<T> frames;
	private Array<Float> durations;
	private int currentFrameIndex;
	private float elapsed;
	private boolean looping;
	private float rotation;
	private float originX;
	private float originY;
	private Color color;
	private boolean flipX;
	private boolean flipY;

	/**
	 * Constructor
	 */
	public Animation() {
		currentFrameIndex = 0;
		frames = new Array<T>(true, 2);
		durations = new Array<Float>(true, 2);
		elapsed = 0f;
		looping = false;
	}

	/**
	 * Adds a frame to the animation
	 *
	 * @param frame
	 *            The frame to be added
	 * @param duration
	 *            The duration of the frame in seconds
	 */
	public void addFrame(T frame, float duration) {
		durations.add(duration);
		frames.add(frame);
		if(color == null)
			color = frame.getTint();
		originX = frame.getOriginX();
		originY = frame.getOriginY();
	}

	/**
	 * Removes a frame from the animation
	 *
	 * @param index
	 *            The index of the frame to be removed
	 */
	public void removeFrame(int index) {
		durations.removeIndex(index);
		frames.removeIndex(index);
	}

	/**
	 * Restarts the animation. This is the equivalent to setting the current
	 * frame index to zero
	 */
	public void restart() {
		currentFrameIndex = 0;
		elapsed = 0f;
	}

	/**
	 * Updates the animation
	 *
	 * @param delta
	 *            The time in seconds since the last update
	 */
	public void update(float delta) {
		elapsed += delta;

		if(durations.size == 0)
			return;

		float duration = durations.get(currentFrameIndex);
		while (elapsed >= duration) {
			elapsed = elapsed - duration;
			if(currentFrameIndex == frames.size - 1) {
				if(looping) {
					currentFrameIndex = 0;
				}
			} else {
				currentFrameIndex++;
			}
			duration = durations.get(currentFrameIndex);
		}
	}

	/**
	 * Draws the current frame of the animation
	 *
	 * @param g
	 *            The {@link Graphics} context available for rendering
	 */
	public void draw(Graphics g) {
		if(currentFrameIndex >= frames.size)
			return;

		T sprite = getCurrentFrame();
		sprite.setOrigin(originX, originY);
		sprite.setRotation(rotation);
		sprite.setTint(color);
		sprite.setFlip(flipX, flipY);
		g.drawSprite(sprite);
		sprite.setRotation(0f);
	}

	/**
	 * Draws the current frame of the animation
	 *
	 * @param g
	 *            The {@link Graphics} context available for rendering
	 * @param x
	 *            The x coordinate to render at
	 * @param y
	 *            The y coordinate to render at
	 */
	public void draw(Graphics g, float x, float y) {
		if(currentFrameIndex >= frames.size)
			return;

		T sprite = getCurrentFrame();
		sprite.setOrigin(originX, originY);
		sprite.setRotation(rotation);
		sprite.setTint(color);
		sprite.setFlip(flipX, flipY);
		g.drawSprite(sprite, x, y);
		sprite.setRotation(0f);
	}

	/**
	 * Returns the frame at a given index
	 * @param index The frame's index
	 * @return Null if there is no frame image
	 */
	public T getFrame(int index) {
		return frames.get(index);
	}

	/**
	 * Returns the {@link T} of the current frame
	 *
	 * @return Null if there is no frame image
	 */
	public T getCurrentFrame() {
		return frames.get(currentFrameIndex);
	}

	/**
	 * Returns the total number of frames in this animation
	 *
	 * @return A value greater than or equal to 0
	 */
	public int getNumberOfFrames() {
		return frames.size;
	}

	/**
	 * Returns the index of the current frame
	 *
	 * @return A value greater than or equal to 0
	 */
	public int getCurrentFrameIndex() {
		return currentFrameIndex;
	}

	/**
	 * Returns if the animation is looping
	 *
	 * @return True if the animation is looping
	 */
	public boolean isLooping() {
		return looping;
	}

	/**
	 * Sets if the animation should loop
	 *
	 * @param looping
	 *            False if the animation should stop when it reaches the last
	 *            frame
	 */
	public void setLooping(boolean looping) {
		this.looping = looping;
	}

	/**
	 * Increases/decreases the rotation of all frames
	 * @param degrees The amount in degrees to rotate by
	 */
	public void rotate(float degrees) {
		this.rotation += degrees;
		this.rotation = rotation % 360f;
	}

	/**
	 * Returns the rotation of all frames
	 * @return The rotation in degrees
	 */
	public float getRotation() {
		return rotation;
	}

	/**
	 * Sets the rotation of all frames
	 * @param rotation The rotation in degrees
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation % 360f;
	}

	/**
	 * Returns the {@link Color} tint applied to each frame
	 * @return
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the {@link Color} tint applied to each frame
	 * @param color
	 */
	public void setColor(Color color) {
		if(color == null)
			return;
		this.color = color;
	}

	/**
	 * Sets the {@link Color} tint applied to each frame
	 * @param r The red value (0f - 1f)
	 * @param g The green value (0f - 1f)
	 * @param b The blue value (0f - 1f)
	 * @param a The alpha value (0f - 1f)
	 */
	public void setColor(float r, float g, float b, float a) {
		if(color == null) {
			color = Mdx.graphics.newColor(r, g, b, a);
		} else {
			color.set(r, g, b, a);
		}
	}

	/**
	 * Returns the origin X coordinate to be applied to each frame
	 * @return
	 */
	public float getOriginX() {
		return originX;
	}

	/**
	 * Sets the origin X coordinate to be applied to each frame
	 * @param originX
	 */
	public void setOriginX(float originX) {
		this.originX = originX;
	}

	/**
	 * Returns the origin Y coordinate to be applied to each frame
	 * @return
	 */
	public float getOriginY() {
		return originY;
	}

	/**
	 * Sets the origin Y coordinate to be applied to each frame
	 * @param originY
	 */
	public void setOriginY(float originY) {
		this.originY = originY;
	}

	/**
	 * Sets the flipX and flipY to the current frame
	 * @param flipX
	 * @param flipY
	 */
	public void flip(boolean flipX, boolean flipY) {
		this.flipX = flipX;
		this.flipY = flipY;
	}

	/**
	 * Sets the flipX to the current frame
	 * @param flipX
	 */
	public void setFlipX(boolean flipX) {
		this.flipX = flipX;
	}

	/**
	 * Sets the flipY to the current frame
	 * @param flipY
	 */
	public void setFlipY(boolean flipY) {
		this.flipY = flipY;
	}

	/**
	 * Returns the flipX of the current frame
	 * @return
	 */
	public boolean getFlipX() {
		return flipX;
	}

	/**
	 * Returns the flipY of the current frame
	 * @return
	 */
	public boolean getFlipY() {
		return flipY;
	}

	/**
	 * Returns if this animation has finished
	 *
	 * If this is a looping animation, this method always returns false
	 *
	 * @return True if animation has finished
	 */
	public boolean isFinished() {
		if(looping) {
			return false;
		}
		if(currentFrameIndex != frames.size - 1)
			return false;
		return elapsed >= durations.get(currentFrameIndex);
	}
}

