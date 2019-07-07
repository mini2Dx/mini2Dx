/**
 * Copyright (c) 2016 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.libgdx.graphics;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.graphics.Texture;

import junit.framework.Assert;
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
				new com.badlogic.gdx.graphics.g2d.TextureRegion(texture));
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
				new com.badlogic.gdx.graphics.g2d.TextureRegion(texture, 2, 2, 32, 36));
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
				new com.badlogic.gdx.graphics.g2d.TextureRegion(texture));
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
				new com.badlogic.gdx.graphics.g2d.TextureRegion(texture, 2, 2, 32, 36));
		LibgdxTextureRegion textureRegion = new LibgdxTextureRegion(initialRegion);
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
				new com.badlogic.gdx.graphics.g2d.TextureRegion(texture, 2, 2, 32, 36));
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
				new com.badlogic.gdx.graphics.g2d.TextureRegion(texture, 2, 2, 32, 36));
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
