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
package org.mini2Dx.libgdx.graphics;

import com.badlogic.gdx.graphics.LibgdxTextureRegionWrapper;
import junit.framework.Assert;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.graphics.TextureRegion;

/**
 * Unit tests for {@link LibgdxTextureRegion}
 */
public class LibgdxTextureRegionTest {
	private static final int TEXTURE_WIDTH = 64;
	private static final int TEXTURE_HEIGHT = 128;
	
	private Mockery mockery;
	private LibgdxTexture texture;
	
	@Before
	public void setUp() {
		mockery = new Mockery();
		mockery.setImposteriser(ClassImposteriser.INSTANCE);
		
		texture = mockery.mock(LibgdxTexture.class);
		mockery.checking(new Expectations(){
			{
				atLeast(1).of(texture).getWidth();
				will(returnValue(TEXTURE_WIDTH));
				atLeast(1).of(texture).getHeight();
				will(returnValue(TEXTURE_HEIGHT));
			}
		});
	}
	
	@After
	public void teardown() {
		mockery.assertIsSatisfied();
	}
	
	@Test
	public void testFullRegionFromTexture() {
		TextureRegion textureRegion = new LibgdxTextureRegion(
				new LibgdxTextureRegionWrapper(texture));
		Assert.assertEquals(0, textureRegion.getRegionX());
		Assert.assertEquals(0, textureRegion.getRegionY());
		Assert.assertEquals(TEXTURE_WIDTH, textureRegion.getRegionWidth());
		Assert.assertEquals(TEXTURE_HEIGHT, textureRegion.getRegionHeight());
		Assert.assertEquals(false, textureRegion.isFlipX());
		Assert.assertEquals(false, textureRegion.isFlipY());
		Assert.assertEquals(true, textureRegion.getU() >= 0f && textureRegion.getU2() <= 1f);
		Assert.assertEquals(true, textureRegion.getV() >= 0f && textureRegion.getV2() <= 1f);
	}

	@Test
	public void testRegionFromTexture() {
		TextureRegion textureRegion = new LibgdxTextureRegion(
				new LibgdxTextureRegionWrapper(texture, 2, 2, 32, 36));
		Assert.assertEquals(2, textureRegion.getRegionX());
		Assert.assertEquals(2, textureRegion.getRegionY());
		Assert.assertEquals(32, textureRegion.getRegionWidth());
		Assert.assertEquals(36, textureRegion.getRegionHeight());
		Assert.assertEquals(false, textureRegion.isFlipX());
		Assert.assertEquals(false, textureRegion.isFlipY());
		Assert.assertEquals(true, textureRegion.getU() >= 0f && textureRegion.getU2() <= 1f);
		Assert.assertEquals(true, textureRegion.getV() >= 0f && textureRegion.getV2() <= 1f);
	}
	
	@Test
	public void testFullRegionFromTextureRegion() {
		LibgdxTextureRegion initialRegion = new LibgdxTextureRegion(
				new LibgdxTextureRegionWrapper(texture));
		LibgdxTextureRegion textureRegion = new LibgdxTextureRegion(initialRegion);
		Assert.assertEquals(0, textureRegion.getRegionX());
		Assert.assertEquals(0, textureRegion.getRegionY());
		Assert.assertEquals(TEXTURE_WIDTH, textureRegion.getRegionWidth());
		Assert.assertEquals(TEXTURE_HEIGHT, textureRegion.getRegionHeight());
		Assert.assertEquals(false, textureRegion.isFlipX());
		Assert.assertEquals(false, textureRegion.isFlipY());
		Assert.assertEquals(true, textureRegion.getU() >= 0f && textureRegion.getU2() <= 1f);
		Assert.assertEquals(true, textureRegion.getV() >= 0f && textureRegion.getV2() <= 1f);
	}
	
	@Test
	public void testRegionFromTextureRegion() {
		LibgdxTextureRegion initialRegion = new LibgdxTextureRegion(
				new LibgdxTextureRegionWrapper(texture, 2, 2, 32, 36));
		LibgdxTextureRegion textureRegion = new LibgdxTextureRegion(initialRegion, 2, 2, 30, 32);
		Assert.assertEquals(4, textureRegion.getRegionX());
		Assert.assertEquals(4, textureRegion.getRegionY());
		Assert.assertEquals(30, textureRegion.getRegionWidth());
		Assert.assertEquals(32, textureRegion.getRegionHeight());
		Assert.assertEquals(false, textureRegion.isFlipX());
		Assert.assertEquals(false, textureRegion.isFlipY());
		Assert.assertEquals(true, textureRegion.getU() >= 0f && textureRegion.getU2() <= 1f);
		Assert.assertEquals(true, textureRegion.getV() >= 0f && textureRegion.getV2() <= 1f);
	}
	
	@Test
	public void testFlipX() {
		LibgdxTextureRegion textureRegion = new LibgdxTextureRegion(
				new LibgdxTextureRegionWrapper(texture, 2, 2, 32, 36));
		Assert.assertEquals(false, textureRegion.isFlipX());
		Assert.assertEquals(false, textureRegion.isFlipY());
		
		textureRegion.flip(true, false);
		Assert.assertEquals(true, textureRegion.isFlipX());
		Assert.assertEquals(false, textureRegion.isFlipY());
		
		textureRegion.flip(true, false);
		Assert.assertEquals(false, textureRegion.isFlipX());
		Assert.assertEquals(false, textureRegion.isFlipY());
		
		textureRegion.setFlipX(true);
		Assert.assertEquals(true, textureRegion.isFlipX());
		Assert.assertEquals(false, textureRegion.isFlipY());
	}
	
	@Test
	public void testFlipY() {
		LibgdxTextureRegion textureRegion = new LibgdxTextureRegion(
				new LibgdxTextureRegionWrapper(texture, 2, 2, 32, 36));
		Assert.assertEquals(false, textureRegion.isFlipX());
		Assert.assertEquals(false, textureRegion.isFlipY());
		
		textureRegion.flip(false, true);
		Assert.assertEquals(false, textureRegion.isFlipX());
		Assert.assertEquals(true, textureRegion.isFlipY());
		
		textureRegion.flip(false, true);
		Assert.assertEquals(false, textureRegion.isFlipX());
		Assert.assertEquals(false, textureRegion.isFlipY());
		
		textureRegion.setFlipY(true);
		Assert.assertEquals(false, textureRegion.isFlipX());
		Assert.assertEquals(true, textureRegion.isFlipY());
	}
}
