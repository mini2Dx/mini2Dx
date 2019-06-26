/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.core.collision;

import junit.framework.Assert;
import org.junit.Test;
import org.mini2Dx.core.geom.PositionChangeListener;

/**
 *
 */
public class CollisionAreaTest implements PositionChangeListener<CollisionArea> {
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
