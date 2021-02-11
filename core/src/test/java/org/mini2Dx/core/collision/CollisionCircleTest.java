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

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.geom.Circle;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.PositionChangeListener;
import org.mini2Dx.core.geom.SizeChangeListener;
import org.mini2Dx.lockprovider.jvm.JvmLocks;

/**
 * Unit tests for {@link CollisionCircle}
 * @author Thomas Cashman
 */
public class CollisionCircleTest implements PositionChangeListener<CollisionCircle>, SizeChangeListener<CollisionCircle> {
	private CollisionCircle circle;
	private boolean receivedPositionNotification, receivedSizeNotification;
	
	@Before
	public void setup() {
		Mdx.locks = new JvmLocks();

		circle = new CollisionCircle(4);
		receivedPositionNotification = false;
		receivedSizeNotification = false;
	}
	
	@Test
	public void testIdGeneration() {
		CollisionCircle circle2 = new CollisionCircle(20f, 20f, 4);
		Assert.assertEquals(true, circle.getId() != circle2.getId());
	}
	
	@Test
	public void testGetXY() {
		CollisionCircle collisionCircle = new CollisionCircle(50f, 100f, 25f);
		Assert.assertEquals(50f, collisionCircle.getX());
		Assert.assertEquals(100f, collisionCircle.getY());
		Assert.assertEquals(25f, collisionCircle.getRadius());
	}
	
	@Test
	public void testIntersectsCircle() {
		Circle circle2 = new Circle(20f, 20f, 4);
		Assert.assertEquals(false, circle.intersects(circle2));
		Assert.assertEquals(false, circle2.intersects(circle));
		
		circle2.setXY(5f, 0f);
		Assert.assertEquals(true, circle.intersects(circle2));
		Assert.assertEquals(true, circle2.intersects(circle));
	}

	@Test
	public void testGetDistanceToPositionable() {
		Point point = new Point(3f, 0f);
		Assert.assertEquals(0f, circle.getDistanceTo(point));
		
		point.set(5f, 0f);
		Assert.assertEquals(1f, circle.getDistanceTo(point));
	}
	
	@Test
	public void testSetCenterWithoutNotification() {
		Assert.assertEquals(false, receivedPositionNotification);
		
		circle.setXY(20f, 25f);
		
		Assert.assertEquals(20f, circle.getX());
		Assert.assertEquals(25f, circle.getY());
		Assert.assertEquals(false, receivedPositionNotification);
	}

	@Test
	public void testSetCenterWithNotification() {
		circle.addPostionChangeListener(this);
		circle.addSizeChangeListener(this);
		Assert.assertEquals(false, receivedPositionNotification);
		
		circle.setXY(20f, 25f);
		
		Assert.assertEquals(20f, circle.getX());
		Assert.assertEquals(25f, circle.getY());
		Assert.assertEquals(true, receivedPositionNotification);
		Assert.assertEquals(false, receivedSizeNotification);
	}
	
	@Test
	public void testSetXWithoutNotification() {
		Assert.assertEquals(false, receivedPositionNotification);
		
		circle.setX(25f);
		
		Assert.assertEquals(25f, circle.getX());
		Assert.assertEquals(0f, circle.getY());
		Assert.assertEquals(false, receivedPositionNotification);
	}
	
	@Test
	public void testSetXWithNotification() {
		circle.addPostionChangeListener(this);
		circle.addSizeChangeListener(this);
		Assert.assertEquals(false, receivedPositionNotification);
		
		circle.setX(25f);
		
		Assert.assertEquals(25f, circle.getX());
		Assert.assertEquals(0f, circle.getY());
		Assert.assertEquals(true, receivedPositionNotification);
		Assert.assertEquals(false, receivedSizeNotification);
	}
	
	@Test
	public void testSetYWithoutNotification() {
		Assert.assertEquals(false, receivedPositionNotification);
		
		circle.setY(25f);
		
		Assert.assertEquals(0f, circle.getX());
		Assert.assertEquals(25f, circle.getY());
		Assert.assertEquals(false, receivedPositionNotification);
	}
	
	@Test
	public void testSetYWithNotification() {
		circle.addPostionChangeListener(this);
		circle.addSizeChangeListener(this);
		Assert.assertEquals(false, receivedPositionNotification);
		
		circle.setY(25f);
		
		Assert.assertEquals(0f, circle.getX());
		Assert.assertEquals(25f, circle.getY());
		Assert.assertEquals(true, receivedPositionNotification);
		Assert.assertEquals(false, receivedSizeNotification);
	}
	
	@Test
	public void testSetRadiusWithNotification() {
		circle.addPostionChangeListener(this);
		circle.addSizeChangeListener(this);
		
		circle.setRadius(100f);
		
		Assert.assertEquals(0f, circle.getX());
		Assert.assertEquals(0f, circle.getY());
		Assert.assertEquals(100f, circle.getRadius());
		Assert.assertEquals(false, receivedPositionNotification);
		Assert.assertEquals(true, receivedSizeNotification);
	}

	@Override
	public void positionChanged(CollisionCircle moved) {
		receivedPositionNotification = true;
	}

	@Override
	public void sizeChanged(CollisionCircle changed) {
		receivedSizeNotification = true;
	}
}
