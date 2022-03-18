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

import org.mini2Dx.gdx.utils.Array;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Represents a group of {@link TiledObject}s loaded from a {@link TiledMap}
 */
public class TiledObjectGroup extends Layer {
	private int width, height;
	private final Array<TiledObject> objects;
	
	/**
	 * Constructor
	 */
	public TiledObjectGroup() {
		super(LayerType.OBJECT);
		objects = new Array<TiledObject>(true, 2, TiledObject.class);
	}

	@Override
	public void writeData(DataOutputStream outputStream) throws IOException {
		super.writeData(outputStream);

		outputStream.writeInt(width);
		outputStream.writeInt(height);

		outputStream.writeInt(objects.size);
		for(int i = 0; i < objects.size; i++) {
			objects.get(i).writeData(outputStream);
		}
	}

	@Override
	public void readData(DataInputStream inputStream) throws IOException {
		super.readData(inputStream);

		width = inputStream.readInt();
		height = inputStream.readInt();

		final int totalObjects = inputStream.readInt();
		for(int i = 0; i < totalObjects; i++) {
			objects.add(TiledObject.fromInputStream(inputStream));
		}
	}

	/**
	 * Returns the width of the group in tiles
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Sets the width of the group in tiles
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * Returns the height of the group in tiles
	 * @return
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Sets the height of the group in tiles
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Returns the objects within this group
	 * @return An empty list if none
	 */
	public Array<TiledObject> getObjects() {
		return objects;
	}

	/**
	 * Returns the {@link TiledObject} with the specified ID
	 * @param id The ID to search for
	 * @return Null if this layer does not contain an object with the specified ID
	 */
	public TiledObject getObjectById(int id) {
		for(int i = 0; i < objects.size; i++) {
			final TiledObject tiledObject = objects.get(i);
			if(tiledObject.getId() < 0) {
				continue;
			}
			if(tiledObject.getId() != id) {
				continue;
			}
			return tiledObject;
		}
		return null;
	}

	/**
	 * Returns the {@link TiledObject} with the specified name
	 * @param name The name to search for
	 * @return Null if this layer does not contain an object with the specified name
	 */
	public TiledObject getObjectByName(String name) {
		if(name == null) {
			return null;
		}
		for(int i = 0; i < objects.size; i++) {
			final TiledObject tiledObject = objects.get(i);
			if(tiledObject.getName() == null) {
				continue;
			}
			if(!tiledObject.getName().equals(name)) {
				continue;
			}
			return tiledObject;
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		TiledObjectGroup that = (TiledObjectGroup) o;
		return width == that.width && height == that.height && Objects.equals(objects, that.objects);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), width, height, objects);
	}

	@Override
	public String toString() {
		return "TiledObjectGroup{" +
				"width=" + width +
				", height=" + height +
				", objects=" + objects +
				"} " + super.toString();
	}
}
