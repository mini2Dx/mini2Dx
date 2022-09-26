/*******************************************************************************
 * Copyright 2021 Viridian Software Limited
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
import org.mini2Dx.gdx.utils.IntMap;
import org.mini2Dx.gdx.utils.IntSet;

public class CellGrid<T extends CollisionArea> extends Rectangle implements CollisionDetection<T>, SizeChangeListener<T> {
	public static Color QUAD_COLOR = Mdx.graphics != null ? Mdx.graphics.newColor(1f, 0f, 0f, 0.5f) : null;
	public static Color ELEMENT_COLOR = Mdx.graphics != null ? Mdx.graphics.newColor(0f, 0f, 1f, 0.5f) : null;

	private final int cellWidth, cellHeight;
	private final Cell<T>[][] cells;

	private final IntMap<T> tmpCollisions = new IntMap<>();
	private final Array<Cell<T>> tmpCells = new Array<>();

	public CellGrid(int x, int y, int width, int height, int cellWidth, int cellHeight) {
		super(x, y, width, height);
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;

		if(width % cellWidth != 0) {
			throw new MdxException("Width of grid must be divisible by cell width");
		}
		if(height % cellHeight != 0) {
			throw new MdxException("Height of grid must be divisible by cell height");
		}
		cells = new Cell[width / cellWidth][height / cellHeight];
	}

	@Override
	public void debugRender(Graphics g) {
		Color tmp = g.getColor();

		for(int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells[0].length; y++) {
				if (cells[x][y] == null) {
					continue;
				}
				if(cells[x][y].getX() - g.getTranslationX() > g.getViewportWidth()) {
					continue;
				}
				if(cells[x][y].getY() - g.getTranslationY() > g.getViewportHeight()) {
					continue;
				}
				if(cells[x][y].getMaxX() - g.getTranslationX() < 0f) {
					continue;
				}
				if(cells[x][y].getMaxY() - g.getTranslationY() < 0f) {
					continue;
				}
				cells[x][y].debugRender(g);

				g.setColor(QUAD_COLOR);
				g.drawRect(cells[x][y].getX(), cells[x][y].getY(), cells[x][y].getWidth(), cells[x][y].getHeight());
			}
		}
		g.setColor(tmp);
	}

	@Override
	public boolean add(T element) {
		tmpCells.clear();
		getOrCreateFor(element, tmpCells);

		if(tmpCells.size == 0) {
			return false;
		}

		for(int i = 0; i < tmpCells.size; i++) {
			tmpCells.get(i).add(element);
		}
		element.addPostionChangeListener(this);
		element.addSizeChangeListener(this);
		return true;
	}

	@Override
	public boolean remove(T element) {
		tmpCells.clear();
		getOrCreateFor(element, tmpCells);

		boolean result = false;
		for(int i = 0; i < tmpCells.size; i++) {
			result |= tmpCells.get(i).remove(element);
		}
		element.removePositionChangeListener(this);
		element.removeSizeChangeListener(this);
		return result;
	}

	@Override
	public void addAll(Array<T> elements) {
		for(int i = 0; i < elements.size; i++) {
			add(elements.get(i));
		}
	}

	@Override
	public void removeAll(Array<T> elements) {
		for(int i = 0; i < elements.size; i++) {
			remove(elements.get(i));
		}
	}

	@Override
	public void clear() {
		for(int x = 0; x < cells.length; x++) {
			for(int y = 0; y < cells[0].length; y++) {
				if(cells[x][y] == null) {
					continue;
				}
				cells[x][y].clear();
			}
		}
	}

	@Override
	public Array<T> getElementsOverlappingArea(Rectangle area) {
		Array<T> result = new Array<>();
		getElementsOverlappingArea(result, area);
		return result;
	}

	@Override
	public void getElementsOverlappingArea(Array<T> result, Rectangle area) {
		tmpCollisions.clear();
		getElementsOverlappingArea(tmpCollisions, area);

		final IntMap.Keys keys = tmpCollisions.keys();
		while(keys.hasNext) {
			final int id = keys.next();
			result.add(tmpCollisions.get(id));
		}
	}

	@Override
	public Array<T> getElementsOverlappingArea(Circle area) {
		Array<T> result = new Array<>();
		getElementsOverlappingArea(result, area);
		return result;
	}

	@Override
	public void getElementsOverlappingArea(Array<T> result, Circle area) {
		tmpCollisions.clear();
		getElementsOverlappingArea(tmpCollisions, area.getBoundingBox());

		final IntMap.Keys keys = tmpCollisions.keys();
		while(keys.hasNext) {
			final int id = keys.next();
			result.add(tmpCollisions.get(id));
		}
	}

	public void getElementsOverlappingArea(IntMap<T> result, Rectangle area) {
		final int minCellX = MathUtils.floor((area.getMinX() - getX()) / cellWidth);
		final int minCellY = MathUtils.floor((area.getMinY() - getY()) / cellHeight);
		final int maxCellX = MathUtils.ceil((area.getMaxX() - getX()) / cellWidth);
		final int maxCellY = MathUtils.ceil((area.getMaxY() - getY()) / cellHeight);

		for(int x = minCellX; x <= maxCellX; x++) {
			for(int y = minCellY; y <= maxCellY; y++) {
				Cell<T> cell = getOrCreateCell(x, y);
				if(cell == null) {
					continue;
				}
				cell.getElementsOverlappingArea(result, area);
			}
		}
	}

	@Override
	public Array<T> getElementsOverlappingAreaIgnoringEdges(Rectangle area) {
		Array<T> result = new Array<>();
		getElementsOverlappingAreaIgnoringEdges(result, area);
		return result;
	}

	@Override
	public void getElementsOverlappingAreaIgnoringEdges(Array<T> result, Rectangle area) {
		tmpCollisions.clear();
		getElementsOverlappingAreaIgnoringEdges(tmpCollisions, area);

		final IntMap.Keys keys = tmpCollisions.keys();
		while(keys.hasNext) {
			final int id = keys.next();
			result.add(tmpCollisions.get(id));
		}
	}

	public void getElementsOverlappingAreaIgnoringEdges(IntMap<T> result, Rectangle area) {
		final int minCellX = MathUtils.floor((area.getMinX() - getX()) / cellWidth);
		final int minCellY = MathUtils.floor((area.getMinY() - getY()) / cellHeight);
		final int maxCellX = MathUtils.ceil((area.getMaxX() - getX()) / cellWidth);
		final int maxCellY = MathUtils.ceil((area.getMaxY() - getY()) / cellHeight);

		for(int x = minCellX; x <= maxCellX; x++) {
			for(int y = minCellY; y <= maxCellY; y++) {
				Cell<T> cell = getOrCreateCell(x, y);
				if(cell == null) {
					continue;
				}
				cell.getElementsOverlappingAreaIgnoringEdges(result, area);
			}
		}
	}

	@Override
	public Array<T> getElementsContainedInArea(Rectangle area) {
		Array<T> result = new Array<>();
		getElementsContainedInArea(result, area);
		return result;
	}

	@Override
	public void getElementsContainedInArea(Array<T> result, Rectangle area) {
		tmpCollisions.clear();
		getElementsContainedInArea(tmpCollisions, area);

		final IntMap.Keys keys = tmpCollisions.keys();
		while(keys.hasNext) {
			final int id = keys.next();
			result.add(tmpCollisions.get(id));
		}
	}

	public void getElementsContainedInArea(IntMap<T> result, Rectangle area) {
		final int minCellX = MathUtils.floor((area.getMinX() - getX()) / cellWidth);
		final int minCellY = MathUtils.floor((area.getMinY() - getY()) / cellHeight);
		final int maxCellX = MathUtils.ceil((area.getMaxX() - getX()) / cellWidth);
		final int maxCellY = MathUtils.ceil((area.getMaxY() - getY()) / cellHeight);

		for(int x = minCellX; x <= maxCellX; x++) {
			for(int y = minCellY; y <= maxCellY; y++) {
				Cell<T> cell = getOrCreateCell(x, y);
				if(cell == null) {
					continue;
				}
				cell.getElementsContainedInArea(result, area);
			}
		}
	}

	@Override
	public Array<T> getElementsContainingArea(Rectangle area) {
		Array<T> result = new Array<>();
		getElementsContainingArea(result, area);
		return result;
	}

	@Override
	public void getElementsContainingArea(Array<T> result, Rectangle area) {
		tmpCollisions.clear();
		getElementsContainingArea(tmpCollisions, area);

		final IntMap.Keys keys = tmpCollisions.keys();
		while(keys.hasNext) {
			final int id = keys.next();
			result.add(tmpCollisions.get(id));
		}
	}

	public void getElementsContainingArea(IntMap<T> result, Rectangle area) {
		final int minCellX = MathUtils.floor((area.getMinX() - getX()) / cellWidth);
		final int minCellY = MathUtils.floor((area.getMinY() - getY()) / cellHeight);
		final int maxCellX = MathUtils.ceil((area.getMaxX() - getX()) / cellWidth);
		final int maxCellY = MathUtils.ceil((area.getMaxY() - getY()) / cellHeight);

		for(int x = minCellX; x <= maxCellX; x++) {
			for(int y = minCellY; y <= maxCellY; y++) {
				Cell<T> cell = getOrCreateCell(x, y);
				if(cell == null) {
					continue;
				}
				cell.getElementsContainingArea(result, area);
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
		tmpCollisions.clear();
		getElementsIntersectingLineSegment(tmpCollisions, lineSegment);

		final IntMap.Keys keys = tmpCollisions.keys();
		while(keys.hasNext) {
			final int id = keys.next();
			result.add(tmpCollisions.get(id));
		}
	}

	public void getElementsIntersectingLineSegment(IntMap<T> result, LineSegment lineSegment) {
		final int minCellX = MathUtils.floor((lineSegment.getMinX() - getX()) / cellWidth);
		final int minCellY = MathUtils.floor((lineSegment.getMinY() - getY()) / cellHeight);
		final int maxCellX = MathUtils.ceil((lineSegment.getMaxX() - getX()) / cellWidth);
		final int maxCellY = MathUtils.ceil((lineSegment.getMaxY() - getY()) / cellHeight);

		for(int x = minCellX; x <= maxCellX; x++) {
			for(int y = minCellY; y <= maxCellY; y++) {
				Cell<T> cell = getOrCreateCell(x, y);
				if(cell == null) {
					continue;
				}
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
		tmpCollisions.clear();
		getElementsContainingPoint(tmpCollisions, point);

		final IntMap.Keys keys = tmpCollisions.keys();
		while(keys.hasNext) {
			final int id = keys.next();
			result.add(tmpCollisions.get(id));
		}
	}

	public void getElementsContainingPoint(IntMap<T> result, Point point) {
		final int cellX = MathUtils.floor((point.getX() - getX()) / cellWidth);
		final int cellY = MathUtils.floor((point.getY() - getY()) / cellHeight);
		Cell<T> cell = getOrCreateCell(cellX, cellY);
		if(cell == null) {
			return;
		}
		cell.getElementsContainingPoint(result, point);
	}

	@Override
	public Array<T> getElements() {
		final Array<T> result = new Array<>();
		getElements(result);
		return result;
	}

	@Override
	public void getElements(Array<T> result) {
		IntMap<T> elements = new IntMap<>();
		for(int x = 0; x < cells.length; x++) {
			for(int y = 0; y < cells[0].length; y++) {
				if(cells[x][y] == null) {
					continue;
				}
				cells[x][y].getElements(elements);
			}
		}
		for(T element : elements.values()) {
			result.addAll(element);
		}
	}

	@Override
	public int getTotalElements() {
		IntSet result = new IntSet();
		for(int x = 0; x < cells.length; x++) {
			for(int y = 0; y < cells[0].length; y++) {
				if(cells[x][y] == null) {
					continue;
				}
				cells[x][y].getTotalElements(result);
			}
		}
		return result.size;
	}

	@Override
	public void positionChanged(T moved) {
		final int minCellX = MathUtils.floor((moved.getMinX() - getX()) / cellWidth);
		final int minCellY = MathUtils.floor((moved.getMinY() - getY()) / cellHeight);
		final int maxCellX = MathUtils.ceil((moved.getMaxX() - getX()) / cellWidth);
		final int maxCellY = MathUtils.ceil((moved.getMaxY() - getY()) / cellHeight);

		for(int x = minCellX; x <= maxCellX; x++) {
			for(int y = minCellY; y <= maxCellY; y++) {
				Cell<T> cell = getOrCreateCell(x, y);
				if(cell == null) {
					continue;
				}
				cell.add(moved);
			}
		}
	}

	@Override
	public void sizeChanged(T changed) {
		final int minCellX = MathUtils.floor((changed.getMinX() - getX()) / cellWidth);
		final int minCellY = MathUtils.floor((changed.getMinY() - getY()) / cellHeight);
		final int maxCellX = MathUtils.ceil((changed.getMaxX() - getX()) / cellWidth);
		final int maxCellY = MathUtils.ceil((changed.getMaxY() - getY()) / cellHeight);

		for(int x = minCellX; x <= maxCellX; x++) {
			for(int y = minCellY; y <= maxCellY; y++) {
				Cell<T> cell = getOrCreateCell(x, y);
				if(cell == null) {
					continue;
				}
				cell.add(changed);
			}
		}
	}

	private void getOrCreateFor(T sizeable, Array<Cell<T>> results) {
		final int minCellX = MathUtils.floor((sizeable.getMinX() - getX()) / cellWidth);
		final int minCellY = MathUtils.floor((sizeable.getMinY() - getY()) / cellHeight);
		final int maxCellX = MathUtils.ceil((sizeable.getMaxX() - getX()) / cellWidth);
		final int maxCellY = MathUtils.ceil((sizeable.getMaxY() - getY()) / cellHeight);

		for(int x = minCellX; x <= maxCellX; x++) {
			for(int y = minCellY; y <= maxCellY; y++) {
				Cell<T> cell = getOrCreateCell(x, y);
				if(cell == null) {
					continue;
				}
				results.add(cell);
			}
		}
	}

	private Cell<T> getOrCreateCell(int cellX, int cellY) {
		if(cellX < 0) {
			return null;
		}
		if(cellY < 0) {
			return null;
		}
		if(cellX >= cells.length) {
			return null;
		}
		if(cellY >= cells[0].length) {
			return null;
		}
		Cell<T> result = cells[cellX][cellY];
		if(result == null) {
			result = new Cell<T>(MathUtils.round(getX() + (cellX * cellWidth)),
					MathUtils.round(getY() + (cellY * cellWidth)),
					cellWidth, cellHeight);
			cells[cellX][cellY] = result;
		}
		return result;
	}
}
