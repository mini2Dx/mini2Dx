/*******************************************************************************
 * Copyright 2021 Viridian Software Limited
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

public interface CollisionDetection<T extends Positionable> extends PositionChangeListener<T> {
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

	public Array<T> getElementsOverlappingArea(Rectangle area);

	public void getElementsOverlappingArea(Array<T> result, Rectangle area);

	public Array<T> getElementsOverlappingArea(Circle area);

	public void getElementsOverlappingArea(Array<T> result, Circle area);

	public Array<T> getElementsOverlappingAreaIgnoringEdges(Rectangle area);

	public void getElementsOverlappingAreaIgnoringEdges(Array<T> result, Rectangle area);

	public Array<T> getElementsContainedInArea(Rectangle area);

	public void getElementsContainedInArea(Array<T> result, Rectangle area);

	public Array<T> getElementsContainingArea(Rectangle area);

	public void getElementsContainingArea(Array<T> result, Rectangle area);

	public Array<T> getElementsIntersectingLineSegment(LineSegment lineSegment);

	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment);

	public Array<T> getElementsContainingPoint(Point point);

	public void getElementsContainingPoint(Array<T> result, Point point);

	public Array<T> getElements();

	public void getElements(Array<T> result);

	public int getTotalElements();
}
