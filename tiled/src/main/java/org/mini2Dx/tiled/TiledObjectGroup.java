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

import org.mini2Dx.gdx.utils.Array;

/**
 * Represents a group of {@link TiledObject}s loaded from a {@link TiledMap}
 */
public class TiledObjectGroup extends Layer {
	private int width, height;
	private Array<TiledObject> objects;
	
	/**
	 * Constructor
	 */
	public TiledObjectGroup() {
		super(LayerType.OBJECT);
		objects = new Array<TiledObject>(true, 2, TiledObject.class);
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
}
