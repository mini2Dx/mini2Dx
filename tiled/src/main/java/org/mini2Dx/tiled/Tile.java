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
package org.mini2Dx.tiled;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.gdx.utils.Disposable;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.tiled.renderer.TileRenderer;

/**
 * Represents a tileset tile
 */
public class Tile implements Disposable {
	private int tileId;
	private TileRenderer tileRenderer;
	private ObjectMap<String, String> properties;

	public void update(float delta) {
		if (tileRenderer == null) {
			return;
		}
		tileRenderer.update(delta);
	}
	
	public void draw(Graphics g, int renderX, int renderY, boolean flipH, boolean flipV, boolean flipD) {
		if (tileRenderer == null) {
			return;
		}
		tileRenderer.draw(g, renderX, renderY, flipH, flipV, flipD);
	}

	public void draw(Graphics g, int renderX, int renderY) {
		if (tileRenderer == null) {
			return;
		}
		tileRenderer.draw(g, renderX, renderY);
	}

	/**
	 * Returns if the layer contains the specified property
	 * 
	 * @param propertyName
	 *            The property name to search for
	 * @return True if the layer contains the property
	 */
	public boolean containsProperty(String propertyName) {
		if (properties == null)
			return false;
		return properties.containsKey(propertyName);
	}

	/**
	 * Returns the value of a specified property
	 * 
	 * @param propertyName
	 *            The property name to search for
	 * @return Null if there is no such property
	 */
	public String getProperty(String propertyName) {
		if (properties == null)
			return null;
		return properties.get(propertyName);
	}

	/**
	 * Sets the value of a specified property
	 * 
	 * @param propertyName
	 *            The property name to set the value for
	 * @param value
	 *            The value of the property to set
	 */
	public void setProperty(String propertyName, String value) {
		if (properties == null)
			properties = new ObjectMap<String, String>();
		properties.put(propertyName, value);
	}

	/**
	 * Returns the properties {@link ObjectMap} of this {@link Tile}
	 * 
	 * @return Null if there are no properties
	 */
	public ObjectMap<String, String> getProperties() {
		return properties;
	}

	@Deprecated
	public int getTileId() {
		return getTileId(0);
	}

	public int getTileId(int firstGid) {
		return firstGid + tileId;
	}

	public void setTileId(int tileId) {
		this.tileId = tileId;
	}

	public TileRenderer getTileRenderer() {
		return tileRenderer;
	}

	public void setTileRenderer(TileRenderer tileRenderer) {
		this.tileRenderer = tileRenderer;
	}

	@Override
	public void dispose() {
		if (tileRenderer == null) {
			return;
		}
		tileRenderer.dispose();
		tileRenderer = null;
	}
}
