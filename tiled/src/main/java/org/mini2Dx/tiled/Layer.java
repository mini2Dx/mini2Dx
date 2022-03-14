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

import org.mini2Dx.core.serialization.GameDataSerializable;
import org.mini2Dx.gdx.utils.ObjectMap;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Base class for {@link TiledMap} layers
 */
public abstract class Layer implements GameDataSerializable {
	private final LayerType layerType;

	private String name;
	private int index;
	private ObjectMap<String, String> properties;
	private boolean visible;

	public Layer(LayerType layerType) {
		super();
		this.layerType = layerType;
	}

	public static Layer fromInputStream(DataInputStream inputStream) throws IOException {
		final LayerType layerType = LayerType.valueOf(inputStream.readUTF());
		switch (layerType) {
		default:
		case TILE:
			return TileLayer.fromInputStream(inputStream);
		case OBJECT:
			final TiledObjectGroup objectGroup = new TiledObjectGroup();
			objectGroup.readData(inputStream);
			return objectGroup;
		case IMAGE:
			//Not yet supported
			return null;
		case GROUP:
			return GroupLayer.fromInputStream(inputStream);
		}
	}

	/**
	 * Returns the name of this layer
	 * 
	 * @return Null if there is no name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this layer
	 * 
	 * @param name
	 *            The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the index in the {@link TiledMap} of this layer
	 * 
	 * @return The index of the layer
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the index in the {@link TiledMap} of this layer
	 * 
	 * @param index
	 *            The index of the layer
	 */
	public void setIndex(int index) {
		this.index = index;
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
	 * Returns the properties {@link ObjectMap} of this {@link Layer}
	 * @return Null if there are no properties
	 */
	public ObjectMap<String, String> getProperties() {
		return properties;
	}

	/**
	 * Returns if this {@link Layer} is visible
	 * @return True if visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets if this {@link Layer} is visible
	 * @param visible True if visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public LayerType getLayerType() {
		return layerType;
	}
}
