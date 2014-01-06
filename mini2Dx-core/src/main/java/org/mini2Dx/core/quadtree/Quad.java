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
import java.util.List;

import org.mini2Dx.core.engine.Parallelogram;
import org.mini2Dx.core.engine.PositionChangeListener;
import org.mini2Dx.core.engine.Positionable;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.graphics.Color;

/**
 * Implements a point quad
 * 
 * @see <a
 *      href="http://en.wikipedia.org/wiki/Quadtree#Point_quadtree">Wikipedia:
 *      Point Quad Tree</a>
 * 
 * @author Thomas Cashman
 */
public class Quad<T extends Positionable> extends Rectangle implements
		PositionChangeListener<T> {
	public static Color QUAD_COLOR = new Color(1f, 0f, 0f, 0.5f);
	public static Color ELEMENT_COLOR = new Color(0f, 0f, 1f, 0.5f);
	
	private static final long serialVersionUID = -2034928347848875105L;

	protected Quad<T> parent;
	protected Quad<T> topLeft, topRight, bottomLeft, bottomRight;
	protected List<T> elements;
	protected int elementLimitPerQuad;

	public Quad(int elementLimitPerQuad, float x, float y, float width,
			float height) {
		super(x, y, width, height);
		this.elementLimitPerQuad = elementLimitPerQuad;
		elements = new ArrayList<T>(1);
	}

	public Quad(Quad<T> parent, float x, float y, float width, float height) {
		this(parent.getElementLimitPerQuad(), x, y, width, height);
		this.parent = parent;
	}
	
	public void render(Graphics g) {
		Color tmp = g.getColor();

		if(topLeft != null) {
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
		for(T element : elements) {
			g.fillRect(element.getX(), element.getY(), 1f, 1f);
		}
		g.setColor(tmp);
	}

	public boolean add(T element) {
		if (element == null)
			return false;

		if (!this.contains(element)) {
			return false;
		}

		if (topLeft != null) {
			return addElementToChild(element);
		}
		return addElement(element);
	}

	protected boolean addElement(T element) {
		elements.add(element);
		element.addPostionChangeListener(this);

		if (elements.size() > elementLimitPerQuad && width > 1f && height > 1f) {
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
		float halfWidth = width / 2f;
		float halfHeight = height / 2f;

		topLeft = new Quad<T>(this, x, y, halfWidth, halfHeight);
		topRight = new Quad<T>(this, x + halfWidth, y, halfWidth, halfHeight);
		bottomLeft = new Quad<T>(this, x, y + halfHeight, halfWidth, halfHeight);
		bottomRight = new Quad<T>(this, x + halfWidth, y + halfHeight,
				halfWidth, halfHeight);

		for (int i = elements.size() - 1; i >= 0; i--) {
			T element = elements.remove(i);
			element.removePositionChangeListener(this);
			addElementToChild(element);
		}
		elements = null;
	}

	public boolean remove(T element) {
		if (element == null)
			return false;

		if (!this.contains(element)) {
			return false;
		}

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
		return result;
	}

	public List<T> getElementsWithinRegion(Parallelogram parallelogram) {
		List<T> result = new ArrayList<T>();
		getElementsWithinRegion(result, parallelogram);
		return result;
	}

	public void getElementsWithinRegion(Collection<T> result,
			Parallelogram parallelogram) {
		if (topLeft != null) {
			topLeft.getElementsWithinRegion(result, parallelogram);
			topRight.getElementsWithinRegion(result, parallelogram);
			bottomLeft.getElementsWithinRegion(result, parallelogram);
			bottomRight.getElementsWithinRegion(result, parallelogram);
		} else {
			for (int i = elements.size() - 1; i >= 0; i--) {
				T element = elements.get(i);
				if (element != null && parallelogram.contains(element)) {
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

	public void getElementsIntersectingLineSegment(Collection<T> result,
			LineSegment lineSegment) {
		if (topLeft != null) {
			if (topLeft.intersects(lineSegment))
				topLeft.getElementsIntersectingLineSegment(result, lineSegment);
			if (topRight.intersects(lineSegment))
				topRight.getElementsIntersectingLineSegment(result, lineSegment);
			if (bottomLeft.intersects(lineSegment))
				bottomLeft.getElementsIntersectingLineSegment(result,
						lineSegment);
			if (bottomRight.intersects(lineSegment))
				bottomRight.getElementsIntersectingLineSegment(result,
						lineSegment);
		} else {
			for (int i = elements.size() - 1; i >= 0; i--) {
				T element = elements.get(i);
				if (element != null
						&& lineSegment.contains(element.getX(), element.getY())) {
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

	@Override
	public void positionChanged(T moved) {
		if (this.contains(moved))
			return;

		removeElement(moved);

		Quad<T> parentQuad = parent;
		while (parentQuad != null) {
			if(parentQuad.add(moved)) {
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
}
