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
package org.mini2Dx.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.LibgdxSpriteBatchWrapper;
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
	private LibgdxSpriteBatchWrapper spriteBatch;
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
		spriteBatch = mockery.mock(LibgdxSpriteBatchWrapper.class);
		polygonSpriteBatch = mockery.mock(PolygonSpriteBatch.class);
		shapeRenderer = mockery.mock(ShapeRenderer.class);
		gdxGraphics = mockery.mock(com.badlogic.gdx.Graphics.class);

		Mdx.fonts = fonts;
		Gdx.graphics = gdxGraphics;
		
		mockery.checking(new Expectations() {
			{
				one(shapeRenderer).setAutoShapeType(true);
				one(fonts).defaultFont();
				will(returnValue(gameFont));
				one(gdxGraphics).getWidth();
				will(returnValue(800));
				one(gdxGraphics).getHeight();
				will(returnValue(600));
				one(spriteBatch).getColor();
				will(returnValue(new Color()));
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
