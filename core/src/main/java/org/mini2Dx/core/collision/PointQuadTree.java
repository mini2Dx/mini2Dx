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

/**
 * Implements a point quadtree
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Quadtree#Point_quadtree">
 *      Wikipedia: Point Quad Tree</a>
 */
public class PointQuadTree<T extends Positionable> extends Rectangle implements QuadTree<T> {
	public static final float DEFAULT_MINIMUM_QUAD_SIZE = 8f;
	public static Color QUAD_COLOR = Mdx.graphics != null ? Mdx.graphics.newColor(1f, 0f, 0f, 0.5f) : null;
	public static Color ELEMENT_COLOR = Mdx.graphics != null ? Mdx.graphics.newColor(0f, 0f, 1f, 0.5f) : null;

	private static final long serialVersionUID = -2034928347848875105L;

	protected PointQuadTree<T> parent;
	protected PointQuadTree<T> topLeft, topRight, bottomLeft, bottomRight;
	protected Array<T> elements;
	protected final int elementLimitPerQuad;
	protected final int mergeWatermark;
	protected final float minimumQuadWidth, minimumQuadHeight;

	protected int totalElementsCache = -1;

	/**
	 * Constructs a {@link PointQuadTree} with a specified element limit and
	 * watermark
	 * 
	 * @param elementLimitPerQuad
	 *            The maximum number of elements in a quad before it is split
	 *            into 4 child {@link PointQuadTree}s
	 * @param mergeWatermark
	 *            When a parent {@link PointQuadTree}'s total elements go lower
	 *            than this mark, the child {@link PointQuadTree}s will be
	 *            merged back together
	 * @param x
	 *            The x coordinate of the {@link PointQuadTree}
	 * @param y
	 *            The y coordiante of the {@link PointQuadTree}
	 * @param width
	 *            The width of the {@link PointQuadTree}
	 * @param height
	 *            The height of the {@link PointQuadTree}
	 */
	public PointQuadTree(int elementLimitPerQuad, int mergeWatermark, float x, float y, float width, float height) {
		this(DEFAULT_MINIMUM_QUAD_SIZE, DEFAULT_MINIMUM_QUAD_SIZE, elementLimitPerQuad, mergeWatermark, x, y, width,
				height);
	}

	/**
	 * Constructs a {@link PointQuadTree} with a specified element limit and no
	 * merging watermark. As elements are removed, small sized child
	 * {@link PointQuadTree}s will not be merged back together.
	 * 
	 * @param elementLimitPerQuad
	 *            The maximum number of elements in a quad before it is split
	 *            into 4 child {@link PointQuadTree}s
	 * @param x
	 *            The x coordinate of the {@link PointQuadTree}
	 * @param y
	 *            The y coordiante of the {@link PointQuadTree}
	 * @param width
	 *            The width of the {@link PointQuadTree}
	 * @param height
	 *            The height of the {@link PointQuadTree}
	 */
	public PointQuadTree(int elementLimitPerQuad, float x, float y, float width, float height) {
		this(elementLimitPerQuad, 0, x, y, width, height);
	}

	/**
	 * Constructs a {@link PointQuadTree} as a child of another
	 * {@link PointQuadTree}
	 * 
	 * @param parent
	 *            The parent {@link PointQuadTree}
	 * @param x
	 *            The x coordinate of the {@link PointQuadTree}
	 * @param y
	 *            The y coordiante of the {@link PointQuadTree}
	 * @param width
	 *            The width of the {@link PointQuadTree}
	 * @param height
	 *            The height of the {@link PointQuadTree}
	 */
	public PointQuadTree(PointQuadTree<T> parent, float x, float y, float width, float height) {
		this(parent.getMinimumQuadWidth(), parent.getMinimumQuadHeight(), parent.getElementLimitPerQuad(),
				parent.getMergeWatermark(), x, y, width, height);
		this.parent = parent;
	}

	/**
	 * Constructs a {@link PointQuadTree} with a specified minimum quad size,
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
	 *            into 4 child {@link PointQuadTree}s
	 * @param mergeWatermark
	 *            When a parent {@link PointQuadTree}'s total elements go lower
	 *            than this mark, the child {@link PointQuadTree}s will be
	 *            merged back together
	 * @param x
	 *            The x coordinate of the {@link PointQuadTree}
	 * @param y
	 *            The y coordiante of the {@link PointQuadTree}
	 * @param width
	 *            The width of the {@link PointQuadTree}
	 * @param height
	 *            The height of the {@link PointQuadTree}
	 */
	public PointQuadTree(float minimumQuadWidth, float minimumQuadHeight, int elementLimitPerQuad, int mergeWatermark,
			float x, float y, float width, float height) {
		super(x, y, width, height);

		if (mergeWatermark >= elementLimitPerQuad) {
			throw new QuadWatermarkException(elementLimitPerQuad, mergeWatermark);
		}

		this.elementLimitPerQuad = elementLimitPerQuad;
		this.mergeWatermark = mergeWatermark;
		this.minimumQuadWidth = minimumQuadWidth;
		this.minimumQuadHeight = minimumQuadHeight;
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
	}

	public void addAll(Array<T> elementsToAdd) {
		if (elementsToAdd == null || elementsToAdd.size == 0) {
			return;
		}

		Array<T> elementsWithinQuad = new Array<T>();
		for (T element : elementsToAdd) {
			if (this.contains(element.getX(), element.getY())) {
				elementsWithinQuad.add(element);
			}
		}

		clearTotalElementsCache();

		if (topLeft != null) {
			for (T element : elementsWithinQuad) {
				addElementToChild(element);
			}
			return;
		}
		for (T element : elementsWithinQuad) {
			elements.add(element);
			element.addPostionChangeListener(this);
		}
		if (elements.size > elementLimitPerQuad && (getWidth() * 0.5f) >= minimumQuadWidth
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

		if (topLeft != null) {
			return addElementToChild(element);
		}
		return addElement(element);
	}

	protected boolean addElement(T element) {
		elements.add(element);
		element.addPostionChangeListener(this);

		QuadTreeAwareUtils.setQuadTreeRef(element, this);

		if (elements.size > elementLimitPerQuad && (getWidth() * 0.5f) >= minimumQuadWidth
				&& (getHeight() * 0.5f) >= minimumQuadHeight) {
			subdivide();
		}
		return true;
	}

	protected boolean addElementToChild(T element) {
		if (topLeft.add(element))
			return true;
		if (topRight.add(element))
			return true;
		if (bottomLeft.add(element))
			return true;
		if (bottomRight.add(element))
			return true;
		return false;
	}

	protected void subdivide() {
		if (topLeft != null) {
			return;
		}

		float halfWidth = getWidth() * 0.5f;
		float halfHeight = getHeight() * 0.5f;

		topLeft = new PointQuadTree<T>(this, getX(), getY(), halfWidth, halfHeight);
		topRight = new PointQuadTree<T>(this, getX() + halfWidth, getY(), halfWidth, halfHeight);
		bottomLeft = new PointQuadTree<T>(this, getX(), getY() + halfHeight, halfWidth, halfHeight);
		bottomRight = new PointQuadTree<T>(this, getX() + halfWidth, getY() + halfHeight, halfWidth, halfHeight);

		for (int i = elements.size - 1; i >= 0; i--) {
			T element = elements.removeIndex(i);
			element.removePositionChangeListener(this);
			addElementToChild(element);
		}
		elements = null;
	}

	protected boolean isMergable() {
		if (topLeft == null) {
			return false;
		}
		if (mergeWatermark <= 0) {
			return false;
		}

		int topLeftTotal = topLeft.getTotalElements();
		if (topLeftTotal >= mergeWatermark) {
			return false;
		}

		int topRightTotal = topRight.getTotalElements();
		if (topRightTotal >= mergeWatermark) {
			return false;
		}

		int bottomLeftTotal = bottomLeft.getTotalElements();
		if (bottomLeftTotal >= mergeWatermark) {
			return false;
		}

		int bottomRightTotal = bottomRight.getTotalElements();
		if (bottomRightTotal >= mergeWatermark) {
			return false;
		}
		return topLeftTotal + topRightTotal + bottomLeftTotal + bottomRightTotal < mergeWatermark;
	}

	protected void merge() {
		if (topLeft == null) {
			return;
		}

		elements = new Array<>(true, elementLimitPerQuad);
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

		topLeft = null;
		topRight = null;
		bottomLeft = null;
		bottomRight = null;
	}

	public void removeAll(Array<T> elementsToRemove) {
		if (elementsToRemove == null || elementsToRemove.size == 0) {
			return;
		}

		Array<T> elementsWithinQuad = new Array<T>();
		for (T element : elementsToRemove) {
			if (this.contains(element.getX(), element.getY())) {
				elementsWithinQuad.add(element);
			}
		}

		clearTotalElementsCache();

		if (topLeft != null) {
			for (T element : elementsWithinQuad) {
				removeElementFromChild(element);
			}
		}
		if (elements == null) {
			return;
		}
		elements.removeAll(elementsWithinQuad, false);
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

	public boolean remove(T element) {
		if (element == null)
			return false;

		if (!this.contains(element.getX(), element.getY())) {
			return false;
		}
		clearTotalElementsCache();

		if (topLeft != null) {
			return removeElementFromChild(element);
		}
		return removeElement(element);
	}

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

			elements = new Array<T>(true, elementLimitPerQuad);
		} else {
			elements.clear();
		}
		clearTotalElementsCache();
	}

	protected boolean removeElementFromChild(T element) {
		if (topLeft.remove(element))
			return true;
		if (topRight.remove(element))
			return true;
		if (bottomLeft.remove(element))
			return true;
		if (bottomRight.remove(element))
			return true;
		return false;
	}

	protected boolean removeElement(T element) {
		boolean result = elements.removeValue(element, false);
		element.removePositionChangeListener(this);

		if (parent == null) {
			return result;
		}
		if (result){
			QuadTreeAwareUtils.removeQuadTreeRef(element);
			if (parent.isMergable()){
				parent.merge();
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
		if (topLeft != null) {
			topLeft.getElementsWithinArea(result, area);
			topRight.getElementsWithinArea(result, area);
			bottomLeft.getElementsWithinArea(result, area);
			bottomRight.getElementsWithinArea(result, area);
		} else {
			addElementsWithinArea(result, area);
		}
	}

	@Override
	public void getElementsWithinArea(Array<T> result, Shape area, QuadTreeSearchDirection searchDirection) {
		switch (searchDirection){
			case UPWARDS:
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
				break;
			case DOWNWARDS:
				getElementsWithinArea(result, area);
				break;
		}
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
			topLeft.getElementsWithinAreaIgnoringEdges(result, area);
			topRight.getElementsWithinAreaIgnoringEdges(result, area);
			bottomLeft.getElementsWithinAreaIgnoringEdges(result, area);
			bottomRight.getElementsWithinAreaIgnoringEdges(result, area);
		} else {
			addElementsWithinArea(result, area);
		}
	}

	@Override
	public void getElementsWithinAreaIgnoringEdges(Array<T> result, Shape area, QuadTreeSearchDirection searchDirection) {
		switch (searchDirection){
			case UPWARDS:
				if (elements != null) {
					addElementsWithinArea(result, area);
				}
				if (parent != null) {
					if (parent.topLeft != this && (area.contains(parent.topLeft) || area.intersectsIgnoringEdges(parent.topLeft))) {
						parent.topLeft.getElementsWithinAreaIgnoringEdges(result, area);
					}
					if (parent.topRight != this && (area.contains(parent.topRight) || area.intersectsIgnoringEdges(parent.topRight))) {
						parent.topRight.getElementsWithinAreaIgnoringEdges(result, area);
					}
					if (parent.bottomLeft != this && (area.contains(parent.bottomLeft) || area.intersectsIgnoringEdges(parent.bottomLeft))) {
						parent.bottomLeft.getElementsWithinAreaIgnoringEdges(result, area);
					}
					if (parent.bottomRight != this && (area.contains(parent.bottomRight) || area.intersectsIgnoringEdges(parent.bottomRight))) {
						parent.bottomRight.getElementsWithinAreaIgnoringEdges(result, area);
					}
					parent.getElementsWithinAreaIgnoringEdges(result, area, searchDirection);
				}
				break;
			case DOWNWARDS:
				getElementsWithinAreaIgnoringEdges(result, area);
				break;
		}
	}

	@Override
	public Array<T> getElementsContainingArea(Shape area, boolean entirelyContained) {
		return new Array<>();
	}

	@Override
	public Array<T> getElementsContainingArea(Shape area, QuadTreeSearchDirection searchDirection, boolean entirelyContained) {
		return new Array<>();
	}

	@Override
	public void getElementsContainingArea(Array<T> result, Shape area, boolean entirelyContained) {

	}

	@Override
	public void getElementsContainingArea(Array<T> result, Shape area, QuadTreeSearchDirection searchDirection, boolean entirelyContained) {

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

	protected void addElementsContainingPoint(Array<T> result, Point point) {
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

	@Override
	public void getElementsContainingPoint(Array<T> result, Point point) {
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
			addElementsContainingPoint(result, point);
		}
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

	protected void addElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment) {
		for (int i = elements.size - 1; i >= 0; i--) {
			T element = elements.get(i);
			if (element != null && lineSegment.contains(element.getX(), element.getY())) {
				result.add(element);
			}
		}
	}

	protected static boolean intersects(PointQuadTree tree, LineSegment segment){
		return tree.intersects(segment) || tree.contains(segment.getPointA()) || tree.contains(segment.getPointB());
	}

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
		} else {
			addElementsIntersectingLineSegment(result, lineSegment);
		}
	}

	@Override
	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment, QuadTreeSearchDirection searchDirection) {
		switch (searchDirection){
			case UPWARDS:
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
		if (topLeft != null) {
			topLeft.getElements(result);
			topRight.getElements(result);
			bottomLeft.getElements(result);
			bottomRight.getElements(result);
		} else {
			result.addAll(elements);
		}
	}

	public int getTotalQuads() {
		if (topLeft != null) {
			int result = topLeft.getTotalQuads();
			result += topRight.getTotalQuads();
			result += bottomLeft.getTotalQuads();
			result += bottomRight.getTotalQuads();
			return result;
		} else {
			return 1;
		}
	}

	public int getTotalElements() {
		if (totalElementsCache >= 0) {
			return totalElementsCache;
		}
		if (topLeft != null) {
			totalElementsCache = topLeft.getTotalElements();
			totalElementsCache += topRight.getTotalElements();
			totalElementsCache += bottomLeft.getTotalElements();
			totalElementsCache += bottomRight.getTotalElements();
		} else {
			totalElementsCache = elements.size;
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

	public boolean hasChildQuads() {
		return topLeft != null;
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
