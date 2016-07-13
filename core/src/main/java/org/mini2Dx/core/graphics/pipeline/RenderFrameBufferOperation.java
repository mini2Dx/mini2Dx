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
package org.mini2Dx.core.graphics.pipeline;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.util.Ref;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Renders a {@link FrameBuffer} at 0,0 during the {@link #apply} phase.
 * 
 * If you want to change the rendering coordinate, extend this class and override
 * the {@link #updateRenderPosition(Vector2, GameContainer, float)} method
 */
public class RenderFrameBufferOperation implements RenderOperation {
	private final Ref<FrameBuffer> frameBufferRef;
	private final Vector2 renderPosition = new Vector2();
	
	private FrameBuffer frameBuffer;
	private TextureFilter minFilter = TextureFilter.Nearest;
	private TextureFilter magFilter = TextureFilter.Nearest;
	private int renderX, renderY;
	
	public RenderFrameBufferOperation(Ref<FrameBuffer> frameBufferRef) {
		this.frameBufferRef = frameBufferRef;
	}
	
	public void updateRenderPosition(Vector2 position, GameContainer gc, float delta) {
	}

	@Override
	public void update(GameContainer gc, float delta) {
		updateRenderPosition(renderPosition, gc, delta);
		
		frameBuffer = frameBufferRef.get();
		renderX = MathUtils.round(renderPosition.x);
		renderY = MathUtils.round(renderPosition.y);
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
	}

	@Override
	public void apply(GameContainer gc, Graphics g) {
		if(frameBuffer == null) {
			return;
		}
		Texture texture = frameBuffer.getColorBufferTexture();
		texture.setFilter(minFilter, magFilter);
		g.drawTexture(texture, renderX, renderY);
	}

	@Override
	public void unapply(GameContainer gc, Graphics g) {
	}

	/**
	 * Returns the min {@link TextureFilter} applied to the {@link FrameBuffer} texture
	 * @return
	 */
	public TextureFilter getMinFilter() {
		return minFilter;
	}

	/**
	 * Sets the min {@link TextureFilter} applied to the {@link FrameBuffer} texture
	 * @param minFilter
	 */
	public void setMinFilter(TextureFilter minFilter) {
		if(minFilter == null) {
			return;
		}
		this.minFilter = minFilter;
	}

	/**
	 * Returns the mag {@link TextureFilter} applied to the {@link FrameBuffer} texture
	 * @return
	 */
	public TextureFilter getMagFilter() {
		return magFilter;
	}

	/**
	 * Sets the mag {@link TextureFilter} applied to the {@link FrameBuffer} texture
	 * @param magFilter
	 */
	public void setMagFilter(TextureFilter magFilter) {
		if(magFilter == null) {
			return;
		}
		this.magFilter = magFilter;
	}

}
