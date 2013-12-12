/**
 * Copyright (c) 2013, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.quadtree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mini2Dx.core.engine.Parallelogram;
import org.mini2Dx.core.geom.LineSegment;

/**
 * Implements a region quad
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Quadtree#The_region_quadtree">Wikipedia: Region Quad Tree</a>
 * 
 * @author Thomas Cashman
 */
public class RegionQuad<T extends Parallelogram> extends Quad<T> {
	private static final long serialVersionUID = -2417612178966065600L;
	
	public RegionQuad(int elementLimit, float x, float y, float width,
			float height) {
		super(elementLimit, x, y, width, height);
	}
	
	public RegionQuad(RegionQuad<T> parent, float x, float y, float width, float height) {
		super(parent, x, y, width, height);
	}

	@Override
	public boolean add(T element) {
		if (element == null)
			return false;

		if (!this.intersects(element) && !this.contains(element)) {
			return false;
		}

		if (topLeft == null) {
			return addElement(element);
		}
		if(!addElementToChild(element)) {
			return addElement(element);
		}
		return true;
	}
	
	@Override
	protected boolean addElementToChild(T element) {
		if(topLeft.contains(element)) {
			topLeft.add(element);
			return true;
		} 
		if(topRight.contains(element)) {
			topRight.add(element);
			return true;
		}
		if(bottomLeft.contains(element)) {
			bottomLeft.add(element);
			return true;
		}
		if(bottomRight.contains(element)) {
			bottomRight.add(element);
			return true;
		}
		return false;
	}
	
	@Override
	protected void subdivide() {
		if(topLeft != null)
			return;
		
		float halfWidth = width / 2f;
		float halfHeight = height / 2f;

		topLeft = new RegionQuad<T>(this, x, y, halfWidth, halfHeight);
		topRight = new RegionQuad<T>(this, x + halfWidth, y, halfWidth, halfHeight);
		bottomLeft = new RegionQuad<T>(this, x, y + halfHeight, halfWidth, halfHeight);
		bottomRight = new RegionQuad<T>(this, x + halfWidth, y + halfHeight,
				halfWidth, halfHeight);

		for (int i = elements.size() - 1; i >= 0; i--) {
			if(addElementToChild(elements.get(i))) {
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
		
		if(topLeft == null) {
			return removeElement(element);
		}
		if(!removeElementFromChild(element)) {
			return removeElement(element);
		}
		return true;
	}
	
	@Override
	public List<T> getElementsWithinRegion(Parallelogram parallelogram) {
		Set<T> result = new HashSet<T>();
		getElementsWithinRegion(result, parallelogram);
		return new ArrayList<T>(result);
	}
	
	@Override
	protected void getElementsWithinRegion(Collection<T> result, Parallelogram parallelogram) {
		if(topLeft != null) {
			if(topLeft.contains(parallelogram) || topLeft.intersects(parallelogram))
				topLeft.getElementsWithinRegion(result, parallelogram);
			if(topRight.contains(parallelogram) || topRight.intersects(parallelogram))
				topRight.getElementsWithinRegion(result, parallelogram);
			if(bottomLeft.contains(parallelogram) || bottomLeft.intersects(parallelogram))
				bottomLeft.getElementsWithinRegion(result, parallelogram);
			if(bottomRight.contains(parallelogram) || bottomRight.intersects(parallelogram))
				bottomRight.getElementsWithinRegion(result, parallelogram);
		}
		for(int i = 0; i < elements.size(); i++) {
			T element = elements.get(i);
			if(element == null)
				continue;
			if(parallelogram.contains(element) || parallelogram.intersects(element)) {
				result.add(element);
			}
		}
	}
	
	@Override
	public List<T> getElementsIntersectingLineSegment(LineSegment lineSegment) {
		Set<T> result = new HashSet<T>();
		getElementsIntersectingLineSegment(result, lineSegment);
		return new ArrayList<T>(result);
	}
	
	@Override
	protected void getElementsIntersectingLineSegment(Collection<T> result, LineSegment lineSegment) {
		if(topLeft != null) {
			if(topLeft.intersects(lineSegment))
				topLeft.getElementsIntersectingLineSegment(result, lineSegment);
			if(topRight.intersects(lineSegment))
				topRight.getElementsIntersectingLineSegment(result, lineSegment);
			if(bottomLeft.intersects(lineSegment))
				bottomLeft.getElementsIntersectingLineSegment(result, lineSegment);
			if(bottomRight.intersects(lineSegment))
				bottomRight.getElementsIntersectingLineSegment(result, lineSegment);
		}
		for(int i = 0; i < elements.size(); i++) {
			T element = elements.get(i);
			if(element != null && element.intersects(lineSegment)) {
				result.add(element);
			}
		}
	}
	
	@Override
	public List<T> getElements() {
		Set<T> result = new HashSet<T>();
		getElements(result);
		return new ArrayList<T>(result);
	}
	
	private void getElements(Set<T> result) {
		if(topLeft != null) {
			((RegionQuad<T>) topLeft).getElements(result);
			((RegionQuad<T>) topRight).getElements(result);
			((RegionQuad<T>) bottomLeft).getElements(result);
			((RegionQuad<T>) bottomRight).getElements(result);
		}
		result.addAll(elements);
	}
	
	@Override
	public void positionChanged(T moved) {
		if(this.contains(moved) || this.intersects(moved))
			return;
		
		remove(moved);
		
		if(parent == null)
			return;
		
		parent.add(moved);
	}
}
