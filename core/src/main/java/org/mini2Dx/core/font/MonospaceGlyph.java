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
package org.mini2Dx.core.font;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.gdx.utils.Queue;

public class MonospaceGlyph {
	private static final Queue<MonospaceGlyph> POOL = new Queue<MonospaceGlyph>();

	public float x;
	public float y;
	public char glyphChar;
	public TextureRegion textureRegion;
	public final Color color = Mdx.graphics.newColor(0f,0f,0f, 1f);

	private MonospaceGlyph() {
		super();
	}

	public void release() {
		x = 0f;
		y = 0f;
		textureRegion = null;

		synchronized(POOL) {
			POOL.addLast(this);
		}
	}

	public static MonospaceGlyph allocate() {
		final MonospaceGlyph result;
		synchronized(POOL) {
			if (POOL.size == 0) {
				result = new MonospaceGlyph();
			} else {
				result = POOL.removeFirst();
			}
		}
		result.x = 0f;
		result.y = 0f;
		result.textureRegion = null;
		return result;
	}
}
