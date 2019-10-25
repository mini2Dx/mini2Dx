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
import org.mini2Dx.gdx.math.Vector2;

/**
 * Unit tests for {@link Polygon}
 */
public class PolygonTest {
	
	@Test
	public void testAdd() {
		Polygon polygon = new Polygon(new Point [] { new Point(0f, 0f),
				new Point(5f, 5f), new Point(2.5f, 10f) });
		polygon.add(5f, 7f);
		Assert.assertEquals(5f, polygon.getX(), 0.1f);
		Assert.assertEquals(7f, polygon.getY(), 0.1f);
		Assert.assertEquals(10f, polygon.getMaxX(), 0.1f);
		Assert.assertEquals(17f, polygon.getMaxY(), 0.1f);
	}
	
	@Test
	public void testSubtract() {
		Polygon polygon = new Polygon(new Point [] { new Point(0f, 0f),
				new Point(5f, 5f), new Point(2.5f, 10f) });
		polygon.subtract(5f, 7f);
		Assert.assertEquals(-5f, polygon.getX(), 0.1f);
		Assert.assertEquals(-7f, polygon.getY(), 0.1f);
		Assert.assertEquals(0f, polygon.getMaxX(), 0.1f);
		Assert.assertEquals(3f, polygon.getMaxY(), 0.1f);
	}
	
	@Test
	public void testAddPoint() {
		Polygon polygon = new Polygon(new Point [] { new Point(0f, 0f),
				new Point(5f, 5f), new Point(2.5f, 10f) });
		polygon.addPoint(new Point(-2.5f, 10f));
		polygon.addPoint(new Point(-5f, 5f));
		Assert.assertEquals(0f, polygon.getX(), 0.01f);
		Assert.assertEquals(0f, polygon.getY(), 0.01f);
		Assert.assertEquals(5f, polygon.getMaxX(), 0.01f);
		Assert.assertEquals(10f, polygon.getMaxY(), 0.01f);
	}

	@Test
	public void testRemovePoint() {
		Point maxX = new Point(5f, 5f);
		Point maxY1 = new Point(2.5f, 10f);
		Point maxY2 = new Point(-2.5f, 10f);
		Polygon polygon = new Polygon(new Point[] { new Point(0f, 0f), maxX, maxY1, 
				maxY2, new Point(-5f, 5f) });
		Assert.assertEquals(5f, polygon.getMaxX(), 0.01f);
		Assert.assertEquals(10f, polygon.getMaxY(), 0.01f);

		polygon.removePoint(maxX);
		Assert.assertEquals(2.5f, polygon.getMaxX(), 0.01f);
		Assert.assertEquals(10f, polygon.getMaxY(), 0.01f);

		polygon.addPoint(maxX);
		polygon.removePoint(maxY1);
		polygon.removePoint(maxY2);
		Assert.assertEquals(5f, polygon.getMaxX(), 0.01f);
		Assert.assertEquals(5f, polygon.getMaxY(), 0.01f);
	}
	
	@Test
	public void testLerp() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(0f, 0f),
				new Point(10f, 0f),
				new Point(10f, 10f),
				new Point(0f, 10f),
		});
		Polygon target = new Polygon(new Point [] {
				new Point(10f, 10f),
				new Point(20f, 10f),
				new Point(20f, 20f),
				new Point(10f, 20f),
		});
		polygon.lerp(target, 0.5f);
		
		Assert.assertEquals(5, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(5, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(15, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(5, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(15, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(15, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(5, MathUtils.round(polygon.getX(3)));
		Assert.assertEquals(15, MathUtils.round(polygon.getY(3)));
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
		Assert.assertEquals(-90f, polygon.getRotation(), 0.01f);
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(-10, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(-20, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(-20, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(3)));
		Assert.assertEquals(-10, MathUtils.round(polygon.getY(3)));
		
		polygon.rotate(90f);
		Assert.assertEquals(0f, polygon.getRotation(), 0.01f);
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(-10, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(-10, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(10, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(0, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(3)));
		Assert.assertEquals(0, MathUtils.round(polygon.getY(3)));
		
		polygon.rotateAround(0f, 0f, 90f);
		Assert.assertEquals(90f, polygon.getRotation(), 0.01f);
		
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
	public void testRotateAndLerp() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(0f, 0f),
				new Point(10f, 0f),
				new Point(10f, 10f),
				new Point(0f, 10f),
		});
		Polygon target = (Polygon) polygon.copy();
		target.rotate(180f);
		polygon.lerp(target, 0.5f);
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(0, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(0, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(10, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(-10, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(10, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(-10, MathUtils.round(polygon.getX(3)));
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
		polygon.setXY(100f, 100f);
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
		Assert.assertEquals(90f, polygon.getRotation(), 0.01f);
		
		polygon.setRotation(0f);
		Assert.assertEquals(0f, polygon.getRotation(), 0.01f);
		
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
	public void testDuplicateSetRotationDoesNotSetDirtyFlag() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(10f, 0f),
				new Point(20f, 0f),
				new Point(20f, 10f),
				new Point(10f, 10f),
		});
		polygon.rotate(10f);
		Assert.assertEquals(true, polygon.isDirty());
		clearDirtyBit(polygon);
		polygon.setRotation(10f);
		Assert.assertEquals(false, polygon.isDirty());
		
		polygon = new Polygon(new Point [] {
				new Point(10f, 0f),
				new Point(20f, 0f),
				new Point(20f, 10f),
				new Point(10f, 10f),
		});
		polygon.rotate(10f);
		Assert.assertEquals(true, polygon.isDirty());
		clearDirtyBit(polygon);
		polygon.setRotation(-10f);
		Assert.assertEquals(true, polygon.isDirty());
		clearDirtyBit(polygon);
		polygon.setRotation(-10f);
		Assert.assertEquals(false, polygon.isDirty());
	}
	
	@Test
	public void testRotateByZeroDegreesDoesNothing() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(10f, 0f),
				new Point(20f, 0f),
				new Point(20f, 10f),
				new Point(10f, 10f),
		});
		polygon.rotate(10f);
		Assert.assertEquals(true, polygon.isDirty());
		clearDirtyBit(polygon);
		polygon.rotate(0f);
		Assert.assertEquals(false, polygon.isDirty());
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
		polygon.setXY(100f, 100f);
		
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
	public void testSetPolygon() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(0f, 0f),
				new Point(10f, 0f),
				new Point(10f, 10f),
				new Point(0f, 10f)
			});
		Polygon target = new Polygon(new Point [] {
				new Point(10f, 0f),
				new Point(20f, 0f),
				new Point(20f, 10f),
				new Point(10f, 10f),
		});
		target.rotate(90f);
		Assert.assertEquals(10, MathUtils.round(target.getX(0)));
		Assert.assertEquals(0, MathUtils.round(target.getY(0)));
		
		Assert.assertEquals(10, MathUtils.round(target.getX(1)));
		Assert.assertEquals(10, MathUtils.round(target.getY(1)));
		
		Assert.assertEquals(0, MathUtils.round(target.getX(2)));
		Assert.assertEquals(10, MathUtils.round(target.getY(2)));
		
		Assert.assertEquals(0, MathUtils.round(target.getX(3)));
		Assert.assertEquals(0, MathUtils.round(target.getY(3)));
		
		polygon.set(target);
		
		Assert.assertEquals(90f, polygon.getRotation(), 0.01f);
		
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
	public void testSetX() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(10f, 0f),
				new Point(20f, 0f),
				new Point(20f, 10f),
				new Point(10f, 10f)
			});
		polygon.setX(100f);
		
		Assert.assertEquals(100f, polygon.getX(), 0.01f);
		Assert.assertEquals(110f, polygon.getMaxX(), 0.01f);
		
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
		
		Assert.assertEquals(200f, polygon.getY(), 0.01f);
		Assert.assertEquals(210f, polygon.getMaxY(), 0.01f);
		
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
	public void testSetCenter() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(0f, 10f),
				new Point(10f, 10f),
				new Point(10f, 20f),
				new Point(0f, 20f)
			});
		Assert.assertEquals(5f, polygon.getCenterX(), 0.01f);
		Assert.assertEquals(15f, polygon.getCenterY(), 0.01f);
		
		polygon.setCenter(10f, 20f);
		
		Assert.assertEquals(10f, polygon.getCenterX(), 0.01f);
		Assert.assertEquals(20f, polygon.getCenterY(), 0.01f);
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
		Assert.assertEquals(10f, polygon.getX(0), 0.01f);
		Assert.assertEquals(0f, polygon.getY(0), 0.01f);
		Assert.assertEquals(20f, polygon.getX(1), 0.01f);
		Assert.assertEquals(0f, polygon.getY(1), 0.01f);
		Assert.assertEquals(20f, polygon.getX(2), 0.01f);
		Assert.assertEquals(10f, polygon.getY(2), 0.01f);
		Assert.assertEquals(10f, polygon.getX(3), 0.01f);
		Assert.assertEquals(10f, polygon.getY(3), 0.01f);
		
		Assert.assertEquals(20f, polygon.getMaxX(), 0.01f);
		Assert.assertEquals(10f, polygon.getMaxY(), 0.01f);
	}
	
	@Test
	public void testSetAndTranslate() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(0f, 0f),
				new Point(10f, 0f),
				new Point(10f, 10f),
				new Point(0f, 10f)
			});
		polygon.setXY(100f, 100f);
		polygon.translate(100f, 50f);
		
		Assert.assertEquals(200, MathUtils.round(polygon.getX(0)));
		Assert.assertEquals(150, MathUtils.round(polygon.getY(0)));
		
		Assert.assertEquals(210, MathUtils.round(polygon.getX(1)));
		Assert.assertEquals(150, MathUtils.round(polygon.getY(1)));
		
		Assert.assertEquals(210, MathUtils.round(polygon.getX(2)));
		Assert.assertEquals(160, MathUtils.round(polygon.getY(2)));
		
		Assert.assertEquals(200, MathUtils.round(polygon.getX(3)));
		Assert.assertEquals(160, MathUtils.round(polygon.getY(3)));
		
		Assert.assertEquals(210f, polygon.getMaxX(), 0.01f);
		Assert.assertEquals(160f, polygon.getMaxY(), 0.01f);
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
		polygon.setXY(0f, 0f);
		
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
		Assert.assertEquals(10f, polygon.getX(0), 0.01f);
		Assert.assertEquals(0f, polygon.getY(0), 0.01f);
		Assert.assertEquals(20f, polygon.getX(1), 0.01f);
		Assert.assertEquals(0f, polygon.getY(1), 0.01f);
		Assert.assertEquals(20f, polygon.getX(2), 0.01f);
		Assert.assertEquals(10f, polygon.getY(2), 0.01f);
		Assert.assertEquals(15f, polygon.getX(3), 0.01f);
		Assert.assertEquals(10f, polygon.getY(3), 0.01f);
		Assert.assertEquals(10f, polygon.getX(4), 0.01f);
		Assert.assertEquals(10f, polygon.getY(4), 0.01f);
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
		Assert.assertEquals(10f, polygon.getX(0), 0.01f);
		Assert.assertEquals(0f, polygon.getY(0), 0.01f);
		Assert.assertEquals(20f, polygon.getX(1), 0.01f);
		Assert.assertEquals(0f, polygon.getY(1), 0.01f);
		Assert.assertEquals(20f, polygon.getX(2), 0.01f);
		Assert.assertEquals(10f, polygon.getY(2), 0.01f);
		Assert.assertEquals(10f, polygon.getX(3), 0.01f);
		Assert.assertEquals(10f, polygon.getY(3), 0.01f);
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
	public void testSetRadius() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(0f, 0f),
				new Point(10f, 0f),
				new Point(10f, 10f),
				new Point(0f, 10f)
		});
		polygon.setRadius(10f);
		Assert.assertEquals(-7.07f, polygon.getX(), 0.1f);
		Assert.assertEquals(-7.07f, polygon.getY(), 0.1f);
		Assert.assertEquals(17.07f, polygon.getMaxX(), 0.1f);
		Assert.assertEquals(17.07f, polygon.getMaxY(), 0.1f);
	}
	
	@Test
	public void testScale() {
		Polygon polygon = new Polygon(new Point [] {
				new Point(0f, 0f),
				new Point(10f, 0f),
				new Point(10f, 10f),
				new Point(0f, 10f)
		});
		polygon.scale(2f);
		Assert.assertEquals(-10f, polygon.getX(), 0.1f);
		Assert.assertEquals(-10f, polygon.getY(), 0.1f);
		Assert.assertEquals(20f, polygon.getMaxX(), 0.1f);
		Assert.assertEquals(20f, polygon.getMaxY(), 0.1f);
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
				Assert.assertEquals(points[i].x, iterator.getPointAX(), 0.01f);
				Assert.assertEquals(points[i].y, iterator.getPointAY(), 0.01f);
				Assert.assertEquals(points[i + 1].x, iterator.getPointBX(), 0.01f);
				Assert.assertEquals(points[i + 1].y, iterator.getPointBY(), 0.01f);
			} else {
				Assert.assertEquals(points[i].x, iterator.getPointAX(), 0.01f);
				Assert.assertEquals(points[i].y, iterator.getPointAY(), 0.01f);
				Assert.assertEquals(points[0].x, iterator.getPointBX(), 0.01f);
				Assert.assertEquals(points[0].y, iterator.getPointBY(), 0.01f);
			}
		}
		iterator.end();
	}
	
	@Test
	public void testEquals() {
		Polygon polygon1 = new Polygon(new Point [] {
			new Point(0f, 0f),
			new Point(10f, 0f),
			new Point(10f, 10f),
			new Point(0f, 10f)
		});
		Polygon polygon2 = new Polygon(new Point [] {
				new Point(0f, 0f),
				new Point(10f, 0f),
				new Point(10f, 10f),
				new Point(0f, 10f)
			});
		
		Assert.assertEquals(true, polygon1.equals(polygon2));
		
		polygon2 = new Polygon(new Point [] {
				new Point(0.00001f, 0f),
				new Point(10f, 0f),
				new Point(10f, 10f),
				new Point(0f, 10f)
			});
		Assert.assertEquals(false, polygon1.equals(polygon2));
	}

	@Test
	public void testDispose() {
		Geometry.DEFAULT_POOL_SIZE = 1;
		Geometry geometry = new Geometry();

		Polygon polygon1 = geometry.polygon();
		org.junit.Assert.assertEquals(0, geometry.getTotalPolygonsAvailable());

		polygon1.dispose();
		org.junit.Assert.assertEquals(1, geometry.getTotalPolygonsAvailable());
		polygon1.dispose();
		org.junit.Assert.assertEquals(1, geometry.getTotalPolygonsAvailable());

		//Test re-allocate and re-dispose
		polygon1 = geometry.polygon();
		org.junit.Assert.assertEquals(0, geometry.getTotalPolygonsAvailable());
		polygon1.dispose();
		org.junit.Assert.assertEquals(1, geometry.getTotalPolygonsAvailable());
	}
	
	private void clearDirtyBit(Polygon polygon) {
		polygon.getMaxX();
		polygon.getTriangles();
		polygon.getCenterX();
	}
}
