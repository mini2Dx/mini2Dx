/**
 * Copyright 2022 Viridian Software Ltd.
 */
package org.mini2Dx.core.collision;

import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.Shape;
import org.mini2Dx.core.geom.Sizeable;
import org.mini2Dx.gdx.utils.Array;

public class GridRegionQuadTreeSmallCell<T extends Sizeable> extends RegionQuadTree<T> {
	private final GridRegionQuadTreeLargeCell<T> parent;

	public GridRegionQuadTreeSmallCell(GridRegionQuadTreeLargeCell parent, float minimumQuadWidth, float minimumQuadHeight,
	                                   int elementLimitPerQuad, float x, float y, float width, float height) {
		super(minimumQuadWidth, minimumQuadHeight, elementLimitPerQuad, -1, x, y, width, height);
		this.parent = parent;

		parent.add(this);
	}

	@Override
	protected void getElementsWithinAreaUpwards(Array<T> result, Shape area, boolean firstInvocation, boolean childNodeCrossed) {
		if (elements != null) {
			addElementsWithinArea(result, area);
		}

		boolean nodeCrossed = false;

		if (firstInvocation && topLeft != null){
			if (topLeft.isSearchRequired()){
				if(area.contains(topLeft)) {
					topLeft.getElementsWithinArea(result, area, true);
				} else if(area.intersects(topLeft)) {
					topLeft.getElementsWithinArea(result, area, false);
				}
			}
			if (topRight.isSearchRequired()){
				if(area.contains(topRight)) {
					topRight.getElementsWithinArea(result, area, true);
				} else if(area.intersects(topRight)) {
					topRight.getElementsWithinArea(result, area, false);
				}
			}
			if (bottomLeft.isSearchRequired()){
				if(area.contains(bottomLeft)) {
					bottomLeft.getElementsWithinArea(result, area, true);
				} else if(area.intersects(bottomLeft)) {
					bottomLeft.getElementsWithinArea(result, area, false);
				}
			}
			if (bottomRight.isSearchRequired()){
				if(area.contains(bottomRight)) {
					bottomRight.getElementsWithinArea(result, area, true);
				} else if(area.intersects(bottomRight)) {
					bottomRight.getElementsWithinArea(result, area, false);
				}
			}
			nodeCrossed = true;
		}
		if (parent == null) {
			return;
		}

		if(childNodeCrossed || firstInvocation) {
			if (!this.contains(area)) {
				parent.getElementsWithinAreaExcluding(this, result, area);
			}
		}

		parent.getElementsWithinAreaUpwards(result, area, false, nodeCrossed);
	}

	@Override
	protected void getElementsWithinAreaIgnoringEdgesUpwards(Array<T> result, Shape area, boolean firstInvocation) {
		super.getElementsWithinAreaIgnoringEdgesUpwards(result, area, firstInvocation);
	}

	@Override
	protected void getElementsContainingAreaUpwards(Array<T> result, Shape area, boolean firstInvocation, boolean entirelyContained) {
		super.getElementsContainingAreaUpwards(result, area, firstInvocation, entirelyContained);
	}

	@Override
	protected void getElementsContainingPointUpwards(Array<T> result, Point point, boolean firstInvocation) {
		super.getElementsContainingPointUpwards(result, point, firstInvocation);
	}

	@Override
	protected void addElementsIntersectingLineSegmentUpwards(Array<T> result, LineSegment lineSegment, boolean firstInvocation) {
		super.addElementsIntersectingLineSegmentUpwards(result, lineSegment, firstInvocation);
	}

	@Override
	public void positionChanged(T moved) {
		if (this.contains(moved))
			return;

		removeElement(moved);

		QuadTree<T> parentQuad = this.parent;
		while (parentQuad != null) {
			if (parentQuad.add(moved)) {
				return;
			}
			parentQuad = parentQuad.getParent();
		}
	}
}
