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
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.geom.*;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.gdx.utils.Array;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Implements a thread-safe point quadtree
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Quadtree#Point_quadtree">
 *      Wikipedia: Point Quad Tree</a>
 */
public class ConcurrentPointQuadTree<T extends Positionable> extends Rectangle implements QuadTree<T> {
	private static final long serialVersionUID = 1926686293793174173L;

	public static Color QUAD_COLOR = Mdx.graphics != null ? Mdx.graphics.newColor(1f, 0f, 0f, 0.5f) : null;
	public static Color ELEMENT_COLOR = Mdx.graphics != null ? Mdx.graphics.newColor(0f, 0f, 1f, 0.5f) : null;

	protected ConcurrentPointQuadTree<T> parent;
	protected ConcurrentPointQuadTree<T> topLeft, topRight, bottomLeft, bottomRight;
	protected Array<T> elements;

	protected final int elementLimitPerQuad;
	protected final int mergeWatermark;
	protected final float minimumQuadWidth, minimumQuadHeight;
	protected final ReadWriteLock lock;

	protected int totalElementsCache = -1;
	protected int totalMerges = 0;

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
		this(parent.getMinimumQuadWidth(), parent.getMinimumQuadHeight(), parent.getElementLimitPerQuad(),
				parent.getMergeWatermark(), x, y, width, height);
		this.parent = parent;
	}

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
		this(PointQuadTree.DEFAULT_MINIMUM_QUAD_SIZE, PointQuadTree.DEFAULT_MINIMUM_QUAD_SIZE, elementLimitPerQuad,
				mergeWatermark, x, y, width, height);
	}

	/**
	 * Constructs a {@link ConcurrentPointQuadTree} with a specified minimum
	 * quad size, element limit and watermark
	 * 
	 * @param minimumQuadWidth
	 *            The minimum width of quads. Quads will not subdivide smaller
	 *            than this width.
	 * @param minimumQuadHeight
	 *            The minimum height of quads. Quads will not subdivide smaller
	 *            than this height.
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
	public ConcurrentPointQuadTree(float minimumQuadWidth, float minimumQuadHeight, int elementLimitPerQuad,
			int mergeWatermark, float x, float y, float width, float height) {
		super(x, y, width, height);

		if (mergeWatermark >= elementLimitPerQuad) {
			throw new QuadWatermarkException(elementLimitPerQuad, mergeWatermark);
		}

		this.elementLimitPerQuad = elementLimitPerQuad;
		this.mergeWatermark = mergeWatermark;
		this.minimumQuadWidth = minimumQuadWidth;
		this.minimumQuadHeight = minimumQuadHeight;
		this.lock = new ReentrantReadWriteLock(false);

		elements = new Array<T>(true, elementLimitPerQuad);
	}

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

		lock.readLock().lock();
		if (topLeft != null) {
			topLeft.debugRender(g);
			topRight.debugRender(g);
			bottomLeft.debugRender(g);
			bottomRight.debugRender(g);
		} else {
			g.setColor(QUAD_COLOR);
			g.drawShape(this);
			g.drawRect(getX(), getY(), getWidth(), getHeight());
			g.setColor(tmp);
		}

		tmp = g.getColor();
		g.setColor(ELEMENT_COLOR);
		for (T element : elements) {
			g.fillRect(element.getX(), element.getY(), 1f, 1f);
		}
		g.setColor(tmp);
		lock.readLock().unlock();
	}

	public void addAll(Array<T> elementsToAdd) {
		if (elementsToAdd == null || elementsToAdd.size == 0)
			return;
		clearTotalElementsCache();

		Array<T> elementsWithinQuad = new Array<T>();
		for (T element : elementsToAdd) {
			if (this.contains(element.getX(), element.getY())) {
				elementsWithinQuad.add(element);
			}
		}

		lock.writeLock().lock();

		if (topLeft != null) {
			lock.readLock().lock();
			lock.writeLock().unlock();
			for (T element : elementsWithinQuad) {
				if (topLeft.add(element)) {
					continue;
				}
				if (topRight.add(element)) {
					continue;
				}
				if (bottomLeft.add(element)) {
					continue;
				}
				if (bottomRight.add(element)) {
					continue;
				}
			}
			lock.readLock().unlock();
			return;
		}

		this.elements.addAll(elementsWithinQuad);
		for (T element : elementsWithinQuad) {
			element.addPostionChangeListener(this);
		}
		int totalElements = this.elements.size;
		lock.writeLock().unlock();

		if (totalElements > elementLimitPerQuad && (getWidth() * 0.5f) >= minimumQuadWidth
				&& (getHeight() * 0.5f) >= minimumQuadHeight) {
			subdivide();
		}
	}

	public boolean add(T element) {
		if (element == null)
			return false;

		if (!this.contains(element.getX(), element.getY())) {
			return false;
		}
		clearTotalElementsCache();
		return addElement(element);
	}

	protected boolean addElement(T element) {
		lock.writeLock().lock();

		// Another write may occur concurrently before this one
		if (topLeft != null) {
			lock.readLock().lock();
			lock.writeLock().unlock();
			return addElementToChild(element);
		}

		elements.add(element);
		element.addPostionChangeListener(this);
		int totalElements = elements.size;
		lock.writeLock().unlock();

		if (totalElements > elementLimitPerQuad && (getWidth() * 0.5f) >= minimumQuadWidth
				&& (getHeight() * 0.5f) >= minimumQuadHeight) {
			subdivide();
		}
		return true;
	}

	protected boolean addElementToChild(T element) {
		if (topLeft.add(element)) {
			lock.readLock().unlock();
			return true;
		}
		if (topRight.add(element)) {
			lock.readLock().unlock();
			return true;
		}
		if (bottomLeft.add(element)) {
			lock.readLock().unlock();
			return true;
		}
		if (bottomRight.add(element)) {
			lock.readLock().unlock();
			return true;
		}
		lock.readLock().unlock();
		return false;
	}

	protected void subdivide() {
		lock.readLock().lock();
		if (topLeft != null) {
			lock.readLock().unlock();
			return;
		}
		lock.readLock().unlock();

		lock.writeLock().lock();

		// Another write may occur concurrently before this one
		if (topLeft != null) {
			lock.writeLock().unlock();
			return;
		}

		float halfWidth = getWidth() / 2f;
		float halfHeight = getHeight() / 2f;

		topLeft = new ConcurrentPointQuadTree<T>(this, getX(), getY(), halfWidth, halfHeight);
		topRight = new ConcurrentPointQuadTree<T>(this, getX() + halfWidth, getY(), halfWidth, halfHeight);
		bottomLeft = new ConcurrentPointQuadTree<T>(this, getX(), getY() + halfHeight, halfWidth, halfHeight);
		bottomRight = new ConcurrentPointQuadTree<T>(this, getX() + halfWidth, getY() + halfHeight, halfWidth,
				halfHeight);

		for (int i = elements.size - 1; i >= 0; i--) {
			T element = elements.removeIndex(i);
			element.removePositionChangeListener(this);
			lock.readLock().lock();
			addElementToChild(element);
		}

		lock.writeLock().unlock();
	}

	protected boolean isMergable() {
		if (mergeWatermark <= 0) {
			return false;
		}

		lock.readLock().lock();
		int topLeftTotal = topLeft.getTotalElements();
		if (topLeftTotal >= mergeWatermark) {
			lock.readLock().unlock();
			return false;
		}

		int topRightTotal = topRight.getTotalElements();
		if (topRightTotal >= mergeWatermark) {
			lock.readLock().unlock();
			return false;
		}

		int bottomLeftTotal = bottomLeft.getTotalElements();
		if (bottomLeftTotal >= mergeWatermark) {
			lock.readLock().unlock();
			return false;
		}

		int bottomRightTotal = bottomRight.getTotalElements();
		if (bottomRightTotal >= mergeWatermark) {
			lock.readLock().unlock();
			return false;
		}

		lock.readLock().unlock();
		return topLeftTotal + topRightTotal + bottomLeftTotal + bottomRightTotal < mergeWatermark;
	}

	protected void merge() {
		if (topLeft == null) {
			return;
		}
		lock.readLock().unlock();

		lock.writeLock().lock();

		// Another write may occur concurrently before this one
		if (topLeft == null) {
			lock.readLock().lock();
			lock.writeLock().unlock();
			return;
		}

		topLeft.getElements(elements);
		topRight.getElements(elements);
		bottomLeft.getElements(elements);
		bottomRight.getElements(elements);

		for (T element : elements) {
			topLeft.elements.removeValue(element, false);
			element.removePositionChangeListener(topLeft);
			topRight.elements.removeValue(element, false);
			element.removePositionChangeListener(topRight);
			bottomLeft.elements.removeValue(element, false);
			element.removePositionChangeListener(bottomLeft);
			bottomRight.elements.removeValue(element, false);
			element.removePositionChangeListener(bottomRight);
			element.addPostionChangeListener(this);
		}
		
		totalMerges += topLeft.getTotalMergeOperations();
		totalMerges += topRight.getTotalMergeOperations();
		totalMerges += bottomLeft.getTotalMergeOperations();
		totalMerges += bottomRight.getTotalMergeOperations();
		totalMerges++;

		topLeft = null;
		topRight = null;
		bottomLeft = null;
		bottomRight = null;

		lock.writeLock().unlock();
		lock.readLock().lock();
	}

	public void removeAll(Array<T> elementsToRemove) {
		if (elementsToRemove == null || elementsToRemove.size == 0) {
			return;
		}
		clearTotalElementsCache();

		Array<T> elementsWithinQuad = new Array<T>();
		for (T element : elementsToRemove) {
			if (this.contains(element.getX(), element.getY())) {
				elementsWithinQuad.add(element);
			}
		}

		lock.readLock().lock();
		if (topLeft != null) {
			for (int i = elementsWithinQuad.size - 1; i >= 0; i--) {
				T element = elementsWithinQuad.get(i);
				if (topLeft.remove(element)) {
					elementsWithinQuad.removeIndex(i);
					continue;
				}
				if (topRight.remove(element)) {
					elementsWithinQuad.removeIndex(i);
					continue;
				}
				if (bottomLeft.remove(element)) {
					elementsWithinQuad.removeIndex(i);
					continue;
				}
				if (bottomRight.remove(element)) {
					elementsWithinQuad.removeIndex(i);
					continue;
				}
			}
		}
		lock.readLock().unlock();

		lock.writeLock().lock();
		elements.removeAll(elementsWithinQuad, false);
		lock.writeLock().unlock();

		for (T element : elementsWithinQuad) {
			element.removePositionChangeListener(this);
		}

		if (parent == null) {
			return;
		}
		if (parent.isMergable()) {
			parent.merge();
		}
	}

	@Override
	public void clear() {
		lock.writeLock().lock();
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
		lock.writeLock().unlock();
	}

	public boolean remove(T element) {
		if (element == null)
			return false;

		if (!this.contains(element.getX(), element.getY())) {
			return false;
		}
		clearTotalElementsCache();

		lock.readLock().lock();
		if (topLeft != null) {
			return removeElementFromChild(element);
		}
		lock.readLock().unlock();
		return removeElement(element, true);
	}

	protected boolean removeElementFromChild(T element) {
		if (topLeft.remove(element)) {
			lock.readLock().unlock();
			return true;
		}
		if (topRight.remove(element)) {
			lock.readLock().unlock();
			return true;
		}
		if (bottomLeft.remove(element)) {
			lock.readLock().unlock();
			return true;
		}
		if (bottomRight.remove(element)) {
			lock.readLock().unlock();
			return true;
		}
		lock.readLock().unlock();
		return false;
	}

	protected boolean removeElement(T element, boolean topDownInvocation) {
		lock.writeLock().lock();

		// Another write may occur concurrently before this one
		if (topLeft != null) {
			lock.readLock().lock();
			lock.writeLock().unlock();
			return removeElementFromChild(element);
		}

		boolean result = elements.removeValue(element, false);
		lock.writeLock().unlock();
		element.removePositionChangeListener(this);

		if (parent == null) {
			return result;
		}
		if (result && parent.isMergable()) {
			if(!topDownInvocation) {
				parent.lock.readLock().lock();
			}
			parent.merge();
			if(!topDownInvocation) {
				parent.lock.readLock().unlock();
			}
		}
		return result;
	}

	@Override
	public Array<T> getElementsWithinArea(Shape area) {
		Array<T> result = new Array<T>();
		getElementsWithinArea(result, area);
		return result;
	}

	@Override
	public void getElementsWithinArea(Array<T> result, Shape area) {
		lock.readLock().lock();
		if (topLeft != null) {
			topLeft.getElementsWithinArea(result, area);
			topRight.getElementsWithinArea(result, area);
			bottomLeft.getElementsWithinArea(result, area);
			bottomRight.getElementsWithinArea(result, area);
		} else {
			for (int i = elements.size - 1; i >= 0; i--) {
				T element = elements.get(i);
				if (element != null && area.contains(element.getX(), element.getY())) {
					result.add(element);
				}
			}
		}
		lock.readLock().unlock();
	}

	@Override
	public Array<T> getElementsContainingPoint(Point point) {
		Array<T> result = new Array<T>();
		getElementsContainingPoint(result, point);
		return result;
	}

	@Override
	public void getElementsContainingPoint(Array<T> result, Point point) {
		lock.readLock().lock();
		if (topLeft != null) {
			if (topLeft.contains(point)) {
				topLeft.getElementsContainingPoint(result, point);
			}
			if (topRight.contains(point)) {
				topRight.getElementsContainingPoint(result, point);
			}
			if (bottomLeft.contains(point)) {
				bottomLeft.getElementsContainingPoint(result, point);
			}
			if (bottomRight.contains(point)) {
				bottomRight.getElementsContainingPoint(result, point);
			}
		} else {
			for (int i = elements.size - 1; i >= 0; i--) {
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
		lock.readLock().unlock();
	}

	public Array<T> getElementsIntersectingLineSegment(LineSegment lineSegment) {
		Array<T> result = new Array<T>();
		getElementsIntersectingLineSegment(result, lineSegment);
		return result;
	}

	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment) {
		lock.readLock().lock();
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
		} else {
			for (int i = elements.size - 1; i >= 0; i--) {
				T element = elements.get(i);
				if (element != null && lineSegment.contains(element.getX(), element.getY())) {
					result.add(element);
				}
			}
		}
		lock.readLock().unlock();
	}

	public Array<T> getElements() {
		Array<T> result = new Array<T>();
		getElements(result);
		return result;
	}

	public void getElements(Array<T> result) {
		lock.readLock().lock();
		if (topLeft != null) {
			topLeft.getElements(result);
			topRight.getElements(result);
			bottomLeft.getElements(result);
			bottomRight.getElements(result);
		} else {
			result.addAll(elements);
		}
		lock.readLock().unlock();
	}

	public int getTotalQuads() {
		lock.readLock().lock();
		if (topLeft != null) {
			int result = topLeft.getTotalQuads();
			result += topRight.getTotalQuads();
			result += bottomLeft.getTotalQuads();
			result += bottomRight.getTotalQuads();
			lock.readLock().unlock();
			return result;
		} else {
			lock.readLock().unlock();
			return 1;
		}
	}

	public int getTotalElements() {
		if (totalElementsCache >= 0) {
			return totalElementsCache;
		}
		lock.readLock().lock();
		if (topLeft != null) {
			totalElementsCache = topLeft.getTotalElements();
			totalElementsCache += topRight.getTotalElements();
			totalElementsCache += bottomLeft.getTotalElements();
			totalElementsCache += bottomRight.getTotalElements();
		} else {
			totalElementsCache = elements.size;
		}
		lock.readLock().unlock();
		return totalElementsCache;
	}

	protected void clearTotalElementsCache() {
		totalElementsCache = -1;
	}

	@Override
	public void positionChanged(T moved) {
		if (this.contains(moved.getX(), moved.getY()))
			return;

		removeElement(moved, false);

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
	
	public int getTotalMergeOperations() {
		lock.readLock().lock();
		int result = totalMerges;
		if(topLeft != null) {
			result += topLeft.getTotalMergeOperations();
			result += topRight.getTotalMergeOperations();
			result += bottomLeft.getTotalMergeOperations();
			result += bottomRight.getTotalMergeOperations();
		}
		lock.readLock().unlock();
		return result;
	}

	public int getElementLimitPerQuad() {
		return elementLimitPerQuad;
	}

	public int getMergeWatermark() {
		return mergeWatermark;
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
