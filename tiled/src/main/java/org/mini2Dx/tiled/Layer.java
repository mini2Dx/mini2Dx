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
import org.mini2Dx.core.serialization.GameDataSerializableUtils;
import org.mini2Dx.gdx.utils.ObjectMap;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

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
			final GroupLayer groupLayer = new GroupLayer();
			groupLayer.readData(inputStream);
			return groupLayer;
		}
	}

	@Override
	public void writeData(DataOutputStream outputStream) throws IOException {
		outputStream.writeUTF(layerType.name());
		GameDataSerializableUtils.writeString(name, outputStream);
		outputStream.writeInt(index);
		outputStream.writeBoolean(visible);

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
		name = GameDataSerializableUtils.readString(inputStream);
		index = inputStream.readInt();
		visible = inputStream.readBoolean();

		final int totalProperties = inputStream.readInt();
		if(totalProperties > 0) {
			properties = new ObjectMap<>();
			for(int i = 0; i < totalProperties; i++) {
				final String key = inputStream.readUTF();
				final String value = GameDataSerializableUtils.readString(inputStream);
				properties.put(key, value);
			}
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Layer layer = (Layer) o;
		return index == layer.index && visible == layer.visible && layerType == layer.layerType && Objects.equals(name, layer.name) && Objects.equals(properties, layer.properties);
	}

	@Override
	public int hashCode() {
		return Objects.hash(layerType, name, index, properties, visible);
	}

	@Override
	public String toString() {
		return "Layer{" +
				"layerType=" + layerType +
				", name='" + name + '\'' +
				", index=" + index +
				", properties=" + properties +
				", visible=" + visible +
				'}';
	}
}
