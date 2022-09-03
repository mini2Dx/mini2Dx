/**
 * Copyright 2022 Viridian Software Ltd.
 */
package org.mini2Dx.core.collision;

import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.Shape;
import org.mini2Dx.core.geom.Sizeable;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.IntMap;

public class GridRegionQuadTreeLooseCell<T extends Sizeable> extends RegionQuadTree<T> {
	protected final GridRegionQuadTree<T> parent;
	protected final int index;
	protected final IntMap<GridRegionQuadTreeTightCell<T>> tightCells = new IntMap<>();

	public GridRegionQuadTreeLooseCell(GridRegionQuadTree parent, int index, float minimumQuadWidth, float minimumQuadHeight,
	                                   int elementLimitPerQuad, float x, float y, float width, float height) {
		super(minimumQuadWidth, minimumQuadHeight, elementLimitPerQuad, -1, x, y, width, height);
		this.index = index;
		this.parent = parent;
		this.pool = parent.pool;
	}

	@Override
	protected void disposeBounds() {
		for(GridRegionQuadTreeTightCell<T> tightCell : tightCells.values()) {
			tightCell.removeCell(this);
		}
		tightCells.clear();
	}

	protected void updateTightGrid() {
		if(elementsBounds == null) {
			return;
		}
		final int minX = MathUtils.floor(elementsBounds.getX() * parent.invCellWidth) - parent.gridWorldX;
		final int minY = MathUtils.floor(elementsBounds.getY() * parent.invCellHeight) - parent.gridWorldY;
		final int maxX = MathUtils.floor(elementsBounds.getMaxX() * parent.invCellWidth) - parent.gridWorldX;
		final int maxY = MathUtils.floor(elementsBounds.getMaxY() * parent.invCellHeight) - parent.gridWorldY;

		for(int gy = minY; gy <= maxY; gy++) {
			for (int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * parent.totalCellsX) + gx;
				parent.getTightCell(index).addCell(this);
			}
		}
	}

	@Override
	protected boolean updateBounds(T element) {
		if(super.updateBounds(element)) {
			updateTightGrid();
			return true;
		}
		return false;
	}

	@Override
	protected boolean updateBounds() {
		if(super.updateBounds()) {
			updateTightGrid();
			return true;
		}
		return false;
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
	protected void getElementsWithinAreaUpwards(Array<T> result, Shape area, boolean firstInvocation, boolean childNodeCrossed) {
		if (elements != null) {
			addElementsWithinArea(result, area);
		}

		if (firstInvocation && topLeft != null){
			if (topLeft.isSearchRequired()){
				if(area.contains(topLeft.elementsBounds)) {
					topLeft.getElementsWithinArea(result, area, true);
				} else if(area.intersects(topLeft.elementsBounds)) {
					topLeft.getElementsWithinArea(result, area, false);
				} else if(topLeft.elementsBounds.contains(area)) {
					topLeft.getElementsWithinArea(result, area, false);
				}
			}
			if (topRight.isSearchRequired()){
				if(area.contains(topRight.elementsBounds)) {
					topRight.getElementsWithinArea(result, area, true);
				} else if(area.intersects(topRight.elementsBounds)) {
					topRight.getElementsWithinArea(result, area, false);
				} else if(topRight.elementsBounds.contains(area)) {
					topRight.getElementsWithinArea(result, area, false);
				}
			}
			if (bottomLeft.isSearchRequired()){
				if(area.contains(bottomLeft.elementsBounds)) {
					bottomLeft.getElementsWithinArea(result, area, true);
				} else if(area.intersects(bottomLeft.elementsBounds)) {
					bottomLeft.getElementsWithinArea(result, area, false);
				} else if(bottomLeft.elementsBounds.contains(area)) {
					bottomLeft.getElementsWithinArea(result, area, false);
				}
			}
			if (bottomRight.isSearchRequired()){
				if(area.contains(bottomRight.elementsBounds)) {
					bottomRight.getElementsWithinArea(result, area, true);
				} else if(area.intersects(bottomRight.elementsBounds)) {
					bottomRight.getElementsWithinArea(result, area, false);
				} else if(bottomRight.elementsBounds.contains(area)) {
					bottomRight.getElementsWithinArea(result, area, false);
				}
			}
		}
		if (parent == null) {
			return;
		}

		if(childNodeCrossed || firstInvocation) {
			if (!elementsBounds.contains(area)) {
				//Scan sibling nodes if intersecting this element
				parent.getElementsWithinArea(result, area, this);
			}
		}
	}

	@Override
	protected void getElementsWithinAreaIgnoringEdgesUpwards(Array<T> result, Shape area, boolean firstInvocation) {
		if (elements != null) {
			addElementsWithinArea(result, area);
		}
		if (firstInvocation && topLeft != null){
			if (topLeft.isSearchRequired() && (area.contains(topLeft.elementsBounds) ||
					area.intersectsIgnoringEdges(topLeft.elementsBounds) || topLeft.elementsBounds.contains(area))){
				topLeft.getElementsWithinAreaIgnoringEdges(result, area);
			}
			if (topRight.isSearchRequired() && (area.contains(topRight.elementsBounds) ||
					area.intersectsIgnoringEdges(topRight.elementsBounds) || topRight.elementsBounds.contains(area))){
				topRight.getElementsWithinAreaIgnoringEdges(result, area);
			}
			if (bottomLeft.isSearchRequired() && (area.contains(bottomLeft.elementsBounds) ||
					area.intersectsIgnoringEdges(bottomLeft.elementsBounds) || bottomLeft.elementsBounds.contains(area))){
				bottomLeft.getElementsWithinAreaIgnoringEdges(result, area);
			}
			if (bottomRight.isSearchRequired() && (area.contains(bottomRight.elementsBounds) ||
					area.intersectsIgnoringEdges(bottomRight.elementsBounds) || bottomRight.elementsBounds.contains(area))){
				bottomRight.getElementsWithinAreaIgnoringEdges(result, area);
			}
		}
		if (parent == null) {
			return;
		}
		if(elementsBounds.contains(area)) {
			return;
		}
		//Intersecting a sibling
		parent.getElementsWithinAreaIgnoringEdges(result, area, this);
	}

	@Override
	protected void getElementsContainingAreaUpwards(Array<T> result, Shape area, boolean firstInvocation, boolean entirelyContained) {
		if (elements != null) {
			addElementsContainingArea(result, area, entirelyContained);
		}
		if (firstInvocation && topLeft != null){
			if (topLeft.isSearchRequired() && (area.contains(topLeft.elementsBounds) || area.intersects(topLeft.elementsBounds))){
				topLeft.getElementsContainingArea(result, area, entirelyContained);
			}
			if (topRight.isSearchRequired() && (area.contains(topRight.elementsBounds) || area.intersects(topRight.elementsBounds))){
				topRight.getElementsContainingArea(result, area, entirelyContained);
			}
			if (bottomLeft.isSearchRequired() && (area.contains(bottomLeft.elementsBounds) || area.intersects(bottomLeft.elementsBounds))){
				bottomLeft.getElementsContainingArea(result, area, entirelyContained);
			}
			if (bottomRight.isSearchRequired() && (area.contains(bottomRight.elementsBounds) || area.intersects(bottomRight.elementsBounds))){
				bottomRight.getElementsContainingArea(result, area, entirelyContained);
			}
		}
		if (parent == null) {
			return;
		}
		parent.getElementsContainingArea(result, area, entirelyContained, this);
	}

	@Override
	protected void getElementsContainingPointUpwards(Array<T> result, Point point, boolean firstInvocation) {
		if (elements != null){
			addElementsContainingPoint(result, point);
		}
		if (firstInvocation && topLeft != null){
			if (topLeft.isSearchRequired() && topLeft.elementsBounds.contains(point)){
				topLeft.getElementsContainingPoint(result, point);
			}
			if (topRight.isSearchRequired() && topRight.elementsBounds.contains(point)){
				topRight.getElementsContainingPoint(result, point);
			}
			if (bottomLeft.isSearchRequired() && bottomLeft.elementsBounds.contains(point)){
				bottomLeft.getElementsContainingPoint(result, point);
			}
			if (bottomRight.isSearchRequired() && bottomRight.elementsBounds.contains(point)){
				bottomRight.getElementsContainingPoint(result, point);
			}
		}
		if (parent == null){
			return;
		}
		parent.getElementsContainingPoint(result, point, this);
	}

	@Override
	protected void addElementsIntersectingLineSegmentUpwards(Array<T> result, LineSegment lineSegment, boolean firstInvocation) {
		if (elements != null) {
			addElementsIntersectingLineSegment(result, lineSegment);
		}
		if (topLeft != null && firstInvocation){
			if (topLeft.isSearchRequired() && intersects(topLeft.elementsBounds, lineSegment)){
				topLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (topRight.isSearchRequired() && intersects(topRight.elementsBounds, lineSegment)){
				topRight.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (bottomLeft.isSearchRequired() && intersects(bottomLeft.elementsBounds, lineSegment)){
				bottomLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (bottomRight.isSearchRequired() && intersects(bottomRight.elementsBounds, lineSegment)){
				bottomRight.getElementsIntersectingLineSegment(result, lineSegment);
			}
		}
		if (parent == null) {
			return;
		}
		parent.getElementsIntersectingLineSegment(result, lineSegment, this);
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
