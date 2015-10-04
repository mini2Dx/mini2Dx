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
package org.mini2Dx.core.collisions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mini2Dx.core.engine.geom.CollisionBox;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Parallelogram;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.graphics.Color;

/**
 * Implements a thread-safe region quadtree
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Quadtree#The_region_quadtree">
 *      Wikipedia: Region Quad Tree</a>
 */
public class ConcurrentRegionQuadTree<T extends CollisionBox> extends ConcurrentPointQuadTree<T> {
	private static final long serialVersionUID = 2344163859287984782L;

	/**
	 * Constructs a {@link ConcurrentRegionQuadTree} with a specified element
	 * limit and watermark
	 * 
	 * @param elementLimitPerQuad
	 *            The maximum number of elements in a
	 *            {@link ConcurrentRegionQuadTree} before it is split into 4
	 *            child quads
	 * @param mergeWatermark
	 *            When a parent {@link ConcurrentRegionQuadTree}'s total
	 *            elements go lower than this mark, the child
	 *            {@link ConcurrentRegionQuadTree}s will be merged back together
	 * @param x
	 *            The x coordinate of the {@link ConcurrentRegionQuadTree}
	 * @param y
	 *            The y coordiante of the {@link ConcurrentRegionQuadTree}
	 * @param width
	 *            The width of the {@link ConcurrentRegionQuadTree}
	 * @param height
	 *            The height of the {@link ConcurrentRegionQuadTree}
	 */
	public ConcurrentRegionQuadTree(int elementLimit, int mergeWatermark, float x, float y, float width, float height) {
		super(elementLimit, mergeWatermark, x, y, width, height);
	}

	/**
	 * Constructs a {@link ConcurrentRegionQuadTree} with a specified element
	 * limit and no merging watermark. As elements are removed, small sized
	 * child {@link ConcurrentRegionQuadTree}s will not be merged back together.
	 * 
	 * @param elementLimitPerQuad
	 *            The maximum number of elements in a quad before it is split
	 *            into 4 child {@link ConcurrentRegionQuadTree}s
	 * @param x
	 *            The x coordinate of the {@link ConcurrentRegionQuadTree}
	 * @param y
	 *            The y coordiante of the {@link ConcurrentRegionQuadTree}
	 * @param width
	 *            The width of the {@link ConcurrentRegionQuadTree}
	 * @param height
	 *            The height of the {@link ConcurrentRegionQuadTree}
	 */
	public ConcurrentRegionQuadTree(int elementLimit, float x, float y, float width, float height) {
		super(elementLimit, x, y, width, height);
	}

	/**
	 * Constructs a {@link ConcurrentRegionQuadTree} as a child of another
	 * {@link ConcurrentRegionQuadTree}
	 * 
	 * @param parent
	 *            The parent {@link ConcurrentRegionQuadTree}
	 * @param x
	 *            The x coordinate of the {@link ConcurrentRegionQuadTree}
	 * @param y
	 *            The y coordiante of the {@link ConcurrentRegionQuadTree}
	 * @param width
	 *            The width of the {@link ConcurrentRegionQuadTree}
	 * @param height
	 *            The height of the {@link ConcurrentRegionQuadTree}
	 */
	public ConcurrentRegionQuadTree(ConcurrentRegionQuadTree<T> parent, float x, float y, float width, float height) {
		super(parent, x, y, width, height);
	}

	@Override
	public void debugRender(Graphics g) {
		Color tmp = g.getColor();

		topLeftLock.readLock().lock();
		if (topLeft != null) {
			topLeft.debugRender(g);
			topLeftLock.readLock().unlock();

			topRightLock.readLock().lock();
			topRight.debugRender(g);
			topRightLock.readLock().unlock();

			bottomLeftLock.readLock().lock();
			bottomLeft.debugRender(g);
			bottomLeftLock.readLock().unlock();

			bottomRightLock.readLock().lock();
			bottomRight.debugRender(g);
			bottomRightLock.readLock().unlock();
		} else {
			topLeftLock.readLock().unlock();
			g.setColor(QUAD_COLOR);
			g.drawRect(x, y, width, height);
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
	public boolean add(T element) {
		if (element == null)
			return false;

		if (!this.intersects(element) && !this.contains(element)) {
			return false;
		}
		clearTotalElementsCache();

		topLeftLock.readLock().lock();
		if (topLeft == null) {
			topLeftLock.readLock().unlock();
			return addElement(element);
		}
		topLeftLock.readLock().unlock();

		if (addElementToChild(element)) {
			return true;
		}
		if (!addElement(element)) {
			return false;
		}
		return true;
	}

	@Override
	protected boolean addElementToChild(T element) {
		topLeftLock.readLock().lock();
		if (topLeft.contains(element)) {
			topLeftLock.readLock().unlock();

			topLeftLock.writeLock().lock();
			boolean result = topLeft.add(element);
			topLeftLock.writeLock().unlock();
			return result;
		}
		topLeftLock.readLock().unlock();

		topRightLock.readLock().lock();
		if (topRight.contains(element)) {
			topRightLock.readLock().unlock();

			topRightLock.writeLock().lock();
			boolean result = topRight.add(element);
			topRightLock.writeLock().unlock();
			return result;
		}
		topRightLock.readLock().unlock();

		bottomLeftLock.readLock().lock();
		if (bottomLeft.contains(element)) {
			bottomLeftLock.readLock().unlock();

			bottomLeftLock.writeLock().lock();
			boolean result = bottomLeft.add(element);
			bottomLeftLock.writeLock().unlock();
			return result;
		}
		bottomLeftLock.readLock().unlock();

		bottomRightLock.readLock().lock();
		if (bottomRight.contains(element)) {
			bottomRightLock.readLock().unlock();

			bottomRightLock.writeLock().lock();
			boolean result = bottomRight.add(element);
			bottomRightLock.writeLock().unlock();
			return result;
		}
		bottomRightLock.readLock().unlock();

		return false;
	}

	@Override
	protected void subdivide() {
		topLeftLock.readLock().lock();
		if (topLeft != null) {
			topLeftLock.readLock().unlock();
			return;
		}
		topLeftLock.readLock().unlock();

		topLeftLock.writeLock().lock();
		topRightLock.writeLock().lock();
		bottomLeftLock.writeLock().lock();
		bottomRightLock.writeLock().lock();

		float halfWidth = width / 2f;
		float halfHeight = height / 2f;

		topLeft = new ConcurrentRegionQuadTree<T>(this, x, y, halfWidth, halfHeight);
		topRight = new ConcurrentRegionQuadTree<T>(this, x + halfWidth, y, halfWidth, halfHeight);
		bottomLeft = new ConcurrentRegionQuadTree<T>(this, x, y + halfHeight, halfWidth, halfHeight);
		bottomRight = new ConcurrentRegionQuadTree<T>(this, x + halfWidth, y + halfHeight, halfWidth, halfHeight);

		for (int i = elements.size() - 1; i >= 0; i--) {
			if (addElementToChild(elements.get(i))) {
				removeElement(elements.get(i));
			}
		}

		topLeftLock.writeLock().unlock();
		topRightLock.writeLock().unlock();
		bottomLeftLock.writeLock().unlock();
		bottomRightLock.writeLock().unlock();
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

		topLeftLock.readLock().lock();
		if (topLeft == null) {
			topLeftLock.readLock().unlock();
			return false;
		}
		topLeftLock.readLock().unlock();
		return removeElementFromChild(element);
	}

	@Override
	public List<T> getElementsWithinRegion(Parallelogram parallelogram) {
		List<T> result = new ArrayList<T>();
		getElementsWithinRegion(result, parallelogram);
		return result;
	}

	@Override
	public void getElementsWithinRegion(Collection<T> result, Parallelogram parallelogram) {
		topLeftLock.readLock().lock();
		if (topLeft != null) {
			if (topLeft.contains(parallelogram) || topLeft.intersects(parallelogram)) {
				topLeft.getElementsWithinRegion(result, parallelogram);
			}
			topLeftLock.readLock().unlock();

			topRightLock.readLock().lock();
			if (topRight.contains(parallelogram) || topRight.intersects(parallelogram)) {
				topRight.getElementsWithinRegion(result, parallelogram);
			}
			topRightLock.readLock().unlock();

			bottomLeftLock.readLock().lock();
			if (bottomLeft.contains(parallelogram) || bottomLeft.intersects(parallelogram)) {
				bottomLeft.getElementsWithinRegion(result, parallelogram);
			}
			bottomLeftLock.readLock().unlock();

			bottomRightLock.readLock().lock();
			if (bottomRight.contains(parallelogram) || bottomRight.intersects(parallelogram)) {
				bottomRight.getElementsWithinRegion(result, parallelogram);
			}
			bottomRightLock.readLock().unlock();
		} else {
			topLeftLock.readLock().unlock();
		}
		for (int i = elements.size() - 1; i >= 0; i--) {
			T element = elements.get(i);
			if (element == null)
				continue;
			if (parallelogram.contains(element) || parallelogram.intersects(element)) {
				result.add(element);
			}
		}
	}

	@Override
	public List<T> getElementsContainingPoint(Point point) {
		List<T> result = new ArrayList<T>();
		getElementsContainingPoint(result, point);
		return result;
	}

	@Override
	public void getElementsContainingPoint(Collection<T> result, Point point) {
		topLeftLock.readLock().lock();
		if (topLeft != null) {
			if (topLeft.contains(point)) {
				topLeft.getElementsContainingPoint(result, point);
			}
			topLeftLock.readLock().unlock();

			topRightLock.readLock().lock();
			if (topRight.contains(point)) {
				topRight.getElementsContainingPoint(result, point);
			}
			topRightLock.readLock().unlock();

			bottomLeftLock.readLock().lock();
			if (bottomLeft.contains(point)) {
				bottomLeft.getElementsContainingPoint(result, point);
			}
			bottomLeftLock.readLock().unlock();

			bottomRightLock.readLock().lock();
			if (bottomRight.contains(point)) {
				bottomRight.getElementsContainingPoint(result, point);
			}
			bottomRightLock.readLock().unlock();
		} else {
			topLeftLock.readLock().unlock();
		}
		for (int i = elements.size() - 1; i >= 0; i--) {
			T element = elements.get(i);
			if (element != null && element.contains(point)) {
				result.add(element);
			}
		}
	}

	@Override
	public List<T> getElementsIntersectingLineSegment(LineSegment lineSegment) {
		List<T> result = new ArrayList<T>();
		getElementsIntersectingLineSegment(result, lineSegment);
		return result;
	}

	@Override
	public void getElementsIntersectingLineSegment(Collection<T> result, LineSegment lineSegment) {
		topLeftLock.readLock().lock();
		if (topLeft != null) {
			if (topLeft.intersects(lineSegment) || topLeft.contains(lineSegment.getPointA())
					|| topLeft.contains(lineSegment.getPointB())) {
				topLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			topLeftLock.readLock().unlock();

			topRightLock.readLock().lock();
			if (topRight.intersects(lineSegment) || topRight.contains(lineSegment.getPointA())
					|| topRight.contains(lineSegment.getPointB())) {
				topRight.getElementsIntersectingLineSegment(result, lineSegment);
			}
			topRightLock.readLock().unlock();

			bottomLeftLock.readLock().lock();
			if (bottomLeft.intersects(lineSegment) || bottomLeft.contains(lineSegment.getPointA())
					|| bottomLeft.contains(lineSegment.getPointB())) {
				bottomLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			bottomLeftLock.readLock().unlock();

			bottomRightLock.readLock().lock();
			if (bottomRight.intersects(lineSegment) || bottomRight.contains(lineSegment.getPointA())
					|| bottomRight.contains(lineSegment.getPointB())) {
				bottomRight.getElementsIntersectingLineSegment(result, lineSegment);
			}
			bottomRightLock.readLock().unlock();
		} else {
			topLeftLock.readLock().unlock();
		}
		for (int i = elements.size() - 1; i >= 0; i--) {
			T element = elements.get(i);
			if (element != null && element.intersects(lineSegment)) {
				result.add(element);
			}
		}
	}

	@Override
	public List<T> getElements() {
		List<T> result = new ArrayList<T>();
		getElements(result);
		return new ArrayList<T>(result);
	}

	@Override
	public void getElements(List<T> result) {
		topLeftLock.readLock().lock();
		if (topLeft != null) {
			((ConcurrentRegionQuadTree<T>) topLeft).getElements(result);
			topLeftLock.readLock().unlock();

			topRightLock.readLock().lock();
			((ConcurrentRegionQuadTree<T>) topRight).getElements(result);
			topRightLock.readLock().unlock();

			bottomLeftLock.readLock().lock();
			((ConcurrentRegionQuadTree<T>) bottomLeft).getElements(result);
			bottomLeftLock.readLock().unlock();

			bottomRightLock.readLock().lock();
			((ConcurrentRegionQuadTree<T>) bottomRight).getElements(result);
			bottomRightLock.readLock().unlock();
		} else {
			topLeftLock.readLock().unlock();
		}
		result.addAll(elements);
	}

	@Override
	public int getTotalElements() {
		if (totalElementsCache >= 0) {
			return totalElementsCache;
		}

		totalElementsCache = 0;

		topLeftLock.readLock().lock();
		if (topLeft != null) {
			totalElementsCache = topLeft.getTotalElements();
			topLeftLock.readLock().unlock();

			topRightLock.readLock().lock();
			totalElementsCache += topRight.getTotalElements();
			topRightLock.readLock().unlock();

			bottomLeftLock.readLock().lock();
			totalElementsCache += bottomLeft.getTotalElements();
			bottomLeftLock.readLock().unlock();

			bottomRightLock.readLock().lock();
			totalElementsCache += bottomRight.getTotalElements();
			bottomRightLock.readLock().unlock();
		} else {
			topLeftLock.readLock().unlock();
		}
		totalElementsCache += elements.size();
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