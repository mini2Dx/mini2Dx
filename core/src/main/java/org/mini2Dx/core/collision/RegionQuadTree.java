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
	protected boolean updateBounds(T element) {
		initBounds();
		float minX = Math.min(element.getX(), elementsBounds.getX());
		float minY = Math.min(element.getY(), elementsBounds.getY());
		float maxX = Math.max(element.getMaxX(), elementsBounds.getMaxX());
		float maxY = Math.max(element.getMaxY(), elementsBounds.getMaxY());
		elementsBounds.set(minX, minY, maxX - minX, maxY - minY);
		return true;
	}

	@Override
	protected boolean updateBounds() {
		if(topLeft == null) {
			if(!elementsRemoved) {
				return false;
			}
			if(elements.size == 0) {
				disposeBounds();
				elementsRemoved = false;
				return true;
			}

			initBounds();
			elementsBounds.set(getCenterX() - 1f, getCenterY() - 1f, 2f, 2f);
			float minX = elementsBounds.getX();
			float minY = elementsBounds.getY();
			float maxX = elementsBounds.getMaxX();
			float maxY = elementsBounds.getMaxY();

			for(int i = 0; i < elements.size; i++) {
				final T element = elements.get(i);
				if(element == null) {
					continue;
				}
				minX = Math.min(element.getX(), elementsBounds.getX());
				minY = Math.min(element.getY(), elementsBounds.getY());
				maxX = Math.max(element.getMaxX(), elementsBounds.getMaxX());
				maxY = Math.max(element.getMaxY(), elementsBounds.getMaxY());
			}
			elementsBounds.set(minX, minY, maxX - minX, maxY - minY);

			elementsRemoved = false;
			return true;
		}

		boolean boundsUpdated = false;
		if(topLeft.updateBounds()) {
			boundsUpdated = true;
		}
		if(topRight.updateBounds()) {
			boundsUpdated = true;
		}
		if(bottomLeft.updateBounds()) {
			boundsUpdated = true;
		}
		if(bottomRight.updateBounds()) {
			boundsUpdated = true;
		}
		if(boundsUpdated) {
			if(topLeft.isSearchRequired() || topRight.isSearchRequired() ||
					bottomLeft.isSearchRequired() || bottomRight.isSearchRequired()) {
				initBounds();
				float minX;
				float minY;
				float maxX;
				float maxY;

				if(topLeft.isSearchRequired()) {
					minX = topLeft.elementsBounds.getX();
					minY = topLeft.elementsBounds.getY();
					maxX = topLeft.elementsBounds.getMaxX();
					maxY = topLeft.elementsBounds.getMaxY();
				} else if(topRight.isSearchRequired()) {
					minX = topRight.elementsBounds.getX();
					minY = topRight.elementsBounds.getY();
					maxX = topRight.elementsBounds.getMaxX();
					maxY = topRight.elementsBounds.getMaxY();
				} else if(bottomLeft.isSearchRequired()) {
					minX = bottomLeft.elementsBounds.getX();
					minY = bottomLeft.elementsBounds.getY();
					maxX = bottomLeft.elementsBounds.getMaxX();
					maxY = bottomLeft.elementsBounds.getMaxY();
				} else {
					minX = bottomRight.elementsBounds.getX();
					minY = bottomRight.elementsBounds.getY();
					maxX = bottomRight.elementsBounds.getMaxX();
					maxY = bottomRight.elementsBounds.getMaxY();
				}

				if(topLeft.isSearchRequired()) {
					minX = Math.min(minX, topLeft.elementsBounds.getMinX());
					minY = Math.min(minY, topLeft.elementsBounds.getMinY());
					maxX = Math.max(maxX, topLeft.elementsBounds.getMaxX());
					maxY = Math.max(maxY, topLeft.elementsBounds.getMaxY());
				}
				if(topRight.isSearchRequired()) {
					minX = Math.min(minX, topRight.elementsBounds.getMinX());
					minY = Math.min(minY, topRight.elementsBounds.getMinY());
					maxX = Math.max(maxX, topRight.elementsBounds.getMaxX());
					maxY = Math.max(maxY, topRight.elementsBounds.getMaxY());
				}
				if(bottomLeft.isSearchRequired()) {
					minX = Math.min(minX, bottomLeft.elementsBounds.getMinX());
					minY = Math.min(minY, bottomLeft.elementsBounds.getMinY());
					maxX = Math.max(maxX, bottomLeft.elementsBounds.getMaxX());
					maxY = Math.max(maxY, bottomLeft.elementsBounds.getMaxY());
				}
				if(bottomRight.isSearchRequired()) {
					minX = Math.min(minX, bottomRight.elementsBounds.getMinX());
					minY = Math.min(minY, bottomRight.elementsBounds.getMinY());
					maxX = Math.max(maxX, bottomRight.elementsBounds.getMaxX());
					maxY = Math.max(maxY, bottomRight.elementsBounds.getMaxY());
				}

				elementsBounds.set(minX, minY, maxX - minX, maxY - minY);
			} else {
				//All child quads have been emptied, make this quad as empty
				disposeBounds();
			}
		} else {
			if(!topLeft.isSearchRequired() && !topRight.isSearchRequired() &&
					!bottomLeft.isSearchRequired() && !bottomRight.isSearchRequired()) {
				//All child quads have been emptied, make this quad as empty
				disposeBounds();
			}
		}
		return boundsUpdated;
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
			if (this.contains(element.getCenterX(), element.getCenterY())) {
				elementsWithinQuad.add(element);
				updateBounds(element);
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

		if (!this.contains(element.getCenterX(), element.getCenterY())) {
			return false;
		}
		clearTotalElementsCache();
		updateBounds(element);

		if (topLeft == null) {
			return addElement(element);
		}
		return addElementToChild(element);
	}

	@Override
	protected boolean addElementToChild(T element) {
		if(element.getCenterX() > getCenterX()) {
			if (topRight.add(element)) {
				return true;
			}
			if (bottomRight.add(element)) {
				return true;
			}
			if (topLeft.add(element)) {
				return true;
			}
			if (bottomLeft.add(element)) {
				return true;
			}
		} else {
			if (topLeft.add(element)) {
				return true;
			}
			if (bottomLeft.add(element)) {
				return true;
			}
			if (topRight.add(element)) {
				return true;
			}
			if (bottomRight.add(element)) {
				return true;
			}
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
			final T element = elements.get(i);
			if (addElementToChild(element)) {
				removeElement(element, false);
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
		result.updateBounds();
		if(result.elements != null) {
			result.elements.clear();
		}
		return result;
	}

	@Override
	public boolean remove(T element) {
		if (element == null)
			return false;

		if (!this.contains(element.getCenterX(), element.getCenterY())) {
			return false;
		}
		clearTotalElementsCache();

		if (removeElement(element, true)) {
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
		elementsRemoved = true;
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
			if(topLeft.isSearchRequired()) {
				if (area.contains(topLeft.elementsBounds)) {
					topLeft.getElementsWithinArea(result, area, true);
				} else if (topLeft.elementsBounds.contains(area)) {
					topLeft.getElementsWithinArea(result, area, false);
				} else if (topLeft.elementsBounds.intersects(area)) {
					topLeft.getElementsWithinArea(result, area, false);
				}
			}
			if(topRight.isSearchRequired()) {
				if (area.contains(topRight.elementsBounds)) {
					topRight.getElementsWithinArea(result, area, true);
				} else if (topRight.elementsBounds.contains(area)) {
					topRight.getElementsWithinArea(result, area, false);
				} else if (topRight.elementsBounds.intersects(area)) {
					topRight.getElementsWithinArea(result, area, false);
				}
			}
			if(bottomLeft.isSearchRequired()) {
				if (area.contains(bottomLeft.elementsBounds)) {
					bottomLeft.getElementsWithinArea(result, area, true);
				} else if (bottomLeft.elementsBounds.contains(area)) {
					bottomLeft.getElementsWithinArea(result, area, false);
				} else if (bottomLeft.elementsBounds.intersects(area)) {
					bottomLeft.getElementsWithinArea(result, area, false);
				}
			}
			if(bottomRight.isSearchRequired()) {
				if (area.contains(bottomRight.elementsBounds)) {
					bottomRight.getElementsWithinArea(result, area, true);
				} else if(bottomRight.elementsBounds.contains(area)) {
					bottomRight.getElementsWithinArea(result, area, false);
				} else if(bottomRight.elementsBounds.intersects(area)) {
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

	protected void getElementsWithinAreaUpwards(Array<T> result, Shape area, boolean firstInvocation, boolean childNodeCrossed) {
		if (elements != null) {
			addElementsWithinArea(result, area);
		}

		boolean nodeCrossed = false;

		if (firstInvocation && topLeft != null){
			if (topLeft.isSearchRequired()){
				if(area.contains(topLeft.elementsBounds)) {
					topLeft.getElementsWithinArea(result, area, true);
				} else if(area.intersects(topLeft.elementsBounds)) {
					topLeft.getElementsWithinArea(result, area, false);
				} else if(topLeft.elementsBounds.contains(area)) {
					topLeft.getElementsWithinArea(result, area, false);
				}
			}
			if (topRight.isSearchRequired()){
				if(area.contains(topRight.elementsBounds)) {
					topRight.getElementsWithinArea(result, area, true);
				} else if(area.intersects(topRight.elementsBounds)) {
					topRight.getElementsWithinArea(result, area, false);
				} else if(topRight.elementsBounds.contains(area)) {
					topRight.getElementsWithinArea(result, area, false);
				}
			}
			if (bottomLeft.isSearchRequired()){
				if(area.contains(bottomLeft.elementsBounds)) {
					bottomLeft.getElementsWithinArea(result, area, true);
				} else if(area.intersects(bottomLeft.elementsBounds)) {
					bottomLeft.getElementsWithinArea(result, area, false);
				} else if(bottomLeft.elementsBounds.contains(area)) {
					bottomLeft.getElementsWithinArea(result, area, false);
				}
			}
			if (bottomRight.isSearchRequired()){
				if(area.contains(bottomRight.elementsBounds)) {
					bottomRight.getElementsWithinArea(result, area, true);
				} else if(area.intersects(bottomRight.elementsBounds)) {
					bottomRight.getElementsWithinArea(result, area, false);
				} else if(bottomRight.elementsBounds.contains(area)) {
					bottomRight.getElementsWithinArea(result, area, false);
				}
			}
			nodeCrossed = true;
		}
		if (parent == null) {
			return;
		}

		if(childNodeCrossed || firstInvocation) {
			if (!this.contains(area)) {
				//Scan sibling nodes if intersecting this element
				if (parent.topLeft != this && parent.topLeft.isSearchRequired()) {
					if(area.contains(parent.topLeft.elementsBounds) ||
							area.intersects(parent.topLeft.elementsBounds)) {
						parent.topLeft.getElementsWithinArea(result, area, false);
						nodeCrossed = true;
					}
				}
				if (parent.topRight != this && parent.topRight.isSearchRequired()) {
					if(area.contains(parent.topRight.elementsBounds) ||
							area.intersects(parent.topRight.elementsBounds)) {
						parent.topRight.getElementsWithinArea(result, area, false);
						nodeCrossed = true;
					}
				}
				if (parent.bottomLeft != this && parent.bottomLeft.isSearchRequired()) {
					if(area.contains(parent.bottomLeft.elementsBounds) ||
							area.intersects(parent.bottomLeft.elementsBounds)) {
						parent.bottomLeft.getElementsWithinArea(result, area, false);
						nodeCrossed = true;
					}
				}
				if (parent.bottomRight != this && parent.bottomRight.isSearchRequired()) {
					if(area.contains(parent.bottomRight.elementsBounds) ||
							area.intersects(parent.bottomRight.elementsBounds)) {
						parent.bottomRight.getElementsWithinArea(result, area, false);
						nodeCrossed = true;
					}
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
			if (topLeft.isSearchRequired()) {
				if(area.contains(topLeft.elementsBounds)) {
					topLeft.getElementsWithinAreaIgnoringEdges(result, area);
				} else if(topLeft.elementsBounds.contains(area)) {
					topLeft.getElementsWithinAreaIgnoringEdges(result, area);
				} else if(topLeft.elementsBounds.intersectsIgnoringEdges(area)) {
					topLeft.getElementsWithinAreaIgnoringEdges(result, area);
				}
			}

			if (topRight.isSearchRequired()) {
				if(area.contains(topRight.elementsBounds)) {
					topRight.getElementsWithinAreaIgnoringEdges(result, area);
				} else if(topRight.elementsBounds.contains(area)) {
					topRight.getElementsWithinAreaIgnoringEdges(result, area);
				} else if(topRight.elementsBounds.intersectsIgnoringEdges(area)) {
					topRight.getElementsWithinAreaIgnoringEdges(result, area);
				}
			}

			if (bottomLeft.isSearchRequired()) {
				if(area.contains(bottomLeft.elementsBounds)) {
					bottomLeft.getElementsWithinAreaIgnoringEdges(result, area);
				} else if(bottomLeft.elementsBounds.contains(area)) {
					bottomLeft.getElementsWithinAreaIgnoringEdges(result, area);
				} else if(bottomLeft.elementsBounds.intersectsIgnoringEdges(area)) {
					bottomLeft.getElementsWithinAreaIgnoringEdges(result, area);
				}
			}

			if (bottomRight.isSearchRequired()) {
				if(area.contains(bottomRight.elementsBounds)) {
					bottomRight.getElementsWithinAreaIgnoringEdges(result, area);
				} else if(bottomRight.elementsBounds.contains(area)) {
					bottomRight.getElementsWithinAreaIgnoringEdges(result, area);
				} else if(bottomRight.elementsBounds.intersectsIgnoringEdges(area)) {
					bottomRight.getElementsWithinAreaIgnoringEdges(result, area);
				}
			}
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

	protected void getElementsWithinAreaIgnoringEdgesUpwards(Array<T> result, Shape area, boolean firstInvocation) {
		if (elements != null) {
			addElementsWithinArea(result, area);
		}
		if (firstInvocation && topLeft != null){
			if (topLeft.isSearchRequired() && (area.contains(topLeft.elementsBounds) ||
					area.intersectsIgnoringEdges(topLeft.elementsBounds) || topLeft.elementsBounds.contains(area))){
				topLeft.getElementsWithinAreaIgnoringEdges(result, area);
			}
			if (topRight.isSearchRequired() && (area.contains(topRight.elementsBounds) ||
					area.intersectsIgnoringEdges(topRight.elementsBounds) || topRight.elementsBounds.contains(area))){
				topRight.getElementsWithinAreaIgnoringEdges(result, area);
			}
			if (bottomLeft.isSearchRequired() && (area.contains(bottomLeft.elementsBounds) ||
					area.intersectsIgnoringEdges(bottomLeft.elementsBounds) || bottomLeft.elementsBounds.contains(area))){
				bottomLeft.getElementsWithinAreaIgnoringEdges(result, area);
			}
			if (bottomRight.isSearchRequired() && (area.contains(bottomRight.elementsBounds) ||
					area.intersectsIgnoringEdges(bottomRight.elementsBounds) || bottomRight.elementsBounds.contains(area))){
				bottomRight.getElementsWithinAreaIgnoringEdges(result, area);
			}
		}
		if (parent != null) {
			if(!elementsBounds.contains(area)) {
				//Intersecting a subling
				if (parent.topLeft != this && parent.topLeft.isSearchRequired() &&
						(area.contains(parent.topLeft.elementsBounds) || area.intersectsIgnoringEdges(parent.topLeft.elementsBounds) ||
								parent.topLeft.elementsBounds.contains(area))) {
					parent.topLeft.getElementsWithinAreaIgnoringEdges(result, area);
				}
				if (parent.topRight != this && parent.topRight.isSearchRequired() &&
						(area.contains(parent.topRight.elementsBounds) || area.intersectsIgnoringEdges(parent.topRight.elementsBounds) ||
								parent.topRight.elementsBounds.contains(area))) {
					parent.topRight.getElementsWithinAreaIgnoringEdges(result, area);
				}
				if (parent.bottomLeft != this && parent.bottomLeft.isSearchRequired() &&
						(area.contains(parent.bottomLeft.elementsBounds) || area.intersectsIgnoringEdges(parent.bottomLeft.elementsBounds) ||
								parent.bottomLeft.elementsBounds.contains(area))) {
					parent.bottomLeft.getElementsWithinAreaIgnoringEdges(result, area);
				}
				if (parent.bottomRight != this && parent.bottomRight.isSearchRequired() &&
						(area.contains(parent.bottomRight.elementsBounds) || area.intersectsIgnoringEdges(parent.bottomRight.elementsBounds) ||
								parent.bottomRight.elementsBounds.contains(area))) {
					parent.bottomRight.getElementsWithinAreaIgnoringEdges(result, area);
				}
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
			if (topLeft.isSearchRequired() && (topLeft.elementsBounds.contains(area) || topLeft.elementsBounds.intersects(area)))
				topLeft.getElementsContainingArea(result, area, entirelyContained);
			if (topRight.isSearchRequired() && (topRight.elementsBounds.contains(area) || topRight.elementsBounds.intersects(area)))
				topRight.getElementsContainingArea(result, area, entirelyContained);
			if (bottomLeft.isSearchRequired() && (bottomLeft.elementsBounds.contains(area) || bottomLeft.elementsBounds.intersects(area)))
				bottomLeft.getElementsContainingArea(result, area, entirelyContained);
			if (bottomRight.isSearchRequired() && (bottomRight.elementsBounds.contains(area) || bottomRight.elementsBounds.intersects(area)))
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
			if (topLeft.isSearchRequired() && (area.contains(topLeft.elementsBounds) || area.intersects(topLeft.elementsBounds))){
				topLeft.getElementsContainingArea(result, area, entirelyContained);
			}
			if (topRight.isSearchRequired() && (area.contains(topRight.elementsBounds) || area.intersects(topRight.elementsBounds))){
				topRight.getElementsContainingArea(result, area, entirelyContained);
			}
			if (bottomLeft.isSearchRequired() && (area.contains(bottomLeft.elementsBounds) || area.intersects(bottomLeft.elementsBounds))){
				bottomLeft.getElementsContainingArea(result, area, entirelyContained);
			}
			if (bottomRight.isSearchRequired() && (area.contains(bottomRight.elementsBounds) || area.intersects(bottomRight.elementsBounds))){
				bottomRight.getElementsContainingArea(result, area, entirelyContained);
			}
		}
		if (parent != null) {
			if (parent.topLeft != this && parent.topLeft.isSearchRequired() &&
					(area.contains(parent.topLeft.elementsBounds) || area.intersects(parent.topLeft.elementsBounds))) {
				parent.topLeft.getElementsContainingArea(result, area, entirelyContained);
			}
			if (parent.topRight != this && parent.topRight.isSearchRequired() &&
					(area.contains(parent.topRight.elementsBounds) || area.intersects(parent.topRight.elementsBounds))) {
				parent.topRight.getElementsContainingArea(result, area, entirelyContained);
			}
			if (parent.bottomLeft != this && parent.bottomLeft.isSearchRequired() &&
					(area.contains(parent.bottomLeft.elementsBounds) || area.intersects(parent.bottomLeft.elementsBounds))) {
				parent.bottomLeft.getElementsContainingArea(result, area, entirelyContained);
			}
			if (parent.bottomRight != this && parent.bottomRight.isSearchRequired() &&
					(area.contains(parent.bottomRight.elementsBounds) || area.intersects(parent.bottomRight.elementsBounds))) {
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
			if (topLeft.isSearchRequired() && topLeft.elementsBounds.contains(point))
				topLeft.getElementsContainingPoint(result, point);
			if (topRight.isSearchRequired() && topRight.elementsBounds.contains(point))
				topRight.getElementsContainingPoint(result, point);
			if (bottomLeft.isSearchRequired() && bottomLeft.elementsBounds.contains(point))
				bottomLeft.getElementsContainingPoint(result, point);
			if (bottomRight.isSearchRequired() && bottomRight.elementsBounds.contains(point))
				bottomRight.getElementsContainingPoint(result, point);
			return;
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

	protected void getElementsContainingPointUpwards(Array<T> result, Point point, boolean firstInvocation) {
		if (elements != null){
			addElementsContainingPoint(result, point);
		}
		if (firstInvocation && topLeft != null){
			if (topLeft.isSearchRequired() && topLeft.elementsBounds.contains(point)){
				topLeft.getElementsContainingPoint(result, point);
			}
			if (topRight.isSearchRequired() && topRight.elementsBounds.contains(point)){
				topRight.getElementsContainingPoint(result, point);
			}
			if (bottomLeft.isSearchRequired() && bottomLeft.elementsBounds.contains(point)){
				bottomLeft.getElementsContainingPoint(result, point);
			}
			if (bottomRight.isSearchRequired() && bottomRight.elementsBounds.contains(point)){
				bottomRight.getElementsContainingPoint(result, point);
			}
		}
		if (parent != null){
			if (parent.topLeft != this && parent.topLeft.isSearchRequired() &&
					(parent.topLeft.elementsBounds.contains(point) || parent.topLeft.elementsBounds.contains(point))) {
				parent.topLeft.getElementsContainingPoint(result, point);
			}
			if (parent.topRight != this && parent.topRight.isSearchRequired() &&
					(parent.topRight.elementsBounds.contains(point) || parent.topRight.elementsBounds.contains(point))) {
				parent.topRight.getElementsContainingPoint(result, point);
			}
			if (parent.bottomLeft != this && parent.bottomLeft.isSearchRequired() &&
					(parent.bottomLeft.elementsBounds.contains(point) || parent.bottomLeft.elementsBounds.contains(point))) {
				parent.bottomLeft.getElementsContainingPoint(result, point);
			}
			if (parent.bottomRight != this && parent.bottomRight.isSearchRequired() &&
					(parent.bottomRight.elementsBounds.contains(point) || parent.bottomRight.elementsBounds.contains(point))) {
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
			if (topLeft.isSearchRequired() && intersects(topLeft.elementsBounds, lineSegment)) {
				topLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (topRight.isSearchRequired() && intersects(topRight.elementsBounds, lineSegment)) {
				topRight.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (bottomLeft.isSearchRequired() && intersects(bottomLeft.elementsBounds, lineSegment)) {
				bottomLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (bottomRight.isSearchRequired() && intersects(bottomRight.elementsBounds, lineSegment)) {
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

	protected void addElementsIntersectingLineSegmentUpwards(Array<T> result, LineSegment lineSegment, boolean firstInvocation) {
		if (elements != null) {
			addElementsIntersectingLineSegment(result, lineSegment);
		}
		if (topLeft != null && firstInvocation){
			if (topLeft.isSearchRequired() && intersects(topLeft.elementsBounds, lineSegment)){
				topLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (topRight.isSearchRequired() && intersects(topRight.elementsBounds, lineSegment)){
				topRight.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (bottomLeft.isSearchRequired() && intersects(bottomLeft.elementsBounds, lineSegment)){
				bottomLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (bottomRight.isSearchRequired() && intersects(bottomRight.elementsBounds, lineSegment)){
				bottomRight.getElementsIntersectingLineSegment(result, lineSegment);
			}
		}
		if (parent != null) {
			if (parent.topLeft != this && parent.topLeft.isSearchRequired() &&
					intersects(parent.topLeft.elementsBounds, lineSegment)) {
				parent.topLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (parent.topRight != this && parent.topRight.isSearchRequired() &&
					intersects(parent.topRight.elementsBounds, lineSegment)) {
				parent.topRight.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (parent.bottomLeft != this && parent.bottomLeft.isSearchRequired() &&
					intersects(parent.bottomLeft.elementsBounds, lineSegment)) {
				parent.bottomLeft.getElementsIntersectingLineSegment(result, lineSegment);
			}
			if (parent.bottomRight != this && parent.bottomRight.isSearchRequired() &&
					intersects(parent.bottomRight.elementsBounds, lineSegment)) {
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
		if (this.contains(moved.getCenterX(), moved.getCenterY()))
			return;

		removeElement(moved, true);

		QuadTree<T> parentQuad = parent;
		while (parentQuad != null) {
			if (parentQuad.add(moved)) {
				return;
			}
			parentQuad = parentQuad.getParent();
		}
	}
}
