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

import java.util.Collection;
import java.util.List;

import org.mini2Dx.core.engine.PositionChangeListener;
import org.mini2Dx.core.engine.Positionable;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Parallelogram;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.graphics.Graphics;

/**
 * Common interface for <a href="http://en.wikipedia.org/wiki/Quadtree">Quad Tree</a> implementation
 */
public interface QuadTree<T extends Positionable> extends PositionChangeListener<T>, Parallelogram {
	public void debugRender(Graphics g);
	
	public boolean add(T element);
	
	public boolean remove(T element);
	
	public List<T> getElementsWithinRegion(Parallelogram parallelogram);
	
	public void getElementsWithinRegion(Collection<T> result, Parallelogram parallelogram);
	
	public List<T> getElementsIntersectingLineSegment(LineSegment lineSegment);
	
	public void getElementsIntersectingLineSegment(Collection<T> result, LineSegment lineSegment);
	
	public List<T> getElementsContainingPoint(Point point);
	
	public void getElementsContainingPoint(Collection<T> result, Point point);
	
	public List<T> getElements();
	
	public void getElements(List<T> result);
	
	public int getTotalQuads();
	
	public int getTotalElements();
	
	public QuadTree<T> getParent();
}
