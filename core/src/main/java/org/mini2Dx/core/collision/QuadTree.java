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
import org.mini2Dx.core.geom.*;
import org.mini2Dx.gdx.utils.Array;

/**
 * Common interface for <a href="http://en.wikipedia.org/wiki/Quadtree">Quad Tree</a> implementation
 */
public interface QuadTree<T extends Positionable> extends PositionChangeListener<T> {
	public void debugRender(Graphics g);

	/**
	 * Adds an element to this {@link QuadTree}
	 * @param element The element to add
	 * @return False if the element exists outside of this QuadTree's bounds and was not added
	 */
	public boolean add(T element);

	/**
	 * Removes an element from this {@link QuadTree}
	 * @param element The element to remove
	 * @return True if the element was found and removed
	 */
	public boolean remove(T element);

	/**
	 * Adds all element to this {@link QuadTree}. Note that if an element exists outside of this QuadTree's bounds, it will not be added
	 * @param elements The elements to add
	 */
	public void addAll(Array<T> elements);

	/**
	 * Removes all elements in this {@link QuadTree} and stores them in an {@link Array}
	 * @param elements After executing, this {@link Array} will contain all the removed elements
	 */
	public void removeAll(Array<T> elements);

	/**
	 * Same a {@link #removeAll(Array)} except the results are not stored
	 */
	public void clear();
	
	public Array<T> getElementsWithinArea(Shape area);

	public Array<T> getElementsWithinArea(Shape area, QuadTreeSearchDirection searchDirection);
	
	public void getElementsWithinArea(Array<T> result, Shape area);

	public void getElementsWithinArea(Array<T> result, Shape area, QuadTreeSearchDirection searchDirection);

	public Array<T> getElementsWithinAreaIgnoringEdges(Shape area);

	public Array<T> getElementsWithinAreaIgnoringEdges(Shape area, QuadTreeSearchDirection searchDirection);

	public void getElementsWithinAreaIgnoringEdges(Array<T> result, Shape area);

	public void getElementsWithinAreaIgnoringEdges(Array<T> result, Shape area, QuadTreeSearchDirection searchDirection);

	public Array<T> getElementsContainingArea(Shape area, boolean entirelyContained);

	public Array<T> getElementsContainingArea(Shape area, QuadTreeSearchDirection searchDirection, boolean entirelyContained);

	public void getElementsContainingArea(Array<T> result, Shape area, boolean entirelyContained);

	public void getElementsContainingArea(Array<T> result, Shape area, QuadTreeSearchDirection searchDirection, boolean entirelyContained);
	
	public Array<T> getElementsIntersectingLineSegment(LineSegment lineSegment);

	public Array<T> getElementsIntersectingLineSegment(LineSegment lineSegment, QuadTreeSearchDirection searchDirection);
	
	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment);

	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment, QuadTreeSearchDirection searchDirection);
	
	public Array<T> getElementsContainingPoint(Point point);

	public Array<T> getElementsContainingPoint(Point point, QuadTreeSearchDirection searchDirection);
	
	public void getElementsContainingPoint(Array<T> result, Point point);

	public void getElementsContainingPoint(Array<T> result, Point point, QuadTreeSearchDirection searchDirection);
	
	public Array<T> getElements();
	
	public void getElements(Array<T> result);
	
	public int getTotalQuads();
	
	public int getTotalElements();
	
	public QuadTree<T> getParent();
	
	public float getMinimumQuadWidth();
	
	public float getMinimumQuadHeight();
}
