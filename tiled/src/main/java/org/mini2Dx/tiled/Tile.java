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
import org.mini2Dx.core.serialization.GameDataSerializable;
import org.mini2Dx.core.serialization.GameDataSerializableUtils;
import org.mini2Dx.gdx.utils.Disposable;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.gdx.utils.Queue;
import org.mini2Dx.tiled.renderer.TileRenderer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Represents a tileset tile
 */
public class Tile implements GameDataSerializable, Disposable {
	public static int DEFAULT_PROPERTY_MAP_SIZE = 8;

	private static final int INITIAL_POOL_SIZE = 16384;
	private static final Queue<Tile> POOL = new Queue<>(INITIAL_POOL_SIZE);

	static {
		for(int i = 0; i < INITIAL_POOL_SIZE; i++) {
			POOL.addLast(new Tile());
		}
	}

	private int tileId;
	private TileRenderer tileRenderer;
	private ObjectMap<String, String> properties;

	private Tile() {}

	public static Tile create() {
		final Tile tile;
		synchronized (POOL) {
			if(POOL.size == 0) {
				tile = new Tile();
			} else {
				tile = POOL.removeFirst();
			}
		}
		return tile;
	}

	@Override
	public void writeData(DataOutputStream outputStream) throws IOException {
		outputStream.writeInt(tileId);
		outputStream.writeInt(properties == null ? 0 : properties.size);

		if(properties != null) {
			for(String key : properties.keys()) {
				outputStream.writeUTF(key);
				GameDataSerializableUtils.writeString(properties.get(key, null), outputStream);
			}
		}
	}

	@Override
	public void readData(DataInputStream inputStream) throws IOException {
		tileId = inputStream.readInt();

		final int totalProperties = inputStream.readInt();
		if(totalProperties > 0) {
			if(properties == null) {
				properties = new ObjectMap<>(DEFAULT_PROPERTY_MAP_SIZE);
			}
			for(int i = 0; i < totalProperties; i++) {
				final String key = inputStream.readUTF();
				final String value = GameDataSerializableUtils.readString(inputStream);
				properties.put(key, value);
			}
		}
	}

	public void update(float delta) {
		if (tileRenderer == null) {
			return;
		}
		tileRenderer.update(this, delta);
	}
	
	public void draw(Graphics g, int renderX, int renderY, float alpha, boolean flipH, boolean flipV, boolean flipD) {
		if (tileRenderer == null) {
			return;
		}
		tileRenderer.draw(g, this, renderX, renderY, alpha, flipH, flipV, flipD);
	}

	public void draw(Graphics g, int renderX, int renderY, float alpha) {
		if (tileRenderer == null) {
			return;
		}
		tileRenderer.draw(g, this, renderX, renderY, alpha);
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
			properties = new ObjectMap<String, String>(DEFAULT_PROPERTY_MAP_SIZE);
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
		tileId = 0;

		if(properties != null) {
			properties.clear();
		}

		if (tileRenderer != null) {
			tileRenderer.dispose();
			tileRenderer = null;
		}

		synchronized (POOL) {
			POOL.addLast(this);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tile tile = (Tile) o;
		return tileId == tile.tileId && Objects.equals(tileRenderer, tile.tileRenderer) && Objects.equals(properties, tile.properties);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tileId, tileRenderer, properties);
	}

	@Override
	public String toString() {
		return "Tile{" +
				"tileId=" + tileId +
				", tileRenderer=" + tileRenderer +
				", properties=" + properties +
				'}';
	}
}
