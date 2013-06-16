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

import org.mini2Dx.core.engine.Parallelogram;
import org.mini2Dx.core.geom.LineSegment;

/**
 * Implements a {@link QuadTree} which contains regions. Each region can contain
 * a maximum number of elements before it is subdivided into four regions.
 * 
 * @author Thomas Cashman
 */
public class RegionalQuadTree<T extends Parallelogram> implements QuadTree<T>, Quad<T> {
	private int elementLimit;
	private float width, height;
	private RegionalQuad<T> rootQuad;
	
	/**
	 * Default constructor
	 * @param width The width of the tree in pixels
	 * @param height The height of the tree in pixels
	 */
	public RegionalQuadTree(float width, float height) {
		this(10, width, height);
	}
	
	/**
	 * Constructor
	 * @param elementLimitPerRegion The maximum number of elements per region before subdivision
	 * @param width The width of the tree in pixels
	 * @param height The height of the tree in pixels
	 */
	public RegionalQuadTree(int elementLimitPerRegion, float width, float height) {
		this.elementLimit = elementLimitPerRegion;
		this.width = width;
		this.height = height;
		rootQuad = new RegionalQuad<T>(this, 0, 0, width, height);
	}

	/**
	 * Returns all {@link Quad}s containing an object
	 * @param object The object to search for
	 * @return An empty {@link List} if the object does not exist within this tree
	 */
	public List<Quad<T>> getQuadsFor(T object) {
		return rootQuad.getQuadsFor(object);
	}
	
	/**
	 * Returns all {@link Quad}s which intersect a {@link LineSegment}
	 * @param line The {@link LineSegment}
	 * @return An empty {@link List} if the {@link LineSegment} does not intersect this tree
	 */
	public List<Quad<T>> getQuadsFor(LineSegment line) {
		return rootQuad.getQuadsFor(line);
	}
	
	/**
	 * Returns all elements which intersect a {@link LineSegment}
	 * @param line The {@link LineSegment}
	 * @return An empty {@link List} if the {@link LineSegment} does not intersect any elements
	 */
	public List<T> getIntersectionsFor(LineSegment line) {
		return rootQuad.getIntersectionsFor(line);
	}

	@Override
	public void add(T object) {
		rootQuad.add(object);
	}

	@Override
	public void remove(T object) {
		rootQuad.remove(object);
	}

	@Override
	public Quad<T> getParent() {
		return null;
	}
	
	@Override
	public int getNumberOfElements() {
		return rootQuad.getNumberOfElements();
	}

	@Override
	public int getElementLimit() {
		return elementLimit;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	@Override
	public List<T> getValues() {
		return rootQuad.getValues();
	}

	@Override
	public void positionChanged(T moved) {
		
	}
}
