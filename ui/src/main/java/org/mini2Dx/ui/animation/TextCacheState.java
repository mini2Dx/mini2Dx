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

import org.mini2Dx.core.font.GameFont;
import org.mini2Dx.gdx.utils.Disposable;
import org.mini2Dx.gdx.utils.Queue;

import java.util.Objects;

public class TextCacheState implements Disposable {
	private static final Queue<TextCacheState> POOL = new Queue<TextCacheState>();

	public static TextCacheState allocate(GameFont gameFont, String text, float renderWidth, int hAlign) {
		final TextCacheState result;
		if(POOL.size == 0) {
			result = new TextCacheState();
		} else {
			result = POOL.removeFirst();
		}
		result.text = text;
		result.renderWidth = renderWidth;
		result.hAlign = hAlign;
		result.font = gameFont;
		return result;
	}

	public GameFont font;
	public String text;
	public float renderWidth;
	public int hAlign;

	@Override
	public void dispose() {
		font = null;
		text = null;
		renderWidth = -1f;
		hAlign = -1;
		POOL.addLast(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TextCacheState that = (TextCacheState) o;
		return Float.compare(that.renderWidth, renderWidth) == 0 &&
				hAlign == that.hAlign &&
				Objects.equals(font, that.font) &&
				Objects.equals(text, that.text);
	}

	@Override
	public int hashCode() {
		return Objects.hash(text, renderWidth, hAlign, font);
	}
}
