/**
 * Copyright (c) 2016 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.geom;

import org.junit.Test;
import org.mini2Dx.core.util.EdgeIterator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import junit.framework.Assert;

/**
 * Unit tests for {@link Polygon}
 */
public class PolygonTest {
	@Test
	public void testAddPoint() {
		Polygon polygon = new Polygon(new Point [] { new Point(0f, 0f),
				new Point(5f, 5f), new Point(2.5f, 10f) });
		polygon.addPoint(new Point(-2.5f, 10f));
		polygon.addPoint(new Point(-5f, 5f));
		Assert.assertEquals(0f, polygon.getX());
		Assert.assertEquals(0f, polygon.getY());
		Assert.assertEquals(5f, polygon.getMaxX());
		Assert.assertEquals(10f, polygon.getMaxY());
	}

	@Test
	public void testRemovePoint() {
		Point maxX = new Point(5f, 5f);
		Point maxY1 = new Point(2.5f, 10f);
		Point maxY2 = new Point(-2.5f, 10f);
		Polygon polygon = new Polygon(new Point[] { new Point(0f, 0f), maxX, maxY1, 
				maxY2, new Point(-5f, 5f) });
		Assert.assertEquals(5f, polygon.getMaxX());
		Assert.assertEquals(10f, polygon.getMaxY());

		polygon.removePoint(maxX);
		Assert.assertEquals(2.5f, polygon.getMaxX());
		Assert.assertEquals(10f, polygon.getMaxY());

		polygon.addPoint(maxX);
		polygon.removePoint(maxY1);
		polygon.removePoint(maxY2);
		Assert.assertEquals(5f, polygon.getMaxX());
		Assert.assertEquals(5f, polygon.getMaxY());
	}
	
	@Test
	public void testRotate() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(10f, 0f),
				new Point(20f, 0f),
				new Point(20f, 10f),
				new Point(10f, 10f),
		});
		polygon.rotate(90f);
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(0, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(10, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(10, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(3)));
		Assert.assertEquals(0, MathUtils.round(polygon.getY(3)));
	}
	
	@Test
	public void testRotateAround() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(10f, 0f),
				new Point(20f, 0f),
				new Point(20f, 10f),
				new Point(10f, 10f),
		});
		
		polygon.rotateAround(0f, 0f, -90f);
		Assert.assertEquals(-90f, polygon.getRotation());
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(-10, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(-20, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(-20, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(3)));
		Assert.assertEquals(-10, MathUtils.round(polygon.getY(3)));
		
		polygon.rotate(90f);
		Assert.assertEquals(0f, polygon.getRotation());
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(-10, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(-10, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(0, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(3)));
		Assert.assertEquals(0, MathUtils.round(polygon.getY(3)));
		
		polygon.rotateAround(0f, 0f, 90f);
		Assert.assertEquals(90f, polygon.getRotation());
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(0, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(10, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(10, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(3)));
		Assert.assertEquals(0, MathUtils.round(polygon.getY(3)));
	}
	
	@Test
	public void testSetAndRotate() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(10f, 0f),
				new Point(20f, 0f),
				new Point(20f, 10f),
				new Point(10f, 10f),
		});
		polygon.set(100f, 100f);
		polygon.rotate(90f);
		
		Assert.assertEquals(100, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(100, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(100, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(110, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(90, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(110, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(90, MathUtils.round(polygon.getX(3)));
		Assert.assertEquals(100, MathUtils.round(polygon.getY(3)));
	}
	
	@Test
	public void testSetRotation() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(10f, 0f),
				new Point(20f, 0f),
				new Point(20f, 10f),
				new Point(10f, 10f),
		});
		polygon.rotate(90f);
		Assert.assertEquals(90f, polygon.getRotation());
		
		polygon.setRotation(0f);
		Assert.assertEquals(0f, polygon.getRotation());
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(0, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(20, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(0, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(20, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(10, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(3)));
		Assert.assertEquals(10, MathUtils.round(polygon.getY(3)));
	}
	
	@Test
	public void testSetRotationAround() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(10f, 0f),
				new Point(20f, 0f),
				new Point(20f, 10f),
				new Point(10f, 10f),
		});
		polygon.setRotationAround(0f, 0f, -90f);
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(-10, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(-20, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(-20, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(3)));
		Assert.assertEquals(-10, MathUtils.round(polygon.getY(3)));
		
		polygon.setRotationAround(0f, 10f, 180f);
		
		Assert.assertEquals(-20, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(10, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(-30, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(10, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(-30, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(0, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(-20, MathUtils.round(polygon.getX(3)));
		Assert.assertEquals(0, MathUtils.round(polygon.getY(3)));
	}
	
	@Test
	public void testSet() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(10f, 10f),
				new Point(20f, 10f),
				new Point(20f, 20f),
				new Point(10f, 20f)
			});
		polygon.set(100f, 100f);
		
		Assert.assertEquals(100, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(100, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(110, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(100, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(110, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(110, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(100, MathUtils.round(polygon.getX(3)));
		Assert.assertEquals(110, MathUtils.round(polygon.getY(3)));
	}
	
	@Test
	public void testSetX() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(10f, 0f),
				new Point(20f, 0f),
				new Point(20f, 10f),
				new Point(10f, 10f)
			});
		polygon.setX(100f);
		
		Assert.assertEquals(100f, polygon.getX());
		Assert.assertEquals(110f, polygon.getMaxX());
		
		Assert.assertEquals(100, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(0, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(110, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(0, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(110, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(10, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(100, MathUtils.round(polygon.getX(3)));
		Assert.assertEquals(10, MathUtils.round(polygon.getY(3)));
	}
	
	@Test
	public void testSetY() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(0f, 10f),
				new Point(10f, 10f),
				new Point(10f, 20f),
				new Point(0f, 20f)
			});
		polygon.setY(200f);
		
		Assert.assertEquals(200f, polygon.getY());
		Assert.assertEquals(210f, polygon.getMaxY());
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(200, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(200, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(210, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(3)));
		Assert.assertEquals(210, MathUtils.round(polygon.getY(3)));
	}
	
	@Test
	public void testTranslate() {
		Polygon polygon = new Polygon(new Point [] {
			new Point(0f, 0f),
			new Point(10f, 0f),
			new Point(10f, 10f),
			new Point(0f, 10f)
		});
		polygon.translate(10f, 0f);
		Assert.assertEquals(10f, polygon.getX(0));
		Assert.assertEquals(0f, polygon.getY(0));
		Assert.assertEquals(20f, polygon.getX(1));
		Assert.assertEquals(0f, polygon.getY(1));
		Assert.assertEquals(20f, polygon.getX(2));
		Assert.assertEquals(10f, polygon.getY(2));
		Assert.assertEquals(10f, polygon.getX(3));
		Assert.assertEquals(10f, polygon.getY(3));
		
		Assert.assertEquals(20f, polygon.getMaxX());
		Assert.assertEquals(10f, polygon.getMaxY());
	}
	
	@Test
	public void testSetAndTranslate() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(0f, 0f),
				new Point(10f, 0f),
				new Point(10f, 10f),
				new Point(0f, 10f)
			});
		polygon.set(100f, 100f);
		polygon.translate(100f, 50f);
		
		Assert.assertEquals(200, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(150, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(210, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(150, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(210, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(160, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(200, MathUtils.round(polygon.getX(3)));
		Assert.assertEquals(160, MathUtils.round(polygon.getY(3)));
		
		Assert.assertEquals(210f, polygon.getMaxX());
		Assert.assertEquals(160f, polygon.getMaxY());
	}
	
	@Test
	public void testTranslateAndSet() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(0f, 0f),
				new Point(10f, 0f),
				new Point(10f, 10f),
				new Point(0f, 10f)
			});
		polygon.translate(100f, 100f);
		polygon.set(0f, 0f);
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(0, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(0, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(10, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(3)));
		Assert.assertEquals(10, MathUtils.round(polygon.getY(3)));
	}
	
	@Test
	public void testTranslateAndRotate() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(10f, 0f),
				new Point(20f, 0f),
				new Point(20f, 10f),
				new Point(10f, 10f),
		});
		polygon.translate(100f, 100f);
		polygon.rotate(90f);
		
		Assert.assertEquals(110, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(100, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(110, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(110, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(100, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(110, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(100, MathUtils.round(polygon.getX(3)));
		Assert.assertEquals(100, MathUtils.round(polygon.getY(3)));
	}
	
	@Test
	public void testTranslateAndAddPoint() {
		Point [] initialPoints = new Point [] {
				new Point(0f, 0f),
				new Point(10f, 0f),
				new Point(10f, 10f),
				new Point(5f, 10f)
		};
		
		Polygon polygon = new Polygon(initialPoints);
		polygon.translate(10f, 0f);
		polygon.addPoint(new Point(10f, 10f));
		Assert.assertEquals(10f, polygon.getX(0));
		Assert.assertEquals(0f, polygon.getY(0));
		Assert.assertEquals(20f, polygon.getX(1));
		Assert.assertEquals(0f, polygon.getY(1));
		Assert.assertEquals(20f, polygon.getX(2));
		Assert.assertEquals(10f, polygon.getY(2));
		Assert.assertEquals(15f, polygon.getX(3));
		Assert.assertEquals(10f, polygon.getY(3));
		Assert.assertEquals(10f, polygon.getX(4));
		Assert.assertEquals(10f, polygon.getY(4));
	}
	
	@Test
	public void testTranslateAndRemovePoint() {
		Point [] initialPoints = new Point [] {
				new Point(0f, 0f),
				new Point(10f, 0f),
				new Point(10f, 10f),
				new Point(5f, 10f),
				new Point(0f, 10f)
		};
		
		Polygon polygon = new Polygon(initialPoints);
		polygon.translate(10f, 0f);
		polygon.removePoint(15f, 10f);
		Assert.assertEquals(10f, polygon.getX(0));
		Assert.assertEquals(0f, polygon.getY(0));
		Assert.assertEquals(20f, polygon.getX(1));
		Assert.assertEquals(0f, polygon.getY(1));
		Assert.assertEquals(20f, polygon.getX(2));
		Assert.assertEquals(10f, polygon.getY(2));
		Assert.assertEquals(10f, polygon.getX(3));
		Assert.assertEquals(10f, polygon.getY(3));
	}
	
	@Test
	public void testContainsPoint() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(0f, 0f),
				new Point(10f, 5f),
				new Point(5f, 10f),
				new Point(-10f, 5f),
				new Point(-5f, 10f)
			});
		
		Assert.assertEquals(true, polygon.contains(0f, 5f));
		Assert.assertEquals(false, polygon.contains(15f, 5f));
		
		Assert.assertEquals(true, polygon.contains(new Vector2(0f, 5f)));
		Assert.assertEquals(false, polygon.contains(new Vector2(15f, 5f)));
		
		polygon = new Polygon(new Point [] {
				new Point(100f, 100f),
				new Point(150f, 100f),
				new Point(150f, 150f),
				new Point(100f, 150f)
			});
		Assert.assertEquals(true, polygon.contains(125f, 125f));
	}
	
	@Test
	public void testContainsPolygon() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(100f, 100f),
				new Point(150f, 100f),
				new Point(150f, 150f),
				new Point(100f, 150f)
			});
		Polygon containedPolygon = new Polygon(new Point [] {
				new Point(125f, 125f),
				new Point(130f, 125f),
				new Point(130f, 130f),
				new Point(125f, 130f)
		});
		Polygon nonContainedPolygon = new Polygon(new Point [] {
				new Point(200f, 200f),
				new Point(250f, 200f),
				new Point(250f, 250f),
				new Point(200f, 250f)
		});
		
		Assert.assertEquals(true, polygon.contains(containedPolygon));
		Assert.assertEquals(false, polygon.contains(nonContainedPolygon));
	}
	
	@Test
	public void testIntersectsLineSegment() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(0f, 0f),
				new Point(10f, 0f),
				new Point(10f, 10f),
				new Point(0f, 10f)
			});
		LineSegment intersectingLineSegment = new LineSegment(5f, -5f, 5f, 15f);
		LineSegment nonIntersectingLineSegment = new LineSegment(100f, 100f, 200f, 200f);
		
		Assert.assertEquals(true, polygon.intersects(intersectingLineSegment));
		Assert.assertEquals(false, polygon.intersects(nonIntersectingLineSegment));
	}
	
	@Test
	public void testIntersectsRectangle() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(0f, 0f),
				new Point(10f, 0f),
				new Point(10f, 10f),
				new Point(0f, 10f)
			});
		
		Rectangle intersectingRectangle = new Rectangle(-5f, -5f, 10f, 10f);
		Rectangle onLineIntersectingRectangle = new Rectangle(-5f, -5f, 10f, 5f);
		Rectangle nonIntersectingRectangle = new Rectangle(100f, 100f, 10f, 10f);
		
		Assert.assertEquals(true, polygon.intersects(intersectingRectangle));
		Assert.assertEquals(true, polygon.intersects(onLineIntersectingRectangle));
		Assert.assertEquals(false, polygon.intersects(nonIntersectingRectangle));
	}
	
	@Test
	public void testNumberOfSides() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(0f, 0f),
				new Point(10f, 0f),
				new Point(10f, 10f),
				new Point(0f, 10f)
			});
		Assert.assertEquals(4, polygon.getNumberOfSides());
		
		polygon.setVertices(new Point [] {
				new Point(0f, 0f),
				new Point(10f, 5f),
				new Point(5f, 10f),
				new Point(-10f, 5f),
				new Point(-5f, 10f),
			});
		Assert.assertEquals(5, polygon.getNumberOfSides());
	}
	
	@Test
	public void testEdgeIterator() {
		Point [] points = new Point [] {
				new Point(0f, 0f),
				new Point(10f, 5f),
				new Point(5f, 10f),
				new Point(-10f, 5f),
				new Point(-5f, 10f),
			};
		Polygon polygon = new Polygon(points);
		
		EdgeIterator iterator = polygon.edgeIterator();
		iterator.begin();
		for(int i = 0; i < points.length; i++) {
			iterator.next();
			if(i < points.length - 1) {
				Assert.assertEquals(points[i].x, iterator.getPointAX());
				Assert.assertEquals(points[i].y, iterator.getPointAY());
				Assert.assertEquals(points[i + 1].x, iterator.getPointBX());
				Assert.assertEquals(points[i + 1].y, iterator.getPointBY());
			} else {
				Assert.assertEquals(points[i].x, iterator.getPointAX());
				Assert.assertEquals(points[i].y, iterator.getPointAY());
				Assert.assertEquals(points[0].x, iterator.getPointBX());
				Assert.assertEquals(points[0].y, iterator.getPointBY());
			}
		}
		iterator.end();
	}
}
