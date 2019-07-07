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
package org.mini2Dx.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import junit.framework.Assert;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.Fonts;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.font.GameFont;
import org.mini2Dx.libgdx.game.GameWrapper;

/**
 * Unit tests for {@link LibgdxGraphics}
 */
public class LibgdxGraphicsTest {
	private Mockery mockery;
	
	private GameWrapper gameWrapper;
	private SpriteBatch spriteBatch;
	private PolygonSpriteBatch polygonSpriteBatch;
	private ShapeRenderer shapeRenderer;
	private GameFont gameFont;
	private com.badlogic.gdx.Graphics gdxGraphics;

	private Fonts fonts;
	private Graphics graphics;
	
	@Before
	public void setup() {
		mockery = new Mockery();
		mockery.setImposteriser(ClassImposteriser.INSTANCE);
		fonts = mockery.mock(Fonts.class);
		gameFont = mockery.mock(GameFont.class);
		gameWrapper = mockery.mock(GameWrapper.class);
		spriteBatch = mockery.mock(SpriteBatch.class);
		polygonSpriteBatch = mockery.mock(PolygonSpriteBatch.class);
		shapeRenderer = mockery.mock(ShapeRenderer.class);
		gdxGraphics = mockery.mock(com.badlogic.gdx.Graphics.class);

		Mdx.fonts = fonts;
		Gdx.graphics = gdxGraphics;
		
		mockery.checking(new Expectations() {
			{
				one(fonts).defaultFont();
				will(returnValue(gameFont));
				one(gdxGraphics).getWidth();
				will(returnValue(800));
				one(gdxGraphics).getHeight();
				will(returnValue(600));
				one(spriteBatch).getColor();
				will(returnValue(null));
			}
		});
		
		graphics = new LibgdxGraphics(gameWrapper, spriteBatch, polygonSpriteBatch, shapeRenderer);
	}

	@Test
	public void testGetLineHeightNeverNull() {
		Assert.assertEquals(1, graphics.getLineHeight());
		
		/* Line height should never be less than 1 */
		graphics.setLineHeight(0);
		Assert.assertEquals(1, graphics.getLineHeight());
		
		graphics.setLineHeight(2);
		Assert.assertEquals(2, graphics.getLineHeight());
	}

	@Test
	public void testGetColorNeverNull() {
		Assert.assertNotNull(graphics.getColor());

		/* Foreground color should never be null */
		graphics.setColor(null);
		Assert.assertNotNull(graphics.getColor());
	}

	@Test
	public void testGetBackgroundColorNeverNull() {
		Assert.assertNotNull(graphics.getBackgroundColor());

		/* Background color should never be null */
		graphics.setBackgroundColor(null);
		Assert.assertNotNull(graphics.getBackgroundColor());
	}

}
