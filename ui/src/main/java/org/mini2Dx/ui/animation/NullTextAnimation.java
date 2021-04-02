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
package org.mini2Dx.ui.animation;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.font.GameFontCache;
/**
 * A {@link TextAnimation} that just renders the text directly
 */
public class NullTextAnimation extends BaseTextAnimation {
	private TextCacheState previousState = null;

	@Override
	public void skip() {}

	@Override
	public void onResize(GameFontCache cache, String text, float renderWidth, int hAlign) {
		boolean previouslyNull = false;
		if(previousState == null) {
			previousState = TextCacheState.allocate(cache.getFont(), text, renderWidth, hAlign);
			previouslyNull = true;
		}

		TextCacheState state = TextCacheState.allocate(cache.getFont(), text, renderWidth, hAlign);
		if(!previouslyNull && previousState.equals(state)) {
			state.dispose();
			return;
		}

		cache.clear();
		finished = false;

		previousState.dispose();
		previousState = state;
	}

	@Override
	public void update(GameFontCache cache, String text, float renderWidth, int hAlign, float delta) {
		if(!isFinished()) {
			cache.addText(text, 0f, 0f, renderWidth, hAlign, true);
		}
		setFinished(true);
	}

	@Override
	public void render(GameFontCache cache, Graphics g, int renderX, int renderY) {
		cache.setPosition(renderX, renderY);
		g.drawFontCache(cache);
	}

	@Override
	protected void resetState() {}
}
