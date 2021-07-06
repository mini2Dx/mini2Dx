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
import org.junit.Test;
import org.mini2Dx.core.Geometry;
import org.mini2Dx.gdx.math.MathUtils;

import java.util.Random;

/**
 * Unit tests for {@link Rectangle}
 */
public class RectangleTest {
	private final int SEED = 235824905;
	private Rectangle rectangle1, rectangle2;

	@Test
	public void testRectangleDefaultConstructor() {
		rectangle1 = new Rectangle();
		Assert.assertEquals(0f, rectangle1.getX(), 0);
		Assert.assertEquals(0f, rectangle1.getY(), 0);
		Assert.assertEquals(1f, rectangle1.getWidth(), 0);
		Assert.assertEquals(1f, rectangle1.getHeight(), 0);
		Assert.assertEquals(1f, rectangle1.getMaxX(), 0);
		Assert.assertEquals(1f, rectangle1.getMaxY(), 0);
	}

	@Test
	public void testRectangle() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		Assert.assertEquals(100f, rectangle1.getX(), 0);
		Assert.assertEquals(100f, rectangle1.getY(), 0);
		Assert.assertEquals(50f, rectangle1.getWidth(), 0);
		Assert.assertEquals(50f, rectangle1.getHeight(), 0);
		Assert.assertEquals(150f, rectangle1.getMaxX(), 0);
		Assert.assertEquals(150f, rectangle1.getMaxY(), 0);
		Assert.assertEquals(125f, rectangle1.getCenterX(), MathUtils.FLOAT_ROUNDING_ERROR);
		Assert.assertEquals(125f, rectangle1.getCenterY(), MathUtils.FLOAT_ROUNDING_ERROR);
	}

	@Test
	public void testManyRectangles() {
		Random random = new Random(SEED);
		for(int i = 0; i < 1000; i++) {
			float x = (random.nextFloat() * 10000000f) - 5000000f;
			float y = (random.nextFloat() * 10000000f) - 5000000f;
			rectangle1 = new Rectangle(x, y, 50f, 50f);
			Assert.assertEquals(x, rectangle1.getX(), 0);
			Assert.assertEquals(y, rectangle1.getY(), 0);
			Assert.assertEquals(50f, rectangle1.getWidth(), 0);
			Assert.assertEquals(50f, rectangle1.getHeight(), 0);
			System.out.println(x + "," + y);
			Assert.assertEquals(x + 25f, rectangle1.getCenterX(), 0.01f);
			Assert.assertEquals(y + 25f, rectangle1.getCenterY(), 0.01f);
		}
	}

	@Test
	public void testCenterXY() {
		Rectangle rectangle = new Rectangle();
		rectangle.set(21378.2f,10422.5f, 30f, 30f);
		Assert.assertEquals(21378.2f + 15f, rectangle.getCenterX(), 0.01f);
		Assert.assertEquals(10422.5f + 15f, rectangle.getCenterY(), 0.01f);
	}

	@Test
	public void testSetX() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle1.setX(200f);
		Assert.assertEquals(200f, rectangle1.getX(), 0);
		Assert.assertEquals(100f, rectangle1.getY(), 0);
		Assert.assertEquals(50f, rectangle1.getWidth(), 0);
		Assert.assertEquals(50f, rectangle1.getHeight(), 0);
		Assert.assertEquals(225f, rectangle1.getCenterX(), MathUtils.FLOAT_ROUNDING_ERROR);
		Assert.assertEquals(125f, rectangle1.getCenterY(), MathUtils.FLOAT_ROUNDING_ERROR);
	}

	@Test
	public void testSetY() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle1.setY(200f);
		Assert.assertEquals(100f, rectangle1.getX(), 0);
		Assert.assertEquals(200f, rectangle1.getY(), 0);
		Assert.assertEquals(50f, rectangle1.getWidth(), 0);
		Assert.assertEquals(50f, rectangle1.getHeight(), 0);
		Assert.assertEquals(125f, rectangle1.getCenterX(), MathUtils.FLOAT_ROUNDING_ERROR);
		Assert.assertEquals(225f, rectangle1.getCenterY(), MathUtils.FLOAT_ROUNDING_ERROR);
	}

	@Test
	public void testSetWidth() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle1.setWidth(100f);
		Assert.assertEquals(100f, rectangle1.getX(), 0);
		Assert.assertEquals(100f, rectangle1.getY(), 0);
		Assert.assertEquals(100f, rectangle1.getWidth(), 0);
		Assert.assertEquals(50f, rectangle1.getHeight(), 0);
	}

	@Test
	public void testSetHeight() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle1.setHeight(100f);
		Assert.assertEquals(100f, rectangle1.getX(), 0);
		Assert.assertEquals(100f, rectangle1.getY(), 0);
		Assert.assertEquals(50f, rectangle1.getWidth(), 0);
		Assert.assertEquals(100f, rectangle1.getHeight(), 0);
	}

	@Test
	public void testSetFloatFloatFloatFloat() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle1.set(0f, 0f, 50f, 50f);
		Assert.assertEquals(0f, rectangle1.getX(), 0);
		Assert.assertEquals(0f, rectangle1.getY(), 0);
		Assert.assertEquals(50f, rectangle1.getWidth(), 0);
		Assert.assertEquals(50f, rectangle1.getHeight(), 0);
	}

	@Test
	public void testSetRectangle() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle2 = new Rectangle(200f, 200f, 100f, 100f);
		rectangle1.set(rectangle2);
		Assert.assertEquals(200f, rectangle1.getX(), 0);
		Assert.assertEquals(200f, rectangle1.getY(), 0);
		Assert.assertEquals(100f, rectangle1.getWidth(), 0);
		Assert.assertEquals(100f, rectangle1.getHeight(), 0);
	}

	@Test
	public void testRotateAround() {
		rectangle1 = new Rectangle(0f, 0f, 10f, 10f);
		rectangle1.rotateAround(0f, 0f, 90f);

		Assert.assertEquals(0f, rectangle1.getVertices()[0], 0);
		Assert.assertEquals(0f, rectangle1.getVertices()[1], 0);

		Assert.assertEquals(0, MathUtils.round(rectangle1.getVertices()[2]));
		Assert.assertEquals(10, MathUtils.round(rectangle1.getVertices()[3]));

		Assert.assertEquals(-10, MathUtils.round(rectangle1.getVertices()[4]));
		Assert.assertEquals(10, MathUtils.round(rectangle1.getVertices()[5]));

		Assert.assertEquals(-10, MathUtils.round(rectangle1.getVertices()[6]));
		Assert.assertEquals(0, MathUtils.round(rectangle1.getVertices()[7]));
	}

	@Test
	public void testContainsPoint() {
		rectangle1 = new Rectangle(0, 0, 50, 50);
		Point point = new Point(5, 1);

		Assert.assertTrue(rectangle1.contains(point));

		point.set(5, -1);
		Assert.assertFalse(rectangle1.contains(point));

		point.set(51, 1);
		Assert.assertFalse(rectangle1.contains(point));

		point.set(5, 51);
		Assert.assertFalse(rectangle1.contains(point));

		point.set(-5, 1);
		Assert.assertFalse(rectangle1.contains(point));

		point.set(5, 1);
		rectangle1.rotate(45f);
		Assert.assertFalse(rectangle1.contains(point));

		point.set(-5, 1);
		Assert.assertFalse(rectangle1.contains(point));
	}

	@Test
	public void testContainsRectangle() {
		rectangle1 = new Rectangle(0, 0, 50, 50);
		rectangle2 = new Rectangle(50, 50, 50, 50);

		Assert.assertFalse(rectangle1.contains(rectangle2));
		Assert.assertFalse(rectangle2.contains(rectangle1));

		rectangle2 = new Rectangle(25, 25, 50, 50);
		Assert.assertFalse(rectangle1.contains(rectangle2));
		Assert.assertFalse(rectangle2.contains(rectangle1));

		rectangle2 = new Rectangle(0, 0, 25, 25);
		Assert.assertTrue(rectangle1.contains(rectangle2));
		Assert.assertFalse(rectangle2.contains(rectangle1));

		rectangle2 = new Rectangle(15, 15, 25, 25);
		Assert.assertTrue(rectangle1.contains(rectangle2));
		Assert.assertFalse(rectangle2.contains(rectangle1));

		rectangle2 = new Rectangle(48, 48, 25, 25);
		Assert.assertFalse(rectangle1.contains(rectangle2));
		Assert.assertFalse(rectangle2.contains(rectangle1));

		rectangle1 = new Rectangle(0, 0, 128, 128);
		rectangle2 = new Rectangle(42, 72, 32, 32);
		Assert.assertTrue(rectangle1.contains(rectangle2));
		Assert.assertFalse(rectangle2.contains(rectangle1));
	}

	@Test
	public void testRotatedContainsRectangle() {
		rectangle1 = new Rectangle(0, 0, 50, 50);
		rectangle1.rotate(45f);

		rectangle2 = new Rectangle(0f, 25f, 10f, 10f);
		Assert.assertTrue(rectangle1.contains(rectangle2));

		rectangle2.rotate(45f);
		Assert.assertTrue(rectangle1.contains(rectangle2));

		rectangle2 = new Rectangle(25, 25, 50, 50);
		Assert.assertFalse(rectangle1.contains(rectangle2));
	}

	@Test
	public void testContainsRectangleRotated() {
		rectangle1 = new Rectangle(0, 0, 50, 50);

		rectangle2 = new Rectangle(0f, 25f, 10f, 10f);
		rectangle2.rotate(45f);
		Assert.assertTrue(rectangle1.contains(rectangle2));
	}

	@Test
	public void testContainsZeroSizeRectangle() {
		rectangle1 = new Rectangle(0, 0, 10, 10);
		rectangle2 = new Rectangle(50, 50, 0, 0);
		Assert.assertFalse(rectangle2.contains(rectangle1));
	}

	@Test
	public void testIntersectsLineSegement() {
		rectangle1 = new Rectangle(2, 2, 4, 4);
		LineSegment segment = new LineSegment(0, 0, 10, 10);

		Assert.assertTrue(rectangle1.intersects(segment));

		segment.getPointA().set(10, 2);

		Assert.assertFalse(rectangle1.intersects(segment));

		rectangle1 = new Rectangle(96, 0, 32, 32);
		segment = new LineSegment(0, 0, 128, 128);
		Assert.assertFalse(rectangle1.intersects(segment));

		rectangle1 = new Rectangle(0, 0, 32, 32);
		Assert.assertTrue(rectangle1.intersects(new LineSegment(0f, 0f, -1f, -1f)));
		Assert.assertTrue(rectangle1.intersects(new LineSegment(-1f, -1f, 0f, 0f)));
		Assert.assertTrue(rectangle1.intersects(new LineSegment(0f, 0f, 1f, 1f)));
		Assert.assertTrue(rectangle1.intersects(new LineSegment(1f, 1f, 0f, 0f)));

		Assert.assertTrue(rectangle1.intersects(new LineSegment(32f, 32f, 31f, 31f)));
		Assert.assertTrue(rectangle1.intersects(new LineSegment(31f, 31f, 32f, 32f)));
		Assert.assertTrue(rectangle1.intersects(new LineSegment(33f, 33f, 32f, 32f)));
		Assert.assertTrue(rectangle1.intersects(new LineSegment(32f, 32f, 33f, 33f)));
	}

	@Test
	public void testIntersectsCircle() {
		rectangle1 = new Rectangle(0f, 0f, 50f, 50f);

		Circle intersectingCircle = new Circle(75f, 75f, 50f);
		Circle nonIntersectingCircle = new Circle(500f, 500f, 50f);

		Assert.assertTrue(rectangle1.intersects(intersectingCircle));
		Assert.assertFalse(rectangle1.intersects(nonIntersectingCircle));
	}

	@Test
	public void testIntersectsRectangle() {
		rectangle1 = new Rectangle(100f, 100f, 75f, 75f);
		rectangle2 = new Rectangle(50f, 50f, 100f, 100f);

		Assert.assertTrue(rectangle1.intersects(rectangle2));
		Assert.assertTrue(rectangle2.intersects(rectangle1));

		rectangle2 = new Rectangle(0f, 0f, 50f, 50f);

		Assert.assertFalse(rectangle1.intersects(rectangle2));
		Assert.assertFalse(rectangle2.intersects(rectangle1));
	}

	@Test
	public void testIntersectsSameRectangle() {
		rectangle1 = new Rectangle(0f, 0f, 32f, 32f);
		rectangle2 = new Rectangle(0f, 0f, 32f, 32f);

		Assert.assertTrue(rectangle1.intersects(rectangle2));
		Assert.assertTrue(rectangle2.intersects(rectangle1));
	}

	@Test
	public void testIntersectsRotatedRectangle() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle2 = new Rectangle(100f, 50f, 75f, 40f);

		Assert.assertFalse(rectangle1.intersects(rectangle2));
		Assert.assertFalse(rectangle2.intersects(rectangle1));

		rectangle2.rotate(45);

		Assert.assertTrue(rectangle1.intersects(rectangle2));
		Assert.assertTrue(rectangle2.intersects(rectangle1));
	}

	@Test
	public void testIntersectsRotatedRectangleFalse() {
		rectangle1 = new Rectangle(0,0,80,80);
		rectangle2 = new Rectangle(120, 0, 20, 160);

		Assert.assertFalse(rectangle1.intersects(rectangle2));
		Assert.assertFalse(rectangle2.intersects(rectangle1));

		rectangle2.rotateAround(130, 80, 45);

		Assert.assertFalse(rectangle1.intersects(rectangle2));
		Assert.assertFalse(rectangle2.intersects(rectangle1));
	}

	@Test
	public void testIntersectsLineWhenNotRotated() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		LineSegment line = new LineSegment(0, 100, 0, 200);

		for (float x = 0; x < rectangle1.getX(); x++) {
			line.setPointA(new Point(x, 100));
			line.setPointB(new Point(x, 200));
			Assert.assertFalse(rectangle1.intersects(line));
		}

		for (float y = 0; y < rectangle1.getY(); y++) {
			line.setPointA(new Point(100, y));
			line.setPointB(new Point(200, y));
			Assert.assertFalse(rectangle1.intersects(line));
		}

		for (float x = rectangle1.getX(); x <= rectangle1.getX()
				+ rectangle1.getWidth(); x++) {
			line.setPointA(new Point(x, 100));
			line.setPointB(new Point(x, 200));
			Assert.assertTrue(rectangle1.intersects(line));
		}

		for (float y = rectangle1.getY(); y <= rectangle1.getY()
				+ rectangle1.getHeight(); y++) {
			line.setPointA(new Point(100, y));
			line.setPointB(new Point(200, y));
			Assert.assertTrue(rectangle1.intersects(line));
		}

		for (float x = rectangle1.getX() + rectangle1.getWidth() + 1; x < (rectangle1
				.getX() + rectangle1.getWidth()) * 2; x++) {
			line.setPointA(new Point(x, 100));
			line.setPointB(new Point(x, 200));
			Assert.assertFalse(rectangle1.intersects(line));
		}

		for (float y = rectangle1.getY() + rectangle1.getHeight() + 1; y < (rectangle1
				.getY() + rectangle1.getHeight()) * 2; y++) {
			line.setPointA(new Point(100, y));
			line.setPointB(new Point(200, y));
			Assert.assertFalse(rectangle1.intersects(line));
		}
	}

	@Test
	public void testIntersectsIgnoringEdgeOverlap() {
		rectangle1 = new Rectangle(0, 0, 50, 50);
		rectangle2 = new Rectangle(49, 0, 50f, 50f);

		Assert.assertTrue(rectangle1.intersectsIgnoringEdges(rectangle2));

		rectangle2.setXY(49, 0);
		Assert.assertTrue(rectangle1.intersectsIgnoringEdges(rectangle2));
		rectangle2.setXY(50, 0);
		Assert.assertFalse(rectangle1.intersectsIgnoringEdges(rectangle2));

		rectangle2.setXY(-49, 0);
		Assert.assertTrue(rectangle1.intersectsIgnoringEdges(rectangle2));
		rectangle2.setXY(-50, 0);
		Assert.assertFalse(rectangle1.intersectsIgnoringEdges(rectangle2));

		rectangle2.setXY(0, 49);
		Assert.assertTrue(rectangle1.intersectsIgnoringEdges(rectangle2));
		rectangle2.setXY(0, 50);
		Assert.assertFalse(rectangle1.intersectsIgnoringEdges(rectangle2));

		rectangle2.setXY(0, -49);
		Assert.assertTrue(rectangle1.intersectsIgnoringEdges(rectangle2));
		rectangle2.setXY(0, -50);
		Assert.assertFalse(rectangle1.intersectsIgnoringEdges(rectangle2));
	}

	@Test
	public void testLerp() {
		rectangle1 = new Rectangle(0, 0, 50, 50);
		rectangle2 = new Rectangle(50, 50, 100f, 100f);

		rectangle1.lerp(rectangle2, 0.5f);
		Assert.assertEquals(25f, rectangle1.getX(), 0);
		Assert.assertEquals(25f, rectangle1.getY(), 0);
		Assert.assertEquals(75f, rectangle1.getWidth(), 0);
		Assert.assertEquals(75f, rectangle1.getHeight(), 0);
		Assert.assertEquals(0f, rectangle1.getRotation(), 0);
	}

	@Test
	public void testStaticLerp() {
		rectangle1 = new Rectangle(0, 0, 50, 50);
		rectangle2 = new Rectangle(50, 50, 100f, 100f);

		final Rectangle result = new Rectangle();

		rectangle1.lerp(result, rectangle2, 0.5f);
		Assert.assertEquals(25f, result.getX(), 0);
		Assert.assertEquals(25f, result.getY(), 0);
		Assert.assertEquals(75f, result.getWidth(), 0);
		Assert.assertEquals(75f, result.getHeight(), 0);
		Assert.assertEquals(0f, result.getRotation(), 0);

		rectangle1.lerp(result, rectangle2, 1f);
		Assert.assertEquals(rectangle2.getX(), result.getX(), 0);
		Assert.assertEquals(rectangle2.getY(), result.getY(), 0);
		Assert.assertEquals(rectangle2.getWidth(), result.getWidth(), 0);
		Assert.assertEquals(rectangle2.getHeight(), result.getHeight(), 0);
		Assert.assertEquals(rectangle2.getRotation(), result.getRotation(), 0);

	}

	@Test(expected = UnsupportedOperationException.class)
	public void testIntersectionRotatedException() {
		rectangle1 = new Rectangle(0, 0, 50, 50);
		rectangle2 = new Rectangle(50, 50, 100f, 100f);

		rectangle1.setRotation(10f);
		rectangle2.setRotation(10f);

		rectangle1.intersection(rectangle1);
	}

	@Test
	public void testIntersection() {
		rectangle1 = new Rectangle(0, 0, 50f, 50f);
		rectangle2 = new Rectangle(25, 25, 100f, 100f);

		Rectangle rectangle3 = rectangle1.intersection(rectangle2);

		Assert.assertEquals(rectangle2.getX(), rectangle3.getX(), 0);
		Assert.assertEquals(rectangle2.getY(), rectangle3.getY(), 0);
		Assert.assertEquals(25f, rectangle3.getWidth(), 0);
		Assert.assertEquals(25f, rectangle3.getHeight(), 0);
	}

	@Test
	public void testSetSizeWidthHeight() {
		rectangle1 = new Rectangle(0, 0, 40f, 60f);
		rectangle1.setSize(25f,30f);

		Assert.assertEquals(25f, rectangle1.getWidth(), 0);
		Assert.assertEquals(30f, rectangle1.getHeight(), 0);
	}

	@Test
	public void testSetSizeXY() {
		rectangle1 = new Rectangle(0, 0, 40f, 60f);
		rectangle1.setSize(25f);

		Assert.assertEquals(25f, rectangle1.getWidth(), 0);
		Assert.assertEquals(25f, rectangle1.getHeight(), 0);
	}

	@Test
	public void testToString() {
		rectangle1 = new Rectangle(0, 0, 40f, 60f);
		String rectangle = rectangle1.toString();
		String expected = "Rectangle [rotation=0.0, x=0.0, y=0.0, width=40.0, height=60.0]";

		Assert.assertEquals(expected, rectangle);
	}

	@Test
	public void testIntersectsXYWidthHeight() {
		rectangle1 = new Rectangle(100f, 100f, 75f, 75f);

		Assert.assertTrue(rectangle1.intersects(50f, 50f, 100f, 100f));
		Assert.assertFalse(rectangle1.intersects(0f, 0f, 50f, 50f));
	}

	@Test
	public void testEquals() {
		rectangle1 = new Rectangle(0, 0, 50, 50);
		rectangle2 = new Rectangle(0, 0, 50, 50);

		Assert.assertEquals(rectangle1, rectangle2);

		rectangle2.rotate(1f);
		Assert.assertFalse(rectangle1.equals(rectangle2));

		rectangle2 = new Rectangle(0.001f, 0, 50, 50);
		Assert.assertFalse(rectangle1.equals(rectangle2));
	}

	@Test
	public void testDispose() {
		Geometry.DEFAULT_POOL_SIZE = 1;
		Geometry geometry = new Geometry();
		Assert.assertEquals(1, geometry.getTotalRectanglesAvailable());
		rectangle1 = geometry.rectangle();
		Assert.assertEquals(0, geometry.getTotalRectanglesAvailable());

		rectangle1.dispose();
		Assert.assertEquals(1, geometry.getTotalRectanglesAvailable());
		rectangle1.dispose();
		Assert.assertEquals(1, geometry.getTotalRectanglesAvailable());

		//Test re-allocate and re-dispose
		rectangle1 = geometry.rectangle();
		Assert.assertEquals(0, geometry.getTotalRectanglesAvailable());
		rectangle1.dispose();
		Assert.assertEquals(1, geometry.getTotalRectanglesAvailable());
	}
}
