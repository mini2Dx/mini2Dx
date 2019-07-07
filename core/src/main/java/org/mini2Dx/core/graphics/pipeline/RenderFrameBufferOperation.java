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
package org.mini2Dx.core.graphics.pipeline;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.FrameBuffer;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.util.Ref;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.math.Vector2;

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
	//private TextureFilter minFilter = TextureFilter.Nearest;
	//private TextureFilter magFilter = TextureFilter.Nearest;
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
		Texture texture = frameBuffer.getTexture();
		//texture.setFilter(minFilter, magFilter);
		g.drawTexture(texture, renderX, renderY);
	}

	@Override
	public void unapply(GameContainer gc, Graphics g) {
	}
}
