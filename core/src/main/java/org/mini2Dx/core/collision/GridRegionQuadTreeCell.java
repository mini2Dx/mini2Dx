/**
 * Copyright 2022 Viridian Software Ltd.
 */
package org.mini2Dx.core.collision;

import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.Shape;
import org.mini2Dx.core.geom.Sizeable;
import org.mini2Dx.gdx.utils.Array;

public class GridRegionQuadTreeCell<T extends Sizeable> extends RegionQuadTree<T> {
	private final GridRegionQuadTree<T> parent;
	private final int index;

	public GridRegionQuadTreeCell(GridRegionQuadTree parent, int index, float minimumQuadWidth, float minimumQuadHeight,
								  int elementLimitPerQuad, float x, float y, float width, float height) {
		super(minimumQuadWidth, minimumQuadHeight, elementLimitPerQuad, -1, x, y, width, height);
		this.index = index;
		this.parent = parent;
		this.pool = parent.pool;
	}

	@Override
	protected boolean removeElement(T element, boolean clearQuadRef) {
		final boolean result = super.removeElement(element, clearQuadRef);
		if(result) {
			parent.dirtyCells.set(index);
		}
		return result;
	}

	@Override
	public void positionChanged(T moved) {
		if (this.contains(moved.getCenterX(), moved.getCenterY()))
			return;

		removeElement(moved, true);

		QuadTree<T> parentQuad = this.parent;
		while (parentQuad != null) {
			if (parentQuad.add(moved)) {
				return;
			}
			parentQuad = parentQuad.getParent();
		}
	}
}
