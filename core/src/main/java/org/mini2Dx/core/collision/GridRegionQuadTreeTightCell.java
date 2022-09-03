/**
 * Copyright 2022 Viridian Software Ltd.
 */
package org.mini2Dx.core.collision;

import org.mini2Dx.core.geom.Sizeable;
import org.mini2Dx.gdx.utils.Array;

public class GridRegionQuadTreeTightCell<T extends Sizeable> {
	public static final int INITIAL_CAPACITY = 8;
	private final Array<GridRegionQuadTreeLooseCell<T>> cells = new Array<>(false, INITIAL_CAPACITY);

	public void addCell(GridRegionQuadTreeLooseCell<T> cell) {
		cells.add(cell);
	}

	public void removeCell(GridRegionQuadTreeLooseCell<T> cell) {
		cells.removeValue(cell, true);
	}

	public Array<GridRegionQuadTreeLooseCell<T>> getCells() {
		return cells;
	}
}
