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
import org.mini2Dx.gdx.utils.Queue;

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
	public void warmupPool(int poolSize) {
		warmupPool(poolSize, 16);
	}

	@Override
	public void warmupPool(int poolSize, int expectedElementsPerQuad) {
		if(pool == null) {
			pool = new Queue<>();
		}
		for(int i = 0; i < poolSize; i++) {
			RegionQuadTree<T> regionQuadTree = new RegionQuadTree<T>(this, 0, 0, 1, 1);
			regionQuadTree.elements = new Array<>(expectedElementsPerQuad);
			pool.addLast(regionQuadTree);
		}
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

		topLeft = allocate(this, getX(), getY(), halfWidth, halfHeight);
		topRight = allocate(this, getX() + halfWidth, getY(), halfWidth, halfHeight);
		bottomLeft = allocate(this, getX(), getY() + halfHeight, halfWidth, halfHeight);
		bottomRight = allocate(this, getX() + halfWidth, getY() + halfHeight, halfWidth, halfHeight);

		for (int i = elements.size - 1; i >= 0; i--) {
			if (addElementToChild(elements.get(i))) {
				removeElement(elements.get(i));
			}
		}
	}

	protected RegionQuadTree<T> allocate(RegionQuadTree<T> parent, float x, float y, float width, float height) {
		if(pool == null || pool.size == 0) {
			return new RegionQuadTree<>(parent, x, y, width, height);
		}
		final RegionQuadTree<T> result = (RegionQuadTree) pool.removeFirst();
		result.parent = parent;
		result.set(x, y, width, height);
		if(result.elements != null) {
			result.elements.clear();
		}
		return result;
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

			if(pool != null) {
				pool.addLast(topLeft);
				pool.addLast(topRight);
				pool.addLast(bottomLeft);
				pool.addLast(bottomRight);
			}

			topLeft = null;
			topRight = null;
			bottomLeft = null;
			bottomRight = null;
		}
		for(int i = 0; i < elements.size; i++) {
			elements.get(i).removePositionChangeListener(this);
		}
		elements.clear();
		clearTotalElementsCache();
	}

	@Override
	protected void addElementsWithinArea(Array<T> result, Shape area, boolean allElements) {
		if(allElements) {
			result.addAll(elements);
			return;
		}

		for (int i = elements.size - 1; i >= 0; i--) {
			T element = elements.get(i);
			if (element != null && (area.contains(element) || area.intersects(element))) {
				result.add(element);
			}
		}
	}

	protected void addElementsWithinAreaIgnoringEdges(Array<T> result, Shape area) {
		for (int i = elements.size - 1; i >= 0; i--) {
			T element = elements.get(i);
			if (element != null && (area.contains(element) || area.intersectsIgnoringEdges(element))) {
				result.add(element);
			}
		}
	}

	@Override
	public void getElementsWithinArea(Array<T> result, Shape area, boolean allElements) {
		if(allElements) {
			if (topLeft != null) {
				topLeft.getElementsWithinArea(result, area, true);
				topRight.getElementsWithinArea(result, area, true);
				bottomLeft.getElementsWithinArea(result, area, true);
				bottomRight.getElementsWithinArea(result, area, true);
			}
			addElementsWithinArea(result, area, true);
			return;
		}

		if (topLeft != null) {
			boolean quadContains = false;
			if(topLeft.isSearchRequired()) {
				if (area.contains(topLeft)) {
					topLeft.getElementsWithinArea(result, area, true);
				} else if (topLeft.contains(area)) {
					topLeft.getElementsWithinArea(result, area, false);
					quadContains = true;
				} else if (topLeft.intersects(area)) {
					topLeft.getElementsWithinArea(result, area, false);
				}
			}
			if(!quadContains && topRight.isSearchRequired()) {
				if (area.contains(topRight)) {
					topRight.getElementsWithinArea(result, area, true);
				} else if (topRight.contains(area)) {
					topRight.getElementsWithinArea(result, area, false);
					quadContains = true;
				} else if (topRight.intersects(area)) {
					topRight.getElementsWithinArea(result, area, false);
				}
			}
			if(!quadContains && bottomLeft.isSearchRequired()) {
				if (area.contains(bottomLeft)) {
					bottomLeft.getElementsWithinArea(result, area, true);
				} else if (bottomLeft.contains(area)) {
					bottomLeft.getElementsWithinArea(result, area, false);
					quadContains = true;
				} else if (bottomLeft.intersects(area)) {
					bottomLeft.getElementsWithinArea(result, area, false);
				}
			}
			if(!quadContains && bottomRight.isSearchRequired()) {
				if (area.contains(bottomRight)) {
					bottomRight.getElementsWithinArea(result, area, true);
				} else if(bottomRight.contains(area)) {
					bottomRight.getElementsWithinArea(result, area, false);
					quadContains = true;
				} else if(bottomRight.intersects(area)) {
					bottomRight.getElementsWithinArea(result, area, false);
				}
			}
		}
		addElementsWithinArea(result, area, false);
	}

	@Override
	public void getElementsWithinArea(Array<T> result, Shape area, QuadTreeSearchDirection searchDirection) {
		switch (searchDirection){
			case UPWARDS:
				getElementsWithinAreaUpwards(result, area, true, false);
				break;
			case DOWNWARDS:
				getElementsWithinArea(result, area);
				break;
		}
	}

	private void getElementsWithinAreaUpwards(Array<T> result, Shape area, boolean firstInvocation, boolean childNodeCrossed) {
		if (elements != null) {
			addElementsWithinArea(result, area);
		}

		boolean nodeCrossed = false;

		if (firstInvocation && topLeft != null){
			if (topLeft.isSearchRequired()){
				if(area.contains(topLeft)) {
					topLeft.getElementsWithinArea(result, area, true);
				} else if(area.intersects(topLeft)) {
					topLeft.getElementsWithinArea(result, area, false);
				}
			}
			if (topRight.isSearchRequired()){
				if(area.contains(topRight)) {
					topRight.getElementsWithinArea(result, area, true);
				} else if(area.intersects(topRight)) {
					topRight.getElementsWithinArea(result, area, false);
				}
			}
			if (bottomLeft.isSearchRequired()){
				if(area.contains(bottomLeft)) {
					bottomLeft.getElementsWithinArea(result, area, true);
				} else if(area.intersects(bottomLeft)) {
					bottomLeft.getElementsWithinArea(result, area, false);
				}
			}
			if (bottomRight.isSearchRequired()){
				if(area.contains(bottomRight)) {
					bottomRight.getElementsWithinArea(result, area, true);
				} else if(area.intersects(bottomRight)) {
					bottomRight.getElementsWithinArea(result, area, false);
				}
			}
			nodeCrossed = true;
		}
		if (parent == null) {
			return;
		}

		if(childNodeCrossed || firstInvocation) {
			if (parent.topLeft != this && parent.topLeft.isSearchRequired()) {
				if(area.contains(parent.topLeft)) {
					parent.topLeft.getElementsWithinArea(result, area, true);
					nodeCrossed = true;
				} else if(area.intersects(parent.topLeft)) {
					parent.topLeft.getElementsWithinArea(result, area, false);
					nodeCrossed = true;
				}
			}
			if (parent.topRight != this && parent.topRight.isSearchRequired()) {
				if(area.contains(parent.topRight)) {
					parent.topRight.getElementsWithinArea(result, area, true);
					nodeCrossed = true;
				} else if(area.intersects(parent.topRight)) {
					parent.topRight.getElementsWithinArea(result, area, false);
					nodeCrossed = true;
				}
			}
			if (parent.bottomLeft != this && parent.bottomLeft.isSearchRequired()) {
				if(area.contains(parent.bottomLeft)) {
					parent.bottomLeft.getElementsWithinArea(result, area, true);
					nodeCrossed = true;
				} else if(area.intersects(parent.bottomLeft)) {
					parent.bottomLeft.getElementsWithinArea(result, area, false);
					nodeCrossed = true;
				}
			}
			if (parent.bottomRight != this && parent.bottomRight.isSearchRequired()) {
				if(area.contains(parent.bottomRight)) {
					parent.bottomRight.getElementsWithinArea(result, area, true);
					nodeCrossed = true;
				} else if(area.intersects(parent.bottomRight)) {
					parent.bottomRight.getElementsWithinArea(result, area, false);
					nodeCrossed = true;
				}
			}
		}

		((RegionQuadTree<T>)parent).getElementsWithinAreaUpwards(result, area, false, nodeCrossed);
	}

	@Override
	public Array<T> getElementsWithinAreaIgnoringEdges(Shape area) {
		Array<T> result = new Array<T>();
		getElementsWithinAreaIgnoringEdges(result, area);
		return result;
	}

	@Override
	public Array<T> getElementsWithinAreaIgnoringEdges(Shape area, QuadTreeSearchDirection searchDirection) {
		Array<T> result = new Array<T>();
		getElementsWithinAreaIgnoringEdges(result, area, searchDirection);
		return result;
	}

	@Override
	public void getElementsWithinAreaIgnoringEdges(Array<T> result, Shape area) {
		if (topLeft != null) {
			if (topLeft.isSearchRequired() && (topLeft.contains(area) || topLeft.intersectsIgnoringEdges(area)))
				topLeft.getElementsWithinAreaIgnoringEdges(result, area);
			if (topRight.isSearchRequired() && (topRight.contains(area) || topRight.intersectsIgnoringEdges(area)))
				topRight.getElementsWithinAreaIgnoringEdges(result, area);
			if (bottomLeft.isSearchRequired() && (bottomLeft.contains(area) || bottomLeft.intersectsIgnoringEdges(area)))
				bottomLeft.getElementsWithinAreaIgnoringEdges(result, area);
			if (bottomRight.isSearchRequired() && (bottomRight.contains(area) || bottomRight.intersectsIgnoringEdges(area)))
				bottomRight.getElementsWithinAreaIgnoringEdges(result, area);
		}
		addElementsWithinAreaIgnoringEdges(result, area);
	}

	@Override
	public void getElementsWithinAreaIgnoringEdges(Array<T> result, Shape area, QuadTreeSearchDirection searchDirection) {
		switch (searchDirection){
			case UPWARDS:
				getElementsWithinAreaIgnoringEdgesUpwards(result, area, true);
				break;
			case DOWNWARDS:
				this.getElementsWithinAreaIgnoringEdges(result, area);
				break;
		}
	}

	private void getElementsWithinAreaIgnoringEdgesUpwards(Array<T> result, Shape area, boolean firstInvocation) {
		if (elements != null) {
			addElementsWithinArea(result, area);
		}
		if (firstInvocation && topLeft != null){
			if (topLeft.isSearchRequired() && (area.contains(topLeft) || area.intersectsIgnoringEdges(topLeft))){
				topLeft.getElementsWithinAreaIgnoringEdges(result, area);
			}
			if (topRight.isSearchRequired() && (area.contains(topRight) || area.intersectsIgnoringEdges(topRight))){
				topRight.getElementsWithinAreaIgnoringEdges(result, area);
			}
			if (bottomLeft.isSearchRequired() && (area.contains(bottomLeft) || area.intersectsIgnoringEdges(bottomLeft))){
				bottomLeft.getElementsWithinAreaIgnoringEdges(result, area);
			}
			if (bottomRight.isSearchRequired() && (area.contains(bottomRight) || area.intersectsIgnoringEdges(bottomRight))){
				bottomRight.getElementsWithinAreaIgnoringEdges(result, area);
			}
		}
		if (parent != null) {
			if (parent.topLeft != this && parent.topLeft.isSearchRequired() && (area.contains(parent.topLeft) || area.intersectsIgnoringEdges(parent.topLeft))) {
				parent.topLeft.getElementsWithinAreaIgnoringEdges(result, area);
			}
			if (parent.topRight != this && parent.topRight.isSearchRequired() && (area.contains(parent.topRight) || area.intersectsIgnoringEdges(parent.topRight))) {
				parent.topRight.getElementsWithinAreaIgnoringEdges(result, area);
			}
			if (parent.bottomLeft != this && parent.bottomLeft.isSearchRequired() && (area.contains(parent.bottomLeft) || area.intersectsIgnoringEdges(parent.bottomLeft))) {
				parent.bottomLeft.getElementsWithinAreaIgnoringEdges(result, area);
			}
			if (parent.bottomRight != this && parent.bottomRight.isSearchRequired() && (area.contains(parent.bottomRight) || area.intersectsIgnoringEdges(parent.bottomRight))) {
				parent.bottomRight.getElementsWithinAreaIgnoringEdges(result, area);
			}
			((RegionQuadTree<T>)parent).getElementsWithinAreaIgnoringEdgesUpwards(result, area, false);
		}
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
		if (topLeft != null) {
			if (topLeft.isSearchRequired() && (topLeft.contains(area) || topLeft.intersects(area)))
				topLeft.getElementsContainingArea(result, area, entirelyContained);
			if (topRight.isSearchRequired() && (topRight.contains(area) || topRight.intersects(area)))
				topRight.getElementsContainingArea(result, area, entirelyContained);
			if (bottomLeft.isSearchRequired() && (bottomLeft.contains(area) || bottomLeft.intersects(area)))
				bottomLeft.getElementsContainingArea(result, area, entirelyContained);
			if (bottomRight.isSearchRequired() && (bottomRight.contains(area) || bottomRight.intersects(area)))
				bottomRight.getElementsContainingArea(result, area, entirelyContained);
		}
		addElementsContainingArea(result, area, entirelyContained);
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
		if (elements != null) {
			addElementsContainingArea(result, area, entirelyContained);
		}
		if (firstInvocation && topLeft != null){
			if (topLeft.isSearchRequired() && (area.contains(topLeft) || area.intersects(topLeft))){
				topLeft.getElementsContainingArea(result, area, entirelyContained);
			}
			if (topRight.isSearchRequired() && (area.contains(topRight) || area.intersects(topRight))){
				topRight.getElementsContainingArea(result, area, entirelyContained);
			}
			if (bottomLeft.isSearchRequired() && (area.contains(bottomLeft) || area.intersects(bottomLeft))){
				bottomLeft.getElementsContainingArea(result, area, entirelyContained);
			}
			if (bottomRight.isSearchRequired() && (area.contains(bottomRight) || area.intersects(bottomRight))){
				bottomRight.getElementsContainingArea(result, area, entirelyContained);
			}
		}
		if (parent != null) {
			if (parent.topLeft != this && parent.topLeft.isSearchRequired() && (area.contains(parent.topLeft) || area.intersects(parent.topLeft))) {
				parent.topLeft.getElementsContainingArea(result, area, entirelyContained);
			}
			if (parent.topRight != this && parent.topRight.isSearchRequired() && (area.contains(parent.topRight) || area.intersects(parent.topRight))) {
				parent.topRight.getElementsContainingArea(result, area, entirelyContained);
			}
			if (parent.bottomLeft != this && parent.bottomLeft.isSearchRequired() && (area.contains(parent.bottomLeft) || area.intersects(parent.bottomLeft))) {
				parent.bottomLeft.getElementsContainingArea(result, area, entirelyContained);
			}
			if (parent.bottomRight != this && parent.bottomRight.isSearchRequired() && (area.contains(parent.bottomRight) || area.intersects(parent.bottomRight))) {
				parent.bottomRight.getElementsContainingArea(result, area, entirelyContained);
			}
			((RegionQuadTree<T>)parent).getElementsContainingAreaUpwards(result, area, false, entirelyContained);
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
			if (topLeft.isSearchRequired() && topLeft.contains(point))
				topLeft.getElementsContainingPoint(result, point);
			if (topRight.isSearchRequired() && topRight.contains(point))
				topRight.getElementsContainingPoint(result, point);
			if (bottomLeft.isSearchRequired() && bottomLeft.contains(point))
				bottomLeft.getElementsContainingPoint(result, point);
			if (bottomRight.isSearchRequired() && bottomRight.contains(point))
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
			if (topLeft.isSearchRequired() && topLeft.contains(point)){
				topLeft.getElementsContainingPoint(result, point);
			}
			if (topRight.isSearchRequired() && topRight.contains(point)){
				topRight.getElementsContainingPoint(result, point);
			}
			if (bottomLeft.isSearchRequired() && bottomLeft.contains(point)){
				bottomLeft.getElementsContainingPoint(result, point);
			}
			if (bottomRight.isSearchRequired() && bottomRight.contains(point)){
				bottomRight.getElementsContainingPoint(result, point);
			}
		}
		if (parent != null){
			if (parent.topLeft != this && parent.topLeft.isSearchRequired() && (parent.topLeft.contains(point) || parent.topLeft.contains(point))) {
				parent.topLeft.getElementsContainingPoint(result, point);
			}
			if (parent.topRight != this && parent.topRight.isSearchRequired() && (parent.topRight.contains(point) || parent.topRight.contains(point))) {
				parent.topRight.getElementsContainingPoint(result, point);
			}
			if (parent.bottomLeft != this && parent.bottomLeft.isSearchRequired() && (parent.bottomLeft.contains(point) || parent.bottomLeft.contains(point))) {
				parent.bottomLeft.getElementsContainingPoint(result, point);
			}
			if (parent.bottomRight != this && parent.bottomRight.isSearchRequired() && (parent.bottomRight.contains(point) || parent.bottomRight.contains(point))) {
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
			if (topLeft.isSearchRequired() && intersects(topLeft, lineSegment)) {
				topLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (topRight.isSearchRequired() && intersects(topRight, lineSegment)) {
				topRight.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (bottomLeft.isSearchRequired() && intersects(bottomLeft, lineSegment)) {
				bottomLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (bottomRight.isSearchRequired() && intersects(bottomRight, lineSegment)) {
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
			if (topLeft.isSearchRequired() && intersects(topLeft, lineSegment)){
				topLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (topRight.isSearchRequired() && intersects(topRight, lineSegment)){
				topRight.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (bottomLeft.isSearchRequired() && intersects(bottomLeft, lineSegment)){
				bottomLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (bottomRight.isSearchRequired() && intersects(bottomRight, lineSegment)){
				bottomRight.getElementsIntersectingLineSegment(result, lineSegment);
			}
		}
		if (parent != null) {
			if (parent.topLeft != this && parent.topLeft.isSearchRequired() && intersects(parent.topLeft, lineSegment)) {
				parent.topLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (parent.topRight != this && parent.topRight.isSearchRequired()  && intersects(parent.topRight, lineSegment)) {
				parent.topRight.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (parent.bottomLeft != this && parent.bottomLeft.isSearchRequired()  && intersects(parent.bottomLeft, lineSegment)) {
				parent.bottomLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (parent.bottomRight != this && parent.bottomRight.isSearchRequired()  && intersects(parent.bottomRight, lineSegment)) {
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
