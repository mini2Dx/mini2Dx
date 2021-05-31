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
package org.mini2Dx.core.geom;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.Geometry;
import org.mini2Dx.gdx.math.Vector2;

/**
 * Unit tests for {@link Circle}
 */
public class CircleTest {
	private Circle circle1, circle2;

	@Before
	public void setup() {
		circle1 = new Circle(4);
	}
	
	@Test
	public void testXY() {
		circle1 = new Circle(100f, 50f, 25f);
		Assert.assertEquals(100f, circle1.getX(), 0f);
		Assert.assertEquals(50f, circle1.getY(), 0f);
		Assert.assertEquals(25f, circle1.getRadius(), 0f);
	}

	@Test
	public void testContains() {
		for (float x = -5f; x <= 5f; x += 0.1f) {
			for (float y = -5f; y <= 5f; y += 0.1f) {
				Assert.assertEquals(circle1.getDistanceFromCenter(x, y) <= circle1.getRadius(), circle1.contains(x, y));
			}
		}
	}

	@Test
	public void testIntersectsCircle() {
		Circle circle2 = new Circle(20f, 20f, 4);
		Assert.assertEquals(false, circle1.intersects(circle2));
		Assert.assertEquals(false, circle2.intersects(circle1));

		circle2.setXY(5f, 0f);
		Assert.assertEquals(true, circle1.intersects(circle2));
		Assert.assertEquals(true, circle2.intersects(circle1));
	}

	@Test
	public void testIntersectsIgnoringEdgesCircle() {
		Circle circle2 = new Circle(20f, 20f, 4);
		Assert.assertEquals(false, circle1.intersectsIgnoringEdges(circle2));
		Assert.assertEquals(false, circle2.intersectsIgnoringEdges(circle1));

		circle2.setXY(8f, 0f);
		Assert.assertEquals(false, circle1.intersectsIgnoringEdges(circle2));
		Assert.assertEquals(false, circle2.intersectsIgnoringEdges(circle1));
	}

	@Test
	public void testGetDistanceToPositionable() {
		Point point = new Point(3f, 0f);
		Assert.assertEquals(0f, circle1.getDistanceTo(point), 0.00001f);

		point.set(5f, 0f);
		Assert.assertEquals(1f, circle1.getDistanceTo(point), 0.00001f);
	}

	@Test
	public void testSetCenter() {
		circle1.setXY(20f, 25f);

		Assert.assertEquals(20f, circle1.getX(), 0f);
		Assert.assertEquals(25f, circle1.getY(), 0f);
	}

	@Test
	public void testSetX() {
		circle1.setX(25f);

		Assert.assertEquals(25f, circle1.getX(), 0f);
		Assert.assertEquals(0f, circle1.getY(), 0f);
	}

	@Test
	public void testSetY() {
		circle1.setY(25f);

		Assert.assertEquals(0f, circle1.getX(), 0f);
		Assert.assertEquals(25f, circle1.getY(), 0f);
	}

	@Test
	public void testContainsShape() {
		Shape shape = new Circle(2f);
		Assert.assertEquals(true, circle1.contains(shape));
		
		shape = new Rectangle(0f,0f,1f,1f);
		Assert.assertEquals(true, circle1.contains(shape));

		shape = new Rectangle(0f,0f,3f,3f);
		Assert.assertEquals(false, circle1.contains(shape));
		
		shape = new Triangle(new Vector2(0f, 4f), new Vector2(-4f,0f), new Vector2(4f,0f));
		Assert.assertEquals(true,circle1.contains(shape));

		shape = new Triangle(new Vector2(0f, 5f), new Vector2(-4f,0f), new Vector2(4f,0f));
		Assert.assertEquals(false,circle1.contains(shape));
	}

	@Test
	public void testContainsRectangle() {
		Rectangle rectangle = new Rectangle(0f,0f,1f,1f);
		Assert.assertEquals(true, circle1.contains(rectangle));

		rectangle = new Rectangle(0f,0f,3f,3f);
		Assert.assertEquals(false, circle1.contains(rectangle));
	}

	@Test
	public void testIntersectLineSegmentsXY() {
		Assert.assertEquals(true, circle1.intersectsLineSegment(0f,0f,5f,5f));
		Assert.assertEquals(false, circle1.intersectsLineSegment(-5f,5f,5f,5f));
	}

	@Test
	public void testIntersectLineSegmentsVectors() {
		Vector2 point1 = new Vector2(0f, 0f);
		Vector2 point2 = new Vector2(5f, 5f);
		Assert.assertEquals(true, circle1.intersectsLineSegment(point1, point2));

		point1 = new Vector2(-5f,5f);
		point2 = new Vector2(5f, 5f);
		Assert.assertEquals(false, circle1.intersectsLineSegment(point1, point2));

	}

	@Test
	public void testGetBoundingBox() {
		Rectangle boundingBox = circle1.getBoundingBox();
		Assert.assertEquals(-4f, boundingBox.getX(), 0f);
		Assert.assertEquals(-4f, boundingBox.getY(), 0f);
		Assert.assertEquals(8f, boundingBox.getWidth(), 0f);
		Assert.assertEquals(8f, boundingBox.getHeight(), 0f);
	}

	@Test
	public void testTranslate() {
		circle1.translate(1f,-1f);
		Assert.assertEquals(1f,circle1.getCenterX(), 0f);
		Assert.assertEquals(-1f,circle1.getCenterY(), 0f);
	}
	
	@Test
	public void testEquals() {
		circle1 = new Circle(4);
		circle2 = new Circle(4);
		
		Assert.assertEquals(true, circle1.equals(circle2));
		
		circle1 = new Circle(1, 1, 4);
		circle2 = new Circle(1, 1, 4);
		
		Assert.assertEquals(true, circle1.equals(circle2));
		
		circle1 = new Circle(4);
		circle2 = new Circle(4.00001f);
		
		Assert.assertEquals(false, circle1.equals(circle2));
	}

	@Test
	public void testDispose() {
		Geometry.DEFAULT_POOL_SIZE = 1;
		Geometry geometry = new Geometry();
		org.junit.Assert.assertEquals(1, geometry.getTotalCirclesAvailable());
		circle1 = geometry.circle();
		org.junit.Assert.assertEquals(0, geometry.getTotalCirclesAvailable());

		circle1.dispose();
		org.junit.Assert.assertEquals(1, geometry.getTotalCirclesAvailable());
		circle1.dispose();
		org.junit.Assert.assertEquals(1, geometry.getTotalCirclesAvailable());

		//Test re-allocate and re-dispose
		circle1 = geometry.circle();
		org.junit.Assert.assertEquals(0, geometry.getTotalCirclesAvailable());

		circle1.dispose();
		org.junit.Assert.assertEquals(1, geometry.getTotalCirclesAvailable());
	}
}
