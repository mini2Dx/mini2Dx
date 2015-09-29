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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mini2Dx.core.engine.PositionChangeListener;
import org.mini2Dx.core.engine.Positionable;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Parallelogram;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.graphics.Color;

/**
 * Implements a point quad
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Quadtree#Point_quadtree">
 *      Wikipedia: Point Quad Tree</a>
 */
public class Quad<T extends Positionable> extends Rectangle implements PositionChangeListener<T> {
	public static Color QUAD_COLOR = new Color(1f, 0f, 0f, 0.5f);
	public static Color ELEMENT_COLOR = new Color(0f, 0f, 1f, 0.5f);

	private static final long serialVersionUID = -2034928347848875105L;

	protected Quad<T> parent;
	protected Quad<T> topLeft, topRight, bottomLeft, bottomRight;
	protected List<T> elements;
	protected final int elementLimitPerQuad;
	protected final int mergeWatermark;

	protected int totalElementsCache;

	/**
	 * Constructs a {@link Quad} with a specified element limit and watermark
	 * 
	 * @param elementLimitPerQuad
	 *            The maximum number of elements in a quad before it is split
	 *            into 4 child {@link Quad}s
	 * @param mergeWatermark
	 *            When a parent {@link Quad}'s total elements go lower than this mark,
	 *            the child {@link Quad}s will be merged back together
	 * @param x The x coordinate of the {@link Quad}
	 * @param y The y coordiante of the {@link Quad}
	 * @param width The width of the {@link Quad}
	 * @param height The height of the {@link Quad}
	 */
	public Quad(int elementLimitPerQuad, int mergeWatermark, float x, float y, float width, float height) {
		super(x, y, width, height);

		if (mergeWatermark >= elementLimitPerQuad) {
			throw new QuadWatermarkException(elementLimitPerQuad, mergeWatermark);
		}

		this.elementLimitPerQuad = elementLimitPerQuad;
		this.mergeWatermark = mergeWatermark;
		elements = new ArrayList<T>(elementLimitPerQuad);
	}

	/**
	 * Constructs a {@link Quad} with a specified element limit and no merging watermark. As elements
	 * are removed, small sized child {@link Quad}s will not be merged back together.
	 * 
	 * @param elementLimitPerQuad
	 *            The maximum number of elements in a quad before it is split
	 *            into 4 child {@link Quad}s
	 * @param x The x coordinate of the {@link Quad}
	 * @param y The y coordiante of the {@link Quad}
	 * @param width The width of the {@link Quad}
	 * @param height The height of the {@link Quad}
	 */
	public Quad(int elementLimitPerQuad, float x, float y, float width, float height) {
		this(elementLimitPerQuad, 0, x, y, width, height);
	}

	/**
	 * Constructs a {@link Quad} as a child of another {@link Quad}
	 * 
	 * @param parent The parent {@link Quad}
	 * @param x The x coordinate of the {@link Quad}
	 * @param y The y coordiante of the {@link Quad}
	 * @param width The width of the {@link Quad}
	 * @param height The height of the {@link Quad}
	 */
	public Quad(Quad<T> parent, float x, float y, float width, float height) {
		this(parent.getElementLimitPerQuad(), parent.getMergeWatermark(), x, y, width, height);
		this.parent = parent;
	}

	public void render(Graphics g) {
		Color tmp = g.getColor();

		if (topLeft != null) {
			topLeft.render(g);
			topRight.render(g);
			bottomLeft.render(g);
			bottomRight.render(g);
		} else {
			g.setColor(QUAD_COLOR);
			g.drawShape(this);
			g.drawRect(x, y, width, height);
			g.setColor(tmp);
		}

		tmp = g.getColor();
		g.setColor(ELEMENT_COLOR);
		for (T element : elements) {
			g.fillRect(element.getX(), element.getY(), 1f, 1f);
		}
		g.setColor(tmp);
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

		if (elements.size() > elementLimitPerQuad && width >= 2f && height >= 2f) {
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

		float halfWidth = width / 2f;
		float halfHeight = height / 2f;

		topLeft = new Quad<T>(this, x, y, halfWidth, halfHeight);
		topRight = new Quad<T>(this, x + halfWidth, y, halfWidth, halfHeight);
		bottomLeft = new Quad<T>(this, x, y + halfHeight, halfWidth, halfHeight);
		bottomRight = new Quad<T>(this, x + halfWidth, y + halfHeight, halfWidth, halfHeight);

		for (int i = elements.size() - 1; i >= 0; i--) {
			T element = elements.remove(i);
			element.removePositionChangeListener(this);
			addElementToChild(element);
		}
		elements = null;
	}

	protected boolean isMergable() {
		if (topLeft == null) {
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

		elements = new ArrayList<>();
		topLeft.getElements(elements);
		topRight.getElements(elements);
		bottomLeft.getElements(elements);
		bottomRight.getElements(elements);

		for (T element : elements) {
			element.removePositionChangeListener(topLeft);
			element.removePositionChangeListener(topRight);
			element.removePositionChangeListener(bottomLeft);
			element.removePositionChangeListener(bottomRight);
			element.addPostionChangeListener(this);
		}

		topLeft = null;
		topRight = null;
		bottomLeft = null;
		bottomRight = null;
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
		boolean result = elements.remove(element);
		element.removePositionChangeListener(this);

		if (parent == null) {
			return result;
		}
		if (parent.isMergable()) {
			parent.merge();
		}
		return result;
	}

	public List<T> getElementsWithinRegion(Parallelogram parallelogram) {
		List<T> result = new ArrayList<T>();
		getElementsWithinRegion(result, parallelogram);
		return result;
	}

	public void getElementsWithinRegion(Collection<T> result, Parallelogram parallelogram) {
		if (topLeft != null) {
			topLeft.getElementsWithinRegion(result, parallelogram);
			topRight.getElementsWithinRegion(result, parallelogram);
			bottomLeft.getElementsWithinRegion(result, parallelogram);
			bottomRight.getElementsWithinRegion(result, parallelogram);
		} else {
			for (int i = elements.size() - 1; i >= 0; i--) {
				T element = elements.get(i);
				if (element != null && parallelogram.contains(element.getX(), element.getY())) {
					result.add(element);
				}
			}
		}
	}

	public List<T> getElementsIntersectingLineSegment(LineSegment lineSegment) {
		List<T> result = new ArrayList<T>();
		getElementsIntersectingLineSegment(result, lineSegment);
		return result;
	}

	public void getElementsIntersectingLineSegment(Collection<T> result, LineSegment lineSegment) {
		if (topLeft != null) {
			if (topLeft.intersects(lineSegment))
				topLeft.getElementsIntersectingLineSegment(result, lineSegment);
			if (topRight.intersects(lineSegment))
				topRight.getElementsIntersectingLineSegment(result, lineSegment);
			if (bottomLeft.intersects(lineSegment))
				bottomLeft.getElementsIntersectingLineSegment(result, lineSegment);
			if (bottomRight.intersects(lineSegment))
				bottomRight.getElementsIntersectingLineSegment(result, lineSegment);
		} else {
			for (int i = elements.size() - 1; i >= 0; i--) {
				T element = elements.get(i);
				if (element != null && lineSegment.contains(element.getX(), element.getY())) {
					result.add(element);
				}
			}
		}
	}

	public List<T> getElements() {
		List<T> result = new ArrayList<T>();
		getElements(result);
		return result;
	}

	private void getElements(List<T> result) {
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
			totalElementsCache = elements.size();
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

		Quad<T> parentQuad = parent;
		while (parentQuad != null) {
			if (parentQuad.add(moved)) {
				return;
			}
			parentQuad = parentQuad.getParent();
		}
	}

	public Quad<T> getParent() {
		return parent;
	}

	public int getElementLimitPerQuad() {
		return elementLimitPerQuad;
	}

	public int getMergeWatermark() {
		return mergeWatermark;
	}
}
