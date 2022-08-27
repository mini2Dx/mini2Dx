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


import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.geom.*;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Queue;

import java.util.BitSet;

/**
 * Combines a grid with a region quadtree to improve performance in massive world scenarios.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Quadtree#The_region_quadtree">
 *      Wikipedia: Region Quad Tree</a>
 */
public class GridRegionQuadTree<T extends Sizeable> extends Rectangle implements QuadTree<T> {
	public static final float DEFAULT_MINIMUM_QUAD_SIZE = 8f;
	public static Color QUAD_COLOR = Mdx.graphics != null ? Mdx.graphics.newColor(1f, 0f, 0f, 0.5f) : null;
	public static Color ELEMENT_COLOR = Mdx.graphics != null ? Mdx.graphics.newColor(0f, 0f, 1f, 0.5f) : null;

	protected final GridRegionQuadTreeLooseCell[] looseGrid;
	protected final GridRegionQuadTreeTightCell[] tightGrid;
	protected final BitSet dirtyCells;

	protected final int gridWorldX, gridWorldY;
	protected final int totalCellsX;
	protected final int cellWidth, cellHeight;
	protected final float invCellWidth, invCellHeight;
	protected final float minimumQuadWidth, minimumQuadHeight;

	protected final Queue<RegionQuadTree<T>> pool = new Queue<>();

	protected int totalElementsCache = -1;

	public GridRegionQuadTree(int elementLimitPerQuad, int x, int y, int width, int height, int gridCellWidth, int gridCellHeight) {
		this(PointQuadTree.DEFAULT_MINIMUM_QUAD_SIZE, PointQuadTree.DEFAULT_MINIMUM_QUAD_SIZE, elementLimitPerQuad,
				x, y, width, height, gridCellWidth, gridCellHeight);
	}

	public GridRegionQuadTree(float minimumQuadWidth, float minimumQuadHeight, int elementLimitPerQuad, int x, int y, int width, int height,
	                              int gridCellWidth, int gridCellHeight) {
		super(x, y, width, height);
		this.minimumQuadWidth = minimumQuadWidth;
		this.minimumQuadHeight = minimumQuadHeight;

		if(width % gridCellWidth != 0) {
			throw new MdxException("width must be divisible by gridCellWidth");
		}
		if(height % gridCellHeight != 0) {
			throw new MdxException("height must be divisible by gridCellHeight");
		}
		this.cellWidth = gridCellWidth;
		this.cellHeight = gridCellHeight;

		totalCellsX = MathUtils.floor(width / cellWidth);

		invCellWidth = 1f / cellWidth;
		invCellHeight = 1f / cellHeight;

		gridWorldX = MathUtils.floor(x / cellWidth);
		gridWorldY = MathUtils.floor(y / cellHeight);

		final int totalGridCellsX = width / cellWidth;
		final int totalGridCellsY = height / cellHeight;
		looseGrid = new GridRegionQuadTreeLooseCell[totalGridCellsX * totalGridCellsY];
		tightGrid = new GridRegionQuadTreeTightCell[totalGridCellsX * totalGridCellsY];
		dirtyCells = new BitSet(totalGridCellsX * totalGridCellsY);

		for(int gy = 0; gy < totalGridCellsY; gy++) {
			for(int gx = 0; gx < totalGridCellsX; gx++) {
				final int index = (gy * totalGridCellsX) + gx;
				final float cellWorldX = x + (gx * cellWidth);
				final float cellWorldY = y + (gy * cellHeight);

				tightGrid[index] = new GridRegionQuadTreeTightCell(index);
				looseGrid[index] = new GridRegionQuadTreeLooseCell(this, index, minimumQuadWidth, minimumQuadHeight,
						elementLimitPerQuad, cellWorldX, cellWorldY, cellWidth, cellHeight);
			}
		}
	}

	protected int getGridIndex(float x, float y) {
		final int gridX = MathUtils.floor(x * invCellWidth) - gridWorldX;
		final int gridY = MathUtils.floor(y * invCellHeight) - gridWorldY;
		return MathUtils.clamp ((gridY * totalCellsX) + gridX, 0, looseGrid.length - 1);
	}

	public int getMinGridIndex(T element) {
		return getGridIndex(element.getX(), element.getY());
	}

	public int getMaxGridIndex(T element) {
		return getGridIndex(element.getMaxX(), element.getMaxY());
	}

	public GridRegionQuadTreeTightCell<T> getTightCell(int index) {
		return tightGrid[index];
	}

	public GridRegionQuadTreeTightCell<T> getTightCell(T element) {
		return getTightCell(element.getCenterX(), element.getCenterY());
	}

	public GridRegionQuadTreeTightCell<T> getTightCell(float x, float y) {
		return tightGrid[getGridIndex(x, y)];
	}

	public GridRegionQuadTreeLooseCell<T> getLooseCell(T element) {
		return getLooseCell(element.getCenterX(), element.getCenterY());
	}

	public GridRegionQuadTreeLooseCell<T> getLooseCell(float x, float y) {
		return looseGrid[getGridIndex(x, y)];
	}

	public void warmupPool(int poolSize) {
		warmupPool(poolSize, 16);
	}

	public void warmupPool(int poolSize, int expectedElementsPerQuad) {
		if(pool.size == 0) {
			pool.ensureCapacity(poolSize);
		}
		for(int i = 0; i < poolSize; i++) {
//			RegionQuadTree<T> regionQuadTree = new RegionQuadTree<T>(null, 0, 0, 1, 1);
//			regionQuadTree.elements = new Array<>(expectedElementsPerQuad);
//			pool.addLast(regionQuadTree);
		}
	}

	public void cleanup() {
		int cursor = 0;
		while(cursor > -1 && cursor < looseGrid.length) {
			looseGrid[cursor].cleanup();
			dirtyCells.clear(cursor);
			cursor = dirtyCells.nextSetBit(cursor);
		}
	}

	@Override
	public void debugRender(Graphics g) {

	}

	@Override
	public boolean add(T element) {
		return getLooseCell(element).add(element);
	}

	@Override
	public boolean remove(T element) {
		return getLooseCell(element).remove(element);
	}

	@Override
	public void addAll(Array<T> elements) {
		for(T element : elements) {
			add(element);
		}
	}

	@Override
	public void removeAll(Array<T> elements) {
		for(T element : elements) {
			remove(element);
		}
	}

	protected boolean removeElement(T element, boolean clearQuadRef) {
		return getLooseCell(element).removeElement(element, clearQuadRef);
	}

	@Override
	public void clear() {
		for(int i = 0; i < looseGrid.length; i++) {
			looseGrid[i].clear();
			tightGrid[i].clear();
		}
	}

	@Override
	public Array<T> getElementsWithinArea(Shape area) {
		Array<T> result = new Array<>();
		getElementsWithinArea(result, area);
		return result;
	}

	@Override
	public void getElementsWithinArea(Array<T> result, Shape area) {
		getElementsWithinArea(result, area, QuadTreeSearchDirection.DOWNWARDS);
	}

	@Override
	public Array<T> getElementsWithinAreaIgnoringEdges(Shape area) {
		Array<T> result = new Array<>();
		getElementsWithinAreaIgnoringEdges(result, area);
		return result;
	}

	@Override
	public void getElementsWithinAreaIgnoringEdges(Array<T> result, Shape area) {
		getElementsWithinAreaIgnoringEdges(result, area, QuadTreeSearchDirection.DOWNWARDS);
	}

	@Override
	public Array<T> getElementsContainingArea(Shape area, boolean entirelyContained) {
		Array<T> result = new Array<>();
		getElementsContainingArea(result, area, entirelyContained);
		return result;
	}

	@Override
	public void getElementsContainingArea(Array<T> result, Shape area, boolean entirelyContained) {
		getElementsContainingArea(result, area, QuadTreeSearchDirection.DOWNWARDS, entirelyContained);
	}

	@Override
	public Array<T> getElementsIntersectingLineSegment(LineSegment lineSegment) {
		Array<T> result = new Array<>();
		getElementsIntersectingLineSegment(result, lineSegment);
		return result;
	}

	@Override
	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment) {
		getElementsIntersectingLineSegment(result, lineSegment, QuadTreeSearchDirection.DOWNWARDS);
	}

	@Override
	public Array<T> getElementsContainingPoint(Point point) {
		Array<T> result = new Array<>();
		getElementsContainingPoint(result, point);
		return result;
	}

	@Override
	public void getElementsContainingPoint(Array<T> result, Point point) {
		getElementsContainingPoint(result, point, QuadTreeSearchDirection.DOWNWARDS);
	}

	@Override
	public Array<T> getElementsWithinArea(Shape area, QuadTreeSearchDirection searchDirection) {
		Array<T> result = new Array<>();
		getElementsWithinArea(result, area, searchDirection);
		return result;
	}

	@Override
	public void getElementsWithinArea(Array<T> result, Shape area, QuadTreeSearchDirection searchDirection) {
		final int minX = MathUtils.floor(area.getX() * invCellWidth) - gridWorldX;
		final int minY = MathUtils.floor(area.getY() * invCellHeight) - gridWorldY;
		final int maxX = MathUtils.floor(area.getMaxX() * invCellWidth) - gridWorldX;
		final int maxY = MathUtils.floor(area.getMaxY() * invCellHeight) - gridWorldY;

		for(int gy = minY; gy <= maxY; gy++) {
			for(int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * totalCellsX) + gx;
				final GridRegionQuadTreeTightCell<T> tightCell = tightGrid[index];
				for(GridRegionQuadTreeLooseCell<T> looseCell : tightCell.getCells()) {
					if(!looseCell.isSearchRequired()) {
						continue;
					}
					looseCell.getElementsWithinArea(result, area, searchDirection);
				}
			}
		}
	}

	void getElementsWithinArea(Array<T> result, Shape area, GridRegionQuadTreeLooseCell<T> excludedCell) {
		final int minX = MathUtils.floor(area.getX() * invCellWidth) - gridWorldX;
		final int minY = MathUtils.floor(area.getY() * invCellHeight) - gridWorldY;
		final int maxX = MathUtils.floor(area.getMaxX() * invCellWidth) - gridWorldX;
		final int maxY = MathUtils.floor(area.getMaxY() * invCellHeight) - gridWorldY;

		for(int gy = minY; gy <= maxY; gy++) {
			for(int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * totalCellsX) + gx;
				final GridRegionQuadTreeTightCell<T> tightCell = tightGrid[index];
				for(GridRegionQuadTreeLooseCell<T> looseCell : tightCell.getCells()) {
					if(looseCell == excludedCell) {
						continue;
					}
					if(!looseCell.isSearchRequired()) {
						continue;
					}
					looseCell.getElementsWithinArea(result, area, QuadTreeSearchDirection.DOWNWARDS);
				}
			}
		}
	}

	@Override
	public Array<T> getElementsWithinAreaIgnoringEdges(Shape area, QuadTreeSearchDirection searchDirection) {
		Array<T> result = new Array<>();
		getElementsWithinAreaIgnoringEdges(result, area, searchDirection);
		return result;
	}

	@Override
	public void getElementsWithinAreaIgnoringEdges(Array<T> result, Shape area, QuadTreeSearchDirection searchDirection) {
		final int minX = MathUtils.floor(area.getX() * invCellWidth) - gridWorldX;
		final int minY = MathUtils.floor(area.getY() * invCellHeight) - gridWorldY;
		final int maxX = MathUtils.floor(area.getMaxX() * invCellWidth) - gridWorldX;
		final int maxY = MathUtils.floor(area.getMaxY() * invCellHeight) - gridWorldY;

		for(int gy = minY; gy <= maxY; gy++) {
			for(int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * totalCellsX) + gx;
				final GridRegionQuadTreeTightCell<T> tightCell = tightGrid[index];
				for(GridRegionQuadTreeLooseCell<T> looseCell : tightCell.getCells()) {
					if(!looseCell.isSearchRequired()) {
						continue;
					}
					looseCell.getElementsWithinAreaIgnoringEdges(result, area, searchDirection);
				}
			}
		}
	}

	void getElementsWithinAreaIgnoringEdges(Array<T> result, Shape area, GridRegionQuadTreeLooseCell<T> excludedCell) {
		final int minX = MathUtils.floor(area.getX() * invCellWidth) - gridWorldX;
		final int minY = MathUtils.floor(area.getY() * invCellHeight) - gridWorldY;
		final int maxX = MathUtils.floor(area.getMaxX() * invCellWidth) - gridWorldX;
		final int maxY = MathUtils.floor(area.getMaxY() * invCellHeight) - gridWorldY;

		for(int gy = minY; gy <= maxY; gy++) {
			for(int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * totalCellsX) + gx;
				final GridRegionQuadTreeTightCell<T> tightCell = tightGrid[index];
				for(GridRegionQuadTreeLooseCell<T> looseCell : tightCell.getCells()) {
					if(looseCell == excludedCell) {
						continue;
					}
					if(!looseCell.isSearchRequired()) {
						continue;
					}
					looseCell.getElementsWithinAreaIgnoringEdges(result, area, QuadTreeSearchDirection.DOWNWARDS);
				}
			}
		}
	}

	@Override
	public Array<T> getElementsContainingArea(Shape area, QuadTreeSearchDirection searchDirection, boolean entirelyContained) {
		Array<T> result = new Array<>();
		getElementsContainingArea(result, area, searchDirection, entirelyContained);
		return result;
	}

	@Override
	public void getElementsContainingArea(Array<T> result, Shape area, QuadTreeSearchDirection searchDirection, boolean entirelyContained) {
		final int minX = MathUtils.floor(area.getX() * invCellWidth) - gridWorldX;
		final int minY = MathUtils.floor(area.getY() * invCellHeight) - gridWorldY;
		final int maxX = MathUtils.floor(area.getMaxX() * invCellWidth) - gridWorldX;
		final int maxY = MathUtils.floor(area.getMaxY() * invCellHeight) - gridWorldY;

		for(int gy = minY; gy <= maxY; gy++) {
			for(int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * totalCellsX) + gx;
				final GridRegionQuadTreeTightCell<T> tightCell = tightGrid[index];
				for(GridRegionQuadTreeLooseCell<T> looseCell : tightCell.getCells()) {
					if(!looseCell.isSearchRequired()) {
						continue;
					}
					looseCell.getElementsContainingArea(result, area, searchDirection, entirelyContained);
				}
			}
		}
	}

	void getElementsContainingArea(Array<T> result, Shape area, boolean entirelyContained, GridRegionQuadTreeLooseCell<T> excludedCell) {
		final int minX = MathUtils.floor(area.getX() * invCellWidth) - gridWorldX;
		final int minY = MathUtils.floor(area.getY() * invCellHeight) - gridWorldY;
		final int maxX = MathUtils.floor(area.getMaxX() * invCellWidth) - gridWorldX;
		final int maxY = MathUtils.floor(area.getMaxY() * invCellHeight) - gridWorldY;

		for(int gy = minY; gy <= maxY; gy++) {
			for(int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * totalCellsX) + gx;
				final GridRegionQuadTreeTightCell<T> tightCell = tightGrid[index];
				for(GridRegionQuadTreeLooseCell<T> looseCell : tightCell.getCells()) {
					if(looseCell == excludedCell) {
						continue;
					}
					if(!looseCell.isSearchRequired()) {
						continue;
					}
					looseCell.getElementsContainingArea(result, area, QuadTreeSearchDirection.DOWNWARDS, entirelyContained);
				}
			}
		}
	}

	@Override
	public Array<T> getElementsIntersectingLineSegment(LineSegment lineSegment, QuadTreeSearchDirection searchDirection) {
		Array<T> result = new Array<>();
		getElementsIntersectingLineSegment(result, lineSegment, searchDirection);
		return result;
	}

	@Override
	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment, QuadTreeSearchDirection searchDirection) {
		final int minX = MathUtils.floor(lineSegment.getMinX() * invCellWidth) - gridWorldX;
		final int minY = MathUtils.floor(lineSegment.getMinY() * invCellHeight) - gridWorldY;
		final int maxX = MathUtils.floor(lineSegment.getMaxX() * invCellWidth) - gridWorldX;
		final int maxY = MathUtils.floor(lineSegment.getMaxY() * invCellHeight) - gridWorldY;

		for(int gy = minY; gy <= maxY; gy++) {
			for(int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * totalCellsX) + gx;
				final GridRegionQuadTreeTightCell<T> tightCell = tightGrid[index];
				for(GridRegionQuadTreeLooseCell<T> looseCell : tightCell.getCells()) {
					if(!looseCell.isSearchRequired()) {
						continue;
					}
					looseCell.getElementsIntersectingLineSegment(result, lineSegment, searchDirection);
				}
			}
		}
	}

	void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment, GridRegionQuadTreeLooseCell<T> excludedCell) {
		final int minX = MathUtils.floor(lineSegment.getMinX() * invCellWidth) - gridWorldX;
		final int minY = MathUtils.floor(lineSegment.getMinY() * invCellHeight) - gridWorldY;
		final int maxX = MathUtils.floor(lineSegment.getMaxX() * invCellWidth) - gridWorldX;
		final int maxY = MathUtils.floor(lineSegment.getMaxY() * invCellHeight) - gridWorldY;

		for(int gy = minY; gy <= maxY; gy++) {
			for(int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * totalCellsX) + gx;
				final GridRegionQuadTreeTightCell<T> tightCell = tightGrid[index];
				for(GridRegionQuadTreeLooseCell<T> looseCell : tightCell.getCells()) {
					if(looseCell == excludedCell) {
						continue;
					}
					if(!looseCell.isSearchRequired()) {
						continue;
					}
					looseCell.getElementsIntersectingLineSegment(result, lineSegment, QuadTreeSearchDirection.DOWNWARDS);
				}
			}
		}
	}

	@Override
	public Array<T> getElementsContainingPoint(Point point, QuadTreeSearchDirection searchDirection) {
		Array<T> result = new Array<>();
		getElementsContainingPoint(result, point, searchDirection);
		return result;
	}

	@Override
	public void getElementsContainingPoint(Array<T> result, Point point, QuadTreeSearchDirection searchDirection) {
		final int cellX = MathUtils.floor(point.getX() * invCellWidth) - gridWorldX;
		final int cellY = MathUtils.floor(point.getY() * invCellHeight) - gridWorldY;

		final int index = (cellY * totalCellsX) + cellX;
		final GridRegionQuadTreeTightCell<T> tightCell = tightGrid[index];
		for(GridRegionQuadTreeLooseCell<T> looseCell : tightCell.getCells()) {
			if(!looseCell.isSearchRequired()) {
				continue;
			}
			looseCell.getElementsContainingPoint(result, point, searchDirection);
		}
	}

	void getElementsContainingPoint(Array<T> result, Point point, GridRegionQuadTreeLooseCell<T> excludedCell) {
		final int cellX = MathUtils.floor(point.getX() * invCellWidth) - gridWorldX;
		final int cellY = MathUtils.floor(point.getY() * invCellHeight) - gridWorldY;

		final int index = (cellY * totalCellsX) + cellX;
		final GridRegionQuadTreeTightCell<T> tightCell = tightGrid[index];
		for(GridRegionQuadTreeLooseCell<T> looseCell : tightCell.getCells()) {
			if(looseCell == excludedCell) {
				continue;
			}
			if(!looseCell.isSearchRequired()) {
				continue;
			}
			looseCell.getElementsContainingPoint(result, point, QuadTreeSearchDirection.DOWNWARDS);
		}
	}

	@Override
	public void positionChanged(T moved) {
		if (this.contains(moved.getCenterX(), moved.getCenterY()))
			return;

		removeElement(moved, true);
	}

	@Override
	public Array<T> getElements() {
		Array<T> result = new Array<T>();
		getElements(result);
		return result;
	}

	@Override
	public void getElements(Array<T> result) {
		for(int i = 0; i < looseGrid.length; i++) {
			looseGrid[i].getElements(result);
		}
	}

	@Override
	public int getTotalElements() {
		if (totalElementsCache >= 0) {
			return totalElementsCache;
		}

		totalElementsCache = 0;
		for(int i = 0; i < looseGrid.length; i++) {
			totalElementsCache += looseGrid[i].getTotalElements();
		}
		return totalElementsCache;
	}

	@Override
	public int getTotalQuads() {
		int result = 0;
		for(int i = 0; i < looseGrid.length; i++) {
			result += looseGrid[i].getTotalQuads();
		}
		return result;
	}

	@Override
	public QuadTree<T> getParent() {
		return null;
	}

	@Override
	public float getMinimumQuadWidth() {
		return minimumQuadWidth;
	}

	@Override
	public float getMinimumQuadHeight() {
		return minimumQuadHeight;
	}
}
