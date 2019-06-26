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
	
	public void getElementsWithinArea(Array<T> result, Shape area);
	
	public Array<T> getElementsIntersectingLineSegment(LineSegment lineSegment);
	
	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment);
	
	public Array<T> getElementsContainingPoint(Point point);
	
	public void getElementsContainingPoint(Array<T> result, Point point);
	
	public Array<T> getElements();
	
	public void getElements(Array<T> result);
	
	public int getTotalQuads();
	
	public int getTotalElements();
	
	public QuadTree<T> getParent();
	
	public float getMinimumQuadWidth();
	
	public float getMinimumQuadHeight();
}
