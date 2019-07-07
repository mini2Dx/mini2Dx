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
package org.mini2Dx.tiled.renderer;

import org.mini2Dx.tiled.TiledMap;

/**
 * Represents the rendered area of a {@link TiledMap}
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
