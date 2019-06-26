/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.collision;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.Shape;
import org.mini2Dx.core.geom.Sizeable;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.gdx.utils.Array;

/**
 * Implements a region quadtree
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Quadtree#The_region_quadtree">
 *      Wikipedia: Region Quad Tree</a>
 */
public class RegionQuadTree<T extends Sizeable> extends PointQuadTree<T> {
	private static final long serialVersionUID = -2417612178966065600L;

	/**
	 * Constructs a {@link RegionQuadTree} with a specified element limit and
	 * watermark
	 * 
	 * @param elementLimit
	 *            The maximum number of elements in a {@link RegionQuadTree}
	 *            before it is split into 4 child quads
	 * @param mergeWatermark
	 *            When a parent {@link RegionQuadTree}'s total elements go lower
	 *            than this mark, the child {@link RegionQuadTree}s will be
	 *            merged back together
	 * @param x
	 *            The x coordinate of the {@link RegionQuadTree}
	 * @param y
	 *            The y coordiante of the {@link RegionQuadTree}
	 * @param width
	 *            The width of the {@link RegionQuadTree}
	 * @param height
	 *            The height of the {@link RegionQuadTree}
	 */
	public RegionQuadTree(int elementLimit, int mergeWatermark, float x, float y, float width, float height) {
		super(elementLimit, mergeWatermark, x, y, width, height);
	}

	/**
	 * Constructs a {@link RegionQuadTree} with a specified element limit and no
	 * merging watermark. As elements are removed, small sized child
	 * {@link RegionQuadTree}s will not be merged back together.
	 * 
	 * @param elementLimit
	 *            The maximum number of elements in a quad before it is split
	 *            into 4 child {@link RegionQuadTree}s
	 * @param x
	 *            The x coordinate of the {@link RegionQuadTree}
	 * @param y
	 *            The y coordiante of the {@link RegionQuadTree}
	 * @param width
	 *            The width of the {@link RegionQuadTree}
	 * @param height
	 *            The height of the {@link RegionQuadTree}
	 */
	public RegionQuadTree(int elementLimit, float x, float y, float width, float height) {
		super(elementLimit, x, y, width, height);
	}

	/**
	 * Constructs a {@link RegionQuadTree} as a child of another
	 * {@link RegionQuadTree}
	 * 
	 * @param parent
	 *            The parent {@link RegionQuadTree}
	 * @param x
	 *            The x coordinate of the {@link RegionQuadTree}
	 * @param y
	 *            The y coordiante of the {@link RegionQuadTree}
	 * @param width
	 *            The width of the {@link RegionQuadTree}
	 * @param height
	 *            The height of the {@link RegionQuadTree}
	 */
	public RegionQuadTree(RegionQuadTree<T> parent, float x, float y, float width, float height) {
		super(parent, x, y, width, height);
	}

	/**
	 * Constructs a {@link RegionQuadTree} with a specified minimum quad size,
	 * element limit and watermark
	 * 
	 * @param minimumQuadWidth
	 *            The minimum width of quads. Quads will not subdivide smaller
	 *            than this width.
	 * @param minimumQuadHeight
	 *            The minimum height of quads. Quads will not subdivide smaller
	 *            than this height.
	 * @param elementLimitPerQuad
	 *            The maximum number of elements in a quad before it is split
	 *            into 4 child {@link RegionQuadTree}s
	 * @param mergeWatermark
	 *            When a parent {@link RegionQuadTree}'s total elements go lower
	 *            than this mark, the child {@link PointQuadTree}s will be
	 *            merged back together
	 * @param x
	 *            The x coordinate of the {@link RegionQuadTree}
	 * @param y
	 *            The y coordiante of the {@link RegionQuadTree}
	 * @param width
	 *            The width of the {@link RegionQuadTree}
	 * @param height
	 *            The height of the {@link RegionQuadTree}
	 */
	public RegionQuadTree(float minimumQuadWidth, float minimumQuadHeight, int elementLimitPerQuad, int mergeWatermark,
			float x, float y, float width, float height) {
		super(minimumQuadWidth, minimumQuadHeight, elementLimitPerQuad, mergeWatermark, x, y, width, height);
	}

	@Override
	public void debugRender(Graphics g) {
		if(getX() - g.getTranslationX() > g.getViewportWidth()) {
			return;
		}
		if(getY() - g.getTranslationY() > g.getViewportHeight()) {
			return;
		}
		if(getMaxX() - g.getTranslationX() < 0f) {
			return;
		}
		if(getMaxY() - g.getTranslationY() < 0f) {
			return;
		}

		Color tmp = g.getColor();

		if (topLeft != null) {
			topLeft.debugRender(g);
			topRight.debugRender(g);
			bottomLeft.debugRender(g);
			bottomRight.debugRender(g);
		} else {
			g.setColor(QUAD_COLOR);
			g.drawRect(getX(), getY(), getWidth(), getHeight());
			g.setColor(tmp);
		}

		tmp = g.getColor();
		g.setColor(ELEMENT_COLOR);
		for (T element : elements) {
			g.drawRect(element.getX(), element.getY(), element.getWidth(), element.getHeight());
		}
		g.setColor(tmp);
	}

	@Override
	public void addAll(Array<T> elementsToAdd) {
		if (elementsToAdd == null || elementsToAdd.size == 0) {
			return;
		}
		clearTotalElementsCache();

		Array<T> elementsWithinQuad = new Array<T>();
		for (T element : elementsToAdd) {
			if (this.contains(element) || this.intersects(element)) {
				elementsWithinQuad.add(element);
			}
		}

		for (T element : elementsWithinQuad) {
			if (topLeft == null) {
				addElement(element);
				continue;
			}
			if (addElementToChild(element)) {
				continue;
			}
			addElement(element);
		}
	}

	@Override
	public boolean add(T element) {
		if (element == null)
			return false;

		if (!this.intersects(element) && !this.contains(element)) {
			return false;
		}
		clearTotalElementsCache();

		if (topLeft == null) {
			return addElement(element);
		}
		if (addElementToChild(element)) {
			return true;
		}
		return addElement(element);
	}

	@Override
	protected boolean addElementToChild(T element) {
		if (topLeft.contains(element)) {
			return topLeft.add(element);
		}
		if (topRight.contains(element)) {
			return topRight.add(element);
		}
		if (bottomLeft.contains(element)) {
			return bottomLeft.add(element);
		}
		if (bottomRight.contains(element)) {
			return bottomRight.add(element);
		}
		return false;
	}

	@Override
	protected void subdivide() {
		if (topLeft != null)
			return;

		float halfWidth = getWidth() / 2f;
		float halfHeight = getHeight() / 2f;

		topLeft = new RegionQuadTree<T>(this, getX(), getY(), halfWidth, halfHeight);
		topRight = new RegionQuadTree<T>(this, getX() + halfWidth, getY(), halfWidth, halfHeight);
		bottomLeft = new RegionQuadTree<T>(this, getX(), getY() + halfHeight, halfWidth, halfHeight);
		bottomRight = new RegionQuadTree<T>(this, getX() + halfWidth, getY() + halfHeight, halfWidth, halfHeight);

		for (int i = elements.size - 1; i >= 0; i--) {
			if (addElementToChild(elements.get(i))) {
				removeElement(elements.get(i));
			}
		}
	}

	@Override
	public boolean remove(T element) {
		if (element == null)
			return false;

		if (!this.intersects(element) && !this.contains(element)) {
			return false;
		}
		clearTotalElementsCache();

		if (removeElement(element)) {
			return true;
		}
		if (topLeft == null) {
			return false;
		}
		return removeElementFromChild(element);
	}

	@Override
	public void clear() {
		if (topLeft != null) {
			topLeft.clear();
			topRight.clear();
			bottomLeft.clear();
			bottomRight.clear();

			topLeft = null;
			topRight = null;
			bottomLeft = null;
			bottomRight = null;
		}
		elements.clear();
	}

	@Override
	public Array<T> getElementsWithinArea(Shape area) {
		Array<T> result = new Array<T>();
		getElementsWithinArea(result, area);
		return result;
	}

	@Override
	public void getElementsWithinArea(Array<T> result, Shape area) {
		if (topLeft != null) {
			if (topLeft.contains(area) || topLeft.intersects(area))
				topLeft.getElementsWithinArea(result, area);
			if (topRight.contains(area) || topRight.intersects(area))
				topRight.getElementsWithinArea(result, area);
			if (bottomLeft.contains(area) || bottomLeft.intersects(area))
				bottomLeft.getElementsWithinArea(result, area);
			if (bottomRight.contains(area) || bottomRight.intersects(area))
				bottomRight.getElementsWithinArea(result, area);
		}
		for (int i = elements.size - 1; i >= 0; i--) {
			T element = elements.get(i);
			if (element == null)
				continue;
			if (area.contains(element) || area.intersects(element)) {
				result.add(element);
			}
		}
	}

	@Override
	public Array<T> getElementsContainingPoint(Point point) {
		Array<T> result = new Array<T>();
		getElementsContainingPoint(result, point);
		return result;
	}

	@Override
	public void getElementsContainingPoint(Array<T> result, Point point) {
		if (topLeft != null) {
			if (topLeft.contains(point))
				topLeft.getElementsContainingPoint(result, point);
			if (topRight.contains(point))
				topRight.getElementsContainingPoint(result, point);
			if (bottomLeft.contains(point))
				bottomLeft.getElementsContainingPoint(result, point);
			if (bottomRight.contains(point))
				bottomRight.getElementsContainingPoint(result, point);
		}
		for (int i = elements.size - 1; i >= 0; i--) {
			T element = elements.get(i);
			if (element != null && element.contains(point)) {
				result.add(element);
			}
		}
	}

	@Override
	public Array<T> getElementsIntersectingLineSegment(LineSegment lineSegment) {
		Array<T> result = new Array<T>();
		getElementsIntersectingLineSegment(result, lineSegment);
		return result;
	}

	@Override
	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment) {
		if (topLeft != null) {
			if (topLeft.intersects(lineSegment) || topLeft.contains(lineSegment.getPointA())
					|| topLeft.contains(lineSegment.getPointB())) {
				topLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (topRight.intersects(lineSegment) || topRight.contains(lineSegment.getPointA())
					|| topRight.contains(lineSegment.getPointB())) {
				topRight.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (bottomLeft.intersects(lineSegment) || bottomLeft.contains(lineSegment.getPointA())
					|| bottomLeft.contains(lineSegment.getPointB())) {
				bottomLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (bottomRight.intersects(lineSegment) || bottomRight.contains(lineSegment.getPointA())
					|| bottomRight.contains(lineSegment.getPointB())) {
				bottomRight.getElementsIntersectingLineSegment(result, lineSegment);
			}
		}
		for (int i = elements.size - 1; i >= 0; i--) {
			T element = elements.get(i);
			if (element != null && element.intersects(lineSegment)) {
				result.add(element);
			}
		}
	}

	@Override
	public Array<T> getElements() {
		Array<T> result = new Array<T>();
		getElements(result);
		return result;
	}

	@Override
	public void getElements(Array<T> result) {
		if (topLeft != null) {
			((RegionQuadTree<T>) topLeft).getElements(result);
			((RegionQuadTree<T>) topRight).getElements(result);
			((RegionQuadTree<T>) bottomLeft).getElements(result);
			((RegionQuadTree<T>) bottomRight).getElements(result);
		}
		result.addAll(elements);
	}

	@Override
	public int getTotalElements() {
		if (totalElementsCache >= 0) {
			return totalElementsCache;
		}

		totalElementsCache = 0;
		if (topLeft != null) {
			totalElementsCache = topLeft.getTotalElements();
			totalElementsCache += topRight.getTotalElements();
			totalElementsCache += bottomLeft.getTotalElements();
			totalElementsCache += bottomRight.getTotalElements();
		}
		totalElementsCache += elements.size;
		return totalElementsCache;
	}

	@Override
	public void positionChanged(T moved) {
		if (this.contains(moved))
			return;

		removeElement(moved);

		QuadTree<T> parentQuad = parent;
		while (parentQuad != null) {
			if (parentQuad.add(moved)) {
				return;
			}
			parentQuad = parentQuad.getParent();
		}
	}
}
