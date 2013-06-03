/*******************************************************
 * Copyright (c) 2013 Thomas Cashman
 * All rights reserved.
 *******************************************************/
package org.mini2Dx.core.quadtree;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.geom.Spatial;

/**
 * 
 * @author Thomas Cashman
 */
public class RegionalQuad<T extends Spatial> extends Rectangle implements
		Quad<T> {
	private RegionalQuad<T>[][] childQuads;
	private Quad<T> parent;
	private float x, y, width, height;
	private List<T> values;

	public RegionalQuad(Quad<T> parent, float x, float y, float width,
			float height) {
		super(x, y, width, height);
		this.parent = parent;
		this.values = new CopyOnWriteArrayList<T>();
	}

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
					if (childQuads[x][y].intersects(spatial.getX(),
							spatial.getX(), spatial.getWidth(),
							spatial.getHeight())) {
						childQuads[x][y].add(spatial);
					}
				}
			}
		}
		values = null;
	}

	@Override
	public void add(T object) {
		if (childQuads == null) {
			values.add(object);
			
			if (values.size() >= getElementLimit()) {
				subdivide();
			}
		} else {
			for (int x = 0; x < 2; x++) {
				for (int y = 0; y < 2; y++) {
					for (T spatial : values) {
						if (childQuads[x][y].intersects(spatial.getX(),
								spatial.getX(), spatial.getWidth(),
								spatial.getHeight())) {
							childQuads[x][y].add(spatial);
						}
					}
				}
			}
		}
	}

	@Override
	public void remove(T object) {
		if (childQuads == null) {
			values.remove(object);
		} else {
			for (int x = 0; x < 2; x++) {
				for (int y = 0; y < 2; y++) {
					for (T spatial : values) {
						if (childQuads[x][y].intersects(spatial.getX(),
								spatial.getX(), spatial.getWidth(),
								spatial.getHeight())) {
							childQuads[x][y].remove(spatial);
						}
					}
				}
			}
		}
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
		if (childQuads != null) {
			for (int x = 0; x < 2; x++) {
				for (int y = 0; y < 2; y++) {
					result.addAll(childQuads[x][y].getQuadsFor(object));
				}
			}
		} else {
			result.add(this);
		}
		return result;
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
