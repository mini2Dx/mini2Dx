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

import java.util.List;

import org.mini2Dx.core.engine.PositionChangeListener;
import org.mini2Dx.core.engine.Positionable;

/**
 * A common interface to {@link Quad} implementations within a {@link QuadTree}
 * implementation
 * 
 * @author Thomas Cashman
 */
public interface Quad<T extends Positionable> extends PositionChangeListener<T> {

	/**
	 * Returns all elements within this {@link Quad}
	 * 
	 * If this {@link Quad} contains child {@link Quad}s then this will return
	 * all elements recursively within the children
	 * 
	 * @return An empty {@link List} if this {@link Quad} contains no elements
	 */
	public List<T> getValues();

	/**
	 * Returns the maximum number of elements per {@link Quad}
	 * 
	 * @return
	 */
	public int getElementLimit();

	/**
	 * Returns the number of elements within this {@link Quad}
	 * 
	 * @return 0 if there are no elements within this {@link Quad}
	 */
	public int getNumberOfElements();

	/**
	 * Returns the {@link Quad} this {@link Quad} is contained in
	 * 
	 * @return Null if this is the root {@link Quad}
	 */
	public Quad<T> getParent();

	/**
	 * Adds an element to this {@link Quad}
	 * 
	 * @param object
	 *            The element to be added
	 */
	public void add(T object);

	/**
	 * Removes an element from this {@link Quad}
	 * 
	 * @param object
	 *            The element to be removed
	 */
	public void remove(T object);
}
