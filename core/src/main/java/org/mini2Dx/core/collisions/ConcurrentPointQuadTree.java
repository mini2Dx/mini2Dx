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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.mini2Dx.core.engine.Positionable;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Parallelogram;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.graphics.Color;

/**
 * Implements a thread-safe point quadtree
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Quadtree#Point_quadtree">
 *      Wikipedia: Point Quad Tree</a>
 */
public class ConcurrentPointQuadTree<T extends Positionable> extends Rectangle implements QuadTree<T> {
	private static final long serialVersionUID = 1926686293793174173L;

	public static Color QUAD_COLOR = new Color(1f, 0f, 0f, 0.5f);
	public static Color ELEMENT_COLOR = new Color(0f, 0f, 1f, 0.5f);

	protected ConcurrentPointQuadTree<T> parent;
	protected ConcurrentPointQuadTree<T> topLeft, topRight, bottomLeft, bottomRight;
	protected List<T> elements;

	protected final int elementLimitPerQuad;
	protected final int mergeWatermark;
	protected final ReadWriteLock topLeftLock, topRightLock, bottomLeftLock, bottomRightLock;

	protected int totalElementsCache = -1;

	/**
	 * Constructs a {@link ConcurrentPointQuadTree} with a specified element
	 * limit and watermark
	 * 
	 * @param elementLimitPerQuad
	 *            The maximum number of elements in a quad before it is split
	 *            into 4 child {@link ConcurrentPointQuadTree}s
	 * @param mergeWatermark
	 *            When a parent {@link ConcurrentPointQuadTree}'s total elements
	 *            go lower than this mark, the child
	 *            {@link ConcurrentPointQuadTree}s will be merged back together
	 * @param x
	 *            The x coordinate of the {@link ConcurrentPointQuadTree}
	 * @param y
	 *            The y coordiante of the {@link ConcurrentPointQuadTree}
	 * @param width
	 *            The width of the {@link ConcurrentPointQuadTree}
	 * @param height
	 *            The height of the {@link ConcurrentPointQuadTree}
	 */
	public ConcurrentPointQuadTree(int elementLimitPerQuad, int mergeWatermark, float x, float y, float width,
			float height) {
		super(x, y, width, height);

		if (mergeWatermark >= elementLimitPerQuad) {
			throw new QuadWatermarkException(elementLimitPerQuad, mergeWatermark);
		}

		this.elementLimitPerQuad = elementLimitPerQuad;
		this.mergeWatermark = mergeWatermark;
		this.topLeftLock = new ReentrantReadWriteLock(false);
		this.topRightLock = new ReentrantReadWriteLock(false);
		this.bottomLeftLock = new ReentrantReadWriteLock(false);
		this.bottomRightLock = new ReentrantReadWriteLock(false);

		elements = new CopyOnWriteArrayList<T>();
	}

	/**
	 * Constructs a {@link ConcurrentPointQuadTree} with a specified element
	 * limit and no merging watermark. As elements are removed, small sized
	 * child {@link ConcurrentPointQuadTree}s will not be merged back together.
	 * 
	 * @param elementLimitPerQuad
	 *            The maximum number of elements in a quad before it is split
	 *            into 4 child {@link ConcurrentPointQuadTree}s
	 * @param x
	 *            The x coordinate of the {@link ConcurrentPointQuadTree}
	 * @param y
	 *            The y coordiante of the {@link ConcurrentPointQuadTree}
	 * @param width
	 *            The width of the {@link ConcurrentPointQuadTree}
	 * @param height
	 *            The height of the {@link ConcurrentPointQuadTree}
	 */
	public ConcurrentPointQuadTree(int elementLimitPerQuad, float x, float y, float width, float height) {
		this(elementLimitPerQuad, 0, x, y, width, height);
	}

	/**
	 * Constructs a {@link ConcurrentPointQuadTree} as a child of another
	 * {@link ConcurrentPointQuadTree}
	 * 
	 * @param parent
	 *            The parent {@link ConcurrentPointQuadTree}
	 * @param x
	 *            The x coordinate of the {@link ConcurrentPointQuadTree}
	 * @param y
	 *            The y coordiante of the {@link ConcurrentPointQuadTree}
	 * @param width
	 *            The width of the {@link ConcurrentPointQuadTree}
	 * @param height
	 *            The height of the {@link ConcurrentPointQuadTree}
	 */
	public ConcurrentPointQuadTree(ConcurrentPointQuadTree<T> parent, float x, float y, float width, float height) {
		this(parent.getElementLimitPerQuad(), x, y, width, height);
		this.parent = parent;
	}

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
			g.drawShape(this);
			g.drawRect(x, y, width, height);
			g.setColor(tmp);
		}

		tmp = g.getColor();
		g.setColor(ELEMENT_COLOR);
		for (T element : elements) {
			g.fillRect(element.getX(), element.getY(), 1f, 1f);
		}
		g.setColor(tmp);
	}

	public boolean add(T element) {
		if (element == null)
			return false;

		if (!this.contains(element.getX(), element.getY())) {
			return false;
		}
		clearTotalElementsCache();

		topLeftLock.readLock().lock();
		if (topLeft != null) {
			topLeftLock.readLock().unlock();
			return addElementToChild(element);
		}
		topLeftLock.readLock().unlock();
		return addElement(element);
	}

	protected boolean addElement(T element) {
		elements.add(element);
		element.addPostionChangeListener(this);

		if (elements.size() > elementLimitPerQuad && width >= 2f && height >= 2f) {
			subdivide();
		}
		return true;
	}

	protected boolean addElementToChild(T element) {
		topLeftLock.writeLock().lock();
		if (topLeft.add(element)) {
			topLeftLock.writeLock().unlock();
			return true;
		}
		topLeftLock.writeLock().unlock();

		topRightLock.writeLock().lock();
		if (topRight.add(element)) {
			topRightLock.writeLock().unlock();
			return true;
		}
		topRightLock.writeLock().unlock();

		bottomLeftLock.writeLock().lock();
		if (bottomLeft.add(element)) {
			bottomLeftLock.writeLock().unlock();
			return true;
		}
		bottomLeftLock.writeLock().unlock();

		bottomRightLock.writeLock().lock();
		if (bottomRight.add(element)) {
			bottomRightLock.writeLock().unlock();
			return true;
		}
		bottomRightLock.writeLock().unlock();

		return false;
	}

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

		topLeft = new ConcurrentPointQuadTree<T>(this, x, y, halfWidth, halfHeight);
		topRight = new ConcurrentPointQuadTree<T>(this, x + halfWidth, y, halfWidth, halfHeight);
		bottomLeft = new ConcurrentPointQuadTree<T>(this, x, y + halfHeight, halfWidth, halfHeight);
		bottomRight = new ConcurrentPointQuadTree<T>(this, x + halfWidth, y + halfHeight, halfWidth, halfHeight);

		for (int i = elements.size() - 1; i >= 0; i--) {
			T element = elements.remove(i);
			element.removePositionChangeListener(this);
			addElementToChild(element);
		}

		topLeftLock.writeLock().unlock();
		topRightLock.writeLock().unlock();
		bottomLeftLock.writeLock().unlock();
		bottomRightLock.writeLock().unlock();
	}

	protected boolean isMergable() {
		if (mergeWatermark <= 0) {
			return false;
		}

		topLeftLock.readLock().lock();
		int topLeftTotal = topLeft.getTotalElements();
		if (topLeftTotal >= mergeWatermark) {
			topLeftLock.readLock().unlock();
			return false;
		}
		topLeftLock.readLock().unlock();

		topRightLock.readLock().lock();
		int topRightTotal = topRight.getTotalElements();
		if (topRightTotal >= mergeWatermark) {
			topRightLock.readLock().unlock();
			return false;
		}
		topRightLock.readLock().unlock();

		bottomLeftLock.readLock().lock();
		int bottomLeftTotal = bottomLeft.getTotalElements();
		if (bottomLeftTotal >= mergeWatermark) {
			bottomLeftLock.readLock().unlock();
			return false;
		}
		bottomLeftLock.readLock().unlock();

		bottomRightLock.readLock().lock();
		int bottomRightTotal = bottomRight.getTotalElements();
		if (bottomRightTotal >= mergeWatermark) {
			bottomRightLock.readLock().unlock();
			return false;
		}
		bottomRightLock.readLock().unlock();

		return topLeftTotal + topRightTotal + bottomLeftTotal + bottomRightTotal < mergeWatermark;
	}

	protected void merge() {
		topLeftLock.readLock().lock();
		if (topLeft == null) {
			topLeftLock.readLock().unlock();
			return;
		}
		topLeftLock.readLock().unlock();

		topLeftLock.writeLock().lock();
		topRightLock.writeLock().lock();
		bottomLeftLock.writeLock().lock();
		bottomRightLock.writeLock().lock();

		topLeft.getElements(elements);
		topRight.getElements(elements);
		bottomLeft.getElements(elements);
		bottomRight.getElements(elements);

		for (T element : elements) {
			element.removePositionChangeListener(topLeft);
			element.removePositionChangeListener(topRight);
			element.removePositionChangeListener(bottomLeft);
			element.removePositionChangeListener(bottomRight);
			element.addPostionChangeListener(this);
		}

		topLeft = null;
		topRight = null;
		bottomLeft = null;
		bottomRight = null;

		topLeftLock.writeLock().unlock();
		topRightLock.writeLock().unlock();
		bottomLeftLock.writeLock().unlock();
		bottomRightLock.writeLock().unlock();
	}

	public boolean remove(T element) {
		if (element == null)
			return false;

		if (!this.contains(element.getX(), element.getY())) {
			return false;
		}
		clearTotalElementsCache();

		topLeftLock.readLock().lock();
		if (topLeft != null) {
			topLeftLock.readLock().unlock();
			return removeElementFromChild(element);
		}
		topLeftLock.readLock().unlock();
		return removeElement(element);
	}

	protected boolean removeElementFromChild(T element) {
		topLeftLock.writeLock().lock();
		if (topLeft.remove(element)) {
			topLeftLock.writeLock().unlock();
			return true;
		}
		topLeftLock.writeLock().unlock();

		topRightLock.writeLock().lock();
		if (topRight.remove(element)) {
			topRightLock.writeLock().unlock();
			return true;
		}
		topRightLock.writeLock().unlock();

		bottomLeftLock.writeLock().lock();
		if (bottomLeft.remove(element)) {
			bottomLeftLock.writeLock().unlock();
			return true;
		}
		bottomLeftLock.writeLock().unlock();

		bottomRightLock.writeLock().lock();
		if (bottomRight.remove(element)) {
			bottomRightLock.writeLock().unlock();
			return true;
		}
		bottomRightLock.writeLock().unlock();

		return false;
	}

	protected boolean removeElement(T element) {
		boolean result = elements.remove(element);
		element.removePositionChangeListener(this);

		if (parent == null) {
			return result;
		}
		if (parent.isMergable()) {
			parent.merge();
		}
		return result;
	}

	public List<T> getElementsWithinRegion(Parallelogram parallelogram) {
		List<T> result = new ArrayList<T>();
		getElementsWithinRegion(result, parallelogram);
		return result;
	}

	public void getElementsWithinRegion(Collection<T> result, Parallelogram parallelogram) {
		topLeftLock.readLock().lock();
		if (topLeft != null) {
			topLeft.getElementsWithinRegion(result, parallelogram);
			topLeftLock.readLock().unlock();

			topRightLock.readLock().lock();
			topRight.getElementsWithinRegion(result, parallelogram);
			topRightLock.readLock().unlock();

			bottomLeftLock.readLock().lock();
			bottomLeft.getElementsWithinRegion(result, parallelogram);
			bottomLeftLock.readLock().unlock();

			bottomRightLock.readLock().lock();
			bottomRight.getElementsWithinRegion(result, parallelogram);
			bottomRightLock.readLock().unlock();
		} else {
			topLeftLock.readLock().unlock();
			for (int i = elements.size() - 1; i >= 0; i--) {
				T element = elements.get(i);
				if (element != null && parallelogram.contains(element.getX(), element.getY())) {
					result.add(element);
				}
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
			for (int i = elements.size() - 1; i >= 0; i--) {
				T element = elements.get(i);
				if (element == null) {
					continue;
				}
				if (element.getX() != point.x) {
					continue;
				}
				if (element.getY() != point.y) {
					continue;
				}
				result.add(element);
			}
		}
	}

	public List<T> getElementsIntersectingLineSegment(LineSegment lineSegment) {
		List<T> result = new ArrayList<T>();
		getElementsIntersectingLineSegment(result, lineSegment);
		return result;
	}

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
			for (int i = elements.size() - 1; i >= 0; i--) {
				T element = elements.get(i);
				if (element != null && lineSegment.contains(element.getX(), element.getY())) {
					result.add(element);
				}
			}
		}
	}

	public List<T> getElements() {
		List<T> result = new ArrayList<T>();
		getElements(result);
		return result;
	}

	public void getElements(List<T> result) {
		topLeftLock.readLock().lock();
		if (topLeft != null) {
			topLeft.getElements(result);
			topLeftLock.readLock().unlock();

			topRightLock.readLock().lock();
			topRight.getElements(result);
			topRightLock.readLock().unlock();

			bottomLeftLock.readLock().lock();
			bottomLeft.getElements(result);
			bottomLeftLock.readLock().unlock();

			bottomRightLock.readLock().lock();
			bottomRight.getElements(result);
			bottomRightLock.readLock().unlock();
		} else {
			topLeftLock.readLock().unlock();
			result.addAll(elements);
		}
	}

	public int getTotalQuads() {
		topLeftLock.readLock().lock();
		if (topLeft != null) {
			int result = topLeft.getTotalQuads();
			topLeftLock.readLock().unlock();

			topRightLock.readLock().lock();
			result += topRight.getTotalQuads();
			topRightLock.readLock().unlock();

			bottomLeftLock.readLock().lock();
			result += bottomLeft.getTotalQuads();
			bottomLeftLock.readLock().unlock();

			bottomRightLock.readLock().lock();
			result += bottomRight.getTotalQuads();
			bottomRightLock.readLock().unlock();

			return result;
		} else {
			topLeftLock.readLock().unlock();
			return 1;
		}
	}

	public int getTotalElements() {
		if (totalElementsCache >= 0) {
			return totalElementsCache;
		}
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
			totalElementsCache = elements.size();
		}
		return totalElementsCache;
	}

	protected void clearTotalElementsCache() {
		totalElementsCache = -1;
	}

	@Override
	public void positionChanged(T moved) {
		if (this.contains(moved.getX(), moved.getY()))
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

	public QuadTree<T> getParent() {
		return parent;
	}

	public int getElementLimitPerQuad() {
		return elementLimitPerQuad;
	}

	public int getMergeWatermark() {
		return mergeWatermark;
	}
}
