/**
 * Copyright (c) 2016 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.util;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Shape;

/**
 * A iterator-type class for iterating over {@link Shape} edges.
 * 
 * Note: This class is not thread safe.
 */
public abstract class EdgeIterator {
	private boolean begun = false;

	/**
	 * Begin iteration
	 */
	public void begin() {
		if(begun) {
			throw new MdxException("Cannot call begin() without first calling end() on previous iteration");
		}
		beginIteration();
		begun = true;
	}
	
	/**
	 * End iteration
	 */
	public void end() {
		if(!begun) {
			throw new MdxException("Cannot call end() without first calling begin()");
		}
		endIteration();
		begun = false;
	}
	
	/**
	 * Moves the iterator to the next edge
	 */
	public void next() {
		if(!begun) {
			throw new MdxException("Cannot call next() without first calling begin()");
		}
		nextEdge();
	}
	
	protected abstract void beginIteration();
	
	protected abstract void endIteration();
	
	protected abstract void nextEdge();
	
	/**
	 * Returns if there is another edge to iterate over
	 * @return True if there is another edge
	 */
	public abstract boolean hasNext();
	
	/**
	 * Returns the x coordinate of the first point in the edge
	 * @return
	 */
	public abstract float getPointAX();
	
	/**
	 * Returns the y coordinate of the first point in the edge
	 * @return
	 */
	public abstract float getPointAY();
	
	/**
	 * Returns the x coordinate of the second point in the edge
	 * @return
	 */
	public abstract float getPointBX();
	
	/**
	 * Returns the y coordinate of the second point in the edge
	 * @return
	 */
	public abstract float getPointBY();
	
	/**
	 * Returns the {@link LineSegment} of the edge
	 * @return
	 */
	public abstract LineSegment getEdgeLineSegment();
}
