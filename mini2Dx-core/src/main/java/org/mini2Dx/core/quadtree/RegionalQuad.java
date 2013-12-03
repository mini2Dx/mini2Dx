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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.mini2Dx.core.engine.Parallelogram;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Rectangle;

/**
 * An implementation of {@link Quad} based on regions
 * @author Thomas Cashman
 */
public class RegionalQuad<T extends Parallelogram> extends
		Rectangle implements Quad<T> {
	private static final long serialVersionUID = 663493452499066041L;
	private RegionalQuad<T>[][] childQuads;
	private Quad<T> parent;
	private List<T> values;

	public RegionalQuad(Quad<T> parent, float x, float y, float width,
			float height) {
		super(x, y, width, height);
		this.parent = parent;
		this.values = new CopyOnWriteArrayList<T>();
	}

	@SuppressWarnings("unchecked")
	private void subdivide() {
		float regionWidth = width / 2f;
		float regionHeight = height / 2f;

		childQuads = new RegionalQuad[2][2];
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 2; y++) {
				float quadX = this.x + (regionWidth * x);
				float quadY = this.y + (regionHeight * y);
				childQuads[x][y] = new RegionalQuad<T>(this, quadX, quadY,
						regionWidth, regionHeight);

				for (T spatial : values) {
					if (childQuads[x][y].contains(spatial)) {
						childQuads[x][y].add(spatial);
					}
				}
			}
		}
		values.clear();
		values = null;
	}

	@Override
	public void add(T object) {
		if (childQuads == null) {
			values.add(object);

			if (values.size() > getElementLimit()) {
				subdivide();
			} else {
				object.addPostionChangeListener(this);
			}
		} else {
			for (int x = 0; x < 2; x++) {
				for (int y = 0; y < 2; y++) {
					if (childQuads[x][y].contains(object)) {
						childQuads[x][y].add(object);
					}
				}
			}
		}
	}

	@Override
	public void remove(T object) {
		if (childQuads == null) {
			values.remove(object);
			object.removePositionChangeListener(this);
		} else {
			int childElementCount = 0;
			for (int x = 0; x < 2; x++) {
				for (int y = 0; y < 2; y++) {
					if (childQuads[x][y].contains(object)) {
						childQuads[x][y].remove(object);
						childElementCount += childQuads[x][y]
								.getNumberOfElements();
					}
				}
			}

			if (childElementCount < (getElementLimit() / 2)) {
				values = new ArrayList<T>();
				for (int x = 0; x < 2; x++) {
					for (int y = 0; y < 2; y++) {
						values.addAll(childQuads[x][y].removeAll());
					}
				}
				childQuads = null;
			}
		}
	}

	public List<T> removeAll() {
		List<T> result = new ArrayList<T>();
		if (childQuads == null) {
			result.addAll(values);
			for (T object : values) {
				object.removePositionChangeListener(this);
			}
			values.clear();
		} else {
			for (int x = 0; x < 2; x++) {
				for (int y = 0; y < 2; y++) {
					result.addAll(childQuads[x][y].removeAll());
				}
			}
			values = new ArrayList<T>();
			childQuads = null;
		}
		return result;
	}

	@Override
	public int getNumberOfElements() {
		if (childQuads == null) {
			return values.size();
		} else {
			int result = 0;
			for (int x = 0; x < 2; x++) {
				for (int y = 0; y < 2; y++) {
					result += childQuads[x][y].getNumberOfElements();
				}
			}
			return result;
		}
	}

	public List<Quad<T>> getQuadsFor(T object) {
		List<Quad<T>> result = new ArrayList<Quad<T>>(1);
		getQuadsFor(object, result);
		return result;
	}
	
	private void getQuadsFor(T object, List<Quad<T>> result) {
		if (childQuads != null) {
			for (int x = 0; x < 2; x++) {
				for (int y = 0; y < 2; y++) {
					if(childQuads[x][y].contains(object)) {
						childQuads[x][y].getQuadsFor(object, result);
					}
				}
			}
		} else {
			result.add(this);
		}
	}

	public List<Quad<T>> getQuadsFor(LineSegment line) {
		List<Quad<T>> result = new ArrayList<Quad<T>>(1);
		getQuadsFor(line, result);
		return result;
	}
	
	private void getQuadsFor(LineSegment line, List<Quad<T>> result) {
		if (childQuads != null) {
			for (int x = 0; x < 2; x++) {
				for (int y = 0; y < 2; y++) {
					if (line.intersects(childQuads[x][y])) {
						childQuads[x][y].getQuadsFor(line, result);
					}
				}
			}
		} else if (line.intersects(this)) {
			result.add(this);
		}
	}

	public List<T> getIntersectionsFor(LineSegment line) {
		List<T> result = new ArrayList<T>(1);
		getIntersectionsFor(line, result);
		return result;
	}
	
	private void getIntersectionsFor(LineSegment line, List<T> result) {
		if (childQuads != null) {
			for (int x = 0; x < 2; x++) {
				for (int y = 0; y < 2; y++) {
					if (line.intersects(childQuads[x][y])) {
						childQuads[x][y]
								.getIntersectionsFor(line, result);
					}
				}
			}
		} else if (line.intersects(this)) {
			for (T value : values) {
				if (value.intersects(line)) {
					result.add(value);
				}
			}
		}
	}
	
	public List<T> getIntersectionsFor(Rectangle rectangle) {
		List<T> result = new ArrayList<T>(1);
		getIntersectionsFor(rectangle, result);
		return result;
	}
	
	private void getIntersectionsFor(Rectangle rectangle, List<T> result) {
		if (childQuads != null) {
			for (int x = 0; x < 2; x++) {
				for (int y = 0; y < 2; y++) {
					if (rectangle.intersects(childQuads[x][y])) {
						childQuads[x][y]
								.getIntersectionsFor(rectangle, result);
					}
				}
			}
		} else if (rectangle.intersects(this)) {
			for (T value : values) {
				if (value.intersects(rectangle)) {
					result.add(value);
				}
			}
		}
	}

	@Override
	public List<T> getValues() {
		if (childQuads != null) {
			List<T> result = new ArrayList<T>(1);
			for (int x = 0; x < 2; x++) {
				for (int y = 0; y < 2; y++) {
					result.addAll(childQuads[x][y].getValues());
				}
			}
			return result;
		} else {
			return values;
		}
	}

	@Override
	public void positionChanged(T moved) {
		if (!this.contains(moved)) {
			if (values != null) {
				remove(moved);
			}
			if (parent != null) {
				parent.positionChanged(moved);
			}
		} else {
			add(moved);
		}
	}

	public Quad<T> getParent() {
		return parent;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	@Override
	public int getElementLimit() {
		return parent.getElementLimit();
	}
}
