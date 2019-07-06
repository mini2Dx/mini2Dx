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
package com.badlogic.gdx.graphics.glutils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.libgdx.graphics.LibgdxTexture;

public class LibgdxFrameBufferWrapper extends com.badlogic.gdx.graphics.glutils.FrameBuffer {

	protected LibgdxFrameBufferWrapper(GLFrameBufferBuilder<? extends GLFrameBuffer<Texture>> bufferBuilder) {
		super(bufferBuilder);
	}

	public LibgdxFrameBufferWrapper(Pixmap.Format format, int width, int height, boolean hasDepth) {
		super(format, width, height, hasDepth);
	}

	public LibgdxFrameBufferWrapper(Pixmap.Format format, int width, int height, boolean hasDepth, boolean hasStencil) {
		super(format, width, height, hasDepth, hasStencil);
	}

	@Override
	protected Texture createTexture(FrameBufferTextureAttachmentSpec attachmentSpec) {
		GLOnlyTextureData data = new GLOnlyTextureData(bufferBuilder.width, bufferBuilder.height, 0, attachmentSpec.internalFormat, attachmentSpec.format, attachmentSpec.type);
		Texture result = new LibgdxTexture(data);
		result.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		result.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
		return result;
	}
}
