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
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.Shape;
import org.mini2Dx.core.geom.Sizeable;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.gdx.utils.Array;

/**
 * Implements a thread-safe region quadtree
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Quadtree#The_region_quadtree">
 *      Wikipedia: Region Quad Tree</a>
 */
public class ConcurrentRegionQuadTree<T extends Sizeable> extends ConcurrentPointQuadTree<T> {
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

		lock.lockRead();
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
		lock.unlockRead();
		g.setColor(tmp);
	}
	
	@Override
	public void addAll(Array<T> elements) {
		if (elements == null || elements.size == 0)
			return;

		Array<T> elementsWithinQuad = new Array<T>();
		for(T element : elements) {
			if (this.contains(element) || this.intersects(element)) {
				elementsWithinQuad.add(element);
			}
		}
		clearTotalElementsCache();
		
		lock.lockWrite();
		
		if(topLeft != null) {
			lock.lockRead();
			lock.unlockWrite();
			for(int i = elementsWithinQuad.size - 1; i >= 0; i--) {
				T element = elementsWithinQuad.get(i);
				if (topLeft.add(element)) {
					elementsWithinQuad.removeIndex(i);
					continue;
				}
				if (topRight.add(element)) {
					elementsWithinQuad.removeIndex(i);
					continue;
				}
				if (bottomLeft.add(element)) {
					elementsWithinQuad.removeIndex(i);
					continue;
				}
				if (bottomRight.add(element)) {
					elementsWithinQuad.removeIndex(i);
					continue;
				}
			}
			lock.unlockRead();
			if(elementsWithinQuad.size == 0) {
				return;
			}
			lock.lockWrite();
		}
		
		this.elements.addAll(elementsWithinQuad);
		for(T element : elementsWithinQuad) {
			element.addPostionChangeListener(this);
		}
		int totalElements = this.elements.size;
		lock.unlockWrite();
		
		if (totalElements > elementLimitPerQuad && getWidth() >= 2f && getHeight() >= 2f) {
			subdivide();
		}
	}

	@Override
	public boolean add(T element) {
		if (element == null) {
			return false;
		}

		if (!this.intersects(element) && !this.contains(element)) {
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
		lock.lockWrite();
		
		//Another write may occur concurrently before this one
		if(topLeft != null) {
			lock.lockRead();
			lock.unlockWrite();
			boolean result = addElementToChild(element);
			if(result) {
				return true;
			}
			lock.lockWrite();
		}
		
		elements.add(element);
		element.addPostionChangeListener(this);
		QuadTreeAwareUtils.setQuadTreeRef(element, this);
		if (elements.size > elementLimitPerQuad && getWidth() >= 2f && getHeight() >= 2f) {
			subdivide();
		}
		lock.unlockWrite();
		return true;
	}

	@Override
	protected boolean addElementToChild(T element) {
		if (topLeft.contains(element)) {
			boolean result = topLeft.add(element);
			lock.unlockRead();
			return result;
		}
		if (topRight.contains(element)) {
			boolean result = topRight.add(element);
			lock.unlockRead();
			return result;
		}
		if (bottomLeft.contains(element)) {
			boolean result = bottomLeft.add(element);
			lock.unlockRead();
			return result;
		}
		if (bottomRight.contains(element)) {
			boolean result = bottomRight.add(element);
			lock.unlockRead();
			return result;
		}
		lock.unlockRead();
		return false;
	}

	@Override
	protected void subdivide() {
		lock.lockRead();
		if (topLeft != null) {
			lock.unlockRead();
			return;
		}
		lock.unlockRead();

		lock.lockWrite();
		
		//Another write may occur concurrently before this one
		if (topLeft != null) {
			lock.unlockWrite();
			return;
		}

		float halfWidth = getWidth() / 2f;
		float halfHeight = getHeight() / 2f;

		topLeft = new ConcurrentRegionQuadTree<T>(this, getX(), getY(), halfWidth, halfHeight);
		topRight = new ConcurrentRegionQuadTree<T>(this, getX() + halfWidth, getY(), halfWidth, halfHeight);
		bottomLeft = new ConcurrentRegionQuadTree<T>(this, getX(), getY() + halfHeight, halfWidth, halfHeight);
		bottomRight = new ConcurrentRegionQuadTree<T>(this, getX() + halfWidth, getY() + halfHeight, halfWidth, halfHeight);

		for (int i = elements.size - 1; i >= 0; i--) {
			lock.lockRead();
			T element = elements.get(i);
			if (addElementToChild(element)) {
				elements.removeValue(element, false);
				element.removePositionChangeListener(this);
			}
		}

		lock.unlockWrite();
	}
	
	@Override
	public void removeAll(Array<T> elementsToRemove) {
		if(elementsToRemove == null || elementsToRemove.size == 0) {
			return;
		}
		clearTotalElementsCache();

		Array<T> elementsWithinQuad = new Array<T>();
		for(T element : elementsToRemove) {
			if(this.contains(element) || this.intersects(element)) {
				elementsWithinQuad.add(element);
			}
		}
		
		lock.lockRead();
		if(topLeft != null) {
			for(int i = elementsWithinQuad.size - 1; i >= 0; i--) {
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
		lock.unlockRead();
		
		lock.lockWrite();
		elements.removeAll(elementsWithinQuad, false);
		lock.unlockWrite();
		
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

		if (!this.intersects(element) && !this.contains(element)) {
			return false;
		}
		clearTotalElementsCache();
		return removeElement(element, true);
	}
	
	@Override
	protected boolean removeElement(T element, boolean topDownInvocation) {
		lock.lockWrite();
		
		//Another write may occur concurrently before this one
		if(topLeft != null) {
			lock.lockRead();
			lock.unlockWrite();
			boolean result = removeElementFromChild(element);
			if(result) {
				return true;
			}
			lock.lockWrite();
		}
		
		boolean result = elements.removeValue(element, false);
		lock.unlockWrite();
		element.removePositionChangeListener(this);

		if (parent == null) {
			return result;
		}
		if (result) {
			if (parent.isMergable()) {
				if (!topDownInvocation) {
					parent.lock.lockRead();
				}
				parent.merge();
				if (!topDownInvocation) {
					parent.lock.unlockRead();
				}
			}
			QuadTreeAwareUtils.removeQuadTreeRef(element);
		}
		return result;
	}

	@Override
	protected void addElementsWithinArea(Array<T> result, Shape area) {
		for (int i = elements.size - 1; i >= 0; i--) {
			T element = elements.get(i);
			if (element != null && (area.contains(element) || area.intersects(element))) {
				result.add(element);
			}
		}
	}

	@Override
	public void getElementsWithinArea(Array<T> result, Shape area) {
		lock.lockRead();
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
		addElementsWithinArea(result, area);
		lock.unlockRead();
	}

	@Override
	public void getElementsWithinArea(Array<T> result, Shape area, QuadTreeSearchDirection searchDirection) {
		switch (searchDirection){
			case UPWARDS:
				getElementsWithinAreaUpwards(result, area, true);
				break;
			case DOWNWARDS:
				getElementsWithinArea(result, area);
				break;
		}
	}

	private void getElementsWithinAreaUpwards(Array<T> result, Shape area, boolean firstInvocation) {
		lock.lockRead();
		if (elements != null) {
			addElementsWithinArea(result, area);
		}
		if (firstInvocation && topLeft != null){
			if (area.contains(topLeft) || area.intersects(topLeft)){
				topLeft.getElementsWithinArea(result, area);
			}
			if (area.contains(topRight) || area.intersects(topRight)){
				topRight.getElementsWithinArea(result, area);
			}
			if (area.contains(bottomLeft) || area.intersects(bottomLeft)){
				bottomLeft.getElementsWithinArea(result, area);
			}
			if (area.contains(bottomRight) || area.intersects(bottomRight)){
				bottomRight.getElementsWithinArea(result, area);
			}
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
			((ConcurrentRegionQuadTree<T>)parent).getElementsWithinAreaUpwards(result, area, false);
		}
		lock.unlockRead();
	}

	@Override
	public Array<T> getElementsContainingArea(Shape area, boolean entirelyContained) {
		Array<T> result = new Array<T>();
		getElementsContainingArea(result, area, entirelyContained);
		return result;
	}

	@Override
	public Array<T> getElementsContainingArea(Shape area, QuadTreeSearchDirection searchDirection, boolean entirelyContained) {
		Array<T> result = new Array<>();

		switch (searchDirection){
		case UPWARDS:
			getElementsContainingArea(result, area, searchDirection, entirelyContained);
			break;
		case DOWNWARDS:
			getElementsContainingArea(result, area, entirelyContained);
			break;
		}

		return result;
	}

	protected void addElementsContainingArea(Array<T> result, Shape area, boolean entirelyContained) {
		if(entirelyContained) {
			for (int i = elements.size - 1; i >= 0; i--) {
				T element = elements.get(i);
				if (element != null && element.contains(area)) {
					result.add(element);
				}
			}
		} else {
			for (int i = elements.size - 1; i >= 0; i--) {
				T element = elements.get(i);
				if (element != null && (element.contains(area) || element.intersects(area))) {
					//If area is larger than element it is not contained.
					if(area.getWidth() > element.getWidth() || area.getHeight() > element.getHeight()) {
						continue;
					}
					result.add(element);
				}
			}
		}
	}

	@Override
	public void getElementsContainingArea(Array<T> result, Shape area, boolean entirelyContained) {
		lock.lockRead();
		if (topLeft != null) {
			if (topLeft.contains(area) || topLeft.intersects(area))
				topLeft.getElementsContainingArea(result, area, entirelyContained);
			if (topRight.contains(area) || topRight.intersects(area))
				topRight.getElementsContainingArea(result, area, entirelyContained);
			if (bottomLeft.contains(area) || bottomLeft.intersects(area))
				bottomLeft.getElementsContainingArea(result, area, entirelyContained);
			if (bottomRight.contains(area) || bottomRight.intersects(area))
				bottomRight.getElementsContainingArea(result, area, entirelyContained);
		}
		addElementsContainingArea(result, area, entirelyContained);
		lock.unlockRead();
	}

	@Override
	public void getElementsContainingArea(Array<T> result, Shape area, QuadTreeSearchDirection searchDirection, boolean entirelyContained) {
		switch (searchDirection){
		case UPWARDS:
			getElementsContainingAreaUpwards(result, area, true, entirelyContained);
			break;
		case DOWNWARDS:
			getElementsContainingArea(result, area, entirelyContained);
			break;
		}
	}

	protected void getElementsContainingAreaUpwards(Array<T> result, Shape area, boolean firstInvocation, boolean entirelyContained) {
		lock.lockRead();
		if (elements != null) {
			addElementsContainingArea(result, area, entirelyContained);
		}
		if (firstInvocation && topLeft != null){
			if (area.contains(topLeft) || area.intersects(topLeft)){
				topLeft.getElementsContainingArea(result, area, entirelyContained);
			}
			if (area.contains(topRight) || area.intersects(topRight)){
				topRight.getElementsContainingArea(result, area, entirelyContained);
			}
			if (area.contains(bottomLeft) || area.intersects(bottomLeft)){
				bottomLeft.getElementsContainingArea(result, area, entirelyContained);
			}
			if (area.contains(bottomRight) || area.intersects(bottomRight)){
				bottomRight.getElementsContainingArea(result, area, entirelyContained);
			}
		}
		if (parent != null) {
			if (parent.topLeft != this && (area.contains(parent.topLeft) || area.intersects(parent.topLeft))) {
				parent.topLeft.getElementsContainingArea(result, area, entirelyContained);
			}
			if (parent.topRight != this && (area.contains(parent.topRight) || area.intersects(parent.topRight))) {
				parent.topRight.getElementsContainingArea(result, area, entirelyContained);
			}
			if (parent.bottomLeft != this && (area.contains(parent.bottomLeft) || area.intersects(parent.bottomLeft))) {
				parent.bottomLeft.getElementsContainingArea(result, area, entirelyContained);
			}
			if (parent.bottomRight != this && (area.contains(parent.bottomRight) || area.intersects(parent.bottomRight))) {
				parent.bottomRight.getElementsContainingArea(result, area, entirelyContained);
			}
			((ConcurrentRegionQuadTree<T>)parent).getElementsContainingAreaUpwards(result, area, false, entirelyContained);
		}
		lock.unlockRead();
	}

	protected void addElementsContainingPoint(Array<T> result, Point point) {
		for (int i = elements.size - 1; i >= 0; i--) {
			T element = elements.get(i);
			if (element != null && element.contains(point)) {
				result.add(element);
			}
		}
	}

	@Override
	public void getElementsContainingPoint(Array<T> result, Point point) {
		lock.lockRead();
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
		addElementsContainingPoint(result, point);
		lock.unlockRead();
	}

	@Override
	public void getElementsContainingPoint(Array<T> result, Point point, QuadTreeSearchDirection searchDirection) {
		switch (searchDirection){
			case UPWARDS:
				getElementsContainingPointUpwards(result, point, true);
				break;
			case DOWNWARDS:
				getElementsContainingPoint(result, point);
				break;
		}
	}

	private void getElementsContainingPointUpwards(Array<T> result, Point point, boolean firstInvocation) {
		lock.lockRead();
		if (elements != null){
			addElementsContainingPoint(result, point);
		}
		if (firstInvocation && topLeft != null){
			if (topLeft.contains(point)){
				topLeft.getElementsContainingPoint(result, point);
			}
			if (topRight.contains(point)){
				topRight.getElementsContainingPoint(result, point);
			}
			if (bottomLeft.contains(point)){
				bottomLeft.getElementsContainingPoint(result, point);
			}
			if (bottomRight.contains(point)){
				bottomRight.getElementsContainingPoint(result, point);
			}
		}
		if (parent != null){
			if (parent.topLeft != this && (parent.topLeft.contains(point) || parent.topLeft.contains(point))) {
				parent.topLeft.getElementsContainingPoint(result, point);
			}
			if (parent.topRight != this && (parent.topRight.contains(point) || parent.topRight.contains(point))) {
				parent.topRight.getElementsContainingPoint(result, point);
			}
			if (parent.bottomLeft != this && (parent.bottomLeft.contains(point) || parent.bottomLeft.contains(point))) {
				parent.bottomLeft.getElementsContainingPoint(result, point);
			}
			if (parent.bottomRight != this && (parent.bottomRight.contains(point) || parent.bottomRight.contains(point))) {
				parent.bottomRight.getElementsContainingPoint(result, point);
			}
			((ConcurrentRegionQuadTree<T>)parent).getElementsContainingPointUpwards(result, point, false);
		}
		lock.unlockRead();
	}

	@Override
	protected void addElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment) {
		for (int i = elements.size - 1; i >= 0; i--) {
			T element = elements.get(i);
			if (element != null && element.intersects(lineSegment)) {
				result.add(element);
			}
		}
	}

	@Override
	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment) {
		lock.lockRead();
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
		}
		addElementsIntersectingLineSegment(result, lineSegment);
		lock.unlockRead();
	}

	@Override
	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment, QuadTreeSearchDirection searchDirection) {

		switch (searchDirection){
			case UPWARDS:
				addElementsIntersectingLineSegmentUpwards(result, lineSegment, true);
				break;
			case DOWNWARDS:
				getElementsIntersectingLineSegment(result, lineSegment);
				break;
		}
	}

	private void addElementsIntersectingLineSegmentUpwards(Array<T> result, LineSegment lineSegment, boolean firstInvocation) {
		lock.lockRead();
		if (elements != null) {
			addElementsIntersectingLineSegment(result, lineSegment);
		}
		if (topLeft != null && firstInvocation){
			if (intersects(topLeft, lineSegment)){
				topLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (intersects(topRight, lineSegment)){
				topRight.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (intersects(bottomLeft, lineSegment)){
				bottomLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (intersects(bottomRight, lineSegment)){
				bottomRight.getElementsIntersectingLineSegment(result, lineSegment);
			}
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
			((ConcurrentRegionQuadTree<T>)parent).addElementsIntersectingLineSegmentUpwards(result, lineSegment, false);
		}
		lock.unlockRead();
	}

	@Override
	public Array<T> getElements() {
		Array<T> result = new Array<T>();
		getElements(result);
		return result;
	}

	@Override
	public void getElements(Array<T> result) {
		lock.lockRead();
		if (topLeft != null) {
			((ConcurrentRegionQuadTree<T>) topLeft).getElements(result);
			((ConcurrentRegionQuadTree<T>) topRight).getElements(result);
			((ConcurrentRegionQuadTree<T>) bottomLeft).getElements(result);
			((ConcurrentRegionQuadTree<T>) bottomRight).getElements(result);
		}
		result.addAll(elements);
		lock.unlockRead();
	}

	@Override
	public int getTotalElements() {
		if (totalElementsCache >= 0) {
			return totalElementsCache;
		}

		totalElementsCache = 0;

		lock.lockRead();
		if (topLeft != null) {
			totalElementsCache = topLeft.getTotalElements();
			totalElementsCache += topRight.getTotalElements();
			totalElementsCache += bottomLeft.getTotalElements();
			totalElementsCache += bottomRight.getTotalElements();
		}
		totalElementsCache += elements.size;
		lock.unlockRead();
		return totalElementsCache;
	}

	@Override
	public void positionChanged(T moved) {
		if (this.contains(moved))
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
}