/**
 * Copyright (c) 2013, mini2Dx Project
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

import com.badlogic.gdx.graphics.Color;

/**
 * A common interface to notify {@link TiledParserListener}s
 * 
 * @author Thomas Cashman
 */
public interface TiledParserNotifier {
	/**
	 * Adds a listener to be notified of parsing results
	 * 
	 * @param listener
	 *            The {@link TiledParserListener} to be added
	 */
	public void addListener(TiledParserListener tiledParserListener);

	/**
	 * Removes a listener from being notified of parsing results
	 * 
	 * @param listener
	 *            The {@link TiledParserListener} to be removed
	 */
	public void removeListener(TiledParserListener tiledParserListener);

	/**
	 * Notify all {@link TiledParserListener}s that parsing has begun
	 * @param orientation The map orientation
	 * @param backgroundColor The map background color
	 * @param width
	 * @param height
	 * @param tileWidth
	 * @param tileHeight
	 */
	public void notifyBeginParsing(String orientation, Color backgroundColor,
			int width, int height, int tileWidth, int tileHeight);

	public void notifyMapPropertyParsed(String propertyName, String value);

	public void notifyTilePropertyParsed(Tile tile);

	public void notifyTilesetParsed(Tileset parsedTileset);

	public void notifyTileLayerParsed(TileLayer parsedLayer);

	public void notifyObjectGroupParsed(TiledObjectGroup parsedObjectGroup);
}
