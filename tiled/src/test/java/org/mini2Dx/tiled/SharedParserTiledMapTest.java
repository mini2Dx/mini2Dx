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
package org.mini2Dx.tiled;

import org.junit.Test;
import org.mini2Dx.tiled.exception.TiledException;

import com.badlogic.gdx.files.FileHandle;

import junit.framework.Assert;

/**
 * Unit tests for {@link TiledMap} instances sharing the same {@link TiledParser}
 */
public class SharedParserTiledMapTest {
	
	@Test
	public void testSharedParser() throws TiledException  {
		TiledParser parser = new TiledParser();
		
		FileHandle orthogonalFile = new FileHandle(Thread.currentThread()
				.getContextClassLoader().getResource("orthogonal.tmx").getFile());
		FileHandle orthogonalTsxFile = new FileHandle(Thread.currentThread()
				.getContextClassLoader().getResource("orthogonal_tsx.tmx").getFile());
		FileHandle isometricFile = new FileHandle(Thread.currentThread()
				.getContextClassLoader().getResource("isometric.tmx").getFile());
		
		TiledMap orthogonalTiledMap = new TiledMap(parser, orthogonalFile, false, false);
		TiledMap orthogonalTsxTiledMap = new TiledMap(parser, orthogonalTsxFile, false, false);
		TiledMap isometricTiledMap = new TiledMap(parser, isometricFile, false, false);
		
		Assert.assertEquals(Orientation.ORTHOGONAL, orthogonalTiledMap.getOrientation());
		Assert.assertEquals(Orientation.ISOMETRIC, isometricTiledMap.getOrientation());
		
		Assert.assertEquals(Orientation.ORTHOGONAL, orthogonalTsxTiledMap.getOrientation());
		Assert.assertEquals(1, orthogonalTsxTiledMap.getTilesets().size);
	}
}
