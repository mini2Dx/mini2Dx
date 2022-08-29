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

	private final GridRegionQuadTreeLargeCell[] largeGrid;
	private final GridRegionQuadTreeSmallCell[] smallGrid;

	private final int smallGridWorldX, smallGridWorldY, largeGridWorldX, largeGridWorldY;
	private final int totalSmallCellsX, totalLargeCellsX;
	private final int smallGridCellWidth, smallGridCellHeight, largeGridCellWidth, largeGridCellHeight;
	private final float invSmallCellWidth, invSmallCellHeight, invLargeCellWidth, invLargeCellHeight;
	private final float minimumQuadWidth, minimumQuadHeight;

	public GridRegionQuadTree(int elementLimitPerQuad, int x, int y, int width, int height,
	                          int smallGridCellWidth, int smallGridCellHeight, int largeGridCellWidth, int largeGridCellHeight) {
		this(PointQuadTree.DEFAULT_MINIMUM_QUAD_SIZE, PointQuadTree.DEFAULT_MINIMUM_QUAD_SIZE, elementLimitPerQuad,
				x, y, width, height, smallGridCellWidth, smallGridCellHeight, largeGridCellWidth, largeGridCellHeight);
	}

	public GridRegionQuadTree(float minimumQuadWidth, float minimumQuadHeight, int elementLimitPerQuad, int x, int y, int width, int height,
	                              int smallGridCellWidth, int smallGridCellHeight, int largeGridCellWidth, int largeGridCellHeight) {
		super(x, y, width, height);
		this.minimumQuadWidth = minimumQuadWidth;
		this.minimumQuadHeight = minimumQuadHeight;

		if(smallGridCellWidth >= largeGridCellWidth) {
			throw new MdxException("smallGridCellWidth must be less than largeGridCellWidth for optimal performance");
		}
		if(smallGridCellHeight >= largeGridCellHeight) {
			throw new MdxException("smallGridCellHeight must be less than largeGridCellHeight for optimal performance");
		}
		if(largeGridCellWidth % smallGridCellWidth != 0) {
			throw new MdxException("largeGridCellWidth must be divisible by smallGridCellWidth");
		}
		if(largeGridCellHeight % smallGridCellHeight != 0) {
			throw new MdxException("largeGridCellHeight must be divisible by smallGridCellHeight");
		}
		if(width % smallGridCellWidth != 0) {
			throw new MdxException("width must be divisible by smallGridCellWidth");
		}
		if(height % smallGridCellHeight != 0) {
			throw new MdxException("height must be divisible by smallGridCellHeight");
		}
		if(width % largeGridCellWidth != 0) {
			throw new MdxException("width must be divisible by largeGridCellWidth");
		}
		if(height % largeGridCellHeight != 0) {
			throw new MdxException("height must be divisible by largeGridCellHeight");
		}
		this.smallGridCellWidth = smallGridCellWidth;
		this.smallGridCellHeight = smallGridCellHeight;
		this.largeGridCellWidth = largeGridCellWidth;
		this.largeGridCellHeight = largeGridCellHeight;

		totalSmallCellsX = MathUtils.floor(width / smallGridCellWidth);
		totalLargeCellsX = MathUtils.floor(width / largeGridCellWidth);

		invSmallCellWidth = 1f / smallGridCellWidth;
		invSmallCellHeight = 1f / smallGridCellHeight;
		invLargeCellWidth = 1f / largeGridCellWidth;
		invLargeCellHeight = 1f / largeGridCellHeight;

		smallGridWorldX = MathUtils.floor(x / smallGridCellWidth);
		smallGridWorldY = MathUtils.floor(y / smallGridCellHeight);
		largeGridWorldX = MathUtils.floor(x / largeGridCellWidth);
		largeGridWorldY = MathUtils.floor(y / largeGridCellHeight);

		final int totalSmallGridCellsX = width / smallGridCellWidth;
		final int totalSmallGridCellsY = height / smallGridCellHeight;
		smallGrid = new GridRegionQuadTreeSmallCell[totalSmallGridCellsX * totalSmallGridCellsY];

		final int totalLargeGridCellsX = width / largeGridCellWidth;
		final int totalLargeGridCellsY = height / largeGridCellHeight;
		largeGrid = new GridRegionQuadTreeLargeCell[totalLargeGridCellsX * totalLargeGridCellsY];

		for(int gy = 0; gy < totalLargeGridCellsY; gy++) {
			for(int gx = 0; gx < totalLargeGridCellsX; gx++) {
				final int index = (gy * totalLargeGridCellsX) + gx;
				final float cellWorldX = x + (gx * largeGridCellWidth);
				final float cellWorldY = y + (gy * largeGridCellHeight);
				largeGrid[index] = new GridRegionQuadTreeLargeCell(
						this, smallGridCellWidth, smallGridCellHeight,
						cellWorldX, cellWorldY, largeGridCellWidth, largeGridCellHeight);
			}
		}

		for(int gy = 0; gy < totalSmallGridCellsY; gy++) {
			for(int gx = 0; gx < totalSmallGridCellsX; gx++) {
				final int index = (gy * totalSmallGridCellsX) + gx;
				final float cellWorldX = x + (gx * smallGridCellWidth);
				final float cellWorldY = y + (gy * smallGridCellHeight);

				final int largeGridIndex = getLargeGridIndex(cellWorldX, cellWorldY);
//				smallGrid[index] = new GridRegionQuadTreeSmallCell(largeGrid[largeGridIndex],
//						elementLimitPerQuad, cellWorldX, cellWorldY, smallGridCellWidth, smallGridCellHeight);
			}
		}
	}

	private int getLargeGridIndex(float x, float y) {
		final int gridX = MathUtils.floor(x * invLargeCellWidth) - largeGridWorldX;
		final int gridY = MathUtils.floor(y * invLargeCellHeight) - largeGridWorldY;
		return (gridY * totalLargeCellsX) + gridX;
	}

	private int getSmallGridIndex(float x, float y) {
		final int gridX = MathUtils.floor(x * invSmallCellWidth) - smallGridWorldX;
		final int gridY = MathUtils.floor(y * invSmallCellHeight) - smallGridWorldY;
		return (gridY * totalSmallCellsX) + gridX;
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
