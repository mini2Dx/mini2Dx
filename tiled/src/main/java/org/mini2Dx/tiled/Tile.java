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
