/**
 * Copyright 2022 Viridian Software Ltd.
 */
package org.mini2Dx.core.collision;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.geom.*;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;

public class GridRegionQuadTreeLargeCell<T extends Sizeable> extends Rectangle implements QuadTree<T> {
	private final GridRegionQuadTree<T> parent;
	private final GridRegionQuadTreeSmallCell[] cells;
	private final float invSmallCellWidth, invSmallCellHeight;
	private final int smallGridWorldX, smallGridWorldY, totalSmallCellsX;

	public GridRegionQuadTreeLargeCell(GridRegionQuadTree<T> parent,
	                                   int smallGridCellWidth, int smallGridCellHeight,
	                                   float x, float y, float width, float height) {
		super(x, y, width, height);
		this.parent = parent;

		invSmallCellWidth = 1f / smallGridCellWidth;
		invSmallCellHeight = 1f / smallGridCellHeight;
		smallGridWorldX = MathUtils.floor(x / smallGridCellWidth);
		smallGridWorldY = MathUtils.floor(y / smallGridCellHeight);
		totalSmallCellsX = MathUtils.floor(width / smallGridCellWidth);

		final int totalSmallGridCellsX = totalSmallCellsX;
		final int totalSmallGridCellsY = MathUtils.floor(height / smallGridCellHeight);
		cells = new GridRegionQuadTreeSmallCell[totalSmallGridCellsX * totalSmallGridCellsY];
	}

	public void add(GridRegionQuadTreeSmallCell cell) {
		final int index = getLocalCellIndex(cell.getX(), cell.getY());
		cells[index] = cell;
	}

	private int getLocalCellIndex(float x, float y) {
		final int gridX = MathUtils.floor(x * invSmallCellWidth) - smallGridWorldX;
		final int gridY = MathUtils.floor(y * invSmallCellHeight) - smallGridWorldY;
		return (gridY * totalSmallCellsX) + gridX;
	}

	void getElementsWithinAreaExcluding(GridRegionQuadTreeSmallCell excluded, Array<T> result, Shape area) {

	}

	@Override
	public void debugRender(Graphics g) {

	}

	@Override
	public boolean add(T element) {
		return false;
	}

	@Override
	public boolean remove(T element) {
		return false;
	}

	@Override
	public void addAll(Array<T> elements) {

	}

	@Override
	public void removeAll(Array<T> elements) {

	}

	@Override
	public void clear() {

	}

	@Override
	public Array<T> getElementsWithinArea(Shape area) {
		return null;
	}

	@Override
	public void getElementsWithinArea(Array<T> result, Shape area) {

	}

	@Override
	public Array<T> getElementsWithinAreaIgnoringEdges(Shape area) {
		return null;
	}

	@Override
	public void getElementsWithinAreaIgnoringEdges(Array<T> result, Shape area) {

	}

	@Override
	public Array<T> getElementsContainingArea(Shape area, boolean entirelyContained) {
		return null;
	}

	@Override
	public void getElementsContainingArea(Array<T> result, Shape area, boolean entirelyContained) {

	}

	@Override
	public Array<T> getElementsIntersectingLineSegment(LineSegment lineSegment) {
		return null;
	}

	@Override
	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment) {

	}

	@Override
	public Array<T> getElementsContainingPoint(Point point) {
		return null;
	}

	@Override
	public void getElementsContainingPoint(Array<T> result, Point point) {

	}

	@Override
	public Array<T> getElements() {
		return null;
	}

	@Override
	public void getElements(Array<T> result) {

	}

	@Override
	public int getTotalElements() {
		return 0;
	}

	@Override
	public Array<T> getElementsWithinArea(Shape area, QuadTreeSearchDirection searchDirection) {
		return null;
	}

	@Override
	public void getElementsWithinArea(Array<T> result, Shape area, QuadTreeSearchDirection searchDirection) {

	}

	@Override
	public Array<T> getElementsWithinAreaIgnoringEdges(Shape area, QuadTreeSearchDirection searchDirection) {
		return null;
	}

	@Override
	public void getElementsWithinAreaIgnoringEdges(Array<T> result, Shape area, QuadTreeSearchDirection searchDirection) {

	}

	@Override
	public Array<T> getElementsContainingArea(Shape area, QuadTreeSearchDirection searchDirection, boolean entirelyContained) {
		return null;
	}

	@Override
	public void getElementsContainingArea(Array<T> result, Shape area, QuadTreeSearchDirection searchDirection, boolean entirelyContained) {

	}

	@Override
	public Array<T> getElementsIntersectingLineSegment(LineSegment lineSegment, QuadTreeSearchDirection searchDirection) {
		return null;
	}

	@Override
	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment, QuadTreeSearchDirection searchDirection) {

	}

	@Override
	public Array<T> getElementsContainingPoint(Point point, QuadTreeSearchDirection searchDirection) {
		return null;
	}

	@Override
	public void getElementsContainingPoint(Array<T> result, Point point, QuadTreeSearchDirection searchDirection) {

	}

	@Override
	public int getTotalQuads() {
		return 0;
	}

	@Override
	public QuadTree<T> getParent() {
		return null;
	}

	@Override
	public float getMinimumQuadWidth() {
		return 0;
	}

	@Override
	public float getMinimumQuadHeight() {
		return 0;
	}

	@Override
	public void positionChanged(T moved) {

	}
}
