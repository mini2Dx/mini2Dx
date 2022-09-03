/**
 * Copyright 2022 Viridian Software Ltd.
 */
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
