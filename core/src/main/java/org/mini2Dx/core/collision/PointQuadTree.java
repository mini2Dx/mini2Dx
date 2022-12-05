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
 * Implements a point quadtree
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Quadtree#Point_quadtree">
 *      Wikipedia: Point Quad Tree</a>
 */
public class PointQuadTree<T extends CollisionObject> implements QuadTree<T> {
	public static int INITIAL_QUAD_ELEMENTS_POOL_SIZE = 64;
	public static float DEFAULT_MINIMUM_QUAD_SIZE = 8f;
	public static Color QUAD_COLOR = Mdx.graphics != null ? Mdx.graphics.newColor(1f, 0f, 0f, 0.5f) : null;
	public static Color BOUNDS_COLOR = Mdx.graphics != null ? Mdx.graphics.newColor(1f, 0f, 1f, 0.5f) : null;
	public static Color ELEMENT_COLOR = Mdx.graphics != null ? Mdx.graphics.newColor(0f, 0f, 1f, 0.5f) : null;

	private static final long serialVersionUID = -2034928347848875105L;

	protected final Queue<QuadElements<T>> quadElementsPool = new Queue<QuadElements<T>>();


	protected final IntIntMap elementsToQuads = new IntIntMap();
	protected final FreeArray<Quad> quads = new FreeArray<Quad>();
	protected final FreeArray<QuadElements<T>> quadElements = new FreeArray<QuadElements<T>>();
	protected final Quad rootQuad = new Quad();

	protected final int elementLimitPerQuad;
	protected final float minimumQuadWidth, minimumQuadHeight;

	protected final Array<Quad> processQueue = new Array<Quad>(false, 256);
	protected final Array<Quad> leaves = new Array<Quad>(false, 256);

	private int totalElements = 0;
	private boolean cleanupRequired = false;


	/**
	 * Constructs a {@link PointQuadTree} with a specified element limit and no
	 * merging watermark. As elements are removed, small sized child
	 * {@link PointQuadTree}s will not be merged back together.
	 * 
	 * @param elementLimitPerQuad
	 *            The maximum number of elements in a quad before it is split
	 *            into 4 child {@link PointQuadTree}s
	 * @param x
	 *            The x coordinate of the {@link PointQuadTree}
	 * @param y
	 *            The y coordiante of the {@link PointQuadTree}
	 * @param width
	 *            The width of the {@link PointQuadTree}
	 * @param height
	 *            The height of the {@link PointQuadTree}
	 */
	public PointQuadTree(int elementLimitPerQuad, float x, float y, float width, float height) {
		this(DEFAULT_MINIMUM_QUAD_SIZE, DEFAULT_MINIMUM_QUAD_SIZE, elementLimitPerQuad, x, y, width, height);
	}

	/**
	 * Constructs a {@link PointQuadTree} with a specified minimum quad size,
	 * element limit and watermark
	 * 
	 * @param minimumQuadWidth
	 *            The minimum width of quads. Quads will not subdivide smaller
	 *            than this width.
	 * @param minimumQuadHeight
	 *            The minimum height of quads. Quads will not subdivide smaller
	 *            than this height.
	 * @param elementLimitPerQuad
	 *            The maximum number of elements in a quad before it is split
	 *            into 4 child {@link PointQuadTree}s
	 * @param x
	 *            The x coordinate of the {@link PointQuadTree}
	 * @param y
	 *            The y coordiante of the {@link PointQuadTree}
	 * @param width
	 *            The width of the {@link PointQuadTree}
	 * @param height
	 *            The height of the {@link PointQuadTree}
	 */
	public PointQuadTree(float minimumQuadWidth, float minimumQuadHeight, int elementLimitPerQuad,
			float x, float y, float width, float height) {
		super();
		rootQuad.x = x;
		rootQuad.y = y;
		rootQuad.maxX = x + width;
		rootQuad.maxY = y + height;

		this.elementLimitPerQuad = elementLimitPerQuad;
		this.minimumQuadWidth = minimumQuadWidth;
		this.minimumQuadHeight = minimumQuadHeight;

		for(int i = 0; i < INITIAL_QUAD_ELEMENTS_POOL_SIZE; i++) {
			quadElementsPool.addLast(new QuadElements<T>());
		}
		quads.ensureCapacity(INITIAL_QUAD_ELEMENTS_POOL_SIZE);
		quadElements.ensureCapacity(INITIAL_QUAD_ELEMENTS_POOL_SIZE);
	}

	protected QuadElements<T> allocateQuadElements() {
		if(quadElementsPool.size == 0) {
			return new QuadElements<T>();
		}
		return quadElementsPool.removeFirst();
	}

	protected void releaseQuadElements(QuadElements<T> elements) {
		elements.clear();
		this.quadElementsPool.addLast(elements);
	}

	public boolean cleanup() {
		return updateBounds(rootQuad);
	}

	public void debugRender(Graphics g) {
		debugRender(g, rootQuad);
	}

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
				g.fillRect(element.getX(), element.getY(), 1f, 1f);
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

	protected Quad findQuad(T element, float x, float y, boolean updateBounds) {
		return findQuad(rootQuad, element, x, y, updateBounds);
	}

	protected Quad findQuad(Quad startQuad, T element, boolean updateBounds) {
		return findQuad(startQuad, element, element.getX(), element.getY(), updateBounds);
	}

	protected Quad findQuad(Quad startQuad, T element, float x, float y, boolean updateBounds) {
		Quad result = startQuad;
		OUTER_LOOP: while(result.childIndex > -1) {
			//Scan down tree
			if(updateBounds) {
				updateBounds(result, element);
			}
			for(int i = 0; i < 4; i++) {
				Quad nextQuad = quads.get(result.childIndex + i);
				if(!nextQuad.contains(x, y)) {
					continue;
				}
				result = nextQuad;
				continue OUTER_LOOP;
			}
			break;
		}
		return result;
	}

	protected boolean belongsToQuad(Quad quad, T element) {
		return quad.contains(element.getX(), element.getY());
	}

	protected boolean addToQuad(Quad quad, T element) {
		if (!belongsToQuad(quad, element)) {
			return false;
		}
		if(quad.elementsIndex < 0) {
			quad.elementsIndex = this.quadElements.add(allocateQuadElements());
		}
		final QuadElements<T> quadElements = this.quadElements.get(quad.elementsIndex);
		quadElements.add(element);
		totalElements++;

		element.addPostionChangeListener(this);
		elementsToQuads.put(element.getId(), quad.index);
		updateBounds(quad, element);

		if(quadElements.size > elementLimitPerQuad) {
			subdivide(quad);
		}
		return true;
	}

	protected boolean removeFromQuad(Quad quad, T element) {
		if(quad.elementsIndex < 0) {
			return false;
		}
		final QuadElements<T> quadElements = this.quadElements.get(quad.elementsIndex);
		if(!quadElements.removeValue(element, false)) {
			return false;
		}
		totalElements--;

		elementsToQuads.remove(element.getId(), -1);
		element.removePositionChangeListener(this);
		cleanupRequired = true;
		return true;
	}

	public void addAll(Array<T> elementsToAdd) {
		if (elementsToAdd == null || elementsToAdd.size == 0) {
			return;
		}

		for (T element : elementsToAdd) {
			add(element);
		}
	}

	public boolean add(T element) {
		if (element == null)
			return false;
		if (!rootQuad.contains(element.getX(), element.getY())) {
			return false;
		}
		final Quad quad = findQuad(element, element.getX(), element.getY(), true);
		return addToQuad(quad, element);
	}

	public void removeAll(Array<T> elementsToRemove) {
		if (elementsToRemove == null || elementsToRemove.size == 0) {
			return;
		}

		for (T element : elementsToRemove) {
			remove(element);
		}
	}

	public boolean remove(T element) {
		if (element == null)
			return false;

		if (!rootQuad.contains(element.getX(), element.getY())) {
			return false;
		}
		final int quadIndex = elementsToQuads.remove(element.getId(), -1);
		final Quad quad = quadIndex < 0 ? rootQuad : quads.get(quadIndex);
		return removeFromQuad(quad, element);
	}

	protected void initBounds(Quad quad, T element) {
		if(quad.elementBounds != null) {
			return;
		}
		quad.elementBounds = QuadElementBounds.allocate();
		quad.elementBounds.x = element.getX();
		quad.elementBounds.y = element.getY();
		quad.elementBounds.maxX = quad.elementBounds.x + 1f;
		quad.elementBounds.maxY = quad.elementBounds.y + 1f;
	}

	private void initBounds(Quad quad) {
		if(quad.elementBounds != null) {
			return;
		}
		quad.elementBounds = QuadElementBounds.allocate();
	}

	private boolean disposeBounds(Quad quad) {
		if(quad.elementBounds == null) {
			return false;
		}
		quad.elementBounds.dispose();
		quad.elementBounds = null;
		return true;
	}

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
		float maxX = Math.max(element.getX(), boundsMaxX);
		float maxY = Math.max(element.getY(), boundsMaxY);

		final float newWidth = maxX - minX;
		final float newHeight = maxY - minY;
		quad.elementBounds.x = minX;
		quad.elementBounds.y = minY;
		quad.elementBounds.maxX = maxX;
		quad.elementBounds.maxY = maxY;
		return boundsWidth != newWidth || boundsHeight != newHeight;
	}

	private boolean updateBounds(Quad quad) {
		if(!cleanupRequired) {
			return false;
		}
		cleanupRequired = false;

		if(quad.childIndex > -1) {
			final Quad topLeft = quads.get(quad.childIndex);
			final Quad topRight = quads.get(quad.childIndex + Quad.CHILD_TOP_RIGHT_OFFSET);
			final Quad bottomLeft = quads.get(quad.childIndex + Quad.CHILD_BOTTOM_LEFT_OFFSET);
			final Quad bottomRight = quads.get(quad.childIndex + Quad.CHILD_BOTTOM_RIGHT_OFFSET);

			boolean childBoundsUpdate = false;

			if(updateBounds(topLeft)) {
				childBoundsUpdate = true;
			}
			if(updateBounds(topRight)) {
				childBoundsUpdate = true;
			}
			if(updateBounds(bottomLeft)) {
				childBoundsUpdate = true;
			}
			if(updateBounds(bottomRight)) {
				childBoundsUpdate = true;
			}
			if(!childBoundsUpdate) {
				return false;
			}
			float minX;
			float minY;
			float maxX;
			float maxY;

			if(topLeft.elementBounds != null) {
				minX = topLeft.elementBounds.x;
				minY = topLeft.elementBounds.y;
				maxX = topLeft.elementBounds.maxX;
				maxY = topLeft.elementBounds.maxY;
			} else if(topRight.elementBounds != null) {
				minX = topRight.elementBounds.x;
				minY = topRight.elementBounds.y;
				maxX = topRight.elementBounds.maxX;
				maxY = topRight.elementBounds.maxY;
			} else if(bottomLeft.elementBounds != null) {
				minX = bottomLeft.elementBounds.x;
				minY = bottomLeft.elementBounds.y;
				maxX = bottomLeft.elementBounds.maxX;
				maxY = bottomLeft.elementBounds.maxY;
			} else if(bottomRight.elementBounds != null) {
				minX = bottomRight.elementBounds.x;
				minY = bottomRight.elementBounds.y;
				maxX = bottomRight.elementBounds.maxX;
				maxY = bottomRight.elementBounds.maxY;
			} else {
				return disposeBounds(quad);
			}

			if(topRight.elementBounds != null) {
				minX = Math.min(minX, topRight.elementBounds.x);
				minY = Math.min(minY, topRight.elementBounds.y);
				maxX = Math.max(maxX, topRight.elementBounds.maxX);
				maxY = Math.max(maxY, topRight.elementBounds.maxY);
			}
			if(bottomLeft.elementBounds != null) {
				minX = Math.min(minX, bottomLeft.elementBounds.x);
				minY = Math.min(minY, bottomLeft.elementBounds.y);
				maxX = Math.max(maxX, bottomLeft.elementBounds.maxX);
				maxY = Math.max(maxY, bottomLeft.elementBounds.maxY);
			}
			if(bottomRight.elementBounds != null) {
				minX = Math.min(minX, bottomRight.elementBounds.x);
				minY = Math.min(minY, bottomRight.elementBounds.y);
				maxX = Math.max(maxX, bottomRight.elementBounds.maxX);
				maxY = Math.max(maxY, bottomRight.elementBounds.maxY);
			}

			initBounds(quad);
			quad.elementBounds.x = minX;
			quad.elementBounds.y = minY;
			quad.elementBounds.maxX = maxX;
			quad.elementBounds.maxY = maxY;
			return true;
		}
		if(quad.elementsIndex < 0) {
			return false;
		}

		final QuadElements<T> quadElements = this.quadElements.get(quad.elementsIndex);

		boolean boundsUpdated = false;
		for(int i = quadElements.size - 1; i >= 0; i--) {
			final T element = quadElements.get(i);
			boundsUpdated |= updateBounds(quad, element);
		}
		return boundsUpdated;
	}

	private void subdivide(Quad quad) {
		final float halfWidth = ((quad.maxX - quad.x) * 0.5f);
		final float halfHeight = ((quad.maxY - quad.y) * 0.5f);

		if(halfWidth < minimumQuadWidth) {
			return;
		}
		if(halfHeight < minimumQuadHeight) {
			return;
		}

		final Quad topLeft = Quad.allocate();
		topLeft.x = quad.x;
		topLeft.y = quad.y;
		topLeft.maxX = quad.x + halfWidth;
		topLeft.maxY = quad.y + halfHeight;

		final Quad topRight = Quad.allocate();
		topRight.x = quad.x + halfWidth;
		topRight.y = quad.y;
		topRight.maxX = topRight.x + halfWidth;
		topRight.maxY = topRight.y + halfHeight;

		final Quad bottomLeft = Quad.allocate();
		bottomLeft.x = quad.x;
		bottomLeft.y = quad.y + halfHeight;
		bottomLeft.maxX = bottomLeft.x + halfWidth;
		bottomLeft.maxY = bottomLeft.y + halfHeight;

		final Quad bottomRight = Quad.allocate();
		bottomRight.x = quad.x + halfWidth;
		bottomRight.y = quad.y + halfHeight;
		bottomRight.maxX = bottomRight.x + halfWidth;
		bottomRight.maxY = bottomRight.y + halfHeight;

		quad.childIndex = quads.add(topLeft);
		topLeft.index = quad.childIndex;
		topLeft.parentIndex = quad.index;

		quads.add(topRight);
		topRight.index = quad.childIndex + Quad.CHILD_TOP_RIGHT_OFFSET;
		topRight.parentIndex = quad.index;

		quads.add(bottomLeft);
		bottomLeft.index = quad.childIndex + Quad.CHILD_BOTTOM_LEFT_OFFSET;
		bottomLeft.parentIndex = quad.index;

		quads.add(bottomRight);
		bottomRight.index = quad.childIndex + Quad.CHILD_BOTTOM_RIGHT_OFFSET;
		bottomRight.parentIndex = quad.index;

		final QuadElements<T> elements = this.quadElements.remove(quad.elementsIndex);
		topLeft.elementsIndex = this.quadElements.add(allocateQuadElements());
		topRight.elementsIndex = this.quadElements.add(allocateQuadElements());
		bottomLeft.elementsIndex = this.quadElements.add(allocateQuadElements());
		bottomRight.elementsIndex = this.quadElements.add(allocateQuadElements());
		quad.elementsIndex = -1;

		totalElements -= elements.size;

		for(T element : elements) {
			element.removePositionChangeListener(this);

			if (addToQuad(topLeft, element)) {
				continue;
			}
			if (addToQuad(topRight, element)) {
				continue;
			}
			if (addToQuad(bottomLeft, element)) {
				continue;
			}
			if (addToQuad(bottomRight, element)) {
				continue;
			}
		}
		releaseQuadElements(elements);
		cleanupRequired = true;
	}

	public void clear() {
		this.elementsToQuads.clear();
		this.rootQuad.reset();

		for(QuadElements<T> elements : this.quadElements) {
			for(int i = elements.size - 1; i >= 0; i--) {
				elements.get(i).removePositionChangeListener(this);
			}
			releaseQuadElements(elements);
		}
		this.quadElements.clear();

		for(Quad quad : this.quads) {
			quad.dispose();
		}
		this.quads.clear();

		totalElements = 0;
		cleanupRequired = true;
	}

	protected void addElementsOverlappingArea(Quad quad, Array<T> result, Rectangle area, boolean allElements) {
		final QuadElements<T> elements = this.quadElements.get(quad.elementsIndex);
		if(allElements) {
			result.addAll(elements);
			return;
		}

		for(int i = elements.size - 1; i >= 0; i--) {
			final T element = elements.get(i);
			if(area.contains(element.getX(), element.getY())) {
				result.add(element);
			}
		}
	}

	protected void addElementsOverlappingAreaIgnoringEdges(Quad quad, Array<T> result, Rectangle area, boolean allElements) {
		final QuadElements<T> elements = this.quadElements.get(quad.elementsIndex);
		if(allElements) {
			result.addAll(elements);
			return;
		}

		for(int i = elements.size - 1; i >= 0; i--) {
			final T element = elements.get(i);
			if(area.contains(element.getX(), element.getY())) {
				result.add(element);
			}
		}
	}

	protected void addElementsContainedInArea(Quad quad, Array<T> result, Rectangle area, boolean allElements) {
		final QuadElements<T> elements = this.quadElements.get(quad.elementsIndex);
		if(allElements) {
			result.addAll(elements);
			return;
		}

		for(int i = elements.size - 1; i >= 0; i--) {
			final T element = elements.get(i);
			if(area.contains(element.getX(), element.getY())) {
				result.add(element);
			}
		}
	}

	protected void addElementsIntersectingLineSegment(Quad quad, Array<T> result, LineSegment lineSegment) {
		final QuadElements<T> elements = this.quadElements.get(quad.elementsIndex);

		for(int i = elements.size - 1; i >= 0; i--) {
			final T element = elements.get(i);
			if(lineSegment.contains(element.getX(), element.getY())) {
				result.add(element);
			}
		}
	}

	protected void addElementsContainingPoint(Quad quad, Array<T> result, Point point) {
		final QuadElements<T> elements = this.quadElements.get(quad.elementsIndex);

		for(int i = elements.size - 1; i >= 0; i--) {
			final T element = elements.get(i);
			if(element.getX() != point.x) {
				continue;
			}
			if(element.getY() != point.y) {
				continue;
			}
			result.add(element);
		}
	}

	private int addElements(Quad quad, QuadTreeInspector<T> inspector) {
		if(quad.elementsIndex < 0) {
			return QuadTreeInspector.CONTINUE_INSPECTING_ELEMENTS;
		}
		final QuadElements<T> elements = this.quadElements.get(quad.elementsIndex);

		for(int i = elements.size - 1; i >= 0; i--) {
			final T element = elements.get(i);
			final int result = inspector.inspect(element);
			if(result >= QuadTreeInspector.CONTINUE_INSPECTING_ELEMENTS) {
				continue;
			}
			return result;
		}
		return QuadTreeInspector.CONTINUE_INSPECTING_ELEMENTS;
	}

	@Override
	public Array<T> getElementsOverlappingArea(Rectangle area) {
		final Array<T> result = new Array<>();
		getElementsOverlappingArea(result, area);
		return result;
	}

	@Override
	public void getElementsOverlappingArea(Array<T> result, Rectangle area) {
		getElementsOverlappingArea(rootQuad, result, area);
	}

	private void getElementsOverlappingArea(Quad initialQuad, Array<T> result, Rectangle area) {
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
			addElementsOverlappingArea(leaf, result, area,
					leaf.elementBounds.containedBy(rectMinX, rectMinY, rectMaxX, rectMaxY));
		}
	}

	@Override
	public Array<T> getElementsOverlappingArea(Circle area) {
		final Array<T> result = new Array<>();
		getElementsOverlappingArea(result, area);
		return result;
	}

	@Override
	public void getElementsOverlappingArea(Array<T> result, Circle area) {
		getElementsOverlappingArea(rootQuad, result, area.getBoundingBox());
	}

	@Override
	public Array<T> getElementsOverlappingAreaIgnoringEdges(Rectangle area) {
		final Array<T> result = new Array<>();
		getElementsOverlappingAreaIgnoringEdges(result, area);
		return result;
	}

	@Override
	public void getElementsOverlappingAreaIgnoringEdges(Array<T> result, Rectangle area) {
		getElementsOverlappingAreaIgnoringEdges(rootQuad, result, area);
	}

	private void getElementsOverlappingAreaIgnoringEdges(Quad initialQuad, Array<T> result, Rectangle area) {
		processQueue.add(initialQuad);

		final float rectMinX = area.getMinX();
		final float rectMinY = area.getMinY();
		final float rectMaxX = area.getMaxX();
		final float rectMaxY = area.getMaxY();

		while(processQueue.size > 0) {
			final Quad quad = processQueue.removeIndex(0);
			if(!quad.elementsOverlapIgnoringEdges(rectMinX, rectMinY, rectMaxX, rectMaxY)) {
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
			addElementsOverlappingAreaIgnoringEdges(leaf, result, area,
					leaf.elementBounds.containedBy(rectMinX, rectMinY, rectMaxX, rectMaxY));
		}
	}

	@Override
	public Array<T> getElementsContainedInArea(Rectangle area) {
		final Array<T> result = new Array<>();
		getElementsContainedInArea(result, area);
		return result;
	}

	@Override
	public void getElementsContainedInArea(Array<T> result, Rectangle area) {
		getElementsContainedInArea(rootQuad, result, area);
	}

	private void getElementsContainedInArea(Quad initialQuad, Array<T> result, Rectangle area) {
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
			addElementsContainedInArea(leaf, result, area,
					leaf.elementBounds.containedBy(rectMinX, rectMinY, rectMaxX, rectMaxY));
		}
	}

	@Override
	public void getElements(QuadTreeInspector<T> inspector) {
		getElements(rootQuad, inspector);
	}

	private void getElements(Quad initialQuad, QuadTreeInspector<T> inspector) {
		processQueue.add(initialQuad);

		while(processQueue.size > 0) {
			final Quad quad = processQueue.removeIndex(0);
			if(!inspector.isQuadValidForInspection(quad)) {
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
			getElements(leaf, inspector);
		}
	}

	@Override
	public Array<T> getElementsContainingArea(Rectangle area) {
		final Array<T> result = new Array<>();
		getElementsContainingArea(result, area);
		return result;
	}

	@Override
	public void getElementsContainingArea(Array<T> result, Rectangle area) {

	}

	@Override
	public Array<T> getElementsIntersectingLineSegment(LineSegment lineSegment) {
		final Array<T> result = new Array<>();
		getElementsIntersectingLineSegment(result, lineSegment);
		return result;
	}

	@Override
	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment) {
		getElementsIntersectingLineSegment(rootQuad, result, lineSegment);
	}

	private void getElementsIntersectingLineSegment(Quad initialQuad, Array<T> result, LineSegment lineSegment) {
		processQueue.add(initialQuad);

		while(processQueue.size > 0) {
			final Quad quad = processQueue.removeIndex(0);
			if(!quad.elementsIntersect(lineSegment)) {
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
			addElementsIntersectingLineSegment(leaf, result, lineSegment);
		}
	}

	@Override
	public Array<T> getElementsContainingPoint(Point point) {
		final Array<T> result = new Array<>();
		getElementsContainingPoint(result, point);
		return result;
	}

	@Override
	public void getElementsContainingPoint(Array<T> result, Point point) {
		getElementsContainingPoint(rootQuad, result, point);
	}

	private void getElementsContainingPoint(Quad initialQuad, Array<T> result, Point point) {
		processQueue.add(initialQuad);

		while(processQueue.size > 0) {
			final Quad quad = processQueue.removeIndex(0);
			if(!quad.elementsContain(point)) {
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
			addElementsContainingPoint(leaf, result, point);
		}
	}

	@Override
	public Array<T> getElements() {
		final Array<T> result = new Array<>();
		getElements(result);
		return result;
	}

	@Override
	public void getElements(Array<T> result) {
		for(int i = this.quadElements.length - 1; i >= 0; i--) {
			final QuadElements<T> quadElements = this.quadElements.get(i);
			if(quadElements == null) {
				continue;
			}
			for(T element : quadElements) {
				result.add(element);
			}
		}
	}

	@Override
	public void positionChanged(T moved) {
		final int quadIndex = this.elementsToQuads.get(moved.getId(), -1);
		Quad quad = quadIndex < 0 ? rootQuad : this.quads.get(quadIndex);
		if(belongsToQuad(quad, moved)) {
			if(!updateBounds(quad, moved)) {
				return;
			}
			while(quad.parentIndex != -1) {
				quad = quads.get(quad.parentIndex);
				if(!updateBounds(quad, moved)) {
					return;
				}
			}
			updateBounds(rootQuad, moved);
			return;
		}

		removeFromQuad(quad, moved);

		while(quad.parentIndex != -1) {
			quad = quads.get(quad.parentIndex);
			if(!belongsToQuad(quad, moved)) {
				continue;
			}
			quad = findQuad(quad, moved, true);
			addToQuad(quad, moved);
			return;
		}
		quad = findQuad(rootQuad, moved, true);
		addToQuad(quad, moved);
	}

	/**
	 * Returns the Quad that the specified element belongs to
	 * @param element The element to search for
	 * @return Null if not contained in this quad tree
	 */
	public Quad getQuad(T element) {
		final int quadIndex = this.elementsToQuads.get(element.getId(), -2);
		switch (quadIndex) {
		case -2:
			return null;
		case -1:
			return rootQuad;
		default:
			return quads.get(quadIndex);
		}
	}

	/**
	 * Checks if an element is added to the quad tree
	 * @param element The element to check
	 * @return True if added to the quad tree
	 */
	public boolean contains(T element) {
		return elementsToQuads.containsKey(element.getId());
	}
	@Override
	public int getElementLimitPerQuad() {
		return elementLimitPerQuad;
	}

	@Override
	public float getMinimumQuadWidth() {
		return minimumQuadWidth;
	}

	@Override
	public float getMinimumQuadHeight() {
		return minimumQuadHeight;
	}

	@Override
	public int getTotalQuads() {
		return quads.totalItems + 1;
	}

	@Override
	public int getTotalElements() {
		return totalElements;
	}
}
