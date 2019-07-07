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
import org.mini2Dx.core.util.Ref;

/**
 * Ends a {@link FrameBuffer} during the {@link #apply} phase
 */
public class EndFrameBufferOperation implements RenderOperation {
	private final Ref<FrameBuffer> frameBufferRef;
	
	private FrameBuffer frameBuffer;
	
	public EndFrameBufferOperation(Ref<FrameBuffer> frameBufferRef) {
		this.frameBufferRef = frameBufferRef;
	}

	@Override
	public void update(GameContainer gc, float delta) {
		frameBuffer = frameBufferRef.get();
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
	}

	@Override
	public void apply(GameContainer gc, Graphics g) {
		if(frameBuffer == null) {
			return;
		}
		g.flush();
		frameBuffer.end();
	}

	@Override
	public void unapply(GameContainer gc, Graphics g) {
	}

}
