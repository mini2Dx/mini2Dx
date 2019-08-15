/*******************************************************************************
 * Copyright 2019 See AUTHORS file
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
			g.setColor(ELEMENT_COLOR);
			for (T element : elements) {
				g.fillRect(element.getX(), element.getY(), 1f, 1f);
			}
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
		QuadTreeAwareUtils.setQuadTreeRef(element, this);
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
		if (result) {
			if (parent.isMergable()) {
				if (!topDownInvocation) {
					parent.lock.readLock().lock();
				}
				parent.merge();
				if (!topDownInvocation) {
					parent.lock.readLock().unlock();
				}
			}
			QuadTreeAwareUtils.removeQuadTreeRef(element);
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
	public Array<T> getElementsWithinArea(Shape area, QuadTreeSearchDirection searchDirection) {
		Array<T> result = new Array<>();

		switch (searchDirection){
			case UPWARDS:
				getElementsWithinArea(result, area, searchDirection);
				break;
			case DOWNWARDS:
				getElementsWithinArea(result, area);
				break;
		}

		return result;
	}

	protected void addElementsWithinArea(Array<T> result, Shape area) {
		for (int i = elements.size - 1; i >= 0; i--) {
			T element = elements.get(i);
			if (element != null && area.contains(element.getX(), element.getY())) {
				result.add(element);
			}
		}
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
			addElementsWithinArea(result, area);
		}
		lock.readLock().unlock();
	}

	@Override
	public void getElementsWithinArea(Array<T> result, Shape area, QuadTreeSearchDirection searchDirection) {
		switch (searchDirection){
			case UPWARDS:
				lock.readLock().lock();
				if (elements != null) {
					addElementsWithinArea(result, area);
				}
				if (parent != null) {
					if (parent.topLeft != this && (area.contains(parent.topLeft) || area.intersects(parent.topLeft))) {
						parent.topLeft.getElementsWithinArea(result, area);
					}
					if (parent.topRight != this && (area.contains(parent.topRight) || area.intersects(parent.topRight))) {
						parent.topRight.getElementsWithinArea(result, area);
					}
					if (parent.bottomLeft != this && (area.contains(parent.bottomLeft) || area.intersects(parent.bottomLeft))) {
						parent.bottomLeft.getElementsWithinArea(result, area);
					}
					if (parent.bottomRight != this && (area.contains(parent.bottomRight) || area.intersects(parent.bottomRight))) {
						parent.bottomRight.getElementsWithinArea(result, area);
					}
					parent.getElementsWithinArea(result, area, searchDirection);
				}
				lock.readLock().unlock();
				break;
			case DOWNWARDS:
				getElementsWithinArea(result, area);
				break;
		}
	}

	@Override
	public Array<T> getElementsContainingPoint(Point point) {
		Array<T> result = new Array<T>();
		getElementsContainingPoint(result, point);
		return result;
	}

	@Override
	public Array<T> getElementsContainingPoint(Point point, QuadTreeSearchDirection searchDirection) {
		Array<T> result = new Array<>();

		switch (searchDirection){
			case UPWARDS:
				getElementsContainingPoint(result, point, searchDirection);
				break;
			case DOWNWARDS:
				getElementsContainingPoint(result, point);
				break;
		}

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

	protected void addElementsContainingPoint(Array<T> result, Point point) {
		lock.readLock().lock();
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
		lock.readLock().unlock();
	}

	@Override
	public void getElementsContainingPoint(Array<T> result, Point point, QuadTreeSearchDirection searchDirection) {
		switch (searchDirection){
			case UPWARDS:
				if (elements != null){
					addElementsContainingPoint(result, point);
				}
				break;
			case DOWNWARDS:
				getElementsContainingPoint(result, point);
				break;
		}
	}

	public Array<T> getElementsIntersectingLineSegment(LineSegment lineSegment) {
		Array<T> result = new Array<T>();
		getElementsIntersectingLineSegment(result, lineSegment);
		return result;
	}

	@Override
	public Array<T> getElementsIntersectingLineSegment(LineSegment lineSegment, QuadTreeSearchDirection searchDirection) {
		Array<T> result = new Array<>();

		switch (searchDirection){
			case UPWARDS:
				getElementsIntersectingLineSegment(result, lineSegment, searchDirection);
				break;
			case DOWNWARDS:
				getElementsIntersectingLineSegment(result, lineSegment);
				break;
		}

		return result;
	}

	protected static boolean intersects(ConcurrentPointQuadTree tree, LineSegment segment){
		return tree.intersects(segment) || tree.contains(segment.getPointA()) || tree.contains(segment.getPointB());
	}

	protected void addElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment) {
		for (int i = elements.size - 1; i >= 0; i--) {
			T element = elements.get(i);
			if (element != null && lineSegment.contains(element.getX(), element.getY())) {
				result.add(element);
			}
		}
	}

	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment) {
		lock.readLock().lock();
		if (topLeft != null) {
			if (intersects(topLeft, lineSegment)) {
				topLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (intersects(topRight, lineSegment)) {
				topRight.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (intersects(bottomLeft, lineSegment)) {
				bottomLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (intersects(bottomRight, lineSegment)) {
				bottomRight.getElementsIntersectingLineSegment(result, lineSegment);
			}
		} else {
			addElementsIntersectingLineSegment(result, lineSegment);
		}
		lock.readLock().unlock();
	}

	@Override
	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment, QuadTreeSearchDirection searchDirection) {
		switch (searchDirection){
			case UPWARDS:
				lock.readLock().lock();
				if (elements != null) {
					addElementsIntersectingLineSegment(result, lineSegment);
				}
				if (parent != null) {
					if (parent.topLeft != this && intersects(parent.topLeft, lineSegment)) {
						parent.topLeft.getElementsIntersectingLineSegment(result, lineSegment);
					}
					if (parent.topRight != this && intersects(parent.topRight, lineSegment)) {
						parent.topRight.getElementsIntersectingLineSegment(result, lineSegment);
					}
					if (parent.bottomLeft != this && intersects(parent.bottomLeft, lineSegment)) {
						parent.bottomLeft.getElementsIntersectingLineSegment(result, lineSegment);
					}
					if (parent.bottomRight != this && intersects(parent.bottomRight, lineSegment)) {
						parent.bottomRight.getElementsIntersectingLineSegment(result, lineSegment);
					}
					parent.getElementsIntersectingLineSegment(result, lineSegment, searchDirection);
				}
				lock.readLock().unlock();
				break;
			case DOWNWARDS:
				getElementsIntersectingLineSegment(result, lineSegment);
				break;
		}
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
