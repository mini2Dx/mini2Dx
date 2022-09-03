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
 * Combines a grid with a region quadtree to improve performance.
 * This works by have two grid layers; a large grid contains cells of small grids.
 * The grids are used to fast-lookup appropriate quads.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Quadtree#The_region_quadtree">
 *      Wikipedia: Region Quad Tree</a>
 */
public class GridRegionQuadTree<T extends Sizeable> extends Rectangle implements QuadTree<T> {
	public static final float DEFAULT_MINIMUM_QUAD_SIZE = 8f;
	public static Color QUAD_COLOR = Mdx.graphics != null ? Mdx.graphics.newColor(1f, 0f, 0f, 0.5f) : null;
	public static Color ELEMENT_COLOR = Mdx.graphics != null ? Mdx.graphics.newColor(0f, 0f, 1f, 0.5f) : null;

	protected final GridRegionQuadTreeLooseCell[] grid;
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
		grid = new GridRegionQuadTreeLooseCell[totalGridCellsX * totalGridCellsY];
		dirtyCells = new BitSet(totalGridCellsX * totalGridCellsY);

		for(int gy = 0; gy < totalGridCellsY; gy++) {
			for(int gx = 0; gx < totalGridCellsX; gx++) {
				final int index = (gy * totalGridCellsX) + gx;
				final float cellWorldX = x + (gx * cellWidth);
				final float cellWorldY = y + (gy * cellHeight);

				grid[index] = new GridRegionQuadTreeLooseCell(this, index, minimumQuadWidth, minimumQuadHeight,
						elementLimitPerQuad, cellWorldX, cellWorldY, cellWidth, cellHeight);
			}
		}
	}

	protected int getGridIndex(float x, float y) {
		final int gridX = MathUtils.floor(x * invCellWidth) - gridWorldX;
		final int gridY = MathUtils.floor(y * invCellHeight) - gridWorldY;
		return MathUtils.clamp ((gridY * totalCellsX) + gridX, 0, grid.length - 1);
	}

	protected GridRegionQuadTreeLooseCell<T> getCell(T element) {
		return getCell(element.getCenterX(), element.getCenterY());
	}

	protected GridRegionQuadTreeLooseCell<T> getCell(float x, float y) {
		return grid[getGridIndex(x, y)];
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
		while(cursor > -1 && cursor < grid.length) {
			grid[cursor].cleanup();
			dirtyCells.clear(cursor);
			cursor = dirtyCells.nextSetBit(cursor);
		}
	}

	@Override
	public void debugRender(Graphics g) {

	}

	@Override
	public boolean add(T element) {
		return getCell(element).add(element);
	}

	@Override
	public boolean remove(T element) {
		return getCell(element).remove(element);
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
		return getCell(element).removeElement(element, clearQuadRef);
	}

	@Override
	public void clear() {
		for(int i = 0; i < grid.length; i++) {
			grid[i].clear();
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
		//Check +1 in all axis in case of bounds overlap
		final int minX = MathUtils.floor(area.getX() * invCellWidth) - gridWorldX - 1;
		final int minY = MathUtils.floor(area.getY() * invCellHeight) - gridWorldY - 1;
		final int maxX = MathUtils.floor(area.getMaxX() * invCellWidth) - gridWorldX + 1;
		final int maxY = MathUtils.floor(area.getMaxY() * invCellHeight) - gridWorldY + 1;

		for(int gy = minY; gy <= maxY; gy++) {
			for(int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * totalCellsX) + gx;
				final GridRegionQuadTreeLooseCell<T> cell = grid[index];
				if (area.contains(cell.elementsBounds)) {
					cell.getElementsWithinArea(result, area, true);
				} else if (cell.elementsBounds.contains(area)) {
					cell.getElementsWithinArea(result, area, false);
				} else if (cell.elementsBounds.intersects(area)) {
					cell.getElementsWithinArea(result, area, false);
				}
			}
		}
	}

	@Override
	public Array<T> getElementsWithinAreaIgnoringEdges(Shape area) {
		Array<T> result = new Array<>();
		getElementsWithinAreaIgnoringEdges(result, area);
		return result;
	}

	@Override
	public void getElementsWithinAreaIgnoringEdges(Array<T> result, Shape area) {
		//Check +1 in all axis in case of bounds overlap
		final int minX = MathUtils.floor(area.getX() * invCellWidth) - 1;
		final int minY = MathUtils.floor(area.getY() * invCellHeight) - 1;
		final int maxX = MathUtils.floor(area.getMaxX() * invCellWidth) + 1;
		final int maxY = MathUtils.floor(area.getMaxY() * invCellHeight) + 1;

		for(int gy = minY; gy <= maxY; gy++) {
			for(int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * totalCellsX) + gx;
				final GridRegionQuadTreeLooseCell<T> cell = grid[index];
				if (area.contains(cell.elementsBounds)) {
					cell.getElementsWithinAreaIgnoringEdges(result, area);
				} else if (cell.elementsBounds.contains(area)) {
					cell.getElementsWithinAreaIgnoringEdges(result, area);
				} else if (cell.elementsBounds.intersectsIgnoringEdges(area)) {
					cell.getElementsWithinAreaIgnoringEdges(result, area);
				}
			}
		}
	}

	@Override
	public Array<T> getElementsContainingArea(Shape area, boolean entirelyContained) {
		Array<T> result = new Array<>();
		getElementsContainingArea(result, area, entirelyContained);
		return result;
	}

	@Override
	public void getElementsContainingArea(Array<T> result, Shape area, boolean entirelyContained) {
		//Check +1 in all axis in case of bounds overlap
		final int minX = MathUtils.floor(area.getX() * invCellWidth) - 1;
		final int minY = MathUtils.floor(area.getY() * invCellHeight) - 1;
		final int maxX = MathUtils.floor(area.getMaxX() * invCellWidth) + 1;
		final int maxY = MathUtils.floor(area.getMaxY() * invCellHeight) + 1;

		for(int gy = minY; gy <= maxY; gy++) {
			for(int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * totalCellsX) + gx;
				final GridRegionQuadTreeLooseCell<T> cell = grid[index];
				if(cell.elementsBounds.contains(area) || cell.elementsBounds.intersects(area)) {
					cell.getElementsContainingArea(result, area, entirelyContained);
				}
			}
		}
	}

	@Override
	public Array<T> getElementsIntersectingLineSegment(LineSegment lineSegment) {
		Array<T> result = new Array<>();
		getElementsIntersectingLineSegment(result, lineSegment);
		return result;
	}

	@Override
	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment) {
		//Check +1 in all axis in case of bounds overlap
		final int minX = MathUtils.floor(lineSegment.getMinX() * invCellWidth) - 1;
		final int minY = MathUtils.floor(lineSegment.getMinY() * invCellHeight) - 1;
		final int maxX = MathUtils.floor(lineSegment.getMaxX() * invCellWidth) + 1;
		final int maxY = MathUtils.floor(lineSegment.getMaxY() * invCellHeight) + 1;

		for(int gy = minY; gy <= maxY; gy++) {
			for(int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * totalCellsX) + gx;
				final GridRegionQuadTreeLooseCell<T> cell = grid[index];
				cell.getElementsIntersectingLineSegment(result, lineSegment);
			}
		}
	}

	@Override
	public Array<T> getElementsContainingPoint(Point point) {
		Array<T> result = new Array<>();
		getElementsContainingPoint(result, point);
		return result;
	}

	@Override
	public void getElementsContainingPoint(Array<T> result, Point point) {
		//Check +1 in all axis in case of bounds overlap
		final int minX = MathUtils.floor(point.getX() * invCellWidth) - 1;
		final int minY = MathUtils.floor(point.getY() * invCellHeight - 1);
		final int maxX = MathUtils.floor(point.getX() * invCellWidth) + 1;
		final int maxY = MathUtils.floor(point.getY() * invCellHeight) + 1;

		for(int gy = minY; gy <= maxY; gy++) {
			for(int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * totalCellsX) + gx;
				final GridRegionQuadTreeLooseCell<T> cell = grid[index];
				if(!cell.elementsBounds.contains(point)) {
					continue;
				}
				cell.getElementsContainingPoint(result, point);
			}
		}
	}

	@Override
	public Array<T> getElementsWithinArea(Shape area, QuadTreeSearchDirection searchDirection) {
		Array<T> result = new Array<>();
		getElementsWithinArea(result, area, searchDirection);
		return result;
	}

	@Override
	public void getElementsWithinArea(Array<T> result, Shape area, QuadTreeSearchDirection searchDirection) {
		//Check +1 in all axis in case of bounds overlap
		final int minX = MathUtils.floor(area.getX() * invCellWidth) - 1;
		final int minY = MathUtils.floor(area.getY() * invCellHeight) - 1;
		final int maxX = MathUtils.floor(area.getMaxX() * invCellWidth) + 1;
		final int maxY = MathUtils.floor(area.getMaxY() * invCellHeight) + 1;

		for(int gy = minY; gy <= maxY; gy++) {
			for(int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * totalCellsX) + gx;
				final GridRegionQuadTreeLooseCell<T> cell = grid[index];
				cell.getElementsWithinArea(result, area, searchDirection);
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
		//Check +1 in all axis in case of bounds overlap
		final int minX = MathUtils.floor(area.getX() * invCellWidth) - 1;
		final int minY = MathUtils.floor(area.getY() * invCellHeight) - 1;
		final int maxX = MathUtils.floor(area.getMaxX() * invCellWidth) + 1;
		final int maxY = MathUtils.floor(area.getMaxY() * invCellHeight) + 1;

		for(int gy = minY; gy <= maxY; gy++) {
			for(int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * totalCellsX) + gx;
				final GridRegionQuadTreeLooseCell<T> cell = grid[index];
				cell.getElementsWithinAreaIgnoringEdges(result, area, searchDirection);
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
		//Check +1 in all axis in case of bounds overlap
		final int minX = MathUtils.floor(area.getX() * invCellWidth) - 1;
		final int minY = MathUtils.floor(area.getY() * invCellHeight) - 1;
		final int maxX = MathUtils.floor(area.getMaxX() * invCellWidth) + 1;
		final int maxY = MathUtils.floor(area.getMaxY() * invCellHeight) + 1;

		for(int gy = minY; gy <= maxY; gy++) {
			for(int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * totalCellsX) + gx;
				final GridRegionQuadTreeLooseCell<T> cell = grid[index];
				cell.getElementsContainingArea(result, area, searchDirection, entirelyContained);
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
		//Check +1 in all axis in case of bounds overlap
		final int minX = MathUtils.floor(lineSegment.getMinX() * invCellWidth) - 1;
		final int minY = MathUtils.floor(lineSegment.getMinY() * invCellHeight) - 1;
		final int maxX = MathUtils.floor(lineSegment.getMaxX() * invCellWidth) + 1;
		final int maxY = MathUtils.floor(lineSegment.getMaxY() * invCellHeight) + 1;

		for(int gy = minY; gy <= maxY; gy++) {
			for(int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * totalCellsX) + gx;
				final GridRegionQuadTreeLooseCell<T> cell = grid[index];
				cell.getElementsIntersectingLineSegment(result, lineSegment, searchDirection);
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
		//Check +1 in all axis in case of bounds overlap
		final int minX = MathUtils.floor(point.getX() * invCellWidth) - 1;
		final int minY = MathUtils.floor(point.getY() * invCellHeight - 1);
		final int maxX = MathUtils.floor(point.getX() * invCellWidth) + 1;
		final int maxY = MathUtils.floor(point.getY() * invCellHeight) + 1;

		for(int gy = minY; gy <= maxY; gy++) {
			for(int gx = minX; gx <= maxX; gx++) {
				final int index = (gy * totalCellsX) + gx;
				final GridRegionQuadTreeLooseCell<T> cell = grid[index];
				if(searchDirection == QuadTreeSearchDirection.DOWNWARDS && !cell.elementsBounds.contains(point)) {
					continue;
				}
				cell.getElementsContainingPoint(result, point, searchDirection);
			}
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
		for(int i = 0; i < grid.length; i++) {
			grid[i].getElements(result);
		}
	}

	@Override
	public int getTotalElements() {
		if (totalElementsCache >= 0) {
			return totalElementsCache;
		}

		totalElementsCache = 0;
		for(int i = 0; i < grid.length; i++) {
			totalElementsCache += grid[i].getTotalElements();
		}
		return totalElementsCache;
	}

	@Override
	public int getTotalQuads() {
		int result = 0;
		for(int i = 0; i < grid.length; i++) {
			result += grid[i].getTotalQuads();
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
