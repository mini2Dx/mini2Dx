/**
 * Copyright (c) 2013, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.graphics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Implements an animation with frames of variable or fixed duration
 * 
 * @author Thomas Cashman
 */
public class Animation<T extends Sprite> {
	private List<T> frames;
	private List<Float> durations;
	private int currentFrameIndex;
	private float elapsed;
	private boolean looping;
	private float rotation;

	/**
	 * Constructor
	 */
	public Animation() {
		currentFrameIndex = 0;
		frames = new ArrayList<T>();
		durations = new ArrayList<Float>();
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
	}

	/**
	 * Removes a frame from the animation
	 * 
	 * @param index
	 *            The index of the frame to be removed
	 */
	public void removeFrame(int index) {
		durations.remove(index);
		frames.remove(index);
	}

	/**
	 * Restarts the animation. This is the equivalent to setting the current
	 * frame index to zero
	 */
	public void restart() {
		currentFrameIndex = 0;
	}

	/**
	 * Updates the animation
	 * 
	 * @param delta
	 *            The time in seconds since the last update
	 */
	public void update(float delta) {
		elapsed += delta;
		float duration = durations.get(currentFrameIndex);
		while (elapsed >= duration) {
			elapsed = elapsed - duration;
			if(currentFrameIndex == frames.size() - 1) {
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
	 * @param x
	 *            The x coordinate to render at
	 * @param y
	 *            The y coordinate to render at
	 */
	public void draw(Graphics g, float x, float y) {
		T sprite = getCurrentFrame();
		sprite.setRotation(rotation);
		g.drawSprite(sprite, x, y);
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
		return frames.size();
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
}
