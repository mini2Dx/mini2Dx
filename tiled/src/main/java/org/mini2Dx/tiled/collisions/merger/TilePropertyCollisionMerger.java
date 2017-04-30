/**
 * Copyright (c) 2017 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.tiled.collisions.merger;

import org.mini2Dx.tiled.Tile;
import org.mini2Dx.tiled.collisions.TiledCollisionMerger;

/**
 * An implementation of {@link TiledCollisionMerger} that merges tiles if they
 * have the same value for a certain property (or both have no value for the
 * property)
 */
public class TilePropertyCollisionMerger implements TiledCollisionMerger {
	private final String propertyName;

	public TilePropertyCollisionMerger(String propertyName) {
		super();
		this.propertyName = propertyName;
	}

	@Override
	public boolean isMergable(Tile tile1, Tile tile2) {
		if(tile1.containsProperty(propertyName)) {
			if(tile2.containsProperty(propertyName)) {
				return tile1.getProperty(propertyName).equals(tile2.getProperty(propertyName));
			}
		} else if(!tile2.containsProperty(propertyName)) {
			return true;
		}
		return false;
	}

}
