package org.mini2Dx.core.geom;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.engine.PositionChangeListener;

public class CircleTest implements PositionChangeListener<Circle> {
	private Circle circle;
	private boolean receivedNotification;
	
	@Before
	public void setup() {
		circle = new Circle(4);
		receivedNotification = false;
	}

	@Test
	public void testIntersectsCircle() {
		Circle circle2 = new Circle(20f, 20f, 4);
		Assert.assertEquals(false, circle.intersects(circle2));
		Assert.assertEquals(false, circle2.intersects(circle));
		
		circle2.setCenter(5f, 0f);
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
		Assert.assertEquals(false, receivedNotification);
		
		circle.setCenter(20f, 25f);
		
		Assert.assertEquals(20f, circle.getX());
		Assert.assertEquals(25f, circle.getY());
		Assert.assertEquals(false, receivedNotification);
	}

	@Test
	public void testSetCenterWithNotification() {
		circle.addPostionChangeListener(this);
		Assert.assertEquals(false, receivedNotification);
		
		circle.setCenter(20f, 25f);
		
		Assert.assertEquals(20f, circle.getX());
		Assert.assertEquals(25f, circle.getY());
		Assert.assertEquals(true, receivedNotification);
	}
	
	@Test
	public void testSetXWithoutNotification() {
		Assert.assertEquals(false, receivedNotification);
		
		circle.setX(25f);
		
		Assert.assertEquals(25f, circle.getX());
		Assert.assertEquals(0f, circle.getY());
		Assert.assertEquals(false, receivedNotification);
	}
	
	@Test
	public void testSetXWithNotification() {
		circle.addPostionChangeListener(this);
		Assert.assertEquals(false, receivedNotification);
		
		circle.setX(25f);
		
		Assert.assertEquals(25f, circle.getX());
		Assert.assertEquals(0f, circle.getY());
		Assert.assertEquals(true, receivedNotification);
	}
	
	@Test
	public void testSetYWithoutNotification() {
		Assert.assertEquals(false, receivedNotification);
		
		circle.setY(25f);
		
		Assert.assertEquals(0f, circle.getX());
		Assert.assertEquals(25f, circle.getY());
		Assert.assertEquals(false, receivedNotification);
	}
	
	@Test
	public void testSetYWithNotification() {
		circle.addPostionChangeListener(this);
		Assert.assertEquals(false, receivedNotification);
		
		circle.setY(25f);
		
		Assert.assertEquals(0f, circle.getX());
		Assert.assertEquals(25f, circle.getY());
		Assert.assertEquals(true, receivedNotification);
	}

	@Override
	public void positionChanged(Circle moved) {
		receivedNotification = true;
	}

}
