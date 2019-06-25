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

import org.mini2Dx.gdx.utils.ObjectMap;

/**
 * Base class for {@link TiledMap} layers
 */
public abstract class Layer {
	private final LayerType layerType;

	private String name;
	private int index;
	private ObjectMap<String, String> properties;
	private boolean visible;

	public Layer(LayerType layerType) {
		super();
		this.layerType = layerType;
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
