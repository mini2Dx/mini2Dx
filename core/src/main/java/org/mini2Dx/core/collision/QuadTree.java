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

import org.mini2Dx.core.geom.*;
import org.mini2Dx.gdx.utils.Array;

/**
 * Common interface for <a href="http://en.wikipedia.org/wiki/Quadtree">Quad Tree</a> implementation
 */
public interface QuadTree<T extends Positionable> extends CollisionDetection<T> {

	public Array<T> getElementsOverlappingArea(Rectangle area);

	public void getElementsOverlappingArea(Array<T> result, Rectangle area);

	public Array<T> getElementsOverlappingAreaIgnoringEdges(Rectangle area);

	public void getElementsOverlappingAreaIgnoringEdges(Array<T> result, Rectangle area);

	public void getElements(QuadTreeInspector<T> inspector);

	public Array<T> getElementsContainingArea(Rectangle area);

	public void getElementsContainingArea(Array<T> result, Rectangle area);

	public Array<T> getElementsIntersectingLineSegment(LineSegment lineSegment);

	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment);

	public Array<T> getElementsContainingPoint(Point point);

	public void getElementsContainingPoint(Array<T> result, Point point);

	public int getTotalQuads();

	public int getTotalElements();
	
	public float getMinimumQuadWidth();
	
	public float getMinimumQuadHeight();

	public int getElementLimitPerQuad();
}
