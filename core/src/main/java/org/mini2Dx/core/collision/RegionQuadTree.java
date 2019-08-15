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
		addElementsWithinArea(result, area);
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
			((RegionQuadTree<T>)parent).getElementsWithinAreaUpwards(result, area, false);
		}
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
			((RegionQuadTree<T>)parent).getElementsContainingPointUpwards(result, point, false);
		}
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
			((RegionQuadTree<T>)parent).addElementsIntersectingLineSegmentUpwards(result, lineSegment, false);
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
