/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.core.engine.geom;

import org.junit.Test;
import org.mini2Dx.core.engine.PositionChangeListener;

import junit.framework.Assert;

/**
 *
 */
public class CollisionShapeTest implements PositionChangeListener<CollisionShape> {
	private boolean receivedPositionChangeNotification = false;
	
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
	
	@Test
	public void testStaticCollisionBox() {
		testCollisionShape(new StaticCollisionBox());
	}
	
	@Test
	public void testStaticCollisionCircle() {
		testCollisionShape(new StaticCollisionCircle(10f));
	}
	
	@Test
	public void testStaticCollisionPolygon() {
		testCollisionShape(new StaticCollisionPolygon(new float [] { 0f, 0f,
				10f, 0f,
				10f, 10f,
				0f, 10f
				}));
	}

	public void testCollisionShape(CollisionShape shape) {
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
		
		shape.set(25f, 30f);
		Assert.assertEquals(25f, shape.getX());
		Assert.assertEquals(30f, shape.getY());
		assertReceivedPositionChange();
	}

	@Override
	public void positionChanged(CollisionShape moved) {
		receivedPositionChangeNotification = true;
	}
	
	public void assertReceivedPositionChange() {
		Assert.assertTrue(receivedPositionChangeNotification);
		receivedPositionChangeNotification = false;
	}
}
