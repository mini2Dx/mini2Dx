/*******************************************************************************
 * Copyright 2022 See AUTHORS file
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
package org.mini2Dx.core.collision;

import org.mini2Dx.core.geom.Sizeable;
import org.mini2Dx.gdx.utils.IntMap;

public class GridRegionQuadTreeTightCell<T extends Sizeable> {
	public static final int INITIAL_CAPACITY = 8;
	private final IntMap<GridRegionQuadTreeLooseCell<T>> cells = new IntMap<>(INITIAL_CAPACITY);
	private final int index;

	public GridRegionQuadTreeTightCell(int index) {
		this.index = index;
	}

	public void addCell(GridRegionQuadTreeLooseCell<T> cell) {
		cells.put(cell.index, cell);
	}

	public void removeCell(GridRegionQuadTreeLooseCell<T> cell) {
		cells.remove(cell.index);
	}

	public IntMap.Values<GridRegionQuadTreeLooseCell<T>> getCells() {
		return cells.values();
	}

	public void clear() {
		cells.clear();
	}
}
