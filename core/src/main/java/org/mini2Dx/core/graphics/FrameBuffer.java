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

import org.mini2Dx.gdx.utils.Disposable;

/**
 * Base interface for frame buffer implementations. {@link #dispose()} must be called when it is no longer needed.
 */
public interface FrameBuffer extends Disposable {
	/**
	 * Calls {@link #bind()} and sets up the viewport
	 */
	public void begin();

	/**
	 * Calls {@link #unbind()} and restores the viewport to its default state
	 */
	public void end();

	/**
	 * Binds the frame buffer so that everything gets rendered to it
	 */
	public void bind();

	/**
	 * Unbinds the frame buffer so that everything is rendered to the default buffer
	 */
	public void unbind();

	/**
	 * Returns the width of the frame buffer
	 * @return The width in pixels
	 */
	public int getWidth();

	/**
	 * Returns the height of the frame buffer
	 * @return The height in pixels
	 */
	public int getHeight();

	/**
	 * Returns the texture containing this frame buffer's data.
	 * @return The texture containing this frame buffer's data.
	 */
	public Texture getTexture();
}
