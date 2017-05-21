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

import org.mini2Dx.core.engine.geom.CollisionShape;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.Shape;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.graphics.Color;

/**
 * Implements a thread-safe region quadtree
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Quadtree#The_region_quadtree">
 *      Wikipedia: Region Quad Tree</a>
 */
public class ConcurrentRegionQuadTree<T extends CollisionShape> extends ConcurrentPointQuadTree<T> {
	private static final long serialVersionUID = 2344163859287984782L;

	/**
	 * Constructs a {@link ConcurrentRegionQuadTree} with a specified element
	 * limit and watermark
	 * 
	 * @param elementLimit
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
	 * @param elementLimit
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
	
	/**
	 * Constructs a {@link ConcurrentRegionQuadTree} with a specified minimum
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
	 *            into 4 child {@link ConcurrentRegionQuadTree}s
	 * @param mergeWatermark
	 *            When a parent {@link ConcurrentRegionQuadTree}'s total elements
	 *            go lower than this mark, the child
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
	public ConcurrentRegionQuadTree(float minimumQuadWidth, float minimumQuadHeight, int elementLimitPerQuad,
			int mergeWatermark, float x, float y, float width, float height) {
		super(minimumQuadWidth, minimumQuadHeight, elementLimitPerQuad, mergeWatermark, x, y, width, height);
	}

	@Override
	public void debugRender(Graphics g) {
		Color tmp = g.getColor();

		lock.readLock().lock();
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
		lock.readLock().unlock();
		g.setColor(tmp);
	}
	
	@Override
	public void addAll(List<T> elements) {
		if (elements == null || elements.isEmpty())
			return;
		
		List<T> elementsWithinQuad = new ArrayList<T>();
		for(T element : elements) {
			if (this.contains(element.getShape()) || this.intersects(element.getShape())) {
				elementsWithinQuad.add(element);
			}
		}
		clearTotalElementsCache();
		
		lock.writeLock().lock();
		
		if(topLeft != null) {
			lock.readLock().lock();
			lock.writeLock().unlock();
			for(int i = elementsWithinQuad.size() - 1; i >= 0; i--) {
				T element = elementsWithinQuad.get(i);
				if (topLeft.add(element)) {
					elementsWithinQuad.remove(i);
					continue;
				}
				if (topRight.add(element)) {
					elementsWithinQuad.remove(i);
					continue;
				}
				if (bottomLeft.add(element)) {
					elementsWithinQuad.remove(i);
					continue;
				}
				if (bottomRight.add(element)) {
					elementsWithinQuad.remove(i);
					continue;
				}
			}
			lock.readLock().unlock();
			if(elementsWithinQuad.isEmpty()) {
				return;
			}
			lock.writeLock().lock();
		}
		
		this.elements.addAll(elementsWithinQuad);
		for(T element : elementsWithinQuad) {
			element.addPostionChangeListener(this);
		}
		int totalElements = this.elements.size();
		lock.writeLock().unlock();
		
		if (totalElements > elementLimitPerQuad && getWidth() >= 2f && getHeight() >= 2f) {
			subdivide();
		}
	}

	@Override
	public boolean add(T element) {
		if (element == null) {
			return false;
		}

		if (!this.intersects(element.getShape()) && !this.contains(element.getShape())) {
			return false;
		}
		clearTotalElementsCache();
		
		if (!addElement(element)) {
			return false;
		}
		return true;
	}
	
	@Override
	protected boolean addElement(T element) {
		lock.writeLock().lock();
		
		//Another write may occur concurrently before this one
		if(topLeft != null) {
			lock.readLock().lock();
			lock.writeLock().unlock();
			boolean result = addElementToChild(element);
			if(result) {
				return true;
			}
			lock.writeLock().lock();
		}
		
		elements.add(element);
		element.addPostionChangeListener(this);

		if (elements.size() > elementLimitPerQuad && getWidth() >= 2f && getHeight() >= 2f) {
			subdivide();
		}
		lock.writeLock().unlock();
		return true;
	}

	@Override
	protected boolean addElementToChild(T element) {
		Shape shape = element.getShape();
		if (topLeft.contains(shape)) {
			boolean result = topLeft.add(element);
			lock.readLock().unlock();
			return result;
		}
		if (topRight.contains(shape)) {
			boolean result = topRight.add(element);
			lock.readLock().unlock();
			return result;
		}
		if (bottomLeft.contains(shape)) {
			boolean result = bottomLeft.add(element);
			lock.readLock().unlock();
			return result;
		}
		if (bottomRight.contains(shape)) {
			boolean result = bottomRight.add(element);
			lock.readLock().unlock();
			return result;
		}
		lock.readLock().unlock();
		return false;
	}

	@Override
	protected void subdivide() {
		lock.readLock().lock();
		if (topLeft != null) {
			lock.readLock().unlock();
			return;
		}
		lock.readLock().unlock();

		lock.writeLock().lock();
		
		//Another write may occur concurrently before this one
		if (topLeft != null) {
			lock.writeLock().unlock();
			return;
		}

		float halfWidth = getWidth() / 2f;
		float halfHeight = getHeight() / 2f;

		topLeft = new ConcurrentRegionQuadTree<T>(this, getX(), getY(), halfWidth, halfHeight);
		topRight = new ConcurrentRegionQuadTree<T>(this, getX() + halfWidth, getY(), halfWidth, halfHeight);
		bottomLeft = new ConcurrentRegionQuadTree<T>(this, getX(), getY() + halfHeight, halfWidth, halfHeight);
		bottomRight = new ConcurrentRegionQuadTree<T>(this, getX() + halfWidth, getY() + halfHeight, halfWidth, halfHeight);

		for (int i = elements.size() - 1; i >= 0; i--) {
			lock.readLock().lock();
			T element = elements.get(i);
			if (addElementToChild(element)) {
				elements.remove(element);
				element.removePositionChangeListener(this);
			}
		}

		lock.writeLock().unlock();
	}
	
	@Override
	public void removeAll(List<T> elementsToRemove) {
		if(elementsToRemove == null || elementsToRemove.isEmpty()) {
			return;
		}
		clearTotalElementsCache();
		
		List<T> elementsWithinQuad = new ArrayList<T>();
		for(T element : elementsToRemove) {
			if(this.contains(element.getShape()) || this.intersects(element.getShape())) {
				elementsWithinQuad.add(element);
			}
		}
		
		lock.writeLock().lock();
		if(topLeft != null) {
			for(int i = elementsWithinQuad.size() - 1; i >= 0; i--) {
				T element = elementsWithinQuad.get(i);
				if (topLeft.remove(element)) {
					elementsWithinQuad.remove(i);
					continue;
				}
				if (topRight.remove(element)) {
					elementsWithinQuad.remove(i);
					continue;
				}
				if (bottomLeft.remove(element)) {
					elementsWithinQuad.remove(i);
					continue;
				}
				if (bottomRight.remove(element)) {
					elementsWithinQuad.remove(i);
					continue;
				}
			}
		}
		
		elements.removeAll(elementsWithinQuad);
		lock.writeLock().unlock();
		
		for(T element : elementsWithinQuad) {
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
	public boolean remove(T element) {
		if (element == null)
			return false;

		if (!this.intersects(element.getShape()) && !this.contains(element.getShape())) {
			return false;
		}
		clearTotalElementsCache();
		return removeElement(element);
	}
	
	@Override
	protected boolean removeElement(T element) {
		lock.writeLock().lock();
		
		//Another write may occur concurrently before this one
		if(topLeft != null) {
			lock.readLock().lock();
			lock.writeLock().unlock();
			boolean result = removeElementFromChild(element);
			if(result) {
				return true;
			}
			lock.writeLock().lock();
		}
		
		boolean result = elements.remove(element);
		lock.writeLock().unlock();
		element.removePositionChangeListener(this);

		if (parent == null) {
			return result;
		}
		if (result && parent.isMergable()) {
			parent.merge();
		}
		return result;
	}

	@Override
	public List<T> getElementsWithinArea(Shape area) {
		List<T> result = new ArrayList<T>();
		getElementsWithinArea(result, area);
		return result;
	}

	@Override
	public void getElementsWithinArea(Collection<T> result, Shape area) {
		lock.readLock().lock();
		if (topLeft != null) {
			if (topLeft.contains(area) || topLeft.intersects(area)) {
				topLeft.getElementsWithinArea(result, area);
			}
			if (topRight.contains(area) || topRight.intersects(area)) {
				topRight.getElementsWithinArea(result, area);
			}
			if (bottomLeft.contains(area) || bottomLeft.intersects(area)) {
				bottomLeft.getElementsWithinArea(result, area);
			}
			if (bottomRight.contains(area) || bottomRight.intersects(area)) {
				bottomRight.getElementsWithinArea(result, area);
			}
		}
		for (int i = elements.size() - 1; i >= 0; i--) {
			T element = elements.get(i);
			if (element == null)
				continue;
			if (area.contains(element.getShape()) || area.intersects(element.getShape())) {
				result.add(element);
			}
		}
		lock.readLock().unlock();
	}

	@Override
	public List<T> getElementsContainingPoint(Point point) {
		List<T> result = new ArrayList<T>();
		getElementsContainingPoint(result, point);
		return result;
	}

	@Override
	public void getElementsContainingPoint(Collection<T> result, Point point) {
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
		}
		for (int i = elements.size() - 1; i >= 0; i--) {
			T element = elements.get(i);
			if (element != null && element.contains(point)) {
				result.add(element);
			}
		}
		lock.readLock().unlock();
	}

	@Override
	public List<T> getElementsIntersectingLineSegment(LineSegment lineSegment) {
		List<T> result = new ArrayList<T>();
		getElementsIntersectingLineSegment(result, lineSegment);
		return result;
	}

	@Override
	public void getElementsIntersectingLineSegment(Collection<T> result, LineSegment lineSegment) {
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
		}
		for (int i = elements.size() - 1; i >= 0; i--) {
			T element = elements.get(i);
			if (element != null && element.intersects(lineSegment)) {
				result.add(element);
			}
		}
		lock.readLock().unlock();
	}

	@Override
	public List<T> getElements() {
		List<T> result = new ArrayList<T>();
		getElements(result);
		return new ArrayList<T>(result);
	}

	@Override
	public void getElements(List<T> result) {
		lock.readLock().lock();
		if (topLeft != null) {
			((ConcurrentRegionQuadTree<T>) topLeft).getElements(result);
			((ConcurrentRegionQuadTree<T>) topRight).getElements(result);
			((ConcurrentRegionQuadTree<T>) bottomLeft).getElements(result);
			((ConcurrentRegionQuadTree<T>) bottomRight).getElements(result);
		}
		result.addAll(elements);
		lock.readLock().unlock();
	}

	@Override
	public int getTotalElements() {
		if (totalElementsCache >= 0) {
			return totalElementsCache;
		}

		totalElementsCache = 0;

		lock.readLock().lock();
		if (topLeft != null) {
			totalElementsCache = topLeft.getTotalElements();
			totalElementsCache += topRight.getTotalElements();
			totalElementsCache += bottomLeft.getTotalElements();
			totalElementsCache += bottomRight.getTotalElements();
		}
		totalElementsCache += elements.size();
		lock.readLock().unlock();
		return totalElementsCache;
	}

	@Override
	public void positionChanged(T moved) {
		if (this.contains(moved.getShape()))
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