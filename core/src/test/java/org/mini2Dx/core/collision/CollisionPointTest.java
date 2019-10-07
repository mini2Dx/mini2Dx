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
 * Unit tests for {@link CollisionPoint}
 * @author Thomas Cashman
 */
public class CollisionPointTest implements PositionChangeListener<CollisionPoint> {
	private CollisionPoint point1, point2, point3;
	
	private int totalNotifications = 0;
	
	@Before
	public void setup() {
		Mdx.locks = new JvmLocks();

		point1 = new CollisionPoint();
		point2 = new CollisionPoint();
		point3 = new CollisionPoint();
		
		point1.addPostionChangeListener(this);
	}
	
	@Test
	public void testIdGeneration() {
		Assert.assertEquals(true, point1.getId() != point2.getId());
		Assert.assertEquals(true, point1.getId() != point3.getId());
		Assert.assertEquals(true, point2.getId() != point3.getId());
	}
	
	@Test
	public void testEquals() {
		point1.set(0f, 0f);
		point2.set(0f, 0f);
		
		Assert.assertEquals(true, point1.equals(point2));
		
		point2.set(0.1f, 0f);
		Assert.assertEquals(false, point1.equals(point2));
		
		point2.set(0f, 0.1f);
		Assert.assertEquals(false, point1.equals(point2));
		
		point2.set(0.1f, 0.1f);
		Assert.assertEquals(false, point1.equals(point2));
		
		point2.set(1f, 1f);
		Assert.assertEquals(false, point1.equals(point2));
	}
	
	@Test
	public void testEqualsWithDelta() {
		point1.set(0f, 0f);
		point2.set(0f, 0f);
		
		Assert.assertEquals(true, point1.equals(point2, 0.1f));
		
		point2.set(0.1f, 0f);
		Assert.assertEquals(true, point1.equals(point2, 0.1f));
		
		point2.set(0.11f, 0f);
		Assert.assertEquals(false, point1.equals(point2, 0.1f));
		
		point2.set(0f, 0.1f);
		Assert.assertEquals(true, point1.equals(point2, 0.1f));
		
		point2.set(0f, 0.11f);
		Assert.assertEquals(false, point1.equals(point2, 0.1f));
		
		point2.set(0.1f, 0.1f);
		Assert.assertEquals(true, point1.equals(point2, 0.1f));
		
		point2.set(0.11f, 0.11f);
		Assert.assertEquals(false, point1.equals(point2, 0.1f));
		
		point2.set(1f, 1f);
		Assert.assertEquals(false, point1.equals(point2, 0.1f));
	}
	
	@Test
	public void testInBetween() {
		point1.set(0, 0);
		point2.set(10, 0);
		point3.set(5, 0);
		
		Assert.assertEquals(true, point3.isOnLineBetween(point1, point2));
		Assert.assertEquals(false, point1.isOnLineBetween(point2, point3));
		Assert.assertEquals(false, point2.isOnLineBetween(point3, point1));
		
		point1.set(0, 0);
		point2.set(0, 10);
		point3.set(0, 5);
		
		Assert.assertEquals(true, point3.isOnLineBetween(point1, point2));
		Assert.assertEquals(false, point1.isOnLineBetween(point2, point3));
		Assert.assertEquals(false, point2.isOnLineBetween(point3, point1));
	}
	
	@Test
	public void testRotateAround() {
		point1.set(0, 0);
		point2.set(10, 0);
		
		point2.rotateAround(point1, 90f);
		Assert.assertEquals(10f, point2.getY(), 0.0f);
		
		point1.set(10, 0);
		point2.set(20, 0);
		
		point2.rotateAround(point1, 90f);
		Assert.assertEquals(10f, point2.getY(), 0.0f);
	}
	
	@Test
	public void testMoveTowards() {
		point1.set(0f, 0f);
		point2.set(10f, 10f);
		
		point1.moveTowards(point2, 1f);
		Assert.assertEquals(0.707f, point1.getX(), 0.01f);
		Assert.assertEquals(0.701f, point1.getY(), 0.01f);
	}
	
	@Test
	public void testPositionChangedNotification() {
		point1.setX(1f);
		Assert.assertEquals(1, totalNotifications);
		
		point1.setY(1f);
		Assert.assertEquals(2, totalNotifications);
		
		point1.add(1f, 1f);
		Assert.assertEquals(3, totalNotifications);
		
		point1.sub(1f, 1f);
		Assert.assertEquals(4, totalNotifications);
		
		point1.set(10f, 10f);
		Assert.assertEquals(5, totalNotifications);
		
		//Check no unnecessary notification
		point1.set(10f, 10f);
		Assert.assertEquals(5, totalNotifications);
	}

	@Override
	public void positionChanged(CollisionPoint moved) {
		totalNotifications++;
	}
}
