/*******************************************************************************
 * Copyright 2021 Viridian Software Limited
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
import org.mini2Dx.core.geom.*;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.IntMap;
import org.mini2Dx.gdx.utils.IntSet;

public class Cell<T extends CollisionArea> extends Rectangle implements CollisionDetection<T> {
	private final IntMap<T> elements = new IntMap<T>();

	public Cell(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void debugRender(Graphics g) {
		g.setColor(CellGrid.ELEMENT_COLOR);
		for (T element : elements.values()) {
			g.drawRect(element.getX(), element.getY(), element.getWidth(), element.getHeight());
		}
	}

	@Override
	public boolean add(T element) {
		if(elements.containsKey(element.getId())) {
			return false;
		}
		elements.put(element.getId(), element);
		element.addPostionChangeListener(this);
		return true;
	}

	@Override
	public boolean remove(T element) {
		if(elements.remove(element.getId()) != null) {
			element.removePositionChangeListener(this);
			return true;
		}
		return false;
	}

	@Override
	public void addAll(Array<T> elements) {
		for(int i = 0; i < elements.size; i++) {
			add(elements.get(i));
		}
	}

	@Override
	public void removeAll(Array<T> elements) {
		for(int i = 0; i < elements.size; i++) {
			add(elements.get(i));
		}
	}

	@Override
	public void clear() {
		elements.clear();
	}

	@Override
	public Array<T> getElementsOverlappingArea(Rectangle area) {
		Array<T> result = new Array<>();
		getElementsOverlappingArea(result, area);
		return result;
	}

	@Override
	public void getElementsOverlappingArea(Array<T> result, Rectangle area) {
		final IntMap.Keys keys = elements.keys();
		while(keys.hasNext) {
			T element = elements.get(keys.next());
			if (element != null && area.intersects(element)) {
				result.add(element);
			}
		}
	}

	@Override
	public Array<T> getElementsOverlappingArea(Circle area) {
		Array<T> result = new Array<>();
		getElementsOverlappingArea(result, area);
		return result;
	}

	@Override
	public void getElementsOverlappingArea(Array<T> result, Circle area) {
		final IntMap.Keys keys = elements.keys();
		while(keys.hasNext) {
			T element = elements.get(keys.next());
			if (element != null && area.intersects(element)) {
				result.add(element);
			}
		}
	}

	public void getElementsOverlappingArea(IntMap<T> result, Rectangle area) {
		final IntMap.Keys keys = elements.keys();
		while(keys.hasNext) {
			T element = elements.get(keys.next());
			if (element == null) {
				continue;
			}
			if(result.containsKey(element.getId())) {
				continue;
			}
			if (area.intersects(element)) {
				result.put(element.getId(), element);
			}
		}
	}

	@Override
	public Array<T> getElementsOverlappingAreaIgnoringEdges(Rectangle area) {
		Array<T> result = new Array<>();
		getElementsOverlappingAreaIgnoringEdges(result, area);
		return result;
	}

	@Override
	public void getElementsOverlappingAreaIgnoringEdges(Array<T> result, Rectangle area) {
		final IntMap.Keys keys = elements.keys();
		while(keys.hasNext) {
			T element = elements.get(keys.next());
			if (element != null && (area.intersectsIgnoringEdges(element))) {
				result.add(element);
			}
		}
	}

	public void getElementsOverlappingAreaIgnoringEdges(IntMap<T> result, Rectangle area) {
		final IntMap.Keys keys = elements.keys();
		while(keys.hasNext) {
			T element = elements.get(keys.next());
			if (element == null) {
				continue;
			}
			if(result.containsKey(element.getId())) {
				continue;
			}
			if (area.intersectsIgnoringEdges(element)) {
				result.put(element.getId(), element);
			}
		}
	}

	@Override
	public Array<T> getElementsContainedInArea(Rectangle area) {
		Array<T> result = new Array<>();
		getElementsContainedInArea(result, area);
		return result;
	}

	@Override
	public void getElementsContainedInArea(Array<T> result, Rectangle area) {
		final IntMap.Keys keys = elements.keys();
		while(keys.hasNext) {
			T element = elements.get(keys.next());
			if (element != null && area.contains(element)) {
				result.add(element);
			}
		}
	}

	public void getElementsContainedInArea(IntMap<T> result, Rectangle area) {
		final IntMap.Keys keys = elements.keys();
		while(keys.hasNext) {
			T element = elements.get(keys.next());
			if (element == null) {
				continue;
			}
			if(result.containsKey(element.getId())) {
				continue;
			}
			if (area.contains(element)) {
				result.put(element.getId(), element);
			}
		}
	}

	@Override
	public Array<T> getElementsContainingArea(Rectangle area) {
		Array<T> result = new Array<>();
		getElementsContainingArea(result, area);
		return result;
	}

	@Override
	public void getElementsContainingArea(Array<T> result, Rectangle area) {
		final IntMap.Keys keys = elements.keys();
		while(keys.hasNext) {
			T element = elements.get(keys.next());
			if (element != null && element.contains(area)) {
				result.add(element);
			}
		}
	}

	public void getElementsContainingArea(IntMap<T> result, Rectangle area) {
		final IntMap.Keys keys = elements.keys();
		while(keys.hasNext) {
			T element = elements.get(keys.next());
			if (element == null) {
				continue;
			}
			if(result.containsKey(element.getId())) {
				continue;
			}
			if (element.contains(area)) {
				result.put(element.getId(), element);
			}
		}
	}

	@Override
	public Array<T> getElementsIntersectingLineSegment(LineSegment lineSegment) {
		Array<T> result = new Array<>();
		getElementsIntersectingLineSegment(result, lineSegment);
		return result;
	}

	@Override
	public void getElementsIntersectingLineSegment(Array<T> result, LineSegment lineSegment) {
		final IntMap.Keys keys = elements.keys();
		while(keys.hasNext) {
			T element = elements.get(keys.next());
			if (element != null && element.intersects(lineSegment)) {
				result.add(element);
			}
		}
	}

	public void getElementsIntersectingLineSegment(IntMap<T> result, LineSegment lineSegment) {
		final IntMap.Keys keys = elements.keys();
		while(keys.hasNext) {
			T element = elements.get(keys.next());
			if (element == null) {
				continue;
			}
			if(result.containsKey(element.getId())) {
				continue;
			}
			if (element.intersects(lineSegment)) {
				result.put(element.getId(), element);
			}
		}
	}

	@Override
	public Array<T> getElementsContainingPoint(Point point) {
		Array<T> result = new Array<>();
		getElementsContainingPoint(result, point);
		return result;
	}

	@Override
	public void getElementsContainingPoint(Array<T> result, Point point) {
		final IntMap.Keys keys = elements.keys();
		while(keys.hasNext) {
			T element = elements.get(keys.next());
			if (element != null && element.contains(point)) {
				result.add(element);
			}
		}
	}

	public void getElementsContainingPoint(IntMap<T> result, Point point) {
		final IntMap.Keys keys = elements.keys();
		while(keys.hasNext) {
			T element = elements.get(keys.next());
			if (element == null) {
				continue;
			}
			if(result.containsKey(element.getId())) {
				continue;
			}
			if (element.contains(point)) {
				result.put(element.getId(), element);
			}
		}
	}

	@Override
	public Array<T> getElements() {
		return elements.values().toArray();
	}

	@Override
	public void getElements(Array<T> result) {
		for(T element : elements.values()) {
			result.add(element);
		}
	}

	public void getElements(IntMap<T> result) {
		result.putAll(elements);
	}

	@Override
	public int getTotalElements() {
		return elements.size;
	}

	public void getTotalElements(IntSet ids) {
		for(T element : elements.values()) {
			ids.add(element.getId());
		}
	}

	@Override
	public void positionChanged(T moved) {
		if(moved.getMaxX() < getX()) {
			remove(moved);
			return;
		}
		if(moved.getMaxY() < getY()) {
			remove(moved);
			return;
		}
		if(moved.getX() > getMaxX()) {
			remove(moved);
			return;
		}
		if(moved.getY() > getMaxY()) {
			remove(moved);
			return;
		}
	}
}
