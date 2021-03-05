package org.mini2Dx.core.font;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.gdx.utils.Queue;

public class FontBuilderGlyph {
	private static final Queue<FontBuilderGlyph> POOL = new Queue<FontBuilderGlyph>();

	public float x;
	public float y;
	public FontBuilderGameFont.FontBuilderChar glyphChar;
	public final Color color = Mdx.graphics.newColor(0f,0f,0f, 1f);

	private FontBuilderGlyph() {
		super();
	}

	public void release() {
		x = 0f;
		y = 0f;
		glyphChar = null;

		POOL.addLast(this);
	}

	public static FontBuilderGlyph allocate() {
		if(POOL.size == 0) {
			return new FontBuilderGlyph();
		}
		final FontBuilderGlyph result = POOL.removeFirst();
		result.x = 0f;
		result.y = 0f;
		result.glyphChar = null;
		return result;
	}
}
