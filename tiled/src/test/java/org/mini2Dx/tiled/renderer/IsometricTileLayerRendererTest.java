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
package org.mini2Dx.tiled.renderer;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.tiled.TileLayer;
import org.mini2Dx.tiled.TiledMap;

/**
 *
 */
public class IsometricTileLayerRendererTest {
	private IsometricTileLayerRenderer renderer;
	
	private TiledMap tiledMap;
	private TileLayer tileLayer;
	private Graphics graphics;
	
	private Mockery mockery;
	
	@Before
	public void setUp() {
		mockery = new Mockery();
		mockery.setImposteriser(ClassImposteriser.INSTANCE);
		
		tiledMap = mockery.mock(TiledMap.class);
		tileLayer = mockery.mock(TileLayer.class);
		graphics = mockery.mock(Graphics.class);
		
		mockery.checking(new Expectations() {
			{
				atLeast(1).of(tiledMap).getTileWidth();
				will(returnValue(32));
				atLeast(1).of(tiledMap).getTileHeight();
				will(returnValue(32));
			}
		});
		
		renderer = new IsometricTileLayerRenderer(tiledMap, false);
	}

	@Test
	public void testRenderLayer() {
		renderer.drawLayer(graphics, tileLayer, 0, 0, 0, 0, 4, 5);
	}
}
