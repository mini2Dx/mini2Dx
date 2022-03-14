/**
 * Copyright (c) 2019 See AUTHORS file
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

import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.ObjectMap;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Represents a group layer per the Tiled specification
 */
public class GroupLayer extends Layer implements TiledLayerParserListener {
	protected final Array<Layer> layers = new Array<Layer>(true, 2, Layer.class);
	protected final ObjectMap<String, TiledObjectGroup> objectGroups = new ObjectMap<String, TiledObjectGroup>();

	public GroupLayer() {
		super(LayerType.GROUP);
	}

	@Override
	public void writeData(DataOutputStream outputStream) throws IOException {
		outputStream.writeInt(layers.size);
		for(int i = 0; i < layers.size; i++) {
			outputStream.writeUTF(layers.get(i).getLayerType().name());
			layers.get(i).writeData(outputStream);
		}
	}

	@Override
	public void readData(DataInputStream inputStream) throws IOException {
		final int totalLayers = inputStream.readInt();
		for(int i = 0; i < totalLayers; i++) {
			final Layer layer = Layer.fromInputStream(inputStream);
			switch (layer.getLayerType()) {
			default:
			case TILE:
				onTileLayerParsed((TileLayer) layer);
				break;
			case OBJECT:
				onObjectGroupParsed((TiledObjectGroup) layer);
				break;
			case IMAGE:
				break;
			case GROUP:
				onGroupLayerParsed((GroupLayer) layer);
				break;
			}
		}
	}

	/**
	 * Returns the {@link TileLayer} with the given name
	 *
	 * @param name The name to search for
	 * @return Null if there is no such {@link TileLayer}
	 */
	public TileLayer getTileLayer(String name) {
		return getTileLayer(name, true);
	}

	/**
	 * Returns the {@link TileLayer} with the given name
	 *
	 * @param name The name to search for
	 * @param recursive False if only the group's immediate child layers should be searched (ignoring descendants)
	 * @return Null if there is no such {@link TileLayer}
	 */
	public TileLayer getTileLayer(String name, boolean recursive) {
		return TiledMapData.getTileLayer(layers, name, recursive);
	}

	/**
	 * Returns the {@link GroupLayer} with the given name
	 * @param name The name of the layer
	 * @return Null if the layer does not exist
	 */
	public GroupLayer getGroupLayer(String name) {
		return getGroupLayer(name, true);
	}

	/**
	 * Returns the {@link GroupLayer} with the given name
	 * @param name The name of the layer
	 * @param recursive False if only the group's immediate child layers should be searched (ignoring descendants)
	 * @return Null if the layer does not exist
	 */
	public GroupLayer getGroupLayer(String name, boolean recursive) {
		return TiledMapData.getGroupLayer(layers, name, recursive);
	}

	/**
	 * Returns the {@link TiledObjectGroup} with the given name
	 *
	 * @param name
	 *            The name to search for
	 * @return Null if there is no such {@link TiledObjectGroup}
	 */
	public TiledObjectGroup getObjectGroup(String name) {
		return getObjectGroup(name, true);
	}

	/**
	 * Returns the {@link TiledObjectGroup} with the given name
	 *
	 * @param name The name to search for
	 * @param recursive False if only the immediate layers should be searched (ignoring descendants)
	 * @return Null if there is no such {@link TiledObjectGroup}
	 */
	public TiledObjectGroup getObjectGroup(String name, boolean recursive) {
		return TiledMapData.getObjectGroup(layers, objectGroups, name, recursive);
	}

	/**
	 * Returns the {@link TileLayer} at the given index
	 *
	 * @param index
	 *            The index of the layer
	 * @return Null if the index is out of bounds
	 */
	public TileLayer getTileLayer(int index) {
		if (index < 0 || index >= layers.size) {
			return null;
		}
		return (TileLayer) layers.get(index);
	}

	/**
	 * Returns the {@link GroupLayer} at the given index
	 * @param index The index of the layer
	 * @return Null if the index is out of bounds
	 */
	public GroupLayer getGroupLayer(int index) {
		if (index < 0 || index >= layers.size) {
			return null;
		}
		return (GroupLayer) layers.get(index);
	}

	@Override
	public void onTileLayerParsed(TileLayer parsedLayer) {
		parsedLayer.setIndex(layers.size);
		layers.add(parsedLayer);
	}

	@Override
	public void onObjectGroupParsed(TiledObjectGroup parsedObjectGroup) {
		parsedObjectGroup.setIndex(layers.size);
		layers.add(parsedObjectGroup);
		objectGroups.put(parsedObjectGroup.getName(), parsedObjectGroup);
	}

	@Override
	public void onGroupLayerParsed(GroupLayer parsedLayer) {
		parsedLayer.setIndex(layers.size);
		layers.add(parsedLayer);
	}

	public Array<Layer> getLayers() {
		return layers;
	}
}
