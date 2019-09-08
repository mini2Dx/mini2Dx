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
package org.mini2Dx.libgdx.graphics;

import com.badlogic.gdx.graphics.Pixmap;
import org.mini2Dx.core.graphics.FrameBuffer;
import org.mini2Dx.core.graphics.Texture;

public class LibgdxFrameBuffer implements FrameBuffer {
	public final com.badlogic.gdx.graphics.glutils.LibgdxFrameBufferWrapper frameBuffer;
	public LibgdxTexture texture = null;

	public LibgdxFrameBuffer(int width, int height) {
		this.frameBuffer = new com.badlogic.gdx.graphics.glutils.LibgdxFrameBufferWrapper(
				Pixmap.Format.RGBA8888, width, height, true);
	}

	@Override
	public void begin() {
		frameBuffer.begin();
	}

	@Override
	public void end() {
		frameBuffer.end();
	}

	@Override
	public void bind() {
		frameBuffer.bind();
	}

	@Override
	public void unbind() {
		frameBuffer.unbind();
	}

	@Override
	public int getWidth() {
		return frameBuffer.getWidth();
	}

	@Override
	public int getHeight() {
		return frameBuffer.getHeight();
	}

	@Override
	public Texture getTexture() {
		return (LibgdxTexture) frameBuffer.getColorBufferTexture();
	}

	@Override
	public void dispose() {
		frameBuffer.dispose();
	}
}
