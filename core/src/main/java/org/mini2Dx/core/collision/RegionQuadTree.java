/*******************************************************************************
 * Copyright 2019 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.core.collision;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.collections.FreeArray;
import org.mini2Dx.core.geom.*;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.util.IntSetPool;
import org.mini2Dx.gdx.utils.*;

/**
 * Implements a region quadtree
 *
 * @see <a href="http://en.wikipedia.org/wiki/Quadtree#The_region_quadtree">
 * Wikipedia: Region Quad Tree</a>
 */
public class RegionQuadTree<T extends CollisionArea> extends PointQuadTree<T> {


	public RegionQuadTree(int elementLimitPerQuad, float x, float y, float width, float height) {
		super(elementLimitPerQuad, x, y, width, height);
	}

	public RegionQuadTree(float minimumQuadWidth, float minimumQuadHeight, int elementLimitPerQuad, float x, float y, float width, float height) {
		super(minimumQuadWidth, minimumQuadHeight, elementLimitPerQuad, x, y, width, height);
	}

	@Override
	protected void debugRender(Graphics g, Quad quad) {
		if(quad.x - g.getTranslationX() > g.getViewportWidth()) {
			return;
		}
		if(quad.y - g.getTranslationY() > g.getViewportHeight()) {
			return;
		}
		if(quad.maxX - g.getTranslationX() < 0f) {
			return;
		}
		if(quad.maxY - g.getTranslationY() < 0f) {
			return;
		}

		if(quad.childIndex != -1) {
			debugRender(g, this.quads.get(quad.childIndex + Quad.CHILD_TOP_LEFT_OFFSET));
			debugRender(g, this.quads.get(quad.childIndex + Quad.CHILD_TOP_RIGHT_OFFSET));
			debugRender(g, this.quads.get(quad.childIndex + Quad.CHILD_BOTTOM_LEFT_OFFSET));
			debugRender(g, this.quads.get(quad.childIndex + Quad.CHILD_BOTTOM_RIGHT_OFFSET));
			return;
		}

		Color tmp = g.getColor();

		g.setColor(QUAD_COLOR);
		g.drawRect(quad.x, quad.y, quad.getWidth(), quad.getHeight());

		g.setColor(ELEMENT_COLOR);

		if(quad.elementsIndex > -1) {
			final QuadElements<T> elements = this.quadElements.get(quad.elementsIndex);
			for(T element : elements) {
				g.fillRect(element.getX(), element.getY(), element.getWidth(), element.getHeight());
			}
		}

		if(quad.elementBounds != null) {
			g.setColor(BOUNDS_COLOR);
			g.drawRect(quad.elementBounds.x, quad.elementBounds.y,
					quad.elementBounds.maxX - quad.elementBounds.x,
					quad.elementBounds.maxY - quad.elementBounds.y);
		}

		g.setColor(tmp);
	}

	@Override
	protected boolean belongsToQuad(Quad quad, T element) {
		return quad.contains(element.getCenterX(), element.getCenterY());
	}

	@Override
	protected Quad findQuad(Quad startQuad, T element, boolean updateBounds) {
		return findQuad(startQuad, element, element.getCenterX(), element.getCenterY(), updateBounds);
	}

	@Override
	public boolean add(T element) {
		if (element == null)
			return false;
		if (!rootQuad.contains(element.getCenterX(), element.getCenterY())) {
			return false;
		}
		final Quad quad = findQuad(element, element.getCenterX(), element.getCenterY(), true);
		return addToQuad(quad, element);
	}

	@Override
	public boolean remove(T element) {
		if (element == null)
			return false;

		if (!rootQuad.contains(element.getCenterX(), element.getCenterY())) {
			return false;
		}
		final int quadIndex = elementsToQuads.remove(element.getId(), -1);
		final Quad quad = quadIndex < 0 ? rootQuad : quads.get(quadIndex);
		return removeFromQuad(quad, element);
	}

	@Override
	protected boolean updateBounds(Quad quad, T element) {
		initBounds(quad, element);
		final float boundsX = quad.elementBounds.x;
		final float boundsY = quad.elementBounds.y;
		final float boundsMaxX = quad.elementBounds.maxX;
		final float boundsMaxY = quad.elementBounds.maxY;
		final float boundsWidth = boundsMaxX - boundsX;
		final float boundsHeight = boundsMaxY - boundsY;

		float minX = Math.min(element.getX(), boundsX);
		float minY = Math.min(element.getY(), boundsY);
		float maxX = Math.max(element.getMaxX(), boundsMaxX);
		float maxY = Math.max(element.getMaxY(), boundsMaxY);

		final float newWidth = maxX - minX;
		final float newHeight = maxY - minY;
		quad.elementBounds.x = minX;
		quad.elementBounds.y = minY;
		quad.elementBounds.maxX = maxX;
		quad.elementBounds.maxY = maxY;
		return boundsWidth != newWidth || boundsHeight != newHeight;
	}

	@Override
	protected void addElementsOverlappingArea(Quad quad, Array<T> result, Rectangle area, boolean allElements) {
		final QuadElements<T> elements = this.quadElements.get(quad.elementsIndex);
		if(allElements) {
			result.addAll(elements);
			return;
		}

		for(int i = elements.size - 1; i >= 0; i--) {
			final T element = elements.get(i);
			if(area.intersects(element)) {
				result.add(element);
			}
		}
	}

	@Override
	protected void addElementsOverlappingAreaIgnoringEdges(Quad quad, Array<T> result, Rectangle area, boolean allElements) {
		final QuadElements<T> elements = this.quadElements.get(quad.elementsIndex);
		if(allElements) {
			result.addAll(elements);
			return;
		}

		for(int i = elements.size - 1; i >= 0; i--) {
			final T element = elements.get(i);
			if(area.intersectsIgnoringEdges(element)) {
				result.add(element);
			}
		}
	}

	@Override
	protected void addElementsContainedInArea(Quad quad, Array<T> result, Rectangle area, boolean allElements) {
		final QuadElements<T> elements = this.quadElements.get(quad.elementsIndex);
		if(allElements) {
			result.addAll(elements);
			return;
		}

		for(int i = elements.size - 1; i >= 0; i--) {
			final T element = elements.get(i);
			if(area.contains(element)) {
				result.add(element);
			}
		}
	}

	@Override
	protected void addElementsIntersectingLineSegment(Quad quad, Array<T> result, LineSegment lineSegment) {
		final QuadElements<T> elements = this.quadElements.get(quad.elementsIndex);

		for(int i = elements.size - 1; i >= 0; i--) {
			final T element = elements.get(i);
			if(element.intersects(lineSegment)) {
				result.add(element);
			}
		}
	}

	@Override
	protected void addElementsContainingPoint(Quad quad, Array<T> result, Point point) {
		final QuadElements<T> elements = this.quadElements.get(quad.elementsIndex);

		for(int i = elements.size - 1; i >= 0; i--) {
			final T element = elements.get(i);
			if(!element.contains(point.x, point.y)) {
				continue;
			}
			result.add(element);
		}
	}

	private void addElementsContainingArea(Quad quad, Array<T> result, Rectangle area) {
		final QuadElements<T> elements = this.quadElements.get(quad.elementsIndex);

		for(int i = elements.size - 1; i >= 0; i--) {
			final T element = elements.get(i);
			if(element.contains(area)) {
				result.add(element);
			}
		}
	}

	@Override
	public void getElementsContainingArea(Array<T> result, Rectangle area) {
		getElementsContainingArea(rootQuad, result, area);
	}

	private void getElementsContainingArea(Quad initialQuad, Array<T> result, Rectangle area) {
		processQueue.add(initialQuad);

		final float rectMinX = area.getMinX();
		final float rectMinY = area.getMinY();
		final float rectMaxX = area.getMaxX();
		final float rectMaxY = area.getMaxY();

		while(processQueue.size > 0) {
			final Quad quad = processQueue.removeIndex(0);
			if(!quad.elementsOverlap(rectMinX, rectMinY, rectMaxX, rectMaxY)) {
				continue;
			}
			if(quad.childIndex < 0) {
				leaves.add(quad);
				continue;
			}
			processQueue.add(this.quads.get(quad.childIndex));
			processQueue.add(this.quads.get(quad.childIndex + Quad.CHILD_TOP_RIGHT_OFFSET));
			processQueue.add(this.quads.get(quad.childIndex + Quad.CHILD_BOTTOM_LEFT_OFFSET));
			processQueue.add(this.quads.get(quad.childIndex + Quad.CHILD_BOTTOM_RIGHT_OFFSET));
		}
		while(leaves.size > 0) {
			final Quad leaf = leaves.removeIndex(0);
			addElementsContainingArea(leaf, result, area);
		}
	}
}
