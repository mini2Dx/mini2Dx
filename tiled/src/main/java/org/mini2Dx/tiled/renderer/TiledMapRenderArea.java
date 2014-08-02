/**
 * Copyright (c) 2014, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.tiled.renderer;

import org.mini2Dx.tiled.TiledMap;

/**
 * Represents the rendered area of a {@link TiledMap}
 * @author Thomas Cashman
 */
public class TiledMapRenderArea {
	private int startTileX;
	private int startTileY;
	private int widthInTiles;
	private int heightInTiles;

	public void set(int startTileX, int startTileY, int widthInTiles,
			int heightInTiles) {
		this.startTileX = startTileX;
		this.startTileY = startTileY;
		this.widthInTiles = widthInTiles;
		this.heightInTiles = heightInTiles;
	}

	public int getStartTileX() {
		return startTileX;
	}

	public void setStartTileX(int startTileX) {
		this.startTileX = startTileX;
	}

	public int getStartTileY() {
		return startTileY;
	}

	public void setStartTileY(int startTileY) {
		this.startTileY = startTileY;
	}

	public int getWidthInTiles() {
		return widthInTiles;
	}

	public void setWidthInTiles(int widthInTiles) {
		this.widthInTiles = widthInTiles;
	}

	public int getHeightInTiles() {
		return heightInTiles;
	}

	public void setHeightInTiles(int heightInTiles) {
		this.heightInTiles = heightInTiles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + heightInTiles;
		result = prime * result + startTileX;
		result = prime * result + startTileY;
		result = prime * result + widthInTiles;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TiledMapRenderArea other = (TiledMapRenderArea) obj;
		if (heightInTiles != other.heightInTiles)
			return false;
		if (startTileX != other.startTileX)
			return false;
		if (startTileY != other.startTileY)
			return false;
		if (widthInTiles != other.widthInTiles)
			return false;
		return true;
	}
}
