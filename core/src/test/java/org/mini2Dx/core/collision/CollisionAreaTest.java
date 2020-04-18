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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.JvmLocks;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.geom.PositionChangeListener;

/**
 *
 */
public class CollisionAreaTest implements PositionChangeListener<CollisionArea> {
	private boolean receivedPositionChangeNotification = false;

	@Before
	public void setUp() {
		Mdx.locks = new JvmLocks();
	}

	@Test
	public void testCollisionBox() {
		testCollisionShape(new CollisionBox());
	}
	
	@Test
	public void testCollisionCircle() {
		testCollisionShape(new CollisionCircle(10f));
	}
	
	@Test
	public void testCollisionPolygon() {
		testCollisionShape(new CollisionPolygon(new float [] { 0f, 0f,
				10f, 0f,
				10f, 10f,
				0f, 10f
				}));
	}
	
//	@Test
//	public void testStaticCollisionBox() {
//		testCollisionShape(new StaticCollisionBox());
//	}
//
//	@Test
//	public void testStaticCollisionCircle() {
//		testCollisionShape(new StaticCollisionCircle(10f));
//	}
//
//	@Test
//	public void testStaticCollisionPolygon() {
//		testCollisionShape(new StaticCollisionPolygon(new float [] { 0f, 0f,
//				10f, 0f,
//				10f, 10f,
//				0f, 10f
//				}));
//	}

	public void testCollisionShape(CollisionArea shape) {
		shape.addPostionChangeListener(this);
		
		shape.setCenter(5f, 1f);
		Assert.assertEquals(5f, shape.getCenterX());
		Assert.assertEquals(1f, shape.getCenterY());
		assertReceivedPositionChange();
		
		shape.setCenterX(10f);
		Assert.assertEquals(10f, shape.getCenterX());
		assertReceivedPositionChange();
		
		shape.setCenterY(15f);
		Assert.assertEquals(15f, shape.getCenterY());
		assertReceivedPositionChange();
		
		shape.setX(20f);
		Assert.assertEquals(20f, shape.getX());
		assertReceivedPositionChange();
		
		shape.setY(25f);
		Assert.assertEquals(25f, shape.getY());
		assertReceivedPositionChange();
		
		shape.setXY(25f, 30f);
		Assert.assertEquals(25f, shape.getX());
		Assert.assertEquals(30f, shape.getY());
		assertReceivedPositionChange();
	}

	@Override
	public void positionChanged(CollisionArea moved) {
		receivedPositionChangeNotification = true;
	}
	
	public void assertReceivedPositionChange() {
		Assert.assertTrue(receivedPositionChangeNotification);
		receivedPositionChangeNotification = false;
	}
}
